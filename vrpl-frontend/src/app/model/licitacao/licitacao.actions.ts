import { AssociacaoLicitacaoLote } from './licitacao.state.model';
import { PendenciaModel } from '../laudo/laudo.state.model';

export class LoadLicitacoesAtivasProposta {
  static readonly type = '[VRPL]LOAD_LICITACOES_ATIVAS_PROPOSTA';

  constructor(public versaoSelecionadaDaProposta: number) {}
}

export class LoadLicitacoesRejeitadasProposta {
  static readonly type = '[VRPL]LOAD_LICITACOES_REJEITADAS_PROPOSTA';

  constructor(public versaoSelecionadaDaProposta: number) {}
}

export class SincronizaLicitacoesProposta {
  static readonly type = '[VRPL]SINCRONIZA_LICITACOES_PROPOSTA';

  constructor() {}
}

export class AssociarLotes {
  static readonly type = '[VRPL]SAVE_ASSOCIAR_LOTES';

  constructor(public licitacaoLote: AssociacaoLicitacaoLote) { }
}

export class RemoverAssociacaoLotes {
  static readonly type = '[VRPL]REMOVER_ASSOCIACAO_LOTES';

  constructor(public idLicitacao: number) { }
}

export class LoadLotesAtivosProposta {
  static readonly type = '[VRPL]LOAD_LOTE_ATIVOS_PROPOSTA';

  constructor(public versaoSelecionadaDaProposta: number) {}
}

export class LoadLotesRejeitadosProposta {
  static readonly type = '[VRPL]LOAD_LOTE_REJEITADOS_PROPOSTA';

  constructor(public versaoSelecionadaDaProposta: number) {}
}

export class LoadLicitacaoDetalhada {
  static readonly type = '[VRPL]LOAD_LICITACAO_DETALHADA';

  constructor(public idLicitacao: number) { }
}

export class SavePendencia {
  static readonly type = '[VRPL]SAVE_PENDENCIA';

  constructor(public pendencia: PendenciaModel) { }
}

export class LoadAnexosDaLicitacao {
  static readonly type = '[VRPL]LOAD_ANEXOS_DA_LICITACAO';

  constructor(public idLicitacao: number) { }
}
