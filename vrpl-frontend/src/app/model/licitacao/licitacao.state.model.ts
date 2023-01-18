import { AnexoModel, TiposDeAnexos } from '../anexo/anexo.state.model';
import { QciStateModel } from '../qci/qci.state.model';
import { PoLicitacaoModel } from '../pocff/po-cff.state.model';
import { QuadroResumoModel } from '../quadroresumo/quadroresumo.state.model';
import { UfModel } from '../siconv/uf.state.model';

export interface SubmetaModel {
  id: number;
  numero: number;
  versao: number;
  meta: string;
  submeta: string;
  social: boolean;
  porEventos: boolean;
  regimeExecucao: string;
  valorAceitoAnalise: number;
  valorLicitado: number;
  vlRepasse: number;
  vlContrapartida: number;
  vlOutros: number;
  descricaoSituacaoVerificacaoLicitacao: string;
}

export interface LoteModel {
  id: number;
  numero: number;
  idLicitacao?: number;
  idFornecedor?: number;
  fornecedor?: FornecedorModel;
  versao: number;
  valorAceitoAnalise?: number;
  valorVerificadoLicitado?: number;
  submetas?: SubmetaModel[];
  referencia?: string;
  vlTotalLicitadoMenorOuIgualRef?: boolean;
  precoUnitarioMenorOuIgualRef?: boolean;
  previsaoTermino?: Date;
}

export interface LicitacaoModel {
  id: number;
  idIntegracao: number;
  numeroAno: string;
  descricaoProcesso: string;
  objeto: string;
  valorProcesso: number;
  valorSomatorioSubmeta: number;
  valorSomatorioSubmetaLicitado: number;
  processoDeExecucao: string;
  regimeContratacao: string;
  modalidade: string;
  situacaoVerificacao: string;
  situacaoDaLicitacao: string;
  situacaoDaLicitacaoDescricao: string;
  lotes: LoteModel[];
  fornecedores: FornecedorModel[];
}

export interface CtefModel {
  numero: string;
  objetoDoContrato: string;
  valorTotal: number;
  dataAssinatura: Date;
  dataFimDaVigencia: Date;
}

export interface FornecedorModel {
  id: number;
  nome: string;
  obsoleto: boolean;
  identificacao: {
    valor: string;
    tipo: string;
  };
}

export interface LicitacaoDetalhadaModel extends LicitacaoModel {
  dataPublicacaoEdital: Date;
  descricaoProcessoExecucao: string;
  modalidade: string;
  valorTotalEdital: number;
  regimeExecucao: string;
  valorAprovadoFaseAnalise: number;
  ctef: CtefModel;
  dataHomologacao: Date;
  anexos?: AnexoModel[];
  anexosParecer?: AnexoModel[];
  ufs?: UfModel[];
  qci?: QciStateModel;
  po?: PoLicitacaoModel;
  quadroResumo?: QuadroResumoModel;
  fornecedor?: FornecedorModel;
  versaoAtual: boolean;
  numeroVersao: number;
  maiorVersao: number;
  versao: number;
  inSocial: boolean;
  existeParecer: boolean;
  existeParecerEmitido: boolean;
}

export interface LicitacaoStateModel {
  licitacoesProposta?: LicitacaoModel[];
  licitacoesRejeitadasProposta?: LicitacaoModel[];
  lotes?: LoteModel[];
  licitacaoDetalhada?: LicitacaoDetalhadaModel;
  tiposAnexos?: TiposDeAnexos[];
  anexos?: AnexoModel[];
}

export interface AssociacaoLicitacaoLote {
  idLicitacao: number;
  idFornecedor: number;
  lotes: {
    id: number;
    numero: number;
    versao: number;
  }[];
  submetasAlteradas: {
    idSubmeta: number;
    idNovoLote: number;
    numeroNovoLote: number;
    idAntigoLote: number;
    versaoSubmeta: number;
  }[];
}
