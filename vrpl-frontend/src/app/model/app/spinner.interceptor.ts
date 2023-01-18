import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { Store } from '@ngxs/store';
import { HTTPResponse, HTTPRequest } from './app.actions';


@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {
  constructor(private store: Store) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.store.dispatch(new HTTPRequest());
    return next.handle(req).pipe(
      finalize(() => {
        this.store.dispatch(new HTTPResponse());
      })
    );
}
}
