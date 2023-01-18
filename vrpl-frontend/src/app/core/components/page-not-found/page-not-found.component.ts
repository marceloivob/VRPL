import { Component, OnInit } from '@angular/core';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { AppConfig } from '../../app.config';
import { User } from '@serpro/ngx-siconv';
import { Store } from '@ngxs/store';
import { SiconvLegacyService } from 'src/app/model/siconv/siconv-legacy.service';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';

@Component({
  selector: 'vrpl-page-not-found',
  templateUrl: './page-not-found.component.html'
})
export class PageNotFoundComponent extends BaseComponent {

  public link = AppConfig.urlToSICONVService;

  init(): void {
  }

}
