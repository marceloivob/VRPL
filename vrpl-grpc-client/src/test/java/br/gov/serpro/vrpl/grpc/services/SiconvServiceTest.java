package br.gov.serpro.vrpl.grpc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse;
import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse.Licitacao.SituacaoLicitacaoVRPLEnum;
import br.gov.serpro.vrpl.grpc.client.VRPLGrpcClient;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@TestMethodOrder(OrderAnnotation.class)
public class SiconvServiceTest {

	private VRPLGrpcClient vrplGRPCClient = new VRPLGrpcClient("localhost", 8001, false);

	@Order(1)
	@DisplayName("Fluxo de Exceção E1. Parâmetro inválido: ZERO")
	@Test
	public void fluxoExcecaoE1ParametroInvalidoZero() {
		final Long idPropostaInvalida = 0L;

		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.consultarPermissoesDasLicitacoesDaProposta(idPropostaInvalida));

		assertNotNull(excecao);
		assertEquals(Status.INVALID_ARGUMENT.getCode(), excecao.getStatus().getCode());
		assertEquals("Parâmetro 'Identificador do Instrumento' inválido: " + idPropostaInvalida,
				excecao.getStatus().getDescription());
	}

	@Order(2)
	@DisplayName("Fluxo de Exceção E1. Parâmetro inválido: nulo")
	@Test
	public void fluxoExcecaoE1ParametroInvalidoNulo() {
		final Long idPropostaInvalida = null;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarPermissoesDasLicitacoesDaProposta(idPropostaInvalida));

		assertNotNull(excecao);
		assertEquals("Parâmetro 'Identificador do Instrumento' inválido: null", excecao.getMessage());
	}

	@Order(3)
	@DisplayName("Fluxo de Exceção E2. Processo de Execução não existe")
	@Test
	public void processoDeExecucaoNaoExiste() {
		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.consultarPermissoesDasLicitacoesDaProposta(Long.MAX_VALUE));

		assertNotNull(excecao);
		assertEquals(Status.NOT_FOUND.getCode(), excecao.getStatus().getCode());
		assertEquals("Processo de Execução não encontrado: " + Long.MAX_VALUE, excecao.getStatus().getDescription());

	}

	@Order(4)
	@DisplayName("Fluxo Principal")
	@Test
	public void fluxoPrincipal() {
		final Long idProposta = 1332647L;

		PermissoesDasLicitacoesResponse resultado = vrplGRPCClient
				.consultarPermissoesDasLicitacoesDaProposta(idProposta);

		assertNotNull(resultado);
		assertEquals(resultado.getLicitacaoCount(), 7);
		assertNotNull(resultado.getLicitacao(0));
		assertEquals(resultado.getLicitacao(0).getAlterar(), true);
		assertEquals(resultado.getLicitacao(0).getExcluir(), false);
		assertEquals(resultado.getLicitacao(0).getEstado(), SituacaoLicitacaoVRPLEnum.EM_COMPLEMENTACAO);

	}
}
