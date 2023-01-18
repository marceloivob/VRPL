
export interface AnexoModel {
    id?: number;
    nomeArquivo: string;
    dataUpload?: Date;
    descricao: string;
    tipoDoAnexo: string;
    tipoDoAnexoAsString?: string;
    arquivo?: any;
    tamanhoArquivo?: number;
    perfilUsuarioEnvio?: string;
    versao: number;
    nomeDoUsuarioQueEnviou?: string;
}

export interface TiposDeAnexos {
    key: string;
    value: string;
}
