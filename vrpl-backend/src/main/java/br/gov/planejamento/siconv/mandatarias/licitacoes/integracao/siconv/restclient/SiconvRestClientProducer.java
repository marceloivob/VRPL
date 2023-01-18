package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.Stage;

@RequestScoped
public class SiconvRestClientProducer {

	@Inject
	private SiconvRestConfig siconvRestConfig;

	@Inject
	private Logger log;

	@Inject
	private ApplicationProperties applicationProperties;

	/**
	 * Construtor Padrão
	 */
	public SiconvRestClientProducer() {
		// noop
	}

	/**
	 * Construtor para Testes Unitários
	 */
	public SiconvRestClientProducer(SiconvRestConfig siconvRestConfig, ApplicationProperties applicationProperties) {
      this.applicationProperties = applicationProperties;
      this.siconvRestConfig = siconvRestConfig;

      this.siconvRestConfig.isValid();
	}

	@Produces
	public Client createHttpClient() throws IOException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, UnrecoverableKeyException, KeyManagementException {

		log.info("Criando SiconvRestClient para o ambiente: {}", applicationProperties.getStage());

		if (false) {
			log.info("Criando client HTTP sem usar SSL, para uso exclusivo em desenvolvimento Local");
			HostnameVerifier noopHostnameVerifier = new NoopHostnameVerifier();

			return ClientBuilder.newBuilder().hostnameVerifier(noopHostnameVerifier).build();
		} else {
          this.siconvRestConfig.isValid();

          log.debug("SSL_TRUSTSTORE_PASS: {}", siconvRestConfig.getTrustStorePass().isBlank());
          log.info("SSL_TRUSTSTORE_PATH: {}", siconvRestConfig.getTrustStorePath().isBlank());
          log.debug("SSL_KEYSTORE_PASS: {}", siconvRestConfig.getKeyStorePass().isBlank());
          log.info("SSL_KEYSTORE_PATH: {}", siconvRestConfig.getKeyStorePath().isBlank());

			SSLContextBuilder sslContext = new SSLContextBuilder();
			HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();

			try (InputStream is = new FileInputStream(siconvRestConfig.getTrustStorePath())) {
				KeyStore trustStore = KeyStore.getInstance("JKS");

				trustStore.load(is, siconvRestConfig.getTrustStorePass().toCharArray());
				sslContext.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy());
				log.info("TrustStore carregada com sucesso.");

				setSSLContext(siconvRestConfig.getKeyStorePass(), siconvRestConfig.getKeyStorePath(),
						sslContext);

				Client client = ClientBuilder.newBuilder() //
						.sslContext(sslContext.build()) //
						.hostnameVerifier(hostnameVerifier) //
						.build();

				log.info("RestClient criado com sucesso.");

				return client;
			}
		}
	}

	public void destroy(@Disposes Client client) {
		client.close();
	}


	/**
	 * Insere o contexto SSL (keystore) no request
	 *
	 * @throws IOException
	 * @throws CertificateException
	 * @throws UnrecoverableKeyException
	 */
	private void setSSLContext(String keyStorePass, String keyStorePath, SSLContextBuilder sslContext)
			throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException,
			UnrecoverableKeyException {

		KeyStore keyStore = KeyStore.getInstance("JKS");

		try (InputStream is = new FileInputStream(keyStorePath)) {

			keyStore.load(is, keyStorePass.toCharArray());
			sslContext.loadKeyMaterial(keyStore, keyStorePass.toCharArray());

			log.info("KeyStore carregada com sucesso.");
		}

	}

	protected boolean deveUsarStub() {
		List<Stage> deve = Arrays.asList(Stage.LOCAL);

		return deve.contains(applicationProperties.getStage());
	}
}
