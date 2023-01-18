import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { Subscription, of, Observable } from 'rxjs';
import { SaveCFFComEventos } from 'src/app/model/pocff/po-cff.actions';
import { Navigate } from '@ngxs/router-plugin';
import { LicitacaoStateModel } from 'src/app/model/licitacao/licitacao.state.model';
import { PoDetalhadaModel, EventoCFFcomEventosModel, FrenteDeObraCFFcomEventosModel } from 'src/app/model/pocff/po-cff.state.model';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-cadastro-cff-com-eventos',
  templateUrl: './cadastro-cff-com-eventos.component.html'
})
export class CadastroCffComEventosComponent implements OnInit, OnDestroy {

  visualizar: boolean;
  actionSubscription$: Subscription;
  po: PoDetalhadaModel;
  parcelasInvalidas: boolean;
  eventoCff: EventoCFFcomEventosModel;
  qtdMeses: number;
  listaMeses: number[];

  constructor(
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private alertMessageService: AlertMessageService,
    private readonly actions: Actions) { }

  ngOnInit() {
    this.visualizar = (this.route.snapshot.url[0].path === 'detalhar');

    this.subscribeActions();

    this.parcelasInvalidas = false;

    this.initForm().subscribe(
      evento => {

      const frentesObra: FrenteDeObraCFFcomEventosModel[] = [];

      for (const frente of evento.listaFrenteObras) {
        frentesObra.push({
          idFrenteObra: frente.idFrenteObra,
          numeroFrenteObra: frente.numeroFrenteObra,
          nomeFrenteObra: frente.nomeFrenteObra,
          mesConclusao: frente.mesConclusao,
          versao: frente.versao
        });
      }

      this.eventoCff = {
        idEvento: evento.idEvento,
        numeroEvento: evento.numeroEvento,
        tituloEvento: evento.tituloEvento,
        listaFrenteObras: frentesObra,
        versao: evento.versao
      };

      this.listaMeses = [];
      for (let i = 0; i < this.qtdMeses; i++) {
        this.listaMeses.push(i + 1);
      }
    });
  }

  subscribeActions() {
    this.actionSubscription$ = this.actions
      .pipe(ofActionSuccessful(SaveCFFComEventos))
      .subscribe(() => {
        this.alertMessageService.success('Alteração dos dados de conclusão do Evento por Frentes de Obra realizada com sucesso.');
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

  initForm(): Observable<EventoCFFcomEventosModel> {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    this.po = licitacaoState.licitacaoDetalhada.po.poDetalhada;

    this.qtdMeses = this.po.cffComEventos.qtdMeses;

    const idEvento = this.route.snapshot.paramMap.get('id');

    if (idEvento) {
      return of(this.po.cffComEventos.listaDeEventoCFF.find(
        evento => evento.idEvento === parseInt(idEvento, 10)));
    }
  }

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  changeMes(idFrenteObra: number, mes: string) {

    for (const f of this.eventoCff.listaFrenteObras) {
      if (f.idFrenteObra === idFrenteObra) {
        f.mesConclusao = +mes;
      }
    }
  }

  onSubmit() {
    const frentesObra: FrenteDeObraCFFcomEventosModel[] = [];

    for (const f of this.eventoCff.listaFrenteObras) {
      if (f.mesConclusao === 0) {
        this.alertMessageService.error('Favor preencher o número do período de conclusão do evento para todas as frentes de obra listadas.');
        return;
      }
      frentesObra.push({
        idFrenteObra: f.idFrenteObra,
        numeroFrenteObra: f.numeroFrenteObra,
        nomeFrenteObra: f.nomeFrenteObra,
        mesConclusao: f.mesConclusao,
        versao: f.versao
      });
    }

    const evento: EventoCFFcomEventosModel = {
      idEvento: this.eventoCff.idEvento,
      numeroEvento: this.eventoCff.numeroEvento,
      tituloEvento: this.eventoCff.tituloEvento,
      listaFrenteObras: frentesObra,
      versao: this.eventoCff.versao
    };

    this.store.dispatch(new SaveCFFComEventos(Number(this.po.id), this.eventoCff.idEvento, evento));
  }

}
