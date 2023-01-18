import { State, Selector, Action, StateContext } from '@ngxs/store';
import { LicitacaoDetalhadaModel } from './licitacao.state.model';
import { LoadAnexosLicitacao, DeleteAnexo, SaveAnexo, LoadAnexosPorTipo } from '../anexo/anexo.actions';
import { AnexoService } from '../anexo/anexo.service';
import { tap } from 'rxjs/operators';
import { QciService } from '../qci/qci.service';
import { LoadQciLicitacao } from '../qci/qci.actions';
import { PoLicitacaoState } from './pocff/po-licitacao.state';
import { QuadroResumoService } from '../quadroresumo/quadroresumo.service';
import { LoadQuadroResumo, SaveNovoEstadoDocumentacao } from '../quadroresumo/quadroresumo.actions';
import { LoadUfs } from '../siconv/uf.actions';
import { UFService } from '../siconv/uf.service';
import { PropostaState } from '../proposta/proposta.state';
import { PropostaStateModel } from '../proposta/proposta.state.model';

@State<LicitacaoDetalhadaModel>({
  name: 'licitacaoDetalhada',
  defaults: null,
  children: [PoLicitacaoState]
})
export class LicitacaoDetalhadaState {

  constructor(
    private anexoService: AnexoService,
    private qciService: QciService,
    private ufService: UFService,
    private quadroResumoService: QuadroResumoService
  ) {}

  @Selector()
  static anexosLicitacaoDetalhada(state: LicitacaoDetalhadaModel) {
    return state.anexos;
  }

  @Selector()
  static anexosComponente(state: LicitacaoDetalhadaModel) {
    return state.anexosParecer;
  }

  @Selector()
  static ufsLicitacaoDetalhada(state: LicitacaoDetalhadaModel) {
    return state.ufs;
  }

  @Selector()
  static qciLicitacaoDetalhada(state: LicitacaoDetalhadaModel) {
    return state.qci;
  }

  @Selector()
  static quadroResumoLicitacaoDetalhada(state: LicitacaoDetalhadaModel) {
    return state.quadroResumo;
  }

  @Action(LoadAnexosLicitacao)
  loadAnexo({ patchState, getState }: StateContext<LicitacaoDetalhadaModel>) {
    const id = getState().id;
    return this.anexoService.recuperarDadosDaListagem(id).pipe(
      tap(anexos => {
        patchState({
          anexos: anexos
        });
      })
    );
  }

  @Action(DeleteAnexo)
  deleteAnexo(ctx: StateContext<LicitacaoDetalhadaModel>, action: DeleteAnexo) {
    return this.anexoService.deleteAnexo(action.anexo);
  }

  @Action(SaveAnexo)
  saveAnexo(ctx: StateContext<LicitacaoDetalhadaModel>, action: SaveAnexo) {
    return this.anexoService.salvarAnexo(action.idLicitacao, action.anexo);
  }

  @Action(LoadUfs)
  loadUfs({ patchState, getState }: StateContext<LicitacaoDetalhadaModel>) {
    return this.ufService.listarUfs().pipe(
      tap(ufs => {
        patchState({
          ufs: ufs
        });
      })
    );
  }

  @Action(LoadQciLicitacao)
  loadQci({ getState, patchState }: StateContext<LicitacaoDetalhadaModel>) {
    const idLicitacao = getState().id;
    return this.qciService.loadQci(idLicitacao).pipe(
      tap(qci => {
        patchState({
          qci: qci
        });
      })
    );
  }

  @Action(LoadQuadroResumo)
  loadQuadroResumo( { patchState, getState }: StateContext<LicitacaoDetalhadaModel>, proposta: LoadQuadroResumo) {
    const idLicitacao = getState().id;
    const versaoDaPropostaSelecionada = proposta.versaoDaProposta;

    return this.quadroResumoService.recuperarHistoricoLicitacaoListagem(idLicitacao, versaoDaPropostaSelecionada).pipe(
      tap(quadroResumo => {
        patchState({
          quadroResumo: quadroResumo
        });
      })
    );
  }

  @Action(SaveNovoEstadoDocumentacao)
  saveNovoEstadoDocumentacaosaveAnexo(ctx: StateContext<LicitacaoDetalhadaModel>, action: SaveNovoEstadoDocumentacao) {
    return this.quadroResumoService.saveNovoEstadoDocumentacao(action.historico);
  }

  @Action(LoadAnexosPorTipo)
  loadAnexosPorTipo({ patchState, getState }: StateContext<LicitacaoDetalhadaModel>, action: LoadAnexosPorTipo) {
    const id = getState().id;
    return this.anexoService.recuperarAnexosPorTipo(id, action.tipoAnexo).pipe(
      tap(anexos => {
        patchState({
          anexosParecer: anexos
        });
      })
    );
  }

}
