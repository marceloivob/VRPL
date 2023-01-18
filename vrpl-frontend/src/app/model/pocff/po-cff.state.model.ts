
export interface PoLicitacaoModel {
  poCff?: PoModel[];
  poDetalhada?: PoDetalhadaModel;
}

export interface PoModel {
    id: number;
    idAnalise: number;
    dataBaseAnalise: Date;
    dataBaseVrpl: Date;
    localidade: string;
    previsaoAnalise: Date;
    previsaoInicioVRPL?: Date;
    duracao: number;
    qtMesesDuracaoObra?: number;
    qtMesesDuracaoObraValorOriginal: number;
    acompanhamentoEvento: boolean;
    precoTotalAnalise: number;
    precoTotalLicitacao?: number;
    descricaoMeta: string;
    numeroMeta: number;
    numeroSubmeta: number;
    descricao: string;
    reaproveitaCFFSemEventos?: boolean;
    inTrabalhoSocial: boolean;
    versao: number;
}

export interface EventoModel {
    id: number;
    tituloEvento: string;
    numeroEvento: number;
    versao: number;
}

export interface PoDetalhadaModel extends PoModel {
    idSubmeta?: number;
    numeroLote?: number;
    listaEventos?: EventoModel[];
    listaFrentesDeObra?: FrenteDeObraModel[];
    listaParcelasPorMacroServico?: VisaoParcelasPorMacroServicoModel[];
    listaMacrosservicos?: MacrosservicoModel[];
    listaEventosServico?: EventoModel[];
    listaFrentesDeObraServico?: FrenteDeObraQuantidadeModel[];
    listaServicoFrenteDeObraAnalise?: ServicoFrenteDeObraAnaliseModel[];
    cffComEventos?: CFFComEventosModel;
    plq?: PLQModel;
    apenasVisualizar?: boolean;
    referencia?: string;
    versao: number;
    poMacrosservicosServicos?: PoMacrosservicoServicoModel;
}

export interface FrenteDeObraModel {
    id: number;
    nomeFrente: string;
    numeroFrente: number;
    versao: number;
}

export interface ServicoFrenteDeObraAnaliseModel {
    idPO: number;
    servicoFk: number;   
    qtItens: number;
    nomeFrente: string;
    numeroFrente: number;
    versao: number;
}

export interface ParcelaCffSemEventosModel {
    idParcela: number;
    numero: number;
    mesAnoParcela: string;
    percentualParcela: number;
    percentualAcumulado: number;
    totalAcumulado: number;
    valorParcela: number;
    versao: number;
}

export interface VisaoParcelasPorMacroServicoModel {
    idMacroServico: number;
    numeroMacroServico: number;
    nomeMacroServico: string;
    precoTotal: number;
    parcelas: ParcelaCffSemEventosModel[];
}

export interface ServicoModel {
    id: number;
    item: number;
    fonte: string;
    nome: string;
    quantidade: number;
    unidade: string;
    custoUnitarioReferencia: number;
    bdi: number;
    bdiLicitado: number;
    precoTotal: number;
    custoUnitarioAnalise: number;
    custoUnitarioDataBase: number;
    eventoFK: number;
    codigo: string;
    observacao: string;
    sinapiPossuiOcorrenciaNaDataBaseDeReferencia: boolean;
    precoUnitarioAnalise: number;
    precoUnitarioLicitado: number;
    precoUnitarioDataBase: number;
    precoTotalAnalise: number;
    precoTotalLicitado: number;
    precoTotalDataBase: number;
    versao: number;
}

export interface MacrosservicoModel {
    id: number;
    item: number;
    nome: string;
    precoTotalLicitado: number;
    precoTotalAnalise: number;
    precoTotalAceitoNaAnalise: number;
    precoTotalNaDataBaseDaLicitacao: number;
    servicos: ServicoModel[];
    submetaId: number;
}

export interface PoMacrosservicoServicoModel {
    id: number;
    totalGeralLicitado: number;
    totalGeralAceitoNaAnalise: number;
    totalGeralNaDataBaseDaLicitacao: number;
    macrosservicos: MacrosservicoModel[];
}

export interface FrenteDeObraQuantidadeModel {
    id: number;
    quantidade: number;
    frenteDeObra: FrenteDeObraModel;
}


export interface MacroServicoParcelaDTOModel {
    id: number;
    macroServicoFk: number;
    nrParcela: number;
    pcParcela: number;
    versao: number;
}

export interface MacroServicoCFFDTOModel {
    id: number;
    idPO: number;
    txDescricao: string;
    nrMacroServico: number;
    precoMacroservico: number;
    parcelas: MacroServicoParcelaDTOModel[];
}

export interface CFFSemEventosDTOModel {
    idPo: number;
    dtPrevisaoInicioObra: string;
    qtMesesDuracaoObra: number;
    listaMacroServicos: MacroServicoCFFDTOModel[];
}

export interface FrenteDeObraCadastroModel {
    frenteObraFk: number;
    qtItens: number;
}

export interface ServicoCadastroModel {
    id: number;
    idSubmeta: number;
    idPo: number;
    eventoFk: number;
    frentesObra: FrenteDeObraCadastroModel[];
    macroServicoFk: number;
    pcBdiLicitado: number;
    vlPrecoUnitarioDaLicitacao: number;
    vlCustoUnitario: number;
    versao: number;
    txObservacao: string;
}

export interface FrenteDeObraCFFcomEventosModel {
    idFrenteObra: number;
    numeroFrenteObra: number;
    nomeFrenteObra: string;
    mesConclusao: number ;
    versao: number;
}

export interface EventoCFFcomEventosModel {
    idEvento: number;
    numeroEvento: number;
    tituloEvento: string ;
    listaFrenteObras: FrenteDeObraCFFcomEventosModel[];
    versao: number;
}

export interface CFFComEventosModel {
    idPO: number;
    qtdMeses: number ;
    totalPO: number;
    mapaMesValorParcela: Map<number, number> ;
    listaDeEventoCFF: EventoCFFcomEventosModel[];
}

export interface QuadroCFFPorEventoPorMes {
    mes: number;
    percentualParcela: number ;
    valorParcela: number;
    percentualAcumulado: number ;
    valorAculumado: number;
}

export interface FrenteObraPLQModel {
    id: number;
    servicoFK: number;
    numero: number;
    descricao: string;
    quantidade: number;
    valorLicitado: number;
}

export interface ServicoPLQModel {
    id: number;
    numero: number;
    descricao: string;
    quantidade: number;
    unidade: string;
    numeroEvento: number;
    descricaoEvento: string;
    precoTotalServico: number;
    precoUnitarioServico: number;
    macroServicoFk: number;
    frentesObra: FrenteObraPLQModel[] ;
}

export interface MacroServicoPLQModel {
    id: number;
    numero: number;
    descricao: string;
    precoTotalLicitado: number;
    servicos: ServicoPLQModel[];
}

export interface PLQModel {
    porEvento: boolean;
    macroservicos: MacroServicoPLQModel[];
}


