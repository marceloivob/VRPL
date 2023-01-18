package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto;

public enum TipoFonteEnum {

	COMPOSICAO("CMP", "Composição"),

	COTACAO("COT", "Cotação"),

	SICRO("SIC", "SICRO"),

	SINAPI("SNP", "SINAPI"),

	OUTROS("OUT", "Outros");

	private final String sigla;
	private final String descricao;

	private TipoFonteEnum(final String sigla, final String descricao) {
		this.sigla = sigla;
		this.descricao = descricao;
	}

	public String getSigla() {
		return sigla;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoFonteEnum fromSigla(final String sigla) {
		for (TipoFonteEnum tipo : TipoFonteEnum.values()) {
			if (tipo.getSigla().equalsIgnoreCase(sigla)) {
				return tipo;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return this.getDescricao();
	}
}
