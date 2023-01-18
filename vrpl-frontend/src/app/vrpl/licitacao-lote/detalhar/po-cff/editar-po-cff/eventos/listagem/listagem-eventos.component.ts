import { EventoModel } from './../../../../../../../model/pocff/po-cff.state.model';
import { DeleteEvento, LoadEventosPO } from './../../../../../../../model/pocff/po-cff.actions';
import { LicitacaoDetalhadaModel, LicitacaoStateModel } from './../../../../../../../model/licitacao/licitacao.state.model';
import { Component, OnInit, TemplateRef, OnDestroy } from '@angular/core';
import { Select, Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { Observable, Subscription } from 'rxjs';
import { BsModalService, BsModalRef } from 'ngx-bootstrap';
import { filter, take } from 'rxjs/operators';
import { UserState } from 'src/app/model/user/user.state';
import { PoModel } from 'src/app/model/pocff/po-cff.state.model';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { UserAuthorizerService } from '../../../../../../../model/user/user-authorizer.service';
import { AlertMessageService } from '@serpro/ngx-siconv';
import { DatePipe } from '@angular/common';
import { DataExport } from 'src/app/model/data-export';
import { onLoad } from '@sentry/browser';
import { ActivatedRoute } from '@angular/router';
import { Navigate } from '@ngxs/router-plugin';

@Component({
  selector: 'vrpl-listagem--eventos',
  templateUrl: './listagem-eventos.component.html'
})
export class ListagemEventosComponent implements OnInit, OnDestroy {

  // Grid
  export: DataExport;

  @Select(PoLicitacaoState.eventosPoDetalhada) eventosObservable: Observable<EventoModel[]>;

  licitacaoDetalhada: LicitacaoDetalhadaModel;
  poDetalhada: PoModel;

  eventoASerExcluido: EventoModel;

  actionSubscription$: Subscription;
  modalRef: BsModalRef;
  proponente = false;

  visualizar = false;
  lista: any[];

  constructor(
    private readonly store: Store,
    private readonly actions$: Actions,
    private alertMessageService: AlertMessageService,
    private readonly authorizer: UserAuthorizerService,
    private readonly modalService: BsModalService,
    private readonly route: ActivatedRoute
  ) { }

  ngOnInit() {

    this.store.dispatch(new LoadEventosPO()).subscribe(() => { this.onLoad(); });

    this.store.select(UserState).pipe(
      filter(user => user != null),
      take(1)
    ).subscribe(user => this.proponente = user.profile === 'PROPONENTE');

    this.store.select(PoLicitacaoState.poDetalhada).pipe(
      filter(po => po != null),
      take(1)
    ).subscribe(po => this.visualizar = po.apenasVisualizar || false);

    this.actionSubscription$ = this.actions$
      .pipe(ofActionSuccessful(DeleteEvento))
      .subscribe(() => {
        this.alertMessageService.success('Exclusão do evento realizada com sucesso!');
        this.store.dispatch(
          this.store.dispatch(new LoadEventosPO())
        );
      });
  }

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  get podeEditar() {
    return this.authorizer.podeEditarLicitacao() && !this.visualizar;
  }

  deleteEvento(evento: EventoModel, template: TemplateRef<any>) {
    this.eventoASerExcluido = evento;
    this.modalRef = this.modalService.show(template);

  }

  confirmaExclusao() {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    const po = licitacaoState.licitacaoDetalhada.po.poDetalhada;
    const idPO = po.id;

    this.store.dispatch(new DeleteEvento(idPO, this.eventoASerExcluido));

    this.modalRef.hide();
  }

  cancelaExclusao() {
    this.modalRef.hide();
  }

  getListaPaginada(listap) {
    this.lista = listap;
  }

  onLoad() {
    this.eventosObservable.subscribe(eventos => {
      this.getExport(eventos);
    });
  }

  getExport(eventos: EventoModel[]): EventoModel[] {
    const data = [];
    const columns = [
      'Número do Evento',
      'Título do Evento'
    ];
    const datepipe = new DatePipe('PT-BR');
    eventos.forEach(evento => {
      const linha = [];

      linha.push(evento.numeroEvento);
      linha.push(evento.tituloEvento);
      data.push(linha);
    });

    this.export = new DataExport(columns, data);

    return eventos;
  }

  voltar() {
    this.store.dispatch(new Navigate(['../../../../listagem'], {}, { relativeTo: this.route }));
  }
}
