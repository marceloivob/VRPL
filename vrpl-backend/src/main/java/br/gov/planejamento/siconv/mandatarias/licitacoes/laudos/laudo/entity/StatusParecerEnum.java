package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity;

public enum StatusParecerEnum {

	RASCUNHO(1, "Rascunho"),

	EMITIDO(2, "Emitido"),

	ASSINADO(3, "Assinado"),

	CANCELADO(4,"Cancelado");

	private final int codigo;
	private final String descricao;

	private StatusParecerEnum(final int codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static StatusParecerEnum fromCodigo(final int codigo) {
		for (StatusParecerEnum situacao : StatusParecerEnum.values()) {
			if (situacao.getCodigo() == codigo) {
				return situacao;
			}
		}

		throw new IllegalArgumentException("Não foi encontrado o Enum com o código: '" + codigo + "'");
	}

}
