import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { skip, throttleTime } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { LoggedUser } from './user.actions';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private _pingRefresh = new Subject();
  private _tokenRefresh = new Subject<string>();

  constructor(private readonly store: Store) {
    this._pingRefresh
      .pipe( throttleTime(1000 * 60) ) // 1min
      .subscribe( () => {
        this.pingIdp();
        this.pingSiconv();
      });

    this._tokenRefresh
      .pipe(
        throttleTime(1000 * 60 * 10), // 10min
        skip(1)
      ).subscribe( (token) => {
        this.refreshToken(token);
      });
  }

  private pingIdp() {
    const done = (data: any) => console.log('Ping IDP ', data);
    fetch(`${AppConfig.urlToIDPService}/public/refresh.jsp`, {credentials: 'include'})
      .then(done)
      .catch(done);
  }

  private pingSiconv() {
    const done = (data: any) => console.log('Ping Siconv ', data);
    fetch(`${AppConfig.urlToSICONVService}/refresh.jsp`, {credentials: 'include'})
      .then(done)
      .catch(done);
  }

  private refreshToken(token: string) {
    fetch(
      `${AppConfig.urlToIDPService}/jwt/refresh`,
      {
        credentials: 'include',
        headers: {Authorization: `Bearer ${token}`}
      }
    ).then( (response) => {
      response.json()
        .then(data => this.store.dispatch(new LoggedUser(data.token)));
    });
  }

  refresh(token: string) {
    this._pingRefresh.next();
    if (token) {
      this._tokenRefresh.next(token);
    }
  }
}
