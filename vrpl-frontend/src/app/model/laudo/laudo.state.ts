import { Action, State, Selector, StateContext } from '@ngxs/store';
import { LaudoService } from './laudo.service';
import { LaudoStateModel } from './laudo.state.model';
import { LoadPerguntas, LoadLaudo, LoadPendencias, DeletePendencia, SaveLaudo, QuemEmitiuLaudo } from './laudo.actions';
import { tap } from 'rxjs/operators';

@State<LaudoStateModel>({
    name: 'laudo',
    defaults: {}
})
export class LaudoState {

    constructor(
      private laudoService: LaudoService
    ) { }

  @Selector()
  static templateLaudo(state: LaudoStateModel) {
    if (state.template) {
      return state.template;
    }
    return null;
  }

  @Selector()
  static parecer(state: LaudoStateModel) {
    if (state.parecer) {
      return state.parecer;
    }
    return null;
  }

  @Selector()
  static pendencias(state: LaudoStateModel) {
    if (state.pendencias) {
      return state.pendencias;
    }
    return null;
  }

  @Selector()
  static quemEmitiu(state: LaudoStateModel) {
    if (state.quemEmitiu) {
      return state.quemEmitiu;
    }
    return null;
  }

  @Action(LoadPerguntas)
  loadPerguntas(ctx: StateContext<LaudoStateModel>, action: LoadPerguntas) {
    return this.laudoService.carregarPerguntas(action.tipoTemplateLaudo, action.idLicitacao, action.idVersaoDaLicitacao).pipe(
      tap(template => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          template: template
        });
      })
    );
  }

  @Action(LoadLaudo)
  loadLaudo(ctx: StateContext<LaudoStateModel>, action: LoadLaudo) {
    return this.laudoService.carregarLaudo(action.idLicitacao, action.tipoLaudo).pipe(
      tap(parecer => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          parecer: parecer
        });
      })
    );
  }

  @Action(LoadPendencias)
  loadPendencias(ctx: StateContext<LaudoStateModel>, action: LoadPendencias) {
    return this.laudoService.carregarPendencias(action.idLaudo).pipe(
      tap(pendencias => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          pendencias: pendencias
        });
      })
    );
  }

  @Action(DeletePendencia)
  deletePendencia(ctx: StateContext<LaudoStateModel>, action: DeletePendencia) {
    return this.laudoService.deletePendencia(action.idPendencia);
  }

  @Action(SaveLaudo)
  SaveLaudo(ctx: StateContext<LaudoStateModel>, action: SaveLaudo) {
    return this.laudoService.salvarLaudo(action.laudo);
  }

  @Action(QuemEmitiuLaudo)
  quemEmitiuLaudo(ctx: StateContext<LaudoStateModel>, action: QuemEmitiuLaudo) {
    return this.laudoService.quemEmitiuLaudo(action.idLicitacao, action.tipoLaudo).pipe(
      tap(quemEmitiu => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          quemEmitiu: quemEmitiu
        });
      })
    );
  }
}
