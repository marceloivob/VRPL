import { State, Selector, Action, StateContext } from '@ngxs/store';
import { PoLicitacaoModel } from '../../pocff/po-cff.state.model';
import {
  LoadPoCffLicitacao,
  SavePoDadosGerais,
  LoadEventosPO,
  LoadPoDetalhada,
  SaveEvento,
  DeleteEvento,
  LoadFrentesDeObraPO,
  LoadCffSemEventosPO,
  LoadMacrosservicosPO,
  SaveFrenteDeObra,
  DeleteFrenteDeObra,
  LoadEventosServico,
  LoadFrentesDeObraServico,
  SaveCFFSemEventos,
  SaveServicoPo,
  LoadCffComEventosPO,
  SaveCFFComEventos,
  UpdateReferenciaPo,
  LoadPLQ,
  ValidarDataBasePo,
  LoadFrentesDeObraServicoAnalise
} from '../../pocff/po-cff.actions';
import { PoCffService } from '../../pocff/po-cff.service';
import { tap } from 'rxjs/operators';


@State<PoLicitacaoModel>({
  name: 'po',
  defaults: null
})
export class PoLicitacaoState {

  constructor(
    private poCffService: PoCffService
  ) {}

  @Selector()
  static eventosPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaEventos) {
      return state.poDetalhada.listaEventos;
    }

    return null;
  }

  @Selector()
  static poCffs(state: PoLicitacaoModel) {
    return state.poCff;
  }

  @Selector()
  static poDetalhada(state: PoLicitacaoModel) {
    return state.poDetalhada;
  }

  @Selector()
  static frentesDeObraPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaFrentesDeObra) {
      return state.poDetalhada.listaFrentesDeObra;
    }

    return null;
  }

  @Selector()
  static listaParcelasPorMacroServico(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaParcelasPorMacroServico) {
      return state.poDetalhada.listaParcelasPorMacroServico;
    }

    return null;
  }

  @Selector()
  static macrosservicosPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaMacrosservicos) {
      return state.poDetalhada.listaMacrosservicos;
    }

    return null;
  }

  @Selector()
  static poMacrosservicosServicosPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.poMacrosservicosServicos) {
      return state.poDetalhada.poMacrosservicosServicos;
    }

    return null;
  }

  @Selector()
  static eventosServicoPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaEventosServico) {
      return state.poDetalhada.listaEventosServico;
    }

    return null;
  }

  @Selector()
  static frenteDeObraServicoPoDetalhada(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaFrentesDeObraServico) {
      return state.poDetalhada.listaFrentesDeObraServico;
    }

    return null;
  }

  @Selector()
  static servicoFrenteDeObraAnalise(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.listaServicoFrenteDeObraAnalise) {
      return state.poDetalhada.listaServicoFrenteDeObraAnalise;
    }

    return null;
  }

  @Selector()
  static cffComEventos(state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.cffComEventos) {
      return state.poDetalhada.cffComEventos;
    }

    return null;
  }

  @Selector()
  static plq (state: PoLicitacaoModel) {
    if (state.poDetalhada
        && state.poDetalhada.plq) {
      return state.poDetalhada.plq;
    }
    return null;
  }

  @Action(LoadPoCffLicitacao)
  loadPoCff({ patchState }: StateContext<PoLicitacaoModel>, action: LoadPoCffLicitacao) {
    return this.poCffService.carregarListaPoCff(action.idLicitacao).pipe(
      tap(poCff => {
        patchState({
          poCff: poCff
        });
      })
    );
  }

  // FIXME resolver atualização de estado das telas específica
  @Action(SavePoDadosGerais)
  savePoDadosGerais({ patchState }: StateContext<PoLicitacaoModel>, action: SavePoDadosGerais) {
    return this.poCffService.salvar(action.po).pipe(
      tap(() => {
        patchState({
          poDetalhada: null
        });
      })
    );
  }

  @Action(ValidarDataBasePo)
  validarDataBasePo({ patchState }: StateContext<PoLicitacaoModel>, action: ValidarDataBasePo) {
    return this.poCffService.validarDataBase(action.po).pipe(
      tap(() => {
        patchState({
          poDetalhada: null
        });
      })
    );
  }



  @Action(LoadEventosPO)
  loadEventos({ patchState, getState }: StateContext<PoLicitacaoModel>) {

    const po = getState().poDetalhada;
    const idPO = po.id;
    const idPOAnalise = po.idAnalise;
    return this.poCffService.recuperarDadosDaListagem(idPO, idPOAnalise).pipe(
      tap(eventos => {
          patchState({
            poDetalhada: {
              ...po,
              listaEventos: eventos
            }
          });
      })
    );
  }

  @Action(LoadPoDetalhada)
  updatePoDetalhada({ patchState }: StateContext<PoLicitacaoModel>, action: LoadPoDetalhada) {
    return this.poCffService.carregarPoDetalhada(action.idPo).pipe(
      tap(poDetalhada => {
        patchState({
          poDetalhada: {
            ...poDetalhada,
            apenasVisualizar: action.apenasVisualizar
          }
        });
      })
    );
  }

  @Action(SaveEvento)
  saveEvento(ctx: StateContext<PoLicitacaoModel>, action: SaveEvento) {
    return this.poCffService.saveEvento(action.idPO, action.evento);
  }

  @Action(DeleteEvento)
  deleteEvento(ctx: StateContext<PoLicitacaoModel>, action: DeleteEvento) {
    return this.poCffService.deleteEvento(action.idPO, action.evento);
  }

  @Action(LoadFrentesDeObraPO)
  loadFrentesDeObra({ patchState, getState }: StateContext<PoLicitacaoModel>) {

    const po = getState().poDetalhada;
    const idPO = po.id;
    return this.poCffService.recuperarFrentesDeObraDaListagem(idPO).pipe(
      tap(frentes => {
          patchState({
            poDetalhada: {
              ...po,
              listaFrentesDeObra: frentes
            }
          });
      })
    );
  }

  @Action(SaveFrenteDeObra)
  saveFrenteDeObra(ctx: StateContext<PoLicitacaoModel>, action: SaveFrenteDeObra) {
    return this.poCffService.saveFrenteDeObra(action.idPO, action.frenteDeObra);
  }

  @Action(DeleteFrenteDeObra)
  deleteFrenteDeObra(ctx: StateContext<PoLicitacaoModel>, action: DeleteFrenteDeObra) {
    return this.poCffService.deleteFrenteDeObra(action.idPO, action.frenteDeObra);
  }

  @Action(LoadCffSemEventosPO)
  loadCffSemEventos({ patchState, getState }: StateContext<PoLicitacaoModel>) {

    const po = getState().poDetalhada;
    const idPO = po.id;
    const idPOAnalise = po.idAnalise;
    return this.poCffService.recuperarCffSemEventosDaListagem(idPO, idPOAnalise).pipe(
      tap(cff => {
          patchState({
            poDetalhada: {
              ...po,
              listaParcelasPorMacroServico: cff
            }
          });
      })
    );
  }

  @Action(SaveCFFSemEventos)
  saveCFFSemEventos(ctx: StateContext<PoLicitacaoModel>, action: SaveCFFSemEventos) {
    return this.poCffService.saveCFFSemEventos(action.idPO, action.macroServico);
  }


  @Action(LoadMacrosservicosPO)
  loadMacrosservicosPO({ patchState, getState }: StateContext<PoLicitacaoModel>) {
    const po = getState().poDetalhada;
    const idPO = po.id;
    const idPOAnalise = po.idAnalise;
    return this.poCffService.loadMacrosservicosPo(idPO, idPOAnalise).pipe(
      tap(poMacrosservicoServicos => {
        patchState({
          poDetalhada: {
            ...po,
            listaMacrosservicos: poMacrosservicoServicos.macrosservicos,
            poMacrosservicosServicos: poMacrosservicoServicos
          }
        });
      })
    );
  }

  @Action(LoadEventosServico)
  loadEventosServico({ patchState, getState }: StateContext<PoLicitacaoModel>, action: LoadEventosServico) {
    const po = getState().poDetalhada;
    return this.poCffService.loadEventosServicoPo(po.id).pipe(
      tap(eventos => {
        patchState({
          poDetalhada: {
            ...po,
            listaEventosServico: eventos
          }
        });
      })
    );
  }

  @Action(LoadFrentesDeObraServico)
  LoadFrentesDeObraServico({ patchState, getState }: StateContext<PoLicitacaoModel>, action: LoadFrentesDeObraServico) {
    const po = getState().poDetalhada;
    return this.poCffService.loadFrentesDeObraServicoPo(action.macrosservicoId, action.servicoId).pipe(
      tap(frentes => {
        patchState({
          poDetalhada: {
            ...po,
            listaFrentesDeObraServico: frentes
          }
        });
      })
    );
  }

  @Action(LoadFrentesDeObraServicoAnalise)
  LoadFrentesDeObraServicoAnalise({ patchState, getState }: StateContext<PoLicitacaoModel>, action: LoadFrentesDeObraServicoAnalise) {
    const po = getState().poDetalhada;
    return this.poCffService.loadServicoFrentesDeObraAnalise(action.servicoId).pipe(
      tap(servicosFrenteObra => {
        patchState({
          poDetalhada: {
            ...po,
            listaServicoFrenteDeObraAnalise: servicosFrenteObra
          }
        });
      })
    );
  }

  @Action(SaveServicoPo)
  saveServicoPo(ctx: StateContext<PoLicitacaoModel>, action: SaveServicoPo) {
    return this.poCffService.saveServicoPo(action.servico);
  }

  @Action(LoadCffComEventosPO)
  LoadCffComEventosPO({ patchState, getState }: StateContext<PoLicitacaoModel>) {
    const po = getState().poDetalhada;
    return this.poCffService.recuperarCffComEventosDaListagem(po.id).pipe(
      tap(cff => {
          patchState({
            poDetalhada: {
              ...po,
              cffComEventos: cff
            }
          });
      })
    );
  }

  @Action(SaveCFFComEventos)
  saveCFFComEventos(ctx: StateContext<PoLicitacaoModel>, action: SaveCFFComEventos) {
    return this.poCffService.saveCFFComEventos(action.idPO, action.idEvento, action.eventoCff);
  }

  @Action(UpdateReferenciaPo)
  updateReferenciaPo(ctx: StateContext<PoLicitacaoModel>, action: UpdateReferenciaPo) {
    return this.poCffService.atualizarReferenciaPo(action.idPO, action.referencia, action.versao);
  }

  @Action(LoadPLQ)
  LoadPLQ({ patchState, getState }: StateContext<PoLicitacaoModel>) {
    const po = getState().poDetalhada;
    return this.poCffService.recuperarPLQ(po.id).pipe(
      tap(plqVar => {
          patchState({
            poDetalhada: {
              ...po,
              plq: plqVar
            }
          });
      })
    );
  }

}

