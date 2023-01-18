package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao;

public enum EventoAceiteRejeicao {
	ACEITAR("01"), REJEITAR("02");

	private String dominio;

	EventoAceiteRejeicao(String dominio) {
		this.dominio = dominio;
	}

	public String getDominio() {
		return this.dominio;
	}

}
