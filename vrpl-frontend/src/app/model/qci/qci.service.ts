import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { QciStateModel, SubmetaVRPLModel, QciExternoStateModel } from './qci.state.model';
import { AppConfig } from 'src/app/core/app.config';



@Injectable({
  providedIn: 'root'
})
export class QciService {

  constructor(private http: HttpClient) { }

  loadQci(idLicitacao: number): Observable<QciStateModel> {
    return this.http.get<QciStateModel>(`${AppConfig.endpoint}/qci?idLicitacao=${idLicitacao}`);
  }

  loadQciProposta(versaoDaProposta: number): Observable<QciExternoStateModel> {
    return this.http.get<QciExternoStateModel>(`${AppConfig.endpoint}/proposta/${versaoDaProposta}/qci`);
  }

  salvarSubmeta(submeta: SubmetaVRPLModel) {
    return this.http.post<{ status: string }>(`${AppConfig.endpoint}/qci`, submeta);
  }

}
