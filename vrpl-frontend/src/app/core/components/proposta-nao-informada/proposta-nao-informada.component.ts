import { Component } from '@angular/core';
import { AppConfig } from '../../app.config';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { User } from '@serpro/ngx-siconv';
import { Store } from '@ngxs/store';
import { SiconvLegacyService } from 'src/app/model/siconv/siconv-legacy.service';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';

@Component({
  selector: 'vrpl-proposta-nao-informada',
  templateUrl: './proposta-nao-informada.component.html'
})
export class PropostaNaoInformadaComponent extends BaseComponent {

  public link = AppConfig.urlToSICONVService;


  init(): void {
  }

}