import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '@ngxs/store';
import { Navigate } from '@ngxs/router-plugin';

import { LoggedUser } from './user.actions';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
  providedIn: 'root'
})
export class IdpService {

  constructor(private readonly store: Store, private readonly http: HttpClient) {
  }

  login(url: string) {
    const idProposta = this.obterIdProposta();
    if (idProposta) {
      this.getJwt(idProposta, url);
    } else {
      this.store.dispatch(new Navigate(['proposta-nao-informada']));
    }
  }

  redirect(idProposta: string, url: string) {
    window.location.href =
      `${AppConfig.urlToIDPService}/token/key?app=${AppConfig.idpAppName}&idProposta=${idProposta}&dest=${url}`;
  }

  getJwt(idProposta: string, url: string) {
    const idpKey = this.getNativeQueryParamByName('token');
    const destUrl = this.getNativeQueryParamByName('dest') || '/vrpl';
    if (idpKey) {
      this.http
        .get<{ token: string }>(`${AppConfig.urlToIDPService}/token/jwt?token=${idpKey}&prop=${idProposta}`, { withCredentials: true })
        .subscribe(data => {
          // FIXME verificar token undefined : erro: "Falha ao obter dados do usuário."
          this.store.dispatch([
            new LoggedUser(data.token),
            new Navigate([destUrl])
          ]);
        });
    } else {
      this.redirect(idProposta, url);
    }
  }

  getNativeQueryParamByName(name: string) {
    const url = window.location.href;
    name = name.replace(/[\[\]]/g, '\\$&');
    const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
    const results = regex.exec(url);
    if (!results) {
      return null;
    }
    if (!results[2]) {
      return '';
    }
    return decodeURIComponent(results[2].replace(/\+/g, ''));
  }

  obterIdProposta() {
    const storageKey = `VRPL_idProposta`;
    let idProposta = this.getNativeQueryParamByName('idProposta');
    if (idProposta) {
      localStorage.setItem(storageKey, idProposta);
    } else {
      idProposta = localStorage.getItem(storageKey);
    }
    return idProposta;
  }
}
