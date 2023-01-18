import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SecurityGuard } from '../core/guards/security.guard';
import { VrplComponent } from './vrpl.component';
import { DadosBasicosComponent } from './dados-basicos/dados-basicos.component';
import { QciExternoComponent } from './qci-externo/qci-externo.component';
import { CheckVerificaInicioVrplGuard } from '../core/guards/check-verifica-inicio-vrpl.guard';

const routes: Routes = [
  { path: '', redirectTo: '/vrpl/licitacao-lote', pathMatch: 'full' },
  {
    path: '',
    component: VrplComponent,
    canActivate: [SecurityGuard, CheckVerificaInicioVrplGuard ],
    children: [
      { path: 'dados-basicos', component: DadosBasicosComponent, data: { breadcrumb: 'Dados Básicos' }},
      { path: 'qci', component: QciExternoComponent, data: { breadcrumb: 'Quadro de Composição do Investimento' } },
      {
        path: 'licitacao-lote',
        loadChildren: './licitacao-lote/licitacao-lote.module#LicitacaoLoteModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class VrplRoutingModule { }
