import { Component } from '@angular/core';
import { AppConfig } from '../../app.config';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { User } from '@serpro/ngx-siconv';
import { Store } from '@ngxs/store';
import { SiconvLegacyService } from 'src/app/model/siconv/siconv-legacy.service';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';

@Component({
  selector: 'vrpl-erro-conexao-servidor',
  templateUrl: './erro-conexao-servidor.component.html'
})
export class ErroConexaoServidorComponent extends BaseComponent {

  public link = AppConfig.urlToSICONVService;

  init(): void {
  }

}
