import { HistoricoLicitacaoModel } from './quadroresumo.state.model';

export class LoadQuadroResumo {
    static readonly type = '[VRPL]LOAD_QUADRO_RESUMO';
    constructor(public versaoDaProposta: number) {}
}

export class SaveNovoEstadoDocumentacao {
    static readonly type = '[VRPL]SAVE_NOVO_ESTADO_DOCUMENTACAO';

    constructor(public historico: HistoricoLicitacaoModel) { }
}
