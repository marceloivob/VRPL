export interface HistoricoLicitacaoModel {
    id?: number;
    identificadorDaLicitacao: number;
    versaoDaLicitacao: number;
    eventoGerador?: any;
    situacaoDaLicitacao?: any;
    consideracoes: string;
    dataDeRegistro?: Date;
    nomeDoResponsavel?: string;
    cpfDoResponsavel?: string;
    papelDoResponsavel?: string;
}


export interface QuadroResumoModel {
    podeEnviarParaAnalise: boolean;
    podeIniciarComplementacao: boolean;
    podeCancelarEnvio: boolean;
    podeCancelarEnvioComplementacao: boolean;

    podeIniciarAnalise: boolean;
    podeSolicitarComplementacao: boolean;
    podeAceitarDocumentacao: boolean;
    podeRejeitarDocumentacao: boolean;
    podeCancelarSolicitacaoComplementacao: boolean;

    podeCancelarAceite: boolean;
    podeCancelarRejeite: boolean;

    listaHistorico: HistoricoLicitacaoModel[];
}

export enum EventoGerador {
    ASSOCIAR_LOTE_LICITACAO  = 'ALL',
    ENVIAR_PARA_ANALISE = 'ENV',
    INICIAR_ANALISE_PELA_MANDATARIA = 'EAN',
    SOLICITAR_COMPLEMENTACAO_CONVENENTE = 'SCC',
    INICIAR_COMPLEMENTACAO_CONVENENTE = 'EMC',
    ACEITAR_DOCUMENTACAO = 'ACT',
    INICIAR_EXECUCAO = 'EXE',
    REJEITAR = 'REJ',
    CANCELAR_ENVIO_ANALISE = 'CEA',
    CANCELAR_ENVIO_ANALISE_COMPLEMENTACAO = 'CEC',
    CANCELAR_COMPLEMENTACAO_CONVENENTE = 'CCC',
    CANCELAR_ACEITE = 'CAC',
    CANCELAR_REJEITE = 'CRJ',
    CANCELAR_EMISSAO_PARECER_ENGENHARIA = 'CPE',
    CANCELAR_EMISSAO_PARECER_SOCIAL = 'CPS',
    EMITIR_PARECER_ENGENHARIA = 'EPE',
    EMITIR_PARECER_SOCIAL = 'EPS',
    ASSINAR_PARECER_ENGENHARIA = 'APE',
    ASSINAR_PARECER_SOCIAL = 'APS'
}

export enum SituacaoDaLicitacao {
    EM_PREENCHIMENTO = 'EPE',
    ENVIADA_PARA_ANALISE = 'EAN',
    EM_ANALISE = 'ANL',
    SOLICITADA_COMPLEMENTACAO = 'SCP',
    EM_COMPLEMENTACAO = 'COM',
    ACEITA_FASE_LICITACAO = 'ACT',
    EM_EXECUCAO = 'EEX',
    REJEITADA = 'REJ',
    EM_HOMOLOGACAO = 'EMH',
    SOLICITADA_COMPLEMENTACAO_CONCEDENTE = 'SCC',
    EM_COMPLEMENTACAO_MANDATARIA = 'ECM',
    HOMOLOGADA = 'HOM'
}

export function isSituacaoLicitacaoEditavelPeloConvenente(situacao: string): boolean {
    return (situacao === SituacaoDaLicitacao.EM_PREENCHIMENTO ||
            situacao === SituacaoDaLicitacao.EM_COMPLEMENTACAO);
}
