import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AlertModule, ModalModule } from 'ngx-bootstrap';
import { VrplRoutingModule } from './vrpl-routing.module';
import { VrplComponent } from './vrpl.component';
import { DadosBasicosComponent } from './dados-basicos/dados-basicos.component';
import { QciExternoComponent } from './qci-externo/qci-externo.component';
import { SiconvModule } from '../siconv/siconv.module';
import { ComponentSiconvModule } from '../componente-siconv.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    VrplRoutingModule,
    AlertModule.forRoot(),
    ModalModule.forRoot(),
    SiconvModule,
    FormsModule,
    ComponentSiconvModule
  ],
  declarations: [
    VrplComponent,
    DadosBasicosComponent,
    QciExternoComponent
  ]
})
export class VrplModule { }
