import { MenuComponent } from './menu/menu.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CnpjPipe } from './pipes/cnpj.pipe';
import { CpfPipe } from './pipes/cpf.pipe';
import { CurrencyFormatterDirective } from './directives/currency.directive';
import { CurrencyHelperService } from './services/currency-helper.service';
import { NgxMaskModule } from 'ngx-mask';
import { PerfilPipe } from './pipes/perfil.pipe';
import { AnexoComponent } from './anexo/anexo.component';
import { PendenciaComponent } from './pendencia/pendencia.component';
import { SiconvTableModule, SiconvSubFieldsetModule, SiconvAlertMessagesModule, SiconvTextAreaModule, SiconvPipesModule } from '@serpro/ngx-siconv';
import { NgSelectModule } from '@ng-select/ng-select';
import { SimNaoPipe } from './pipes/simnao.pipe';
import { InscricaoGenericaPipe } from './pipes/inscricaogenerica.pipe';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    SiconvSubFieldsetModule,
    SiconvTableModule,
    SiconvAlertMessagesModule,
    SiconvPipesModule,
    NgSelectModule,
    SiconvTextAreaModule,
    NgxMaskModule.forRoot()
  ],
  declarations: [
    CnpjPipe,
    CpfPipe,
    InscricaoGenericaPipe,
    SimNaoPipe,
    CurrencyFormatterDirective,
    PerfilPipe,
    AnexoComponent,
    MenuComponent,
    PendenciaComponent
  ],
  providers: [CurrencyHelperService, CnpjPipe, CpfPipe, InscricaoGenericaPipe, SimNaoPipe],
  exports: [
    CnpjPipe,
    CpfPipe,
    InscricaoGenericaPipe,
    SimNaoPipe,
    CurrencyFormatterDirective,
    NgxMaskModule,
    PerfilPipe,
    AnexoComponent,
    MenuComponent,
    PendenciaComponent,
    SiconvSubFieldsetModule,
    NgSelectModule,
    SiconvAlertMessagesModule,
    SiconvTextAreaModule
  ]
})
export class SiconvModule { }
