package br.gov.serpro.vrpl.grpc.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.gov.serpro.vrpl.grpc.ListaSubmetaRequest;
import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.submeta.SubmetaRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

@DisplayName("Teste do Serviço para Recuperar as Submetas Do Projeto Básico a Partir Das Submetas Do VRPL")
public class SubmetasDEPARAServiceTest {

	@Nested
	@DisplayName("Validação das Pré-Condições")
	class Precondicoes {

		@Mock
		private SubmetaRepository submetaRepository;

		@BeforeEach
		public void setup() {
			MockitoAnnotations.openMocks(this);
		}

		@DisplayName("Validando parâmetro Repositório nulo")
		@Test
		public void validaPreCondicoesRepositorioNulo() {
			assertThrows(IllegalArgumentException.class, () -> new SubmetasDEPARAService(null, null));
		}

		@DisplayName("Validando parâmetro Lista de Submetas nulo")
		@Test
		public void validaPreCondicoesListaDeSubmetaNulo() {

			StatusRuntimeException resultado = assertThrows(StatusRuntimeException.class,
					() -> new SubmetasDEPARAService(submetaRepository, null));

			assertEquals(resultado.getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
		}

		@DisplayName("Validando parâmetro Lista de Submetas vazia")
		@Test
		public void validaPreCondicoesParametroVazio() {
			ListaSubmetaRequest listaDeSubmetasVazia = ListaSubmetaRequest.newBuilder().build();

			StatusRuntimeException resultado = assertThrows(StatusRuntimeException.class,
					() -> new SubmetasDEPARAService(submetaRepository, listaDeSubmetasVazia));

			assertEquals(resultado.getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
		}
	}

	@Nested
	@DisplayName("Regras de Negócio")
	class Negocio {

		private SubmetaRepository submetaRepository;

		@BeforeEach
		public void setup() {
			this.submetaRepository = new SubmetaRepository(JDBIProducer.getJdbi());
		}

		@Test
		@DisplayName("Submetas não encontradas")
		public void submetasNaoEncontradas() {
			final Long idInvalido = -1l;
			ListaSubmetaRequest listaDeSubmetas = ListaSubmetaRequest //
					.newBuilder() //
					.addIds(idInvalido) //
					.build();

			SubmetasDEPARAService servico = new SubmetasDEPARAService(submetaRepository, listaDeSubmetas);
			StatusRuntimeException resultado = assertThrows(StatusRuntimeException.class,
					() -> servico.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL());

			assertEquals(resultado.getStatus().getCode(), Status.NOT_FOUND.getCode());
		}

		@Test
		@DisplayName("Caso feliz - Submetas encontradas")
		public void submetasEncontradas() {
			final Long idSubmeta2370 = 2370L;
			final Long idSubmeta2371 = 2371L;
			final Long idSubmetaInexistente = -1L;

			ListaSubmetaRequest listaDeSubmetas = ListaSubmetaRequest //
					.newBuilder() //
					.addIds(idSubmeta2370) //
					.addIds(idSubmeta2371) //
					.addIds(idSubmetaInexistente) //
					.build();

			SubmetasDEPARAService servico = new SubmetasDEPARAService(submetaRepository, listaDeSubmetas);

			Map<Long, Long> submetasDoProjetoBasico = servico
					.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL();

			assertNotNull(submetasDoProjetoBasico);
		}

	}
}
