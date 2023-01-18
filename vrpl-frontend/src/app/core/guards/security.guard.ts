import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Store } from '@ngxs/store';
import { UserState } from '../../model/user/user.state';
import { IDPLogin } from '../../model/user/user.actions';

@Injectable()
export class SecurityGuard implements CanActivate {

  constructor(private readonly store: Store) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    const loggedIn = this.store.selectSnapshot(UserState.isLoggedin);
    if (!loggedIn) {
      this.store.dispatch(new IDPLogin(state.url));
    }
    return loggedIn;
  }
}
