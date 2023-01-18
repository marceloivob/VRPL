package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;

public interface TipoDeIdentificacao {

	PersonalidadeJuridica getTipo();

	String getValor();

}
