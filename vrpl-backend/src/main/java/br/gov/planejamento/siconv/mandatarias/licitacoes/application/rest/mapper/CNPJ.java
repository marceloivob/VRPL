package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;
import lombok.Data;

@Data
public class CNPJ implements TipoDeIdentificacao {

	@org.hibernate.validator.constraints.br.CNPJ
	private String valor;

	public CNPJ(String cnpj) {
		this.valor = cnpj;
	}

	@Override
	public PersonalidadeJuridica getTipo() {
		return PersonalidadeJuridica.CNPJ;
	}

	@Override
	public String getValor() {
		return this.valor;
	}

}
