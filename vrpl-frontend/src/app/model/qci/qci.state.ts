import { QciStateModel, QciExternoStateModel } from './qci.state.model';
import { State, Selector, Action, StateContext } from '@ngxs/store';
import { QciService } from './qci.service';
import { LoadQciProposta, SaveSubmetaVRPL } from './qci.actions';
import { tap } from 'rxjs/operators';


@State<QciStateModel>({
  name: 'qci',
  defaults: {
  id: null,
  idQciVrpl: null,
  vlRepasse: 0,
  vlContrapartida: 0,
  vlTotal: 0,
  vlOutros: 0,
  diferencaVlRepasseParaSubmetasSociais: 0,
  diferencaVlContrapartidaParaSubmetasSociais: 0,
  diferencaVlTotalLicitadoParaSubmetasSociais: 0,
  diferencaVlRepasseParaDemaisMetas: 0,
  diferencaVlContrapartidaParaDemaisMetas: 0,
  diferencaVlTotalLicitadoParaDemaisMetas: 0,
  possuiSubmetaSocial: false,
  difSocialRepasseLicitado: 0,
  difSocialContrapartidaLicitada: 0,
  difSocialTotalLicitado: 0,
  difMetasRepasseLicitado: 0,
  difMetasContrapartidaLicitada: 0,
  difMetasTotalLicitado: 0,
  difValorOrcadoRepasseLicitado: 0,
  difValorOrcadoContrapartidaLicitada: 0,
  difValorOrcadoTotalLicitado: 0,
  modalidade: ''
  }
})
export class QciState {

  constructor(
    private qciService: QciService
  ) { }

  @Selector()
  static metas(state: QciStateModel) {
    if (state.metas) {
      return state.metas;
    }
    return null;
  }

  @Action(LoadQciProposta)
  updateQci({ patchState }: StateContext<QciExternoStateModel>, action: LoadQciProposta) {
    return this.qciService.loadQciProposta(action.versaoDaProposta).pipe(
       tap(qci => patchState(qci))
    );
  }

  @Action(SaveSubmetaVRPL)
  salvarSubmetaVrpl(ctx: StateContext<QciStateModel>, action: SaveSubmetaVRPL) {
    return this.qciService.salvarSubmeta(action.submeta);
  }
}
