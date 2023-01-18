import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LicitacaoLoteListagemComponent } from './licitacao-lote-listagem/licitacao-lote-listagem.component';


const routes: Routes = [
  { path: '', redirectTo: 'listagem', pathMatch: 'full' },
  { path: 'listagem', component: LicitacaoLoteListagemComponent, data: { breadcrumb: 'Licitações / Lotes' } },
  {
    path: 'detalhar-licitacao-ativa',
    loadChildren: './detalhar/detalhar.module#DetalharModule'
  },
  {
    path: 'detalhar-licitacao-ativa/:id',
    loadChildren: './detalhar/detalhar.module#DetalharModule'
  },
  {
    path: 'detalhar-licitacao-rejeitada',
    loadChildren: './detalhar/detalhar.module#DetalharModule'
  },
  {
    path: 'detalhar-licitacao-rejeitada/:id',
    loadChildren: './detalhar/detalhar.module#DetalharModule'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LicitacaoLoteRoutingModule { }
