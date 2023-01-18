import { EditarPoCffComponent } from './editar-po-cff.component';
import { DadosGeraisComponent } from './dados-gerais/dados-gerais.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ListagemEventosComponent } from './eventos/listagem/listagem-eventos.component';
import { EventosComponent } from './eventos/eventos.component';
import { CadastroEventoComponent } from './eventos/cadastro-evento/cadastro-evento.component';
import { FrenteDeObraComponent } from './frente-de-obra/frente-de-obra.component';
import { ListagemFrenteDeObraComponent } from './frente-de-obra/listagem-frente-de-obra/listagem-frente-de-obra.component';
import { CadastroFrenteDeObraComponent } from './frente-de-obra/cadastro-frente-de-obra/cadastro-frente-de-obra.component';
import { PoComponent } from './po/po.component';
import { ListagemPoComponent } from './po/listagem-po/listagem-po.component';
import { CffComEventosComponent } from './cff-com-eventos/cff-com-eventos.component';
import { ListagemCffComEventosComponent } from './cff-com-eventos/listagem-cff-com-eventos/listagem-cff-com-eventos.component';
import { CffSemEventosComponent } from './cff-sem-eventos/cff-sem-eventos.component';
import { ListagemCffSemEventosComponent } from './cff-sem-eventos/listagem-cff-sem-eventos/listagem-cff-sem-eventos.component';
import { CadastroPoComponent } from './po/cadastro-po/cadastro-po.component';
import { CadastroCffSemEventosComponent } from './cff-sem-eventos/cadastro-cff-sem-eventos/cadastro-cff-sem-eventos.component';
import { CadastroCffComEventosComponent } from './cff-com-eventos/cadastro-cff-com-eventos/cadastro-cff-com-eventos.component';
import { PlqComponent } from './plq/plq.component';

const routes: Routes = [
  { path: '', redirectTo: 'dados-gerais', pathMatch: 'full' },
  {
    path: '',
    component: EditarPoCffComponent,
    children: [
      { path: 'dados-gerais', component: DadosGeraisComponent },
      { path: 'eventos', redirectTo: 'eventos/listagem', pathMatch: 'full' },
      { path: 'frente-de-obra', redirectTo: 'frente-de-obra/listagem', pathMatch: 'full' },
      { path: 'po', redirectTo: 'po/listagem', pathMatch: 'full' },
      { path: 'cff-com-eventos', redirectTo: 'cff-com-eventos/listagem', pathMatch: 'full' },
      { path: 'cff-sem-eventos', redirectTo: 'cff-sem-eventos/listagem', pathMatch: 'full' },
      {
        path: 'eventos', component: EventosComponent,
        children: [
          { path: 'listagem', component: ListagemEventosComponent },
          { path: 'salvar', component: CadastroEventoComponent },
          { path: 'editar/:id', component: CadastroEventoComponent },
          { path: 'detalhar/:id', component: CadastroEventoComponent }
        ]
      },
      {
        path: 'frente-de-obra', component: FrenteDeObraComponent,
        children: [
          { path: 'listagem', component: ListagemFrenteDeObraComponent },
          { path: 'salvar', component: CadastroFrenteDeObraComponent },
          { path: 'editar/:id', component: CadastroFrenteDeObraComponent },
          { path: 'detalhar/:id', component: CadastroFrenteDeObraComponent }
        ]
      },
      {
        path: 'po', component: PoComponent,
        children: [
          { path: 'listagem', component: ListagemPoComponent },
          { path: 'salvar', component: CadastroPoComponent },
          { path: 'editar/:id', component: CadastroPoComponent },
          { path: 'detalhar/:id', component: CadastroPoComponent }

        ]
      },
      {
        path: 'cff-com-eventos', component: CffComEventosComponent,
        children: [
          { path: 'listagem', component: ListagemCffComEventosComponent },
          { path: 'editar/:id', component: CadastroCffComEventosComponent },
          { path: 'detalhar/:id', component: CadastroCffComEventosComponent }
        ]
      },
      {
        path: 'cff-sem-eventos', component: CffSemEventosComponent,
        children: [
          { path: 'listagem', component: ListagemCffSemEventosComponent },
          { path: 'editar/:id', component: CadastroCffSemEventosComponent },
          { path: 'detalhar/:id', component: CadastroCffSemEventosComponent }
        ]
      },
      {
        path: 'plq', component: PlqComponent
      }

    ]
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EditarPoCffRoutingModule { }
