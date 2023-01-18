package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

public enum TipoOrgao {

	CONCEDENTE,
	PROPONENTE,
	AGENDA_COMPROMISSOS,
	ACOMPANHAMENTO,
	IMPRENSA_NACIONAL,
	INST_MANDATARIA,
	ORGAO_MAXIMO,
	UNIDADE_CADASTRADORA,
	TRIBUNAL,
	COMISSAO_SELECAO,
	sistema;

	TipoOrgao() {
		if (name().length() > 20) {
			String mensagem = "\n\nValor muito longo para codigo do TipoOrgao: " + name() + "\n\n";
			throw new RuntimeException(mensagem);
		}
	}

	public String getCodigo() {
		return name();
	}

	public String toString() {
		return name();
	}

}
