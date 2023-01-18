import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PageNotFoundComponent } from './core/components/page-not-found/page-not-found.component';
import { NaoExisteSPAHomologadoComponent } from './core/components/nao-existe-spa-homologado-error/nao-existe-spa-homologado.component';
import { PropostaNaoInformadaComponent } from './core/components/proposta-nao-informada/proposta-nao-informada.component';
import { IntegrationsGuard } from './core/guards/integration.guard';
import { ErroCarregamentoPropostaComponent } from './core/components/erro-carregamento-proposta/erro-carregamento-proposta.component';
import { ErroConexaoServidorComponent } from './core/components/erro-conexao-servidor/erro-conexao-servidor.component';
import { AcessoPropostaNaoPermitidoVrplComponent } from './core/components/acesso-proposta-nao-permitido-vrpl/acesso-proposta-nao-permitido-vrpl.component';
import { PropostaSemDadosImportadosComponent } from './core/components/proposta-sem-dados-importados/proposta-sem-dados-importados.component';
import { PropostaEstaSendoImportadaComponent } from './core/components/proposta-esta-sendo-importada/proposta-esta-sendo-importada.component';
import { ErroGenericoComponent } from './core/components/erro-generico/erro-generico.component';


const routes: Routes = [
  { path: '', canActivate: [IntegrationsGuard], redirectTo: '/vrpl/licitacao-lote', pathMatch: 'full' },
  { path: 'vrpl', canActivate: [IntegrationsGuard], loadChildren: './vrpl/vrpl.module#VrplModule' },
  { path: 'nao-existe-spa-homologado', component: NaoExisteSPAHomologadoComponent },
  { path: 'acesso-proposta-nao-permitido-vrpl', component: AcessoPropostaNaoPermitidoVrplComponent},
  { path: 'erro-conexao-servidor', component: ErroConexaoServidorComponent },
  { path: 'proposta-nao-informada', component: PropostaNaoInformadaComponent },
  { path: 'erro-carregamento-proposta', component: ErroCarregamentoPropostaComponent },
  { path: 'proposta-sem-dados-importados', component: PropostaSemDadosImportadosComponent },
  { path: 'proposta-esta-sendo-importada', component: PropostaEstaSendoImportadaComponent },
  { path: 'erro-apostilamento-processo-sem-vrpl', component: ErroGenericoComponent },
  { path: '**', canActivate: [IntegrationsGuard], component: PageNotFoundComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
