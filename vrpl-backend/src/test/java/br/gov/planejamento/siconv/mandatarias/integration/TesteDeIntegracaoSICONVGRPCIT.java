package br.gov.planejamento.siconv.mandatarias.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.net.ssl.SSLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.grpc.CpsVinculadoResponse;
import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvClientGRPCProducer;

@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class TesteDeIntegracaoSICONVGRPCIT {

	private Logger logger = LoggerFactory.getLogger(TesteDeIntegracaoSICONVGRPCIT.class);

	private SiconvGRPCClient clientSiconvGRPC;

	@BeforeAll
	public void setUp() throws SSLException {
		MockitoAnnotations.initMocks(this);

		SiconvClientGRPCProducer siconvClientProducer = new SiconvClientGRPCProducer();

		System.setProperty("integrations.PRIVATE.GRPC.SICONV.endpoint", "nodes.estaleiro.serpro");
		System.setProperty("integrations.PRIVATE.GRPC.SICONV.port", "32424");
		System.setProperty("integrations.PRIVATE.GRPC.SICONV.useSSL", "false");

		clientSiconvGRPC = siconvClientProducer.create();
	}

	@AfterAll
	public void shutdown() throws Exception {
		clientSiconvGRPC.shutdown();
	}

	@Test
	public void testeDaIntegracaoComSiconvGRPC() throws SSLException {
// Recurso não implementado pelo serviço
//		GenericResponse resposta = clientSiconvGRPC.liveness("VRPL");
//
//		assertEquals("Hello VRPL", resposta.getHello());
	}

	@Test
	public void vincularCps() {
		Long identificadorDaProposta = 1332647L;

		CpsVinculadoResponse cpsVinculado = clientSiconvGRPC.vincularCps(identificadorDaProposta);

		assertNotNull(cpsVinculado);
		
		assertEquals(cpsVinculado.getNumeroCps(), "007/2018");
		assertEquals(cpsVinculado.getIdNumCps(), 8);
		
	}

}
