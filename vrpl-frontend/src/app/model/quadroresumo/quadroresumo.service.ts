import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HistoricoLicitacaoModel, QuadroResumoModel } from './quadroresumo.state.model';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
    providedIn: 'root'
})
export class QuadroResumoService {
    constructor(private readonly http: HttpClient) { }

    recuperarHistoricoLicitacaoListagem(idLicitacao: number, versaoDaPropostaSelecionada: number): Observable<QuadroResumoModel> {

        const url = `${AppConfig.endpoint}/quadroresumo/?idLicitacao=${idLicitacao}&versaoDaProposta=${versaoDaPropostaSelecionada}`;
        const dtoQuadroResumo: Observable<QuadroResumoModel> = this.http.get<QuadroResumoModel>(url);

        return dtoQuadroResumo;
    }

    saveNovoEstadoDocumentacao(historico: HistoricoLicitacaoModel): any {
        const url = `${AppConfig.endpoint}/quadroresumo`;
        return this.http.post<{ status: string }>(url, historico);
    }



}
