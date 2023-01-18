import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { LicitacaoModel, LoteModel, LicitacaoDetalhadaModel, AssociacaoLicitacaoLote } from './licitacao.state.model';
import { Observable } from 'rxjs';
import { AppConfig } from 'src/app/core/app.config';
import { PendenciaModel } from '../laudo/laudo.state.model';

@Injectable({
  providedIn: 'root'
})
export class LicitacaoService {

  constructor(private http: HttpClient) { }

  loadLicitacoesAtivasProposta(versaoSelecionadaDaProposta: number): Observable<LicitacaoModel[]> {
    return this.http.get<LicitacaoModel[]>(`${AppConfig.endpoint}/licitacao/ativa/${versaoSelecionadaDaProposta}`);
  }

  loadLicitacoesRejeitadasProposta(versaoSelecionadaDaProposta: number): Observable<LicitacaoModel[]> {
    return this.http.get<LicitacaoModel[]>(`${AppConfig.endpoint}/licitacao/rejeitada/${versaoSelecionadaDaProposta}`);
  }

  sincronizaLicitacoesDaPropostaComSICONV(): Observable<LicitacaoModel[]> {
    return this.http.get<LicitacaoModel[]>(`${AppConfig.endpoint}/licitacao/sincroniza-licitacoes`);
  }

  associarLotes(dadosAssociar: AssociacaoLicitacaoLote): Observable<any> {

    return this.http.post(
        `${AppConfig.endpoint}/licitacao/${dadosAssociar.idLicitacao}/lotes`,
        dadosAssociar
      );
  }

  removerAssociacaoLotes(idLicitacao: number): Observable<any> {
    return this.http.delete(`${AppConfig.endpoint}/licitacao/${idLicitacao}/lotes`);
  }

  loadAtivosLotesProposta(versaoSelecionadaDaProposta: number): Observable<LoteModel[]> {
    return this.http.get<LoteModel[]>(`${AppConfig.endpoint}/proposta/${versaoSelecionadaDaProposta}/lotes-ativos`);
  }

  loadRejeitadosLotesProposta(versaoSelecionadaDaProposta: number): Observable<LoteModel[]> {
    return this.http.get<LoteModel[]>(`${AppConfig.endpoint}/proposta/${versaoSelecionadaDaProposta}/lotes-rejeitados`);
  }

  loadLicitacaoDetalhada(id: number): Observable<LicitacaoDetalhadaModel> {
    return this.http.get<LicitacaoDetalhadaModel>(`${AppConfig.endpoint}/licitacao/${id}/detalhada`);
  }

  savePendencia(pendencia: PendenciaModel): Observable<any> {
    return this.http.put(`${AppConfig.endpoint}/pendencia`, pendencia);
  }

}
