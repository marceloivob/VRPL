package br.gov.planejamento.siconv.mandatarias.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

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

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.LicitacaoClientProducer;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.GenericResponse;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse.ProcessoExecucao;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;

@Disabled
@TestInstance(Lifecycle.PER_CLASS)
public class TesteDeIntegracaoLicitacaoGRPCIT {

	private Logger logger = LoggerFactory.getLogger(TesteDeIntegracaoLicitacaoGRPCIT.class);

	private ClientLicitacoesInterface clientLicitacoes;

	@BeforeAll
	public void setUp() throws SSLException {
		MockitoAnnotations.initMocks(this);

		LicitacaoClientProducer licitacaoClientProducer = new LicitacaoClientProducer();

		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.endpoint", "nodes.estaleiro.serpro");
		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.port", "30287");
		System.setProperty("integrations.PRIVATE.GRPC.LICITACAO.useSSL", "false");

		clientLicitacoes = licitacaoClientProducer.create();
	}

	@AfterAll
	public void shutdown() throws Exception {
		clientLicitacoes.shutdown();
	}

	@Test
	public void testeDaIntegracaoComLicitacaogRPC() throws SSLException {

		GenericResponse resposta = clientLicitacoes.liveness("VRPL");

		assertEquals("Hello VRPL", resposta.getHello());
	}

	@Test
	public void listarProcessosExecucao() {
		Long identificadorDaProposta = 1332647L;

		List<ProcessoExecucao> processosExcecucao = clientLicitacoes.listarProcessosExecucao(identificadorDaProposta)
				.getListaProcessoExecucaoList();

		assertEquals(processosExcecucao.size(), 1);
		
		assertEquals(processosExcecucao.get(0).getId(), 615289);
		assertEquals(processosExcecucao.get(0).getNumeroAno(), "006/2018");
		assertEquals(processosExcecucao.get(0).getDescricaoProcesso(), "Licitação");
		assertEquals(processosExcecucao.get(0).getObjeto(),
				"Contratação de empresa para a prestação dos serviços de mão de obra com o fornecimento de todos os materiais necessários a serem empregados, sob o regime de empreitada por menor preço global, na execução das obras de pavimentação asfáltica das ruas Espírito Santo e Martin Berg.");
		assertFalse(processosExcecucao.get(0).getListaFornecedoresVencedoresList().isEmpty());
		assertEquals(processosExcecucao.get(0).getListaFornecedoresVencedoresList().size(), 1);

		assertEquals(processosExcecucao.get(0).getListaFornecedoresVencedores(0).getRazaoSocial(),
				"CONSTRUTORA E PAVIMENTADORA PAVICON LTDA");
		assertEquals(processosExcecucao.get(0).getListaFornecedoresVencedores(0).getTipoIdentificacao(), "CNPJ");
		assertEquals(processosExcecucao.get(0).getListaFornecedoresVencedores(0).getIdentificacao(), "88256979000104");
		
	}

	@Test
	public void detalharProcessosExecucao() {
		Long identificadorDaLicitacao = 615289L;
		try {
			clientLicitacoes.detalharProcessosExecucao(identificadorDaLicitacao);
		} catch (RuntimeException re) {
			assertEquals("Processo de Execução Inexistente", re.getMessage());
		}
	}

	@Test
	public void detalharProcessosExecucaoDoTipoInexigibilidade() {
		Long identificadorDaLicitacao = 634114L;

		ProcessoExecucaoResponse pe = clientLicitacoes.detalharProcessosExecucao(identificadorDaLicitacao);

		assertEquals(pe.getId(), 634114);
		assertEquals(pe.getNumeroAno(), "0712018");
		assertEquals(pe.getDescricaoProcesso(), "Inexigibilidade");
		assertEquals(pe.getObjeto(),
				"CONTRATAÇÃO DE EMPRESA ESPECIALIZA DA ÁREA DE ARQUITETURA E URBANISMO OU ENGENHARIA CIVIL PARA ELABORAÇÃO DE PROJETO DE IMPLANTAÇÃO E PROJETO EXECUTIVO");
		assertEquals(pe.getValor(), 22000.0);
		assertEquals(pe.getStatus(), "Concluído");
		assertFalse(pe.getListaFornecedoresVencedoresList().isEmpty());
		assertEquals(pe.getListaFornecedoresVencedoresList().size(), 1);

		assertEquals(pe.getListaFornecedoresVencedores(0).getRazaoSocial(), "SILVANA MESSIAS DE FIGUEIREDO FERREIRA");
		assertEquals(pe.getListaFornecedoresVencedores(0).getTipoIdentificacao(), "CNPJ");
		assertEquals(pe.getListaFornecedoresVencedores(0).getIdentificacao(), "00443138000165");

	}

}
