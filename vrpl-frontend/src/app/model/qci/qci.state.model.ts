export interface SubItemInvestimentoModel {
  id: number;
  descricao: string;
  tipoProjetoSocial: string;
  codigoUnidade: string;
}

export interface SubmetaVRPLModel {
  idSubmeta: number;
  idMeta: number;
  vlRepasse: number;
  vlOutros: number;
  versao: number;
}

export interface SubmetaModel {
  id: number;
  descricao: string;
  lote: number;
  vlRepasse: number;
  vlContrapartida: number;
  vlOutros: number;
  vlTotal: number;
  idMeta: number;
  numero: number;
  situacao: string;
  descricaoSituacao: string;
  descricaoSituacaoVerificacaoLicitacao: string;
  regimeExecucao: string;
  descricaoRegimeExecucao: string;
  itemPad: string;
  descricaoItemPad: string;
  versao: number;
}

export interface MetaModel {
  id: number;
  descricao: string;
  quantidade: number;
  numero: number;
  unidade: string;
  repasseLicitado: number;
  contrapartidaLicitada: number;
  totalLicitado: number;
  itemInvestimento: string;
  submetas?: SubmetaModel[];
  subItemInvestimento?: SubItemInvestimentoModel;
}

export interface QciStateModel {
    id: number;
    idQciVrpl: number;
    vlRepasse: number;
    vlContrapartida: number;
    vlOutros: number;
    vlTotal: number;
    possuiSubmetaSocial: boolean;
    diferencaVlRepasseParaSubmetasSociais: number;
    diferencaVlContrapartidaParaSubmetasSociais: number;
    diferencaVlTotalLicitadoParaSubmetasSociais: number;
    diferencaVlRepasseParaDemaisMetas: number;
    diferencaVlContrapartidaParaDemaisMetas: number;
    diferencaVlTotalLicitadoParaDemaisMetas: number;
    difSocialRepasseLicitado: number;
    difSocialContrapartidaLicitada: number;
    difSocialTotalLicitado: number;
    difMetasRepasseLicitado: number;
    difMetasContrapartidaLicitada: number;
    difMetasTotalLicitado: number;
    difValorOrcadoRepasseLicitado: number;
    difValorOrcadoContrapartidaLicitada: number;
    difValorOrcadoTotalLicitado: number;
    modalidade: string;
    metas?: MetaModel[];
}


export interface QciExternoStateModel {
  vlRepasse: number;
  vlContrapartida: number;
  vlOutros: number;
  vlTotal: number;
  diferencaVlRepasseParaSubmetasSociais: number;
  diferencaVlContrapartidaParaSubmetasSociais: number;
  diferencaVlTotalLicitadoParaSubmetasSociais: number;
  diferencaVlRepasseParaDemaisMetas: number;
  diferencaVlContrapartidaParaDemaisMetas: number;
  diferencaVlTotalLicitadoParaDemaisMetas: number;
  difSocialRepasseLicitado: number;
  difSocialContrapartidaLicitada: number;
  difSocialTotalLicitado: number;
  difMetasRepasseLicitado: number;
  difMetasContrapartidaLicitada: number;
  difMetasTotalLicitado: number;
  difValorOrcadoRepasseLicitado: number;
  difValorOrcadoContrapartidaLicitada: number;
  difValorOrcadoTotalLicitado: number;
  metas?: MetaQciExternoModel[];
}

export interface MetaQciExternoModel {
  numero: number;
  descricao: string;
  itemInvestimento: string;
  quantidade: number;
  unidade: string;
  valorRepasseLicitado: number;
  valorContrapartidaLicitada: number;
  valorTotalLicitado: number;
  submetas?: SubmetaQciExternoModel[];
}

export interface SubmetaQciExternoModel {
  id: number;
  descricao: string;
  lote: number;
  vlRepasse: number;
  vlContrapartida: number;
  vlOutros: number;
  vlTotal: number;
  idMeta: number;
  numero: number;
  situacao: string;
  descricaoSituacao: string;
  descricaoSituacaoVerificacaoLicitacao: string;
  regimeExecucao: string;
  descricaoRegimeExecucao: string;
  itemPad: string;
  descricaoItemPad: string;
  versao: number;
}
