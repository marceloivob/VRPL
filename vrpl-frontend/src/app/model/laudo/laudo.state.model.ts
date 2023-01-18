import { LoteModel } from '../licitacao/licitacao.state.model';

export interface TemplateLaudoModel {
    id: number;
    tipo: string;
    grupos?: GrupoPerguntaModel[];
}

export interface LaudoModel {
    id: number;
    templateFk: number;
    licitacaoFk: number;
    respostas?: RespostaModel[];
    inStatus: number;
    versao: number;
    adtLogin?: string;
}

export interface GrupoPerguntaModel {
    grupoId: number;
    numero: number;
    titulo: string;
    inGrupoObrigatorio: boolean;
    perguntas?: PerguntaModel[];
    versao: number;
}

export interface PerguntaModel {
    perguntaId: number;
    numero: number;
    titulo: string;
    tipoResposta: string;
    valorResposta: string;
    valorEsperado: string;
    grupo: GrupoPerguntaModel;
    loteId: number;
    versao: number;
}

export interface RespostaModel {
    respostaId: number;
    resposta: string;
    grupo: number;
    pergunta: PerguntaModel;
    laudoFk: number;
    lote?: LoteModel;
    versao: number;
}

export interface PendenciaModel {
    id: number;
    inResolvida: boolean;
    prazo: string;
    laudoFk: number;
    descricao: string;
    versao: number;
    submetaFk: number;
    prazoDescricao?: string;
    submetaDescricao?: string;
}


export interface PrazoPendenciaModel {
    codigo: string;
    descricao: string;
}

export interface LaudoStateModel {
    template?: TemplateLaudoModel;
    parecer?: LaudoModel;
    pendencias?: PendenciaModel[];
    quemEmitiu?: string;
}

export class TextoModel {
    texto: any;
}
