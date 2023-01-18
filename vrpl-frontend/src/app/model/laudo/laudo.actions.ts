import { LaudoModel } from './laudo.state.model';

export class LoadPerguntas {
    static readonly type = '[VRPL]LOAD_PERGUNTAS';
    constructor(public tipoTemplateLaudo: string, public idLicitacao: number, public idVersaoDaLicitacao: number) { }
}

export class LoadLaudo {
    static readonly type = '[VRPL]LOAD_LAUDO';
    constructor(public idLicitacao: number, public tipoLaudo: string) { }
}

export class LoadPendencias {
    static readonly type = '[VRPL]LOAD_PENDENCIAS';
    constructor(public idLaudo: number) {}
}

export class DeletePendencia {
    static readonly type = '[VRPL]DELETE_PENDENCIAS';
    constructor(public idPendencia: number) {}
}

export class SaveLaudo {
    static readonly type = '[VRPL]SAVE_LAUDO';
    constructor(public laudo: LaudoModel) { }
}

export class QuemEmitiuLaudo {
    static readonly type = '[VRPL]QUEM_EMITIU_LAUDO';
    constructor(public idLicitacao: number, public tipoLaudo: string) { }
}
