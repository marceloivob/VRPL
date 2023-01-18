package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class SenderEmailProducer {

	private SenderEmailMock fakeMail = new SenderEmailMock();

	private ConcreteSenderEmail realMail = new ConcreteSenderEmail();

	@Inject
	private ApplicationProperties applicationProperties;

	@EmailProducer
	@Produces
	public SenderEmail create() {

		if (deveUsarStub()) {
			log.debug("Criando SenderEmail Mockado");
			return fakeMail;
		}

		log.debug("Criando SenderEmail concreto");
		return realMail;
	}

	private boolean deveUsarStub() {
		return applicationProperties.isStubActive();
	}
}
