package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.EmailProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.SenderEmail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class MailerUtils {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	@EmailProducer
	private SenderEmail senderEmail;

	public void send(EmailInfo mailInfo) {
		senderEmail.send(mailInfo.getDestinatarios(), mailInfo.getAssunto(), mailInfo.getConteudo());
	}

}
