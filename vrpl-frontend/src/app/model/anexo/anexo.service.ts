import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnexoModel, TiposDeAnexos } from './anexo.state.model';
import { map } from 'rxjs/operators';
import { AppConfig } from 'src/app/core/app.config';

interface AnexoFromApi {
  identificadorDoAnexo: number;
  nomeDoArquivo: string;
  dataDoUpload: Date;
  descricaoDoAnexo: string;
  tipoDoAnexo: string;
  tipoDoAnexoAsString: string;
  nomeDoUsuarioQueEnviou: string;
  perfilDoUsuarioQueEnviou: string;
  linkToDownload: string;
  versao: number;
}

@Injectable({
  providedIn: 'root'
})
export class AnexoService {

  constructor(private readonly http: HttpClient) { }

  recuperarDadosDaListagem(idLicitacao: number): Observable<AnexoModel[]> {
    return this.http
      .get<AnexoFromApi[]>(`${AppConfig.endpoint}/anexos/${idLicitacao}`)
      .pipe(
        map((anexosApi) => {
          const anexos: AnexoModel[] = anexosApi.map(anexoApi => {
            return {
              id: anexoApi.identificadorDoAnexo,
              nomeArquivo: anexoApi.nomeDoArquivo,
              dataUpload: anexoApi.dataDoUpload,
              descricao: anexoApi.descricaoDoAnexo,
              tipoDoAnexo: anexoApi.tipoDoAnexo,
              tipoDoAnexoAsString: anexoApi.tipoDoAnexoAsString,
              nomeDoUsuarioQueEnviou: anexoApi.nomeDoUsuarioQueEnviou,
              perfilUsuarioEnvio: anexoApi.perfilDoUsuarioQueEnviou,
              linkToDownload: anexoApi.linkToDownload,
              versao: anexoApi.versao,
            };
          });
          return anexos;
        })
      );
  }

  salvarAnexo(idLicitacao: number, anexo: AnexoModel) {
    const formData: FormData = new FormData();
    formData.append('_charset_', 'utf-8');

    if (anexo.arquivo) {
      formData.append('arquivo', anexo.arquivo, anexo.nomeArquivo);
      formData.append('tamanhoArquivo', anexo.arquivo.size);
    }

    formData.append('nomeArquivo', anexo.nomeArquivo);
    formData.append('descricao', anexo.descricao);
    formData.append('tipoAnexo', anexo.tipoDoAnexo);
    formData.append('versao', anexo.versao.toString());

    if (anexo.id) {
      const url = `${AppConfig.endpoint}/anexos/${anexo.id}`;
      return this.http.put<{ status: string }>(url, formData);
    } else {
      const url = `${AppConfig.endpoint}/anexos/${idLicitacao}`;
      return this.http.post<{ status: string }>(url, formData);
    }
  }

  deleteAnexo(anexo: AnexoModel) {
    return this.http
      .delete<{ status: string }>(`${AppConfig.endpoint}/anexos/${anexo.id}`);
  }

  recuperarDominioDoTipoDeAnexo(): Observable<TiposDeAnexos[]> {
    return this.http
      .get<TiposDeAnexos[]>(`${AppConfig.endpoint}/anexos/tipos/`);
  }

  recuperarAnexosPorTipo(idLicitacao: number, tipoAnexo: string): Observable<AnexoModel[]> {
    return this.http
      .get<AnexoFromApi[]>(`${AppConfig.endpoint}/anexos/${idLicitacao}/${tipoAnexo}`)
      .pipe(
        map((anexosApi) => {
          const anexos: AnexoModel[] = anexosApi.map(anexoApi => {
            return {
              id: anexoApi.identificadorDoAnexo,
              nomeArquivo: anexoApi.nomeDoArquivo,
              dataUpload: anexoApi.dataDoUpload,
              descricao: anexoApi.descricaoDoAnexo,
              tipoDoAnexo: anexoApi.tipoDoAnexo,
              tipoDoAnexoAsString : anexoApi.tipoDoAnexoAsString,
              nomeDoUsuarioQueEnviou: anexoApi.nomeDoUsuarioQueEnviou,
              perfilUsuarioEnvio: anexoApi.perfilDoUsuarioQueEnviou,
              linkToDownload: anexoApi.linkToDownload,
              versao: anexoApi.versao
            };
          });
          return anexos;
        })
      );
    }
}
