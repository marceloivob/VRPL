import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { PropostaStateModel } from './proposta.state.model';
import { Observable, of } from 'rxjs';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
  providedIn: 'root'
})
export class PropostaService {

  constructor(private readonly http: HttpClient) { }

  loadProposta(versaoDaProposta: number): Observable<PropostaStateModel> {
    return this.http.get<PropostaStateModel>(`${AppConfig.endpoint}/proposta/${versaoDaProposta}`);
  }

  verificaInicioVrpl(): Observable<boolean> {
    return this.http.get<boolean>(`${AppConfig.endpoint}/proposta/verifica-inicio-vrpl`);
  }

  verificaFornecedorObsoleto(): any {
    return this.http.get(`${AppConfig.endpoint}/proposta/verifica-fornecedor-obsoleto`);
  }

}
