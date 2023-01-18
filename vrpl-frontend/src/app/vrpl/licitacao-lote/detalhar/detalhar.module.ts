import { CadastroAnexoComponent } from './anexos/cadastro-anexo/cadastro-anexo.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DetalharRoutingModule } from './detalhar-routing.module';
import { DetalharComponent } from './detalhar.component';
import { LicitacaoDetalhadaComponent } from './licitacao-detalhada/licitacao-detalhada.component';
import { ListagemAnexosComponent } from './anexos/listagem-anexos/listagem-anexos.component';
import { AnexosComponent } from './anexos/anexos.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { QciInternoComponent } from './qci-interno/qci-interno.component';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { CadastroQciComponent } from './qci-interno/cadastro-qci/cadastro-qci.component';
import { ListagemQciComponent } from './qci-interno/listagem-qci/listagem-qci.component';
import { SiconvModule } from '../../../siconv/siconv.module';
import { ptBrLocale, defineLocale } from 'ngx-bootstrap';
import { ListagemPoCffComponent } from './po-cff/listagem-po-cff/listagem-po-cff.component';
import { PoCffComponent } from './po-cff/po-cff.component';
import { VrplComponent } from './vrpl/vrpl.component';
import { QuadroResumoComponent } from './quadro-resumo/quadro-resumo.component';
import { ComponentSiconvModule } from 'src/app/componente-siconv.module';
import { SiconvSubFieldsetModule, SiconvAlertMessagesModule } from '@serpro/ngx-siconv';
import { EdicaoSubmetasComponent } from './associar-lote/edicao-submetas/edicao-submetas.component';
import { LoteInputComponent } from './associar-lote/edicao-submetas/lote-input/lote-input.component';
import { AssociarLoteComponent } from './associar-lote/associar-lote.component';
import { VrplsComponent } from './vrpls/vrpls.component';

defineLocale('pt-br', ptBrLocale);

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SiconvModule,
    SiconvSubFieldsetModule,
    SiconvAlertMessagesModule,
    ComponentSiconvModule,
    DetalharRoutingModule,
    BsDatepickerModule.forRoot()
  ],
  declarations: [
    DetalharComponent,
    LicitacaoDetalhadaComponent,
    CadastroAnexoComponent,
    ListagemAnexosComponent,
    AnexosComponent,
    QciInternoComponent,
    CadastroQciComponent,
    ListagemQciComponent,
    ListagemPoCffComponent,
    PoCffComponent,
    VrplComponent,
    QuadroResumoComponent,
    AssociarLoteComponent,
    EdicaoSubmetasComponent,
    LoteInputComponent,
    VrplsComponent
  ]
})
export class DetalharModule { }
