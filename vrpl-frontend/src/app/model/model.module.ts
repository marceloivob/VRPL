import { QciState } from './qci/qci.state';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgxsModule } from '@ngxs/store';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { UserState } from './user/user.state';
import { PropostaState } from './proposta/proposta.state';
import { LicitacaoState } from './licitacao/licitacao.state';
import { AppState } from './app/app.state';
import { AppHttpInterceptor } from './app/app.http.interceptor';
import { LicitacaoDetalhadaState } from './licitacao/licitacao.detalhada.state';
import { PoLicitacaoState } from './licitacao/pocff/po-licitacao.state';
import { LaudoState } from './laudo/laudo.state';



@NgModule({
  imports: [
    CommonModule,
    NgxsModule.forFeature([
      AppState, UserState, PropostaState, LicitacaoState, LicitacaoDetalhadaState, PoLicitacaoState, QciState,
       LaudoState
    ]),
  ],
  declarations: [],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AppHttpInterceptor, multi: true },
  ]
})
export class ModelModule { }
