import { TiposDeAnexos } from './../../../../../model/anexo/anexo.state.model';
import { Component, OnInit, ViewChild, OnDestroy, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AnexoModel } from '../../../../../model/anexo/anexo.state.model';
import { Store, Actions, ofActionSuccessful, Select } from '@ngxs/store';
import { SaveAnexo, LoadTipoDeAnexo, LoadAnexosLicitacao } from '../../../../../model/anexo/anexo.actions';
import { Subscription, Observable } from 'rxjs';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute } from '@angular/router';
import { LicitacaoDetalhadaModel } from '../../../../../model/licitacao/licitacao.state.model';
import { LicitacaoState } from '../../../../../model/licitacao/licitacao.state';
import { UserStateModel } from '../../../../../model/user/user.state.model';
import { UserState } from '../../../../../model/user/user.state';
import { filter, take } from 'rxjs/operators';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-cadastro-anexo',
  styleUrls: ['./cadastro-anexo.component.scss'],
  templateUrl: './cadastro-anexo.component.html'
})
export class CadastroAnexoComponent implements OnInit, OnDestroy {

  actionSubscription$: Subscription;
  anexoForm: FormGroup;
  visualizar: boolean;

  @ViewChild('anexoInput') anexoInput;
  licitacao: LicitacaoDetalhadaModel;
  usuario: UserStateModel;
  idAnexo: number;
  nomeArquivo = '';
  submitted = false;
  tipoDoAnexo: string;
  perfilDoUsuario: string;
  tiposAnexos: TiposDeAnexos[];
  versao: number;

  @Select(LicitacaoDetalhadaState.ufsLicitacaoDetalhada) tiposDeAnexos: Observable<TiposDeAnexos[]>;

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private alertMessageService: AlertMessageService,
    private readonly actions$: Actions) { }

  ngOnInit() {
    this.visualizar = (this.route.snapshot.url[0].path === 'detalhar');

    this.store.dispatch(new LoadTipoDeAnexo());

    this.actions$.pipe(
      ofActionSuccessful(LoadTipoDeAnexo), take(1)
    ).subscribe(() => {
      this.store.select(LicitacaoDetalhadaState)
        .pipe(
          filter(licitacao => licitacao != null),
          take(1)
        ).subscribe(licitacao => {
          this.licitacao = licitacao;
          if (this.licitacao.anexos) {
            this.initForm();
          } else {
            this.store.dispatch(new LoadAnexosLicitacao()).subscribe(() => this.initForm());
          }
        });
    });

  }

  initForm() {
    const id = this.route.snapshot.paramMap.get('id');
    this.usuario = this.store.selectSnapshot(UserState);
    this.tiposAnexos = this.store.selectSnapshot(LicitacaoState.tiposAnexos);

    let descricao = '';

    if (id) {
      this.idAnexo = Number(id);
      const anexo = this.licitacao.anexos
        .find(anexoLicitacao => anexoLicitacao.id === this.idAnexo);

      descricao = anexo.descricao;
      this.perfilDoUsuario = anexo.perfilUsuarioEnvio;
      this.tipoDoAnexo = anexo.tipoDoAnexo;
      this.nomeArquivo = anexo.nomeArquivo;
      this.versao = anexo.versao;
    } else {
      this.perfilDoUsuario = this.usuario.profile;
    }

    this.anexoForm = this.fb.group({
      descricao: [{ value: descricao, disabled: this.visualizar }, [Validators.required, Validators.maxLength(30)]],
      tipoDoAnexo: [{ value: this.tipoDoAnexo, disabled: this.visualizar }, Validators.required],
      perfilDoUsuario: [{value: this.perfilDoUsuario, disabled: true}, Validators.required],
      anexo: [null]
    });

    this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(SaveAnexo))
      .subscribe(() => {
        this.alertMessageService.success('Anexo salvo com sucesso!');
        this.store.dispatch(new LoadAnexosLicitacao());
        this.store.dispatch(
          this.voltarListagemAction()
        );
      });
  }

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  addAnexo() {
    this.anexoInput.nativeElement.click();
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.anexoForm.get('anexo').setValue(file);
      this.nomeArquivo = file.name;
    }
  }

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine || this.submitted);
  }

  field(fieldName: string) {
    return this.anexoForm.get(fieldName);
  }

  onSubmit(templateModal: TemplateRef<any>) {
    this.salvarAnexo();
  }

  salvarAnexo() {
    if (this.nomeArquivo.length > 100) {
      this.alertMessageService.error('O tamanho do nome do arquivo não deve ultrapassar 100 caracteres.');
      return;
    }

    this.submitted = true;
    if (!this.nomeArquivo || !this.anexoForm.valid) {
      this.alertMessageService.error('Por favor, verifique os campos obrigatórios.');
      return;
    }

    const formValues = this.anexoForm.value;
    const DEZ_MEGAS_BYTES = 10485760;
    if (formValues.anexo && formValues.anexo.size > DEZ_MEGAS_BYTES) {
      this.alertMessageService.error('Tamanho máximo do arquivo: 10MB!');

      return;
    }

    const anexo: AnexoModel = {
      id: this.idAnexo,
      nomeArquivo: this.nomeArquivo,
      descricao: formValues.descricao,
      tipoDoAnexo: formValues.tipoDoAnexo,
      arquivo: formValues.anexo,
      tamanhoArquivo: formValues.anexo ? formValues.anexo.size : 0,
      versao: this.versao ? this.versao : 0
    };

    this.store.dispatch(new SaveAnexo(this.licitacao.id, anexo));
  }

  confirmaAlteracao() {
    this.salvarAnexo();
  }

  get form() { return this.anexoForm.controls; }


  voltar() {
    this.store.dispatch(this.voltarListagemAction());
  }

  voltarListagemAction() {
    const id = this.route.snapshot.paramMap.get('id');
    let caminho;
    if (id) {
      caminho = '../../listagem';
    } else {
      caminho = '../listagem';
    }
    return new Navigate([caminho], {}, { relativeTo: this.route });
  }


}
