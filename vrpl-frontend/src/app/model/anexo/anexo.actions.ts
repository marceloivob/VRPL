import { AnexoModel } from './anexo.state.model';

export class LoadAnexosLicitacao {
    static readonly type = '[VRPL]LOAD_ANEXO_LICITACAO';
    constructor() { }
}

export class SaveAnexo {
    static readonly type = '[VRPL]SAVE_ANEXO';

    constructor(public idLicitacao: number, public anexo: AnexoModel) { }
}

export class LoadTipoDeAnexo {
    static readonly type = '[VRPL]LOAD_TIPO_DE_ANEXO';

    constructor() { }
}

export class DeleteAnexo {
  static readonly type = '[VRPL]DELETE_ANEXO';

  constructor(public anexo: AnexoModel) { }
}

export class LoadAnexosPorTipo {
    static readonly type = '[VRPL]LOAD_ANEXO_POR_TIPO';
    constructor(public tipoAnexo: string) { }
}
