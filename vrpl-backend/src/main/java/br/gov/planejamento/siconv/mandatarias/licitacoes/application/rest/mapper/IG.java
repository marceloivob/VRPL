package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;

public class IG implements TipoDeIdentificacao {

	private String valor;

	public IG(String inscricaoGenerica) {
		this.valor = inscricaoGenerica;
	}

	@Override
	public PersonalidadeJuridica getTipo() {
		return PersonalidadeJuridica.IG;
	}

	@Override
	public String getValor() {
		return this.valor;
	}

}
