package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity;

public enum PrazoPendenciaEnum {
	
	RETIRADA_DE_SUSPENSIVA("RSU", "Retirada de suspensiva"),
	CONTRATACAO("CON", "Contratação"),
	AIO("AIO", "AIO"),
	PROXIMO_DESBLOQUEIO("PRO", "Próximo desbloqueio"),
	ULTIMO_DESBLOQUEIO("ULT", "Último desbloqueio"),
	PRESTACAO_CONTAS_FINAL("PCF", "Prestação de contas final"),
	APROVACAO_PLANO_TRABALHO("APT", "Aprovação do plano de trabalho"),
	ACEITE_DA_LICITACAO("ACL", "Aceite da licitação"),
	PROXIMA_VISITA_IN_LOCO("PVL", "Próxima visita in loco");
	
	private final String sigla;
	private final String descricao;
	
	
	PrazoPendenciaEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}
	
	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static PrazoPendenciaEnum fromSigla(final String sigla) {
		for (PrazoPendenciaEnum prazo : PrazoPendenciaEnum.values()) {
			if (prazo.getSigla().equalsIgnoreCase(sigla)) {
				return prazo;
			}
		}

		throw new IllegalArgumentException("Não foi encontrado o Enum: " + sigla);
	}

	@Override
	public String toString() {
		return this.name();
	}

}
