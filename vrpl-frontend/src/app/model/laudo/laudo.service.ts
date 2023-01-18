import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TemplateLaudoModel, LaudoModel, PendenciaModel } from './laudo.state.model';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
    providedIn: 'root'
})
export class LaudoService {

    constructor(private readonly http: HttpClient) { }

    carregarPerguntas(tipoTemplateLaudo: string, idLicitacao: number, idVersaoDaLicitacao: number): Observable<TemplateLaudoModel> {
        const url = `${AppConfig.endpoint}/templatelaudo/${tipoTemplateLaudo}/licitacao/${idLicitacao}/versao/${idVersaoDaLicitacao}/perguntas`;

        return this.http.get<TemplateLaudoModel>(url);
    }

    carregarLaudo(idLicitacao: number, tipoLaudo: string): Observable<LaudoModel> {
        return this.http.get<LaudoModel>(`${AppConfig.endpoint}/parecer/${idLicitacao}?tipoLaudo=${tipoLaudo}`);
    }

    carregarPendencias(idLaudo: number): Observable<PendenciaModel[]> {
        return this.http.get<PendenciaModel[]>(`${AppConfig.endpoint}/pendencia/${idLaudo}`);
    }

    deletePendencia(idPendencia: number): Observable<any> {
        return this.http.delete(`${AppConfig.endpoint}/pendencia?id=${idPendencia}`);
    }

    salvarLaudo(laudo: LaudoModel): Observable<any> {
        return this.http.post(`${AppConfig.endpoint}/parecer`, laudo);
    }

    quemEmitiuLaudo(idLicitacao: number, tipoLaudo: string): Observable<string> {
        return this.http.get<string>(`${AppConfig.endpoint}/parecer/quememitiu?idLicitacao=${idLicitacao}&tipoLaudo=${tipoLaudo}`);
    }
}
