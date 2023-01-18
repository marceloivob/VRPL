import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { InterceptorMessageSkipHeader } from '../app/app.state.model';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
  providedIn: 'root'
})
export class SiconvLegacyService {

  constructor(private http: HttpClient) { }

  getMenu(): Observable<any> {
    const headers = new HttpHeaders()
      .set(InterceptorMessageSkipHeader, '')
      .set('Accept',  'application/json');

    const httpOptions = {
      headers: headers,
      withCredentials: true
    };
    return this.http.get<any>(`${AppConfig.urlToSICONVService}/api/menu`, httpOptions);
  }

  loadUrlRetornoSiconv(): Observable<string> {
    const headers = new HttpHeaders()
      .set(InterceptorMessageSkipHeader, '');
    return this.http.get(`${AppConfig.urlToSICONVService}/api/menu/urlretornosiconv`, {
      headers: headers,
      responseType: 'text',
      withCredentials: true
    });
  }

}
