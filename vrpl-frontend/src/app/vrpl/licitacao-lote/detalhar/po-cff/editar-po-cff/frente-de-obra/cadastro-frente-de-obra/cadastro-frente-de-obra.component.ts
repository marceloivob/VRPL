import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { PoCffService } from '../../../../../../../model/pocff/po-cff.service';
import { FrenteDeObraModel } from '../../../../../../../model/pocff/po-cff.state.model';
import { Subscription, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LicitacaoStateModel } from '../../../../../../../model/licitacao/licitacao.state.model';
import { Navigate } from '@ngxs/router-plugin';
import { SaveFrenteDeObra } from '../../../../../../../model/pocff/po-cff.actions';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { BaseComponent } from 'src/app/siconv/util/base.component';

@Component({
  selector: 'vrpl-cadastro-frente-de-obra',
  templateUrl: './cadastro-frente-de-obra.component.html'
})
export class CadastroFrenteDeObraComponent extends BaseComponent {

  visualizar: boolean;
  frenteDeObraForm: FormGroup;
  submitted: boolean;
  frenteDeObra: FrenteDeObraModel;

  constructor(
    protected store: Store,
    private fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private alertMessageService: AlertMessageService,
    private readonly poCff: PoCffService
  ) {
    super(store);
  }

  init() {
    if (this.route.snapshot.url[0].path === 'detalhar') {
      this.visualizar = true;
    }

    this.initForm().subscribe(
      frente => {
        this.frenteDeObra = frente;
        this.frenteDeObraForm = this.fb.group({
          id: [{ value: frente.id, disabled: this.visualizar }],
          numeroFrente: [{ value: frente.numeroFrente, disabled: this.visualizar }, [Validators.required, Validators.min(1), Validators.max(999)]],
          nomeFrente: [{ value: frente.nomeFrente, disabled: this.visualizar }, [Validators.required, Validators.maxLength(100)]],
          versao: frente.versao
        });
      }
    );
  }

  // onLoad é chamado após todas as Actions do loadActions serem executadas.
  // Se não tem actions, o onLoad não é chamado.
  onLoad() { }

  loadActions() {
    return [];
  }

  initForm(): Observable<FrenteDeObraModel> {
    const idFrente = this.route.snapshot.paramMap.get('id');
    const idPO = this.getIdPO();

    if (idFrente) {
      return this.poCff.loadFrenteDeObraById(Number(idPO), Number(idFrente));
    } else {
      return this.poCff.recuperarSequencialFrenteDeObra(idPO).pipe(
        map(sequencial => {
          return {
            id: null,
            numeroFrente: sequencial,
            nomeFrente: null,
            versao: 1
          };
        })
      );
    }
  }

  getIdPO() {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    const po = licitacaoState.licitacaoDetalhada.po.poDetalhada;
    const idPO = po.id;

    return idPO;
  }

  onSubmit() {
    this.submitted = true;

    if (!this.frenteDeObraForm.valid) {
      this.alertMessageService.error('Verifique erros nos campos!');
      return;
    }

    const formValues = this.frenteDeObraForm.value;

    const frente: FrenteDeObraModel = {
      id: formValues.id,
      nomeFrente: formValues.nomeFrente,
      numeroFrente: formValues.numeroFrente,
      versao: this.frenteDeObra.versao
    };

    const idPO = this.getIdPO();

    this.store.dispatch(new SaveFrenteDeObra(Number(idPO), frente))
    .subscribe(() => {
      this.alertMessageService.success('Frente de Obra salva com sucesso!');
      this.store.dispatch(
        this.voltarListagemAction()
      );
    });
  }

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

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine || this.submitted);
  }

  field(fieldName: string) {
    return this.frenteDeObraForm.get(fieldName);
  }

  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;

  }

}
