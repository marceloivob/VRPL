import { CheckVerificaInicioVrplGuard} from './core/guards/check-verifica-inicio-vrpl.guard';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, LOCALE_ID, ErrorHandler, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import localeptBRExtra from '@angular/common/locales/extra/br';
import { NgxsModule } from '@ngxs/store';
import { NgxsRouterPluginModule } from '@ngxs/router-plugin';
import { NgxsLoggerPluginModule } from '@ngxs/logger-plugin';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { ModelModule } from './model/model.module';
import { SecurityGuard } from './core/guards/security.guard';
import { httpInterceptorProviders } from './core/interceptors';
import { SpinnerComponent } from './core/components/spinner/spinner.component';
import { NaoExisteSPAHomologadoComponent } from './core/components/nao-existe-spa-homologado-error/nao-existe-spa-homologado.component';
import { PropostaNaoInformadaComponent } from './core/components/proposta-nao-informada/proposta-nao-informada.component';
import { IntegrationsGuard } from './core/guards/integration.guard';
import { ErroCarregamentoPropostaComponent } from './core/components/erro-carregamento-proposta/erro-carregamento-proposta.component' ;
import { ComponentSiconvModule } from './componente-siconv.module';
import { SimpleTimer } from 'ng2-simple-timer';
import { SentryErrorHandler } from './core/sentry.error.handler';
import { AppConfig } from './core/app.config';
import { ErroConexaoServidorComponent } from './core/components/erro-conexao-servidor/erro-conexao-servidor.component';
import { AcessoPropostaNaoPermitidoVrplComponent} from '././core/components/acesso-proposta-nao-permitido-vrpl/acesso-proposta-nao-permitido-vrpl.component';
import { SiconvModule } from './siconv/siconv.module';
import { PropostaSemDadosImportadosComponent } from './core/components/proposta-sem-dados-importados/proposta-sem-dados-importados.component';
import { PropostaEstaSendoImportadaComponent } from './core/components/proposta-esta-sendo-importada/proposta-esta-sendo-importada.component';
import { ErroGenericoComponent } from './core/components/erro-generico/erro-generico.component';


export function loadSettings() {
  return () => AppConfig.loadSettings();
}

registerLocaleData(localePt, localeptBRExtra);

@NgModule({
  declarations: [
    AppComponent,
    PageNotFoundComponent,
    NaoExisteSPAHomologadoComponent,
    AcessoPropostaNaoPermitidoVrplComponent,
    SpinnerComponent,
    PropostaNaoInformadaComponent,
    ErroConexaoServidorComponent,
    ErroCarregamentoPropostaComponent,
    PropostaSemDadosImportadosComponent,
    PropostaEstaSendoImportadaComponent,
    ErroGenericoComponent
  ],
  exports: [
    ComponentSiconvModule
  ],
  imports: [
    BrowserModule,
    SiconvModule,
    HttpClientModule,
    AppRoutingModule,
    ComponentSiconvModule,
    NgxsModule.forRoot([], { developmentMode: false  }),
    NgxsRouterPluginModule.forRoot(),
    NgxsLoggerPluginModule.forRoot({
      disabled: false
    }),
    ModelModule
  ],
  providers: [
    { provide: APP_INITIALIZER, useFactory: loadSettings, deps: [], multi: true },
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    // Comentário temporário enquanto não resolve a questão do Endpoint de recebimento das mensagens do Sentry
    // { provide: ErrorHandler, useClass: SentryErrorHandler },
    IntegrationsGuard,
    SecurityGuard,
    CheckVerificaInicioVrplGuard,
    httpInterceptorProviders,
    SimpleTimer
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
