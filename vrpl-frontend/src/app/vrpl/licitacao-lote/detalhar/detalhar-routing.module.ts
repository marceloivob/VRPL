import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DetalharComponent } from './detalhar.component';
import { LicitacaoDetalhadaComponent } from './licitacao-detalhada/licitacao-detalhada.component';
import { AnexosComponent } from './anexos/anexos.component';
import { ListagemAnexosComponent } from './anexos/listagem-anexos/listagem-anexos.component';
import { CadastroAnexoComponent } from './anexos/cadastro-anexo/cadastro-anexo.component';
import { QciInternoComponent } from './qci-interno/qci-interno.component';
import { ListagemQciComponent } from './qci-interno/listagem-qci/listagem-qci.component';
import { CadastroQciComponent } from './qci-interno/cadastro-qci/cadastro-qci.component';
import { ListagemPoCffComponent } from './po-cff/listagem-po-cff/listagem-po-cff.component';
import { PoCffComponent } from './po-cff/po-cff.component';
import { VrplComponent } from './vrpl/vrpl.component';
import { QuadroResumoComponent } from './quadro-resumo/quadro-resumo.component';
import { AssociarLoteComponent } from './associar-lote/associar-lote.component';
import { VrplsComponent } from './vrpls/vrpls.component';

const editar = 'editar/:id';
const detalhar = 'detalhar/:id';
const listagem = 'listagem';
const salvar = 'salvar';
const routes: Routes = [
  { path: '', redirectTo: 'associar-lote', pathMatch: 'full' },
  { path: 'anexos', redirectTo: 'anexos/listagem', pathMatch: 'full' },
  { path: 'po-cff', redirectTo: 'po-cff/listagem', pathMatch: 'full' },
  { path: 'qci', redirectTo: 'qci/listagem', pathMatch: 'full' },
  {
    path: '',
    component: DetalharComponent, data: { breadcrumb: 'Detalhar Licitação' } ,
    children: [
      { path: 'licitacao', component: LicitacaoDetalhadaComponent },
      { path: 'associar-lote', component: AssociarLoteComponent, data: { breadcrumb: 'Associação de Lotes e Licitações' } },
      { path: 'associar-lote/:id', component: AssociarLoteComponent, data: { breadcrumb: 'Associação de Lotes e Licitações' } },
      {
        path: 'anexos',
        component: AnexosComponent, data: { breadcrumb: 'Anexos' },
        children: [
          { path: listagem, component: ListagemAnexosComponent },
          { path: detalhar, component: CadastroAnexoComponent },
          { path: salvar, component: CadastroAnexoComponent },
          { path: editar, component: CadastroAnexoComponent }
        ]
      },
      {
        path: 'po-cff',
        component: PoCffComponent,
        children: [
          { path: listagem, component: ListagemPoCffComponent },
          { path: detalhar, loadChildren: './po-cff/editar-po-cff/editar-po-cff.module#EditarPoCffModule' },
          {
            path: 'editar-po-cff/:id',
            loadChildren: './po-cff/editar-po-cff/editar-po-cff.module#EditarPoCffModule'
          }
        ]
      },
      {
        path: 'qci',
        component: QciInternoComponent,
        children: [
          { path: listagem, component: ListagemQciComponent },
          { path: editar, component: CadastroQciComponent },
          { path: detalhar, component: CadastroQciComponent }
        ]
      },
      { path: 'vrpl', component: VrplComponent },
      { path: 'vrpls', component: VrplsComponent },
      { path: 'quadro-resumo', component: QuadroResumoComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DetalharRoutingModule { }
