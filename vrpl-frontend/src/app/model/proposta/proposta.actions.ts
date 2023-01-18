
export class LoadProposta {
  static readonly type = '[VRPL]LOAD_PROPOSTA';

  constructor(public versaoDaProposta: number) {}
}

export class VerificaFornecedorObsoleto {
  static readonly type = '[VRPL]VERIFICA_FORNECEDOR_OBSOLETO';

  constructor() {}
}
