package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import java.util.Set;

import javax.mail.Address;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.Data;

@Data
public class EmailInfo {

	private String assunto;
	private String conteudo;
	private Set<Address> destinatarios;
	private Profile profile;
	private PropostaBD proposta;

}
