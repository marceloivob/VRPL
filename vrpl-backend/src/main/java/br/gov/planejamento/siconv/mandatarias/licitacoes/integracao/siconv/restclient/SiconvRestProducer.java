package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.client.Client;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class SiconvRestProducer {

	@Inject
	private SiconvRest siconvRestImpl;

	private SiconvRest siconvRestStub;

	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private SiconvRestConfig siconvRestConfig;

	@Inject
	private BasicAuthenticatorFilter basicAuthenticatorFilter;

	@Inject
	private URLSiconvRestGenerator urlSiconvRestGenerator;

	@Produces
	@SiconvRestQualifier
	public SiconvRest create() throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		if (deveUsarStub()) {
			log.info("Usando o stub da integração com o SICONV.");
			siconvRestStub = new SiconvRestStub();

			return siconvRestStub;
		} else {
			log.info("Usando a integração real com o SICONV.");

			if (siconvRestImpl == null) {
				SiconvRestClientProducer siconvRestClientProducer = new SiconvRestClientProducer(siconvRestConfig,
						applicationProperties);

				Client siconvRestClient = siconvRestClientProducer.createHttpClient();

				this.siconvRestImpl = new SiconvRestImpl(siconvRestClient, basicAuthenticatorFilter,
						urlSiconvRestGenerator);
			}

			return siconvRestImpl;
		}

	}
	
	protected boolean deveUsarStub() {
		return applicationProperties.isStubActive();
	}
}
