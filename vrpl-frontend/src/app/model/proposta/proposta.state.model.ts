

export interface PropostaStateModel {
  idProposta: number;
  numeroProposta: string;
  anoProposta: string;
  valorGlobal: number;
  valorRepasse: number;
  valorContrapartida: number;
  modalidade: number;
  descricaoModalidade: string;
  objeto: string;
  numeroConvenio?: string;
  anoConvenio?: string;
  dataAssinaturaConvenio?: Date;
  codigoPrograma: string;
  nomePrograma: string;
  identificadorDoProponente: string;
  nomeProponente: string;
  uf: string;
  percentualMinimoContrapartida: number;
  nomeMandataria?: string;
  categoria: string;
  nivelContrato?: string;
  apelidoDoEmpreendimento: string;
  situacaoAcffo: string;
  spaHomologado: boolean;
  urlRetornoSiconv?: string;
  infoConvenio?: string;
  versaoAtual: boolean; // Indicador se essa versão da Proposta é a atual (que pode ser editada)
  propostaAtual: number; // Número da Versão (campo Versão Nr)
  versao: number; // Número da Versão do Controle de Concorrência
  versaoSelecionada?: number; // Número da Versão Selecionada no Combo de Versionamento
  versoes: number[]; // Versões possíveis de serem selecionadas no Combo de Versionamento
  termoCompromissoTemMandatar?: boolean;
}

export enum ValidadorProposta {
  ACESSO_PROPOSTA_PERMITIDO = 'acesso',
  SPA_HOMOLOGADO = 'spa'
}
