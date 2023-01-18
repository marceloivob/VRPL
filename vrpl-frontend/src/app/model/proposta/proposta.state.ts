import { State, Action, StateContext, Store, Selector } from '@ngxs/store';
import { tap, catchError } from 'rxjs/operators';

import { PropostaStateModel } from './proposta.state.model';
import { LoadProposta, VerificaFornecedorObsoleto } from './proposta.actions';
import { PropostaService } from './proposta.service';
import { forkJoin, of } from 'rxjs';
import { SiconvLegacyService } from '../siconv/siconv-legacy.service';
import { Navigate } from '@ngxs/router-plugin';

@State<PropostaStateModel>({
  name: 'proposta',
  defaults: null
})
export class PropostaState {

  constructor(
    private readonly propostaService: PropostaService,
    private readonly siconvService: SiconvLegacyService) { }

  @Action(LoadProposta)
  login(ctx: StateContext<PropostaStateModel>, proposta: LoadProposta) {
    return forkJoin(
        this.propostaService.loadProposta(proposta.versaoDaProposta)
          .pipe(catchError( err => {
            console.error('Falha carregamento Proposta!', err);
            ctx.dispatch(new Navigate(['erro-carregamento-proposta']));
            throw err;
          })),
        this.siconvService.loadUrlRetornoSiconv()
          .pipe(catchError( err => {
            console.error('Falha integração SICONV!!');
            return of('');
          })),
        this.siconvService.getMenu().pipe(catchError( err => {
          console.error('Falha integração SICONV!!');
          return of({InfoConvenio: ''});
        }))
      ).pipe(
        tap(([propostaOriginal, urlRetorno, menu]) => {
          const propostaModel: PropostaStateModel = {
            ...propostaOriginal,
            urlRetornoSiconv: urlRetorno,
            infoConvenio: menu.InfoConvenio,
            versaoSelecionada: propostaOriginal.versaoSelecionada
          };
          ctx.patchState(propostaModel);
        })
      );
  }

  @Action(VerificaFornecedorObsoleto)
  verificaFornecedorObsoleto(ctx: StateContext<PropostaStateModel>, action: VerificaFornecedorObsoleto) {
    return this.propostaService.verificaFornecedorObsoleto();
  }

}
