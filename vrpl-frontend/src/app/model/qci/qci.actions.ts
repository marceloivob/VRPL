import { SubmetaVRPLModel } from './qci.state.model';

export class LoadQciProposta {
    static readonly type = '[VRPL]LOAD_QCI_PROPOSTA';

    constructor(public versaoDaProposta: number) {}
}


export class LoadQciLicitacao {
  static readonly type = '[VRPL]LOAD_QCI_LICITACAO';

  constructor() { }
}

export class SaveSubmetaVRPL {
  static readonly type = '[VRPL]SAVE_QCI_SUBMETA';

  constructor(public submeta: SubmetaVRPLModel) { }
}
