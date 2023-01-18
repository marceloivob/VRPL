package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity;

public enum ResultadoParecerEnum {

	VIAVEL("VI", "Viável"),

	INVIAVEL("IN", "Inviável");

	private final String sigla;
	private final String descricao;

	private ResultadoParecerEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static ResultadoParecerEnum fromSigla(final String sigla) {
		for (ResultadoParecerEnum resultado : ResultadoParecerEnum.values()) {
			if (resultado.getSigla().equalsIgnoreCase(sigla)) {
				return resultado;
			}
		}
		throw new IllegalArgumentException("Não foi encontrado o Enum: " + sigla);
	}

	public static ResultadoParecerEnum fromDescricao(final String descricao) {
		for (ResultadoParecerEnum resultado : ResultadoParecerEnum.values()) {
			if (resultado.getDescricao().equalsIgnoreCase(descricao)) {
				return resultado;
			}
		}
		throw new IllegalArgumentException("Não foi encontrado o Enum: " + descricao);
	}

}
