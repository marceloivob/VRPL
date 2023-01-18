package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EventoQuadroResumoEnum {

	ASSOCIAR_LOTE_LICITACAO("ALL", "Realizada associação entre a Licitação e ao menos um Lote."),

	ENVIAR_PARA_ANALISE("ENV", "Enviado para Análise da Mandatária/Concedente",
			SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE),

	INICIAR_ANALISE_PELA_MANDATARIA("EAN", "Análise iniciada pela Mandatária/Concedente",
			SituacaoLicitacaoEnum.EM_ANALISE),

	SOLICITAR_COMPLEMENTACAO_CONVENENTE("SCC", "Solicitação de Complementação",
			SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO),

	INICIAR_COMPLEMENTACAO_CONVENENTE("EMC", "Em Complementação pelo Convenente", 
			SituacaoLicitacaoEnum.EM_COMPLEMENTACAO),

	ACEITAR_DOCUMENTACAO("ACT", "Aceite realizado pela Mandatária/Concedente",
			SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA),

	INICIAR_EXECUCAO("EXE", "Em Execução"),

	REJEITAR("REJ", "Documentação rejeitada", SituacaoLicitacaoEnum.REJEITADA),

	CANCELAR_ENVIO_ANALISE("CEA",
			"Envio da documentação orçamentária para análise foi cancelado. Situação retornada para \"Em Preenchimento\"",
			SituacaoLicitacaoEnum.EM_PREENCHIMENTO),

	CANCELAR_ENVIO_ANALISE_COMPLEMENTACAO("CEC",
			"Envio da documentação orçamentária para análise foi cancelado. Situação retornada para \"Em Complementação\"",
			SituacaoLicitacaoEnum.EM_COMPLEMENTACAO),

	CANCELAR_COMPLEMENTACAO_CONVENENTE("CCC",
			"Cancelada solicitação de complementação pela mandatária, situação retornada para \"Em Análise\"",
			SituacaoLicitacaoEnum.EM_ANALISE),

	CANCELAR_ACEITE("CAC", "O aceite foi cancelado, situação retornada para \"Em análise\"",
			SituacaoLicitacaoEnum.EM_ANALISE),
	
	CANCELAR_REJEITE("CRJ", "O rejeite foi cancelado, situação retornada para \"Em análise\"",
			SituacaoLicitacaoEnum.EM_ANALISE),
	
	EMITIR_PARECER_ENGENHARIA("EPE", "O Parecer Técnico de Engenharia foi emitido.",
			SituacaoLicitacaoEnum.EM_ANALISE),

	EMITIR_PARECER_SOCIAL("EPS", "O Parecer Técnico de Trabalho Social foi emitido.", SituacaoLicitacaoEnum.EM_ANALISE),
	
	CANCELAR_EMISSAO_PARECER_ENGENHARIA("CPE", "A emissão do Parecer Técnico de Engenharia foi cancelada.",
			SituacaoLicitacaoEnum.EM_ANALISE),

	CANCELAR_EMISSAO_PARECER_SOCIAL("CPS", "A emissão do Parecer Técnico de Trabalho Social foi cancelada.",
			SituacaoLicitacaoEnum.EM_ANALISE),

	ASSINAR_PARECER_ENGENHARIA("APE", "O Parecer Técnico de Engenharia foi assinado.", SituacaoLicitacaoEnum.EM_ANALISE),

	ASSINAR_PARECER_SOCIAL("APS", "O Parecer Técnico de Trabalho Social foi assinado.",
			SituacaoLicitacaoEnum.EM_ANALISE),

	LOTE_LIBERADO_APOS_REJEICAO("LAR", "Lote Liberado após a Rejeição.", SituacaoLicitacaoEnum.EM_PREENCHIMENTO);

	private final String sigla;
	private final String descricao;

	@JsonIgnore
	private SituacaoLicitacaoEnum proximaSituacaoNoCicloDeVida;

	EventoQuadroResumoEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	EventoQuadroResumoEnum(final String sigla, final String descricao,
			final SituacaoLicitacaoEnum proximaSituacaoNoCicloDeVida) {
		this.sigla = sigla;
		this.descricao = descricao;
		this.proximaSituacaoNoCicloDeVida = proximaSituacaoNoCicloDeVida;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static EventoQuadroResumoEnum fromSigla(final String sigla) {
		for (EventoQuadroResumoEnum situacao : EventoQuadroResumoEnum.values()) {
			if (situacao.getSigla().equalsIgnoreCase(sigla)) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Não foi encontrado o Enum: " + sigla);
	}

	@Override
	public String toString() {
		return this.name();
	}

	public SituacaoLicitacaoEnum getProximaSituacaoNoCicloDeVida() {
		return proximaSituacaoNoCicloDeVida;
	}

}
