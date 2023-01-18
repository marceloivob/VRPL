package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum {

	/**
	 * "ELA" - "Em Elaboração - Fase de Análise"
	 */
	EM_ELABORACAO("ELA", "Em Elaboração - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_PREENCHIMENTO;
		}
	},

	/**
	 * "EAN" - "Enviada para Análise - Fase de Análise"
	 */
	ENVIADA_PARA_ANALISE("EAN", "Enviada para Análise - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE;
		}
	},

	/**
	 * "ANL" - "Em Análise - Fase de Análise"
	 */
	EM_ANALISE("ANL", "Em Análise - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_ANALISE;
		}
	},

	/**
	 * "SCP" - "Solicitada Complementação - Fase de Análise"
	 */
	SOLICITADA_COMPLEMENTACAO("SCP", "Solicitada Complementação - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO;
		}
	},

	/**
	 * "COM" - "Em Complementação - Fase de Análise"
	 */
	EM_COMPLEMENTACAO("COM", "Em Complementação - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;
		}
	},

	/**
	 * "ACT" - "Aceito / Fase de Análise - Fase de Análise"
	 */
	DOCUMENTACAO_ACEITA("ACT", "Aceito / Fase de Análise - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA;
		}
	},

	/**
	 * "EEX" - "Em Execução - Fase de Análise"
	 */
	EM_EXECUCAO("EEX", "Em Execução - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_EXECUCAO;
		}
	},

	/**
	 * "REJ" - "Rejeitada - Fase de Análise"
	 */
	REJEITADA("REJ", "Rejeitada - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.REJEITADA;
		}
	},

	/**
	 * "EMH" - "Em homologação - Fase de Análise"
	 */
	EM_HOMOLOGACAO("EMH", "Em homologação - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_HOMOLOGACAO;
		}
	},

	/**
	 * "SCC" - "Solicitada Complementação pela Concedente - Fase de Análise"
	 */
	SOLICITADA_COMPLEMENTACAO_CONCEDENTE("SCC", "Solicitada Complementação pela Concedente - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO_CONCEDENTE;
		}
	},

	/**
	 * "ECM" - "Em Complementação pela Mandatária - Fase de Análise"
	 */
	EM_COMPLEMENTACAO_MANDATARIA("ECM", "Em Complementação pela Mandatária - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.EM_COMPLEMENTACAO_MANDATARIA;
		}
	},

	/**
	 * "HOM" - "Homologada - Fase de Análise"
	 */
	HOMOLOGADA("HOM", "SPA Homologada - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.HOMOLOGADA_NO_PROJETO_BASICO;
		}
	},

	/**
	 * "HAS" - "Homologada Automaticamente pelo Sistema - Fase de Análise"
	 */
	HOMOLOGADA_AUTOMATICAMENTE_SISTEMA("HAS", "SPA Concluída Automaticamente pelo Sistema - Fase de Análise") {
		@Override
		public SituacaoLicitacaoEnum getEquivalenteNoVRPL() {
			return SituacaoLicitacaoEnum.HOMOLOGADA_AUTOMATICAMENTE_PELO_SISTEMA_NO_PROJETO_BASICO;
		}
	};

	private final String sigla;
	private final String descricao;

	public abstract SituacaoLicitacaoEnum getEquivalenteNoVRPL();

	private SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum fromSigla(final String sigla) {
		for (SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum situacao : SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.values()) {
			if (situacao.getSigla().equalsIgnoreCase(sigla)) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Enum não encontrado: " + sigla);
	}

}