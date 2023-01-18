package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import java.util.Set;

import javax.mail.Address;

public interface SenderEmail {

	void send(Set<Address> destinatarios, String assunto, String textoMensagem);

}
