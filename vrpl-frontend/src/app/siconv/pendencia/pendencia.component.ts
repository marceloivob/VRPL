import { Component, OnInit, TemplateRef, Input, Output, EventEmitter } from '@angular/core';
import { PendenciaModel, PrazoPendenciaModel } from 'src/app/model/laudo/laudo.state.model';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { SubmetaModel } from 'src/app/model/qci/qci.state.model';
import { SavePendencia } from 'src/app/model/licitacao/licitacao.actions';
import { DeletePendencia } from 'src/app/model/laudo/laudo.actions';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'vrpl-pendencia',
  templateUrl: './pendencia.component.html'
})
export class PendenciaComponent implements OnInit {

  @Input() pendencias: Observable<PendenciaModel[]>;
  @Input() submetas: SubmetaModel[];
  @Input() laudoId: number;
  @Input() podeEditar: boolean;

  @Output() changed = new EventEmitter<any>();

  pendenciaForm: FormGroup;

  prazos: PrazoPendenciaModel[];

  lista: any[];

  modalRef: BsModalRef;
  showCadastro = false;
  visualizar: boolean;
  submitted = false;

  submetaSelecionada: SubmetaModel = null;
  prazoSelecionado: PrazoPendenciaModel = null;

  descricao = '';
  submeta = '';
  prazo = '';
  versao = 0;
  idPendencia: number;

  pendenciaASerExcluida: PendenciaModel;

  constructor(
    private readonly store: Store,
    private readonly fb: FormBuilder,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService
  ) { }

  ngOnInit() {
    this.prazos = this.listaPrazos();
    this.submitted = false;

    this.pendenciaForm = this.fb.group({
      descricao: [{ value: this.descricao, disabled: this.visualizar }, [Validators.required, Validators.maxLength(1500)]],
      submetaSelecionada: [{ value: this.submetaSelecionada, disabled: this.visualizar }, Validators.required],
      prazoSelecionado: [{ value: this.prazoSelecionado, disabled: this.visualizar }, Validators.required],
    });

  }

  cadastrar() {
    this.showCadastro = true;
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  changedEvent() {
    this.changed.emit('');
  }

  editar(pendencia: PendenciaModel) {
    this.idPendencia = pendencia.id;
    this.descricao = pendencia.descricao;
    this.submetaSelecionada = this.submetaDescricao(pendencia.submetaFk);
    this.prazoSelecionado = this.prazoDescricao(pendencia.prazo);
    this.versao = pendencia.versao;

    this.showCadastro = true;
  }

  deletePendencia(pendencia: PendenciaModel, template: TemplateRef<any>) {
    this.pendenciaASerExcluida = pendencia;
    this.modalRef = this.modalService.show(template);
  }

  cancelar() {
    this.limparCampos();
    this.showCadastro = false;
    this.submitted = false;
    this.pendenciaForm = this.fb.group({
      descricao: [{ value: this.descricao, disabled: this.visualizar }, [Validators.required, Validators.nullValidator, Validators.maxLength(1500)]],
      submetaSelecionada: [{ value: this.submetaSelecionada, disabled: this.visualizar }, Validators.required],
      prazoSelecionado: [{ value: this.prazoSelecionado, disabled: this.visualizar }, Validators.required],
    });
  }

  limparCampos() {
    setTimeout(() => {
      this.idPendencia = null;
      this.descricao = '';
      this.submetaSelecionada = null;
      this.prazoSelecionado = null;
      this.versao = 0;
    });
  }

  onSubmit() {
    this.salvarPendencia();
  }

  salvarPendencia() {
    this.submitted = true;

    if (!this.pendenciaForm.valid || this.descricao === '' || this.descricao === null) {
      this.alertMessageService.error('Por favor, verifique os campos obrigatórios.');
      return;
    }

    const pendencia: PendenciaModel = {
      id: this.idPendencia,
      inResolvida: false,
      prazo: this.prazoSelecionado.codigo,
      laudoFk: this.laudoId,
      descricao: this.descricao,
      versao: this.versao,
      submetaFk: this.submetaSelecionada.id
    };

    this.store.dispatch(new SavePendencia(pendencia))
      .subscribe(() => {
        this.limparCampos();
        this.showCadastro = false;
        this.changedEvent();
        this.submitted = false;
        this.descricao = '';
        this.pendenciaForm = this.fb.group({
          descricao: [{ value: this.descricao, disabled: this.visualizar }, [Validators.required, Validators.nullValidator, Validators.maxLength(1500)]],
          submetaSelecionada: [{ value: this.submetaSelecionada, disabled: this.visualizar }, Validators.required],
          prazoSelecionado: [{ value: this.prazoSelecionado, disabled: this.visualizar }, Validators.required],
        });

        
      });



      
      
  }

  confirmaExclusao() {
    this.store.dispatch(new DeletePendencia(this.pendenciaASerExcluida.id))
      .subscribe(() => {
        this.changedEvent();
      });
    this.modalRef.hide();
  }

  cancelaExclusao() {
    this.modalRef.hide();
  }

  listaPrazos(): PrazoPendenciaModel[] {
    const prazos: PrazoPendenciaModel[] = [];

    const rsu: PrazoPendenciaModel = {
      codigo: 'RSU',
      descricao: 'Retirada de suspensiva'
    };

    const con: PrazoPendenciaModel = {
      codigo: 'CON',
      descricao: 'Contratação'
    };

    const aio: PrazoPendenciaModel = {
      codigo: 'AIO',
      descricao: 'AIO'
    };

    const pro: PrazoPendenciaModel = {
      codigo: 'PRO',
      descricao: 'Próximo desbloqueio'
    };

    const ult: PrazoPendenciaModel = {
      codigo: 'ULT',
      descricao: 'Último desbloqueio'
    };

    const pcf: PrazoPendenciaModel = {
      codigo: 'PCF',
      descricao: 'Prestação de contas final'
    };

    const apt: PrazoPendenciaModel = {
      codigo: 'APT',
      descricao: 'Aprovação do plano de trabalho'
    };

    const acl: PrazoPendenciaModel = {
      codigo: 'ACL',
      descricao: 'Aceite da licitação'
    };

    const pvl: PrazoPendenciaModel = {
      codigo: 'PVL',
      descricao: 'Próxima visita in loco'
    };

    prazos.push(acl);
    prazos.push(aio);
    prazos.push(apt);
    prazos.push(con);
    prazos.push(pcf);
    prazos.push(pvl);
    prazos.push(pro);
    prazos.push(rsu);
    prazos.push(ult);

    return prazos;
  }

  submetaDescricao(idSubmeta: number): SubmetaModel {
    let ret: SubmetaModel;
    this.submetas.forEach(submeta => {
      if (submeta.id === idSubmeta) {
        ret = submeta;
      }
    });

    return ret;
  }

  prazoDescricao(cdPrazo: string): PrazoPendenciaModel {
    let ret: PrazoPendenciaModel;
    this.prazos.forEach(prazo => {
      if (prazo.codigo === cdPrazo) {
        ret = prazo;
      }
    });

    return ret;
  }

  showError(fieldName: string): boolean {
    const field = this.field(fieldName);

    const showError = !field.valid && (!field.pristine || this.submitted);

    return showError;
  }

  showErrorDescricao(): boolean {

    const showError = (this.descricao === null || this.descricao === '') ||
    (this.descricao !== null && this.descricao !== '' && this.descricao.length > 1500);

    return showError;
  }

  showErrorDescricaoNula(): boolean {
    return this.descricao === null || this.descricao === '';
  }

  showErrorTamanhoDescricaoMaiorQueOPermitido(): boolean {
   return this.descricao !== null && this.descricao !== '' && this.descricao.length > 1500;
  }

  field(fieldName: string) {
    return this.pendenciaForm.get(fieldName);
  }

}
