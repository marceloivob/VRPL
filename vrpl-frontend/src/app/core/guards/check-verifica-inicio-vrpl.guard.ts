import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { Store } from '@ngxs/store';
import { PropostaService } from '../../model/proposta/proposta.service';
import { Navigate } from '@ngxs/router-plugin';
import { catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CheckVerificaInicioVrplGuard implements CanActivate {

  constructor(private readonly store: Store, private readonly propostaService: PropostaService) { }

  canActivate(next: ActivatedRouteSnapshot,state: RouterStateSnapshot): Observable<boolean>{

    const ERRO_NAO_EXISTE_SPA_HOMOLOGADA = 540930;
    const ERRO_ACESSO_PROPOSTA_NAO_PERMITIDO = 707016;
    const PROPOSTA_SEM_DADOS_IMPORTADOS = 721422;
    const PROPOSTA_ESTA_SENDO_IMPORTADA = 721423;
    const APOSTILAMENTO_PROCESSO_EXECUCAO_SEM_VRPL = 777320;

    return this.propostaService.verificaInicioVrpl().pipe(
        tap(value => {
            return true;
         }),catchError(err => {
          if (err.error == null ) {
            this.store.dispatch(new Navigate(['erro-carregamento-proposta']));
          } else {
            const error = {
              ticket: err.error.ticket,
              message: err.error.message
            }
            if (err.error.data == null) {
              this.store.dispatch(new Navigate(['erro-carregamento-proposta'], error ));
            } else {
              switch (err.error.data.codigo) {
                case ERRO_NAO_EXISTE_SPA_HOMOLOGADA:
                    this.store.dispatch(new Navigate(['nao-existe-spa-homologado']));
                    break;

                case ERRO_ACESSO_PROPOSTA_NAO_PERMITIDO:
                    this.store.dispatch(new Navigate(['acesso-proposta-nao-permitido-vrpl']));
                    break;

                case PROPOSTA_SEM_DADOS_IMPORTADOS:
                    this.store.dispatch(new Navigate(['proposta-sem-dados-importados']));
                    break;

                case PROPOSTA_ESTA_SENDO_IMPORTADA:
                    this.store.dispatch(new Navigate(['proposta-esta-sendo-importada']));
                    break;

                case APOSTILAMENTO_PROCESSO_EXECUCAO_SEM_VRPL:
                    const params = {
                      ticket: err.error.ticket,
                      message: err.error.data.message,
                    }
                    
                    this.store.dispatch(new Navigate(['erro-apostilamento-processo-sem-vrpl'], params ));
                    break;
  
                default:
                    this.store.dispatch(new Navigate(['erro-carregamento-proposta'], error ));
                    break;
              }
            }
            return of(false);
          }
        })
      );
  }
}
