import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { SiconvModule } from './../../../../../siconv/siconv.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EditarPoCffRoutingModule } from './editar-po-cff-routing.module';
import { EditarPoCffComponent } from './editar-po-cff.component';
import { DadosGeraisComponent } from './dados-gerais/dados-gerais.component';
import { EventosComponent } from './eventos/eventos.component';
import { ListagemEventosComponent } from './eventos/listagem/listagem-eventos.component';
import { CadastroEventoComponent } from './eventos/cadastro-evento/cadastro-evento.component';
import { FrenteDeObraComponent } from './frente-de-obra/frente-de-obra.component';
import { CadastroFrenteDeObraComponent } from './frente-de-obra/cadastro-frente-de-obra/cadastro-frente-de-obra.component';
import { ListagemFrenteDeObraComponent } from './frente-de-obra/listagem-frente-de-obra/listagem-frente-de-obra.component';
import { PoComponent } from './po/po.component';
import { CffSemEventosComponent } from './cff-sem-eventos/cff-sem-eventos.component';
import { CffComEventosComponent } from './cff-com-eventos/cff-com-eventos.component';
import { ListagemPoComponent } from './po/listagem-po/listagem-po.component';
import { ListagemCffComEventosComponent } from './cff-com-eventos/listagem-cff-com-eventos/listagem-cff-com-eventos.component';
import { ListagemCffSemEventosComponent } from './cff-sem-eventos/listagem-cff-sem-eventos/listagem-cff-sem-eventos.component';
import { CadastroPoComponent } from './po/cadastro-po/cadastro-po.component';
import { CadastroCffSemEventosComponent } from './cff-sem-eventos/cadastro-cff-sem-eventos/cadastro-cff-sem-eventos.component';
import { CadastroCffComEventosComponent } from './cff-com-eventos/cadastro-cff-com-eventos/cadastro-cff-com-eventos.component';
import { SiconvFieldsetModule, SiconvDatePickerModule, SiconvTableModule } from '@serpro/ngx-siconv';
import { PlqComponent } from './plq/plq.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SiconvModule,
    SiconvTableModule,
    SiconvDatePickerModule,
    SiconvFieldsetModule,
    BsDatepickerModule.forRoot(),
    EditarPoCffRoutingModule
  ],
  declarations: [
    EditarPoCffComponent,
    DadosGeraisComponent,
    EventosComponent,
    ListagemEventosComponent,
    CadastroEventoComponent,
    FrenteDeObraComponent,
    CadastroFrenteDeObraComponent,
    ListagemFrenteDeObraComponent,
    PoComponent,
    CffSemEventosComponent,
    CffComEventosComponent,
    ListagemPoComponent,
    ListagemCffComEventosComponent,
    ListagemCffSemEventosComponent,
    CadastroPoComponent,
    CadastroCffSemEventosComponent,
    CadastroCffComEventosComponent,
    PlqComponent
  ]
})
export class EditarPoCffModule { }
