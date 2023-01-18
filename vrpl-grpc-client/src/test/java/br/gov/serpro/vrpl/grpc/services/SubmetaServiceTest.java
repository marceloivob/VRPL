package br.gov.serpro.vrpl.grpc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import br.gov.serpro.vrpl.grpc.ListaSubmetaResponse;
import br.gov.serpro.vrpl.grpc.PropostaLote;
import br.gov.serpro.vrpl.grpc.SubmetaResponse;
import br.gov.serpro.vrpl.grpc.client.VRPLGrpcClient;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@TestMethodOrder(OrderAnnotation.class)
public class SubmetaServiceTest {

	private VRPLGrpcClient vrplGRPCClient = new VRPLGrpcClient("localhost", 8001, false);

	@Order(1)
	@DisplayName("[Lista de Submetas] Fluxo Principal - Lista de Submetas")
	@Test
	public void fluxoPrincipal_listasubmetas() {
		final Long id = 131L;
		final Long id2 = 209L;
		final Long id3 = 260L;
		List<Long> ids = new ArrayList<>();
		ids.add(id);
		ids.add(id2);
		ids.add(id3);

		ListaSubmetaResponse response = vrplGRPCClient.consultarListaSubmetas(ids);

		assertNotNull(response);
		assertEquals(response.getSubmetaList().get(0).getDescricao(), "Construção de Creas");
		assertEquals(response.getSubmetaList().get(0).getValorLicitado(), "421648.73");

	}

	@Order(2)
	@DisplayName("[Lista de Submetas] Fluxo de Exceção E1. Parâmetro inválido: empty")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoEmpty_listasubmetas() {
		List<Long> ids = new ArrayList<>();

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarListaSubmetas(ids));

		assertNotNull(excecao);
		assertEquals("Parâmetro 'Identificador das Submetas' inválido: []", excecao.getMessage());
	}

	@Order(3)
	@DisplayName("[Lista de Submetas] Fluxo de Exceção E1. Parâmetro inválido: nulo")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoNulo_listasubmetas() {
		List<Long> ids = null;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarListaSubmetas(ids));

		assertNotNull(excecao);
		assertEquals("Parâmetro 'Identificador das Submetas' inválido: null", excecao.getMessage());
	}

	@Order(4)
	@DisplayName("[Lista de Submetas] Fluxo de Exceção E2. Submeta não existe")
	@Test
	public void fluxoExcecao_NaoExiste_listasubmetas() {
		List<Long> ids = new ArrayList<>();
		ids.add(Long.MAX_VALUE);
		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.consultarListaSubmetas(ids));

		assertNotNull(excecao);
		assertEquals(Status.NOT_FOUND.getCode(), excecao.getStatus().getCode());
		assertEquals("Submetas não encontradas: [" + Long.MAX_VALUE + "]", excecao.getStatus().getDescription());

	}

	@Order(5)
	@DisplayName("[Submeta por Id] Fluxo Principal - Submeta por Id")
	@Test
	public void fluxoPrincipal_get_submeta_porid() {
		final Long id = 131L;

		SubmetaResponse response = vrplGRPCClient.consultarSubmetaPorId(id);

		assertNotNull(response);
		assertEquals(response.getSubmeta().getDescricao(), "Construção de Creas");
		assertEquals(response.getSubmeta().getValorLicitado(), "421648.73");
	}

	@Order(6)
	@DisplayName("[Submeta por Id] Fluxo de Exceção E1. Parâmetro inválido: ZERO")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoZero_submeta_porid() {
		final Long id = 0L;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarSubmetaPorId(id));

		assertNotNull(excecao);
		assertEquals("Parâmetro 'Identificador da Submeta' inválido: " + id, excecao.getMessage());
	}

	@Order(7)
	@DisplayName("[Submeta por Id] Fluxo de Exceção E1. Parâmetro inválido: nulo")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoNulo_submeta_porid() {
		final Long id = null;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarSubmetaPorId(id));

		assertNotNull(excecao);
		assertEquals("Parâmetro 'Identificador da Submeta' inválido: null", excecao.getMessage());
	}

	@Order(8)
	@DisplayName("[Submeta por Id] Fluxo de Exceção E2. Submeta não existe")
	@Test
	public void fluxoExcecao_NaoExiste_submeta_porid() {
		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.consultarSubmetaPorId(Long.MAX_VALUE));

		assertNotNull(excecao);
		assertEquals(Status.NOT_FOUND.getCode(), excecao.getStatus().getCode());
		assertEquals("Submeta não encontrada: " + Long.MAX_VALUE, excecao.getStatus().getDescription());

	}

	@Order(9)
	@DisplayName("[Lotes com Submetas] Fluxo Principal - Lista de Lotes com Submetas")
	@Test
	public void fluxoPrincipal_listarLotesComSubmetas() {

		Long idPropostaSiconv = 1480235L;

		PropostaLote propostaLote = vrplGRPCClient.consultarListaLotesComSubmetas(idPropostaSiconv).getPropostaLote();

		assertNotNull(propostaLote);
		assertEquals(4, propostaLote.getLotesList().size());
		assertEquals(2, propostaLote.getLotesList().get(0).getSubmetasCount());
		assertEquals(2, propostaLote.getLotesList().get(1).getSubmetasCount());
		assertEquals(1, propostaLote.getLotesList().get(2).getSubmetasCount());
		assertEquals(1, propostaLote.getLotesList().get(3).getSubmetasCount());
	}

	@Order(10)
	@DisplayName("[Lotes com Submetas] Fluxo de Exceção E1. Parâmetro inválido: nulo")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoNulo_listarLotesComSubmetas() {

		Long idPropostaSiconv = null;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarListaLotesComSubmetas(idPropostaSiconv));

		assertEquals("Parâmetro 'Identificador da Proposta no Siconv' inválido: null", excecao.getMessage());
	}

	@Order(11)
	@DisplayName("[Lotes com Submetas] Fluxo de Exceção E1. Parâmetro inválido: zero")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoZero_listarLotesComSubmetas() {

		Long idPropostaSiconv = 0L;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarListaLotesComSubmetas(idPropostaSiconv));

		assertEquals("Parâmetro 'Identificador da Proposta no Siconv' inválido: 0", excecao.getMessage());
	}

	@Order(12)
	@DisplayName("[Lotes com Submetas] Fluxo de Exceção E1. Parâmetro inválido: número negativo")
	@Test
	public void fluxoExcecao_E1ParametroInvalidoNegativo_listarLotesComSubmetas() {

		Long idPropostaSiconv = -5L;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.consultarListaLotesComSubmetas(idPropostaSiconv));

		assertEquals("Parâmetro 'Identificador da Proposta no Siconv' inválido: -5", excecao.getMessage());
	}

	@Order(13)
	@DisplayName("[Lotes com Submetas] Fluxo de Exceção E2. Lotes/Submetas não encontrados")
	@Test
	public void fluxoExcecao_E2LotesSubmetasNaoEncontrados_listarLotesComSubmetas() {

		Long idPropostaSiconv = 123456L;

		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.consultarListaLotesComSubmetas(idPropostaSiconv));

		assertEquals(Status.NOT_FOUND.getCode(), excecao.getStatus().getCode());
		assertEquals("Lotes/Submetas não encontrados para o identificador da proposta: " + idPropostaSiconv,
				excecao.getStatus().getDescription());
	}

	@Order(14)
	@DisplayName("[Recuperar Submetas de Análise a partir das Submetas do VRPL] Fluxo de Exceção E1. Identificadores não fornecidos")
	@Test
	public void fluxoExcecao_E1ListaVazia_recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL() {

		List<Long> listaVazia = new ArrayList<>();

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(listaVazia));

		assertEquals("Parâmetro 'Lista de IDs das Submetas do VRPL' inválido: []", excecao.getMessage());
	}

	@Order(14)
	@DisplayName("[Recuperar Submetas de Análise a partir das Submetas do VRPL] Fluxo de Exceção E1. Identificadores não fornecidos")
	@Test
	public void fluxoExcecao_E1ListaNula_recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL() {

		List<Long> listaNula = null;

		IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class,
				() -> vrplGRPCClient.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(listaNula));

		assertEquals("Parâmetro 'Lista de IDs das Submetas do VRPL' inválido: null", excecao.getMessage());
	}

	@Order(14)
	@DisplayName("[Recuperar Submetas de Análise a partir das Submetas do VRPL] Fluxo de Exceção E2. Submetas não encontradas")
	@Test
	public void fluxoExcecao_E2SubmetaNaoEncontrada_recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL() {

		List<Long> submetaInexistente = new ArrayList<>();
		submetaInexistente.add(-1L);

		StatusRuntimeException excecao = assertThrows(StatusRuntimeException.class,
				() -> vrplGRPCClient.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(submetaInexistente));

		assertEquals(Status.NOT_FOUND.getCode(), excecao.getStatus().getCode());
		assertEquals("Não foi encontrada nenhuma Submeta com os IDs: " + submetaInexistente,
				excecao.getStatus().getDescription());

	}

	@Order(14)
	@DisplayName("[Recuperar Submetas de Análise a partir das Submetas do VRPL] Fluxo Principal")
	@Test
	public void cenarioFeliz_recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL() {

		final Long idSubmeta2370 = 2370L;
		final Long idSubmeta2371 = 2371L;
		final Long idSubmetaInexistente = -1L;

		List<Long> submetas = new ArrayList<>();
		submetas.add(idSubmeta2370);
		submetas.add(idSubmeta2371);
		submetas.add(idSubmetaInexistente);

		Map<Long, Long> submetasEncontradas = vrplGRPCClient
				.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(submetas);

		assertNotNull(submetasEncontradas);

		assertEquals(submetasEncontradas.size(), 2);

	}

}
