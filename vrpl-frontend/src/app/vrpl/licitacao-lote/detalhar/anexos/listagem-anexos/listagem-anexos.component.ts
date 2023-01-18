
import { Component, TemplateRef } from '@angular/core';
import { Store, Select } from '@ngxs/store';
import { Observable } from 'rxjs';
import { LoadAnexosLicitacao, DeleteAnexo } from '../../../../../model/anexo/anexo.actions';
import { AnexoModel } from '../../../../../model/anexo/anexo.state.model';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { DataExport } from 'src/app/model/data-export';
import { DatePipe } from '@angular/common';
import { Locale } from 'ngx-bootstrap/chronos/locale/locale.class';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'vrpl-listagem-anexos',
  templateUrl: './listagem-anexos.component.html'
})
export class ListagemAnexosComponent extends BaseComponent {

  @Select(LicitacaoDetalhadaState.anexosLicitacaoDetalhada) anexosObservable: Observable<AnexoModel[]>;

  // Grid
  export: DataExport;

  anexoASerExcluido: AnexoModel;
  modalRef: BsModalRef;
  lista: any[];

  constructor(
    protected readonly store: Store,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService,
    private readonly auth: UserAuthorizerService,
    private route: ActivatedRoute) {
      super(store);
  }

  init() { }

  loadActions() {
    return new LoadAnexosLicitacao();
  }

  onLoad() {
    this.anexosObservable.subscribe(anexos => {
      this.getExport(anexos);
    });
  }

  deleteAnexo(anexo: AnexoModel, template: TemplateRef<any>) {
    this.anexoASerExcluido = anexo;
    this.modalRef = this.modalService.show(template);
  }

  confirmaExclusao() {
    this.store.dispatch(new DeleteAnexo(this.anexoASerExcluido))
      .subscribe(() => {
        this.alertMessageService.success('Anexo excluído com sucesso!');
        this.dispatch(this.loadActions());
      });

    this.modalRef.hide();
  }

  cancelaExclusao() {
    this.modalRef.hide();
  }

  downloadFile(linkToDownload: string) {
    window.open(linkToDownload);
  }

  get podeEditar() {
    return this.auth.podeEditarLicitacao();
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
    anexos.forEach(anexo => {
      const linha = [];

      linha.push(anexo.descricao);
      linha.push(anexo.tipoDoAnexoAsString);
      linha.push(datepipe.transform(anexo.dataUpload, 'dd/MM/yyyy'));
      linha.push(anexo.nomeDoUsuarioQueEnviou);
      linha.push(anexo.perfilUsuarioEnvio);

      data.push(linha);
    });

    this.export = new DataExport(columns, data);

    return anexos;
  }

  get pathId() {
    const id = this.route.parent.snapshot.paramMap.get('id');
    return id ? Number(id) : null;
  }

  voltar() {
    const url = this.pathId ? '../../../../../listagem' : '../../../../listagem';
    this.dispatch(new Navigate([url], {}, { relativeTo: this.route }));
  }

  mostrarBotaoDownload(link: string): boolean {
    if (link === null || link === undefined || link === '') {
      return false;
    }
    return true;
  }
}
