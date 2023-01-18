package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.Stage;

@ApplicationScoped
public class CephClientProducer {

	@Inject
	private CephConfig cephConfig;

	@Inject
	private Logger logger;

	@Inject
	private ApplicationProperties applicationProperties;

	private AmazonS3 cephClient;

	public void validaConfiguracoesDoCEPH(@Observes @Initialized(ApplicationScoped.class) Object o) {
		cephConfig.isValid();

		ignoraCertificado();
	}

	private void ignoraCertificado() {
		if (Stage.LOCAL.equals(applicationProperties.getStage())) {
			logger.info("Desabilitando a validação do uso de Certificado quando usando o protocolo HTTPS");

			/**
			 * Disable validation of server certificates when using the HTTPS protocol. This
			 * should ONLY be used to do quick smoke tests against endpoints which don't yet
			 * have valid certificates; it should NEVER be used in production. This property
			 * is meant to be used as a flag (i.e. -Dcom.amazonaws.sdk.disableCertChecking)
			 * rather then taking a value (-Dcom.amazonaws.sdk.disableCertChecking=true).
			 * This property is treated as false by default (i.e. check certificates by
			 * default)
			 * <p>
			 * https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/SDKGlobalConfiguration.html#DISABLE_CERT_CHECKING_SYSTEM_PROPERTY
			 */
			System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, Boolean.TRUE.toString());
		}
	}

	@Produces
	public AmazonS3 cephClientProducer() {

		if (this.cephClient != null) {
			return this.cephClient;
		}

		logger.info("Criando cephClient");

		AWSCredentials credentials = new BasicAWSCredentials(cephConfig.getAccessToken(), cephConfig.getSecretKey());
		AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);

		EndpointConfiguration epc = new AwsClientBuilder.EndpointConfiguration(cephConfig.getEndpointUrl(),
				cephConfig.getRegion());

		AmazonS3 cephClient = AmazonS3ClientBuilder.standard().withEndpointConfiguration(epc)
				.withCredentials(credentialsProvider).withPathStyleAccessEnabled(true).build();

		// Do not make bucket create or delete calls in the high availability code path
		// of an application. Create or delete buckets in a separate
		// initialization or setup routine that runs less often.
		// Código trazido para inicialização pela razão citada acima!!!
		cephClient.createBucket(cephConfig.getBucketName());

		this.cephClient = cephClient;

		return this.cephClient;
	}

	public void setCephConfig(CephConfig cephConfig) {
		this.cephConfig = cephConfig;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
