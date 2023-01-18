package br.gov.planejamento.siconv.mandatarias.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph.CephActions;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph.CephClientProducer;
import br.gov.planejamento.siconv.mandatarias.test.core.ProjectConfiguration;

@Disabled
public class CephTest {

	private Logger logger = LoggerFactory.getLogger(CephTest.class);

	private AmazonS3 cephOperations;

	private CephActions cephAction;

	private ProjectConfiguration configuration = new ProjectConfiguration();

	@BeforeEach
	public void setup() throws URISyntaxException, IOException {

		CephClientProducer cephClientProducer = new CephClientProducer();
		cephClientProducer.setCephConfig(configuration.getCephConfig());
		cephClientProducer.setLogger(logger);

		cephOperations = cephClientProducer.cephClientProducer();

		cephAction = new CephActions();

		cephAction.setCephClient(cephOperations);
		cephAction.setCephConfig(configuration.getCephConfig());
		cephAction.setLogger(logger);
	}

	@Test
	public void testPutFile() throws IOException {

		String string = "ArquivoDeTeste";

		// use ByteArrayInputStream to get the bytes of the String and convert them to
		// InputStream.
		InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));

		String filename = "ArquivoTesteIntegrado.txt";
		Long fileLength = new Long(inputStream.available());

		String hash = cephAction.uploadFile(inputStream, filename, fileLength);

		assertNotNull(hash);

	}

}
