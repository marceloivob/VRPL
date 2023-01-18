import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { AppConfig } from '../app.config';
import { Store } from '@ngxs/store';
import { Navigate } from '@ngxs/router-plugin';

@Injectable()
export class IntegrationsGuard implements CanActivate {

  constructor(private readonly store: Store) { }


  canActivate() {
    const loaded = AppConfig.loaded;
    if (!loaded) {
      this.store.dispatch(new Navigate(['erro-conexao-servidor']));
    }

    return AppConfig.loaded;
  }
}
