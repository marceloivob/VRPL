import { Component, OnInit, ViewChild, Input, TemplateRef, Output, EventEmitter } from '@angular/core';
import { Observable } from 'rxjs';
import { AnexoModel } from '../../model/anexo/anexo.state.model';
import { Store } from '@ngxs/store';
import { UserState } from '../../model/user/user.state';
import { filter, take } from 'rxjs/operators';
import { UserStateModel } from '../../model/user/user.state.model';
import { SaveAnexo, DeleteAnexo } from '../../model/anexo/anexo.actions';
import { LicitacaoDetalhadaState } from '../../model/licitacao/licitacao.detalhada.state';
import { LicitacaoDetalhadaModel } from '../../model/licitacao/licitacao.state.model';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { DataExport } from 'src/app/model/data-export';
import { DatePipe } from '@angular/common';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'vrpl-anexo',
  styleUrls: ['./anexo.component.scss'],
  templateUrl: './anexo.component.html'
})
export class AnexoComponent implements OnInit {

  @Input() tipoDoAnexo: string;
  @Input() anexos: Observable<AnexoModel[]>;
  @Input() podeEditar: boolean;

  @Output() changed = new EventEmitter<any>();

  anexoForm: FormGroup;
  submitted = false;

  anexoASerExcluido: AnexoModel;

  @ViewChild('anexoInput') anexoInput;

  idAnexo: number;
  nomeArquivo = '';
  descricao = '';
  anexo: any;
  versao = 0;
  visualizar: boolean;

  usuario: UserStateModel;
  licitacao: LicitacaoDetalhadaModel;

  modalRef: BsModalRef;
  showAnexoInput = true;
  showCadastro = false;

  // Grid
  export: DataExport;
  lista: any[];

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService
  ) { }

  ngOnInit() {
    this.usuario = this.store.selectSnapshot(UserState);
    this.visualizar = (this.route.snapshot.url[0].path === 'detalhar');

    this.store.select(LicitacaoDetalhadaState)
      .pipe(
        filter(licitacao => licitacao != null),
        take(1)
      ).subscribe(licitacao => {
        this.licitacao = licitacao;

        this.anexoForm = this.fb.group({
          descricao: [{ value: this.descricao, disabled: this.visualizar }, [Validators.required, Validators.maxLength(30)]],
          tipoDoAnexo: [{ value: this.tipoDoAnexo, disabled: this.visualizar }, Validators.required],
          anexo: [null]
        });
      });

    this.anexos.subscribe(anexos => {
      this.getExport(anexos);
    });

  }

  changedEvent() {
    this.changed.emit('');
  }

  deleteAnexo(anexo: AnexoModel, template: TemplateRef<any>) {
    this.anexoASerExcluido = anexo;
    this.modalRef = this.modalService.show(template);
  }

  confirmaExclusao() {
    this.store.dispatch(new DeleteAnexo(this.anexoASerExcluido))
      .subscribe(() => {
        this.changedEvent();
      });
    this.modalRef.hide();
  }

  cancelaExclusao() {
    this.modalRef.hide();
  }

  downloadFile(linkToDownload: string) {
    window.open(linkToDownload);
  }

  addAnexo() {
    this.anexoInput.nativeElement.click();
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.anexo = file;
      this.nomeArquivo = file.name;
    }
  }

  onSubmit() {
    this.salvarAnexo();
  }

  salvarAnexo() {
    this.submitted = true;
    if (!this.nomeArquivo || !this.anexoForm.valid) {
      this.alertMessageService.error('Por favor, verifique os campos obrigatórios.');
      return;
    }

    if (this.nomeArquivo.length > 100) {
      this.alertMessageService.error('O tamanho do nome do arquivo não deve ultrapassar 100 caracteres.');
      return;
    }

    const DEZ_MEGAS_BYTES = 10485760;
    if (this.anexo && this.anexo.size > DEZ_MEGAS_BYTES) {
      this.alertMessageService.error('Tamanho máximo do arquivo: 10MB!');

      return;
    }

    const anexo: AnexoModel = {
      id: this.idAnexo,
      nomeArquivo: this.nomeArquivo,
      descricao: this.descricao,
      tipoDoAnexo: this.tipoDoAnexo,
      arquivo: this.anexo,
      tamanhoArquivo: this.anexo ? this.anexo.size : 0,
      versao: this.versao
    };

    this.store.dispatch(new SaveAnexo(this.licitacao.id, anexo))
      .subscribe(() => {
        this.changedEvent();
        this.limparCampos();
        this.showCadastro = false;
      });
  }

  cancelar() {
    this.limparCampos();
    this.showCadastro = false;
    this.submitted = false;
  }

  editar(anexo: AnexoModel) {
    this.idAnexo = anexo.id;
    this.descricao = anexo.descricao;
    this.nomeArquivo = anexo.nomeArquivo;
    this.versao = anexo.versao;
    this.showCadastro = true;
  }

  limparCampos() {
    this.showAnexoInput = false;
    setTimeout(() => {
      this.anexo = null;
      this.nomeArquivo = '';
      this.idAnexo = null;
      this.descricao = '';
      this.showAnexoInput = true;
      this.versao = 0;
    });
  }

  cadastrar() {
    this.showCadastro = true;
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(anexos: AnexoModel[]): AnexoModel[] {
    const data = [];
    const columns = [
      'Descrição', 'Tipo', 'Data de Envio',
      'Enviado por', 'Perfil'
    ];

    const datepipe = new DatePipe('PT-BR');
    if (anexos) {
      anexos.forEach(anexo => {
        const linha = [];

        linha.push(anexo.descricao);
        linha.push(anexo.tipoDoAnexo);
        linha.push(datepipe.transform(anexo.dataUpload, 'dd/MM/yyyy'));
        linha.push(anexo.nomeDoUsuarioQueEnviou);
        linha.push(anexo.perfilUsuarioEnvio);

        data.push(linha);
      });
    }

    this.export = new DataExport(columns, data);

    return anexos;
  }


  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine || this.submitted);
  }

  field(fieldName: string) {
    return this.anexoForm.get(fieldName);
  }

}
