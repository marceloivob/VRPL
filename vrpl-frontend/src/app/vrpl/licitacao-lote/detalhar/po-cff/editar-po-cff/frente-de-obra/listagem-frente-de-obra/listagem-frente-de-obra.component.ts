import { Component, TemplateRef, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { FrenteDeObraModel, PoModel } from '../../../../../../../model/pocff/po-cff.state.model';
import { Select, Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { PoLicitacaoState } from '../../../../../../../model/licitacao/pocff/po-licitacao.state';
import { LoadFrentesDeObraPO, DeleteFrenteDeObra } from '../../../../../../../model/pocff/po-cff.actions';
import { UserAuthorizerService } from '../../../../../../../model/user/user-authorizer.service';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { LicitacaoStateModel, LicitacaoDetalhadaModel } from '../../../../../../../model/licitacao/licitacao.state.model';
import { UserState } from '../../../../../../../model/user/user.state';
import { filter, take } from 'rxjs/operators';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { DataExport } from 'src/app/model/data-export';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute } from '@angular/router';
import { BaseComponent } from 'src/app/siconv/util/base.component';

@Component({
  selector: 'vrpl-listagem-frente-de-obra',
  templateUrl: './listagem-frente-de-obra.component.html'
})
export class ListagemFrenteDeObraComponent extends BaseComponent {

  @Select(PoLicitacaoState.frentesDeObraPoDetalhada) frenteDeObraObservable: Observable<FrenteDeObraModel[]>;

  frenteASerExcluida: FrenteDeObraModel;
  modalRef: BsModalRef;
  licitacaoDetalhada: LicitacaoDetalhadaModel;
  poDetalhada: PoModel;
  proponente = false;
  visualizar = false;
  lista: any[];

  export: DataExport;

  constructor(
    protected store: Store,
    private readonly authorizer: UserAuthorizerService,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService,
    private readonly route: ActivatedRoute
  ) {
    super(store);
  }

  init() {}

  onLoad() {

    this.frenteDeObraObservable.subscribe(frentes => {
      this.getExport(frentes);
    });

    this.store.select(UserState).pipe(
      filter(user => user != null),
      take(1)
    ).subscribe(user => this.proponente = user.profile === 'PROPONENTE');

    this.store.select(PoLicitacaoState.poDetalhada).pipe(
      filter(po => po != null),
      take(1)
    ).subscribe(po => this.visualizar = po.apenasVisualizar || false);

  }

  loadActions() {
    return [new LoadFrentesDeObraPO()];
  }

  get podeEditar() {
    return this.authorizer.podeEditarLicitacao() && !this.visualizar;
  }

  deleteFrente(frente: FrenteDeObraModel, template: TemplateRef<any>) {
    this.frenteASerExcluida = frente;
    this.modalRef = this.modalService.show(template);
  }

  confirmaExclusao() {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    const po = licitacaoState.licitacaoDetalhada.po.poDetalhada;
    const idPO = po.id;

    this.store.dispatch(new DeleteFrenteDeObra(idPO, this.frenteASerExcluida))
      .subscribe(() => {
        this.alertMessageService.success('Exclusão da Frente de Obra realizada com sucesso!');
        this.store.dispatch([
          this.store.dispatch(new LoadFrentesDeObraPO())
        ]);
      });

    this.modalRef.hide();
  }

  cancelaExclusao() {
    this.modalRef.hide();
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  getExport(frentes: FrenteDeObraModel[]): FrenteDeObraModel[] {
    const data = [];
    const columns = [
      'Número da Frente de Obra', 'Frente de Obra'
    ];

    frentes.forEach(frente => {
      const linha = [];

      linha.push(frente.numeroFrente);
      linha.push(frente.nomeFrente);

      data.push(linha);
    });

    this.export = new DataExport(columns, data);

    return frentes;
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../../listagem'], {}, { relativeTo: this.route }));
  }

}
