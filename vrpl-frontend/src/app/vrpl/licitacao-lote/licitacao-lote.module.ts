import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LicitacaoLoteRoutingModule } from './licitacao-lote-routing.module';
import { LicitacaoLoteListagemComponent } from './licitacao-lote-listagem/licitacao-lote-listagem.component';
import { SiconvModule } from '../../siconv/siconv.module';
import { ComponentSiconvModule } from 'src/app/componente-siconv.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    LicitacaoLoteRoutingModule,
    SiconvModule,
    ComponentSiconvModule
  ],
  declarations: [LicitacaoLoteListagemComponent]
})
export class LicitacaoLoteModule { }
