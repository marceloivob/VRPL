package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

public enum NivelContratoEnum {

	CONTRATO_NIVEL_I("Nível I"),
	CONTRATO_NIVEL_I_A("Nível I - A"),
	CONTRATO_NIVEL_II("Nível II"),
	CONTRATO_NIVEL_III("Nível III"),
	CONTRATO_NIVEL_III_A("Nível III - A"),
	CONTRATO_NIVEL_III_B("Nível III - B"),
	CONTRATO_NIVEL_III_C("Nível III - C"),
	CONTRATO_NIVEL_IV("Nível IV"),
	CONTRATO_NIVEL_V("Nível V"),
	NA("Não se enquadra em nenhum nível!"),
	NA_TC("Não se aplica");

	private String descricao;

	NivelContratoEnum(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	public static NivelContratoEnum fromDescricao(String descricao) {
		
		for (NivelContratoEnum nivelContrato : values()) {
			if (nivelContrato.getDescricao().equalsIgnoreCase(descricao)) {
				return nivelContrato;
			}
		}
		
		return NA;
	}

	public static NivelContratoEnum fromCodigoCps(int codigo) {
		if (codigo == 1)
			return CONTRATO_NIVEL_I;
		
		if (codigo == 11)
			return CONTRATO_NIVEL_I_A;

		if (codigo == 2)
			return CONTRATO_NIVEL_II;

		if (codigo == 31)
			return CONTRATO_NIVEL_III_A;

		if (codigo == 32)
			return CONTRATO_NIVEL_III_B;

		if (codigo == 33)
			return CONTRATO_NIVEL_III_C;

		if (codigo == 4)
			return CONTRATO_NIVEL_IV;

		if (codigo == 5)
			return CONTRATO_NIVEL_V;

		return NA;
	}
}

