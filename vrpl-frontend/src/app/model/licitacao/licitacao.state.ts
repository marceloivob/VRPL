
import { State, Action, StateContext, Selector } from '@ngxs/store';
import { tap } from 'rxjs/operators';
import {
  AssociarLotes,
  LoadLicitacaoDetalhada,
  RemoverAssociacaoLotes,
  SavePendencia,
  LoadLicitacoesAtivasProposta,
  LoadLicitacoesRejeitadasProposta,
  LoadLotesAtivosProposta,
  LoadLotesRejeitadosProposta,
  LoadAnexosDaLicitacao,
  SincronizaLicitacoesProposta
} from './licitacao.actions';
import { LicitacaoService } from './licitacao.service';
import { LicitacaoStateModel } from './licitacao.state.model';
import { LoadTipoDeAnexo } from '../anexo/anexo.actions';
import { AnexoService } from '../anexo/anexo.service';
import { LicitacaoDetalhadaState } from './licitacao.detalhada.state';
import { LaudoModel } from '../laudo/laudo.state.model';

@State<LicitacaoStateModel>({
  name: 'licitacao',
  defaults: {},
  children: [LicitacaoDetalhadaState]
})
export class LicitacaoState {

  constructor(
    private licitacaoService: LicitacaoService,
    private anexoService: AnexoService
  ) { }

  // FIXME appState
  @Selector()
  static tiposAnexos(state: LicitacaoStateModel) {
    if (state.tiposAnexos) {
      return state.tiposAnexos;
    }
    return null;
  }

  @Selector()
  static licitacoesAtivasProposta(state: LicitacaoStateModel) {
    if (state.licitacoesProposta) {
      return state.licitacoesProposta;
    }
    return null;
  }

  @Selector()
  static licitacoesRejeitadasProposta(state: LicitacaoStateModel) {
    if (state.licitacoesRejeitadasProposta) {
      return state.licitacoesRejeitadasProposta;
    }
    return null;
  }


  @Selector()
  static lotesProposta(state: LicitacaoStateModel) {
    if (state.lotes) {
      return state.lotes;
    }
    return [];
  }

  @Selector()
  static anexosDaLicitacao(state: LicitacaoStateModel) {
    if(state.anexos) {
      return state.anexos;
    }
    return [];
  }

  @Action(LoadLicitacoesAtivasProposta)
  loadLicitacoesAtivas(ctx: StateContext<LicitacaoStateModel>, licitacoesProposta: LoadLicitacoesAtivasProposta) {
    return this.licitacaoService.loadLicitacoesAtivasProposta(licitacoesProposta.versaoSelecionadaDaProposta).pipe(
      tap(licitacoes => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          licitacoesProposta: licitacoes
        });
      })
    );
  }


  @Action(SincronizaLicitacoesProposta)
  sincronizarLicitacoesProposta(ctx: StateContext<LicitacaoStateModel>) {
    return this.licitacaoService.sincronizaLicitacoesDaPropostaComSICONV().pipe(
      tap(licitacoes => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          licitacoesProposta: licitacoes
        });
      })
    );
  }

  @Action(LoadLicitacoesRejeitadasProposta)
  loadLicitacoesRejeitadas(ctx: StateContext<LicitacaoStateModel>, licitacoesProposta: LoadLicitacoesRejeitadasProposta) {
    return this.licitacaoService.loadLicitacoesRejeitadasProposta(licitacoesProposta.versaoSelecionadaDaProposta).pipe(
      tap(licitacoes => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          licitacoesRejeitadasProposta: licitacoes
        });
      })
    );
  }


  @Action(AssociarLotes)
  associarLotes(ctx: StateContext<LicitacaoStateModel>, action: AssociarLotes) {
    return this.licitacaoService
      .associarLotes(action.licitacaoLote);
  }

  @Action(RemoverAssociacaoLotes)
  removerAssociacaoLotes(ctx: StateContext<LicitacaoStateModel>, action: RemoverAssociacaoLotes) {
    return this.licitacaoService
      .removerAssociacaoLotes(action.idLicitacao);
  }

  @Action(LoadLotesAtivosProposta)
  updateLotesAtivosNaoAssociados(ctx: StateContext<LicitacaoStateModel>, lotesProposta: LoadLotesAtivosProposta) {
    return this.licitacaoService.loadAtivosLotesProposta(lotesProposta.versaoSelecionadaDaProposta).pipe(
      tap(lotes => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          lotes: lotes
        });
      })
    );
  }

  @Action(LoadLotesRejeitadosProposta)
  updateLotesRejeitadosNaoAssociados(ctx: StateContext<LicitacaoStateModel>, lotesProposta: LoadLotesRejeitadosProposta) {
    return this.licitacaoService.loadRejeitadosLotesProposta(lotesProposta.versaoSelecionadaDaProposta).pipe(
      tap(lotes => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          lotes: lotes
        });
      })
    );
  }


  @Action(LoadLicitacaoDetalhada)
  updateLicitacaoDetalhada({ patchState }: StateContext<LicitacaoStateModel>, action: LoadLicitacaoDetalhada) {
    const id = action.idLicitacao;
    if (id) {
      return this.licitacaoService.loadLicitacaoDetalhada(action.idLicitacao).pipe(
        tap(licitacao => patchState({ licitacaoDetalhada: licitacao }))
      );
    } else {
      return patchState({ licitacaoDetalhada: null });
    }
  }

  @Action(LoadTipoDeAnexo)
  loadTipoDeAnexo({ patchState }: StateContext<LicitacaoStateModel>) {
    return this.anexoService.recuperarDominioDoTipoDeAnexo().pipe(
      tap(tiposDeAnexos => patchState({
        tiposAnexos: tiposDeAnexos
      }))
    );
  }

  @Action(SavePendencia)
  savePendencia(ctx: StateContext<LaudoModel>, action: SavePendencia) {
    return this.licitacaoService.savePendencia(action.pendencia);
  }

  @Action(LoadAnexosDaLicitacao)
  loadAnexosDaLicitacao(ctx: StateContext<LicitacaoStateModel>, action: LoadAnexosDaLicitacao) {
    return this.anexoService.recuperarDadosDaListagem(action.idLicitacao).pipe(
      tap(anexos => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
        anexos: anexos
      });
    }))
  }

}
