package br.gov.serpro.vrpl.grpc.services;

public enum SituacaoLicitacaoEnum {

	/**
	 * "ELA" - "Em Preenchimento - Fase de Licitação"
	 */
	EM_ELABORACAO("ELA", "Em Preenchimento - Fase de Licitação"),

	/**
	 * "EAN" - "Enviada para Análise - Fase de Licitação"
	 */
	ENVIADA_PARA_ANALISE("EAN", "Enviada para Análise - Fase de Licitação"),

	/**
	 * "ANL" - "Em Análise - Fase de Licitação"
	 */
	EM_ANALISE("ANL", "Em Análise - Fase de Licitação"),

	/**
	 * "SCP" - "Solicitada Complementação - Fase de Licitação"
	 */
	SOLICITADA_COMPLEMENTACAO("SCP", "Solicitada Complementação - Fase de Licitação"),

	/**
	 * "COM" - "Em Complementação pelo Proponente - Fase de Licitação"
	 */
	EM_COMPLEMENTACAO("COM", "Em Complementação pelo Proponente - Fase de Licitação"),

	/**
	 * "ACT" - "Aceito - Fase de Licitação"
	 */
	DOCUMENTACAO_ACEITA("ACT", "Aceito - Fase de Licitação"),

	/**
	 * "ACL" - "Aceitar / A Licitar"
	 */
	ACEITAR_A_LICITAR("ACL", "Aceitar / A Licitar"),

	/**
	 * "EEX" - "Em Execução - Fase de Licitação"
	 */
	EM_EXECUCAO("EEX", "Em Execução - Fase de Licitação"),

	/**
	 * "EXT" - "Executada - Fase de Licitação"
	 */
	EXECUTADA("EXT", "Executada - Fase de Licitação"),

	/**
	 * "REJ" - "Rejeitada - Fase de Licitação"
	 */
	REJEITADA("REJ", "Rejeitada - Fase de Licitação"),

	/**
	 * "EMH" - "Em homologação - Fase de Licitação"
	 */
	EM_HOMOLOGACAO("EMH", "Em homologação - Fase de Licitação"),

	/**
	 * "SCC" - "Solicitada Complementação pela Concedente - Fase de Licitação"
	 */
	SOLICITADA_COMPLEMENTACAO_CONCEDENTE("SCC", "Solicitada Complementação pela Concedente - Fase de Licitação"),

	/**
	 * "ECM" - "Em Complementação pela Mandatária - Fase de Licitação"
	 */
	EM_COMPLEMENTACAO_MANDATARIA("ECM", "Em Complementação pela Mandatária - Fase de Licitação"),

	/**
	 * "HOM" - "Homologada - Fase de Licitação"
	 */
	HOMOLOGADA("HOM", "Homologada - Fase de Licitação"),

	/**
	 * "HOM" - "Homologada - Fase de Análise"
	 * <p>
	 * <b>Usado quando ainda não foi realizada a associação entre uma Licitação e
	 * está exibindo a situação da Fase de Análise</b>
	 */
	HOMOLOGADA_NO_PROJETO_BASICO("HOM", "Homologada - Fase de Análise");

	private final String sigla;
	private final String descricao;

	SituacaoLicitacaoEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoLicitacaoEnum fromSigla(final String sigla) {
		for (SituacaoLicitacaoEnum situacao : SituacaoLicitacaoEnum.values()) {
			if (situacao.getSigla().equalsIgnoreCase(sigla)) {
				return situacao;
			}
		}

		throw new IllegalArgumentException(sigla);
	}
}
