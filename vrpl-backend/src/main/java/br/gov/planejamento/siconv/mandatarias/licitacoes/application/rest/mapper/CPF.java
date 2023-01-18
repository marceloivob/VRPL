package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;
import lombok.Data;

@Data
public class CPF implements TipoDeIdentificacao {

	@org.hibernate.validator.constraints.br.CPF
	private String valor;

	public CPF(String cpf) {
		this.valor = cpf;
	}

	@Override
	public PersonalidadeJuridica getTipo() {
		return PersonalidadeJuridica.CPF;
	}

	@Override
	public String getValor() {
		return this.valor;
	}

}
