import { Component } from "@angular/core";
import { AppConfig } from "../../app.config";
import { BaseComponent } from "src/app/siconv/util/base.component";
import { User } from "@serpro/ngx-siconv";
import { Store } from "@ngxs/store";
import { SiconvLegacyService } from "src/app/model/siconv/siconv-legacy.service";
import { UserAuthorizerService } from "src/app/model/user/user-authorizer.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: "vrpl-erro-generico",
  templateUrl: "./erro-generico.component.html",
  styleUrls: ["./erro-generico.component.css"]
})
export class ErroGenericoComponent extends BaseComponent {
  menuLoaded = false;
  menu: any;
  usuario: User;
  ticket: any;
  message: any;

  public link = AppConfig.urlToSICONVService;

  constructor(
    protected store: Store,
    private service: SiconvLegacyService,
    private authorizer: UserAuthorizerService,
    private route: ActivatedRoute
  ) {
    super(store);
  }

  init() {
    this.route.queryParams
      .filter(params => params.ticket)
      .subscribe(params => {
        this.ticket = params.ticket;
      });

    this.route.queryParams
      .filter(params => params.message)
      .subscribe(params => {
        this.message = params.message;
      });

    this.getUsuario();
    this.recuperarMenu();
  }

  getUsuario() {
    this.usuario = new User(this.authorizer.user.name);
  }

  getPasswordUrl() {
    return (
      AppConfig.urlToSICONVService +
      "/siconv/secure/TrocaDeSenhaObrigatoriaProcessar.do?menu=true"
    );
  }

  getProfileUrl() {
    return (
      AppConfig.urlToSICONVService +
      "/siconv/participe/AtualizarDadosUsuario/AtualizarDadosUsuario.do"
    );
  }

  sessionTimeFeedback(sessionTime) {
    if (sessionTime === "expired") {
      const initialState = {
        msg: "A sessão encerrou. Será preciso logar novamente.",
        logout: true
      };
    }
    if (sessionTime === "warning") {
      const initialState = {
        msg:
          "Esta sessão irá se encerrar em menos 3 minutos! Salve o seu trabalho para evitar perdas.",
        logout: false
      };
    }
  }

  logoutFeedback(logout) {
    const initialState = {
      msg: "A sessão encerrou. Será preciso logar novamente.",
      logout: true
    };
    window.location.href = AppConfig.urlToSICONVService + "?LLO=true";
  }

  recuperarMenu() {
    this.service.getMenu().subscribe((values: any) => {
      this.menuLoaded = true;
      this.menu = values;
    });
  }
}
