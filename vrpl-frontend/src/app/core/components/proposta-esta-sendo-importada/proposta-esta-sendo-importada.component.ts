import { Component } from '@angular/core';
import { AppConfig } from '../../app.config';
import { User } from '@serpro/ngx-siconv';
import { Store } from '@ngxs/store';
import { SiconvLegacyService } from 'src/app/model/siconv/siconv-legacy.service';
import { UserAuthorizerService } from 'src/app/model/user/user-authorizer.service';
import { BaseComponent } from 'src/app/siconv/util/base.component';

@Component({
  selector: 'vrpl-proposta-esta-sendo-importada',
  templateUrl: './proposta-esta-sendo-importada.html'
})
export class PropostaEstaSendoImportadaComponent extends BaseComponent {

  public link = AppConfig.urlToSICONVService;

  menuLoaded = false;
  menu: any;
  usuario: User;

  constructor(
    protected store: Store,
    private service: SiconvLegacyService,
    private authorizer: UserAuthorizerService) {
    super(store);
  }

  init() {
    this.getUsuario();
    this.recuperarMenu();
  }

  getUsuario() {
    try {
      this.usuario = new User(this.authorizer.user.name);

    } catch (e) {
      console.log(e);
    }
  }

  getPasswordUrl() {
    return AppConfig.urlToSICONVService + '/siconv/secure/TrocaDeSenhaObrigatoriaProcessar.do?menu=true';
  }

  getProfileUrl() {
    return AppConfig.urlToSICONVService + '/siconv/participe/AtualizarDadosUsuario/AtualizarDadosUsuario.do';
  }

  sessionTimeFeedback(sessionTime) {
    if (sessionTime === 'expired') {
      const initialState = {
        msg: 'A sessão encerrou. Será preciso logar novamente.',
        logout: true
      };

    }
    if (sessionTime === 'warning') {
      const initialState = {
        msg: 'Esta sessão irá se encerrar em menos 3 minutos! Salve o seu trabalho para evitar perdas.',
        logout: false
      };
    }
  }

  logoutFeedback(logout) {
    const initialState = {
      msg: 'A sessão encerrou. Será preciso logar novamente.',
      logout: true
    };
    window.location.href = AppConfig.urlToSICONVService + '?LLO=true';
  }

  recuperarMenu() {
    this.service.getMenu().subscribe(
      (values: any) => {
        this.menuLoaded = true;
        this.menu = values;
      }
    );
  }


}
