package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;

@Disabled
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class LicitacaoRestIT {

	@BeforeAll
	public void setUp() {
		RestAssured.config = RestAssured.config().encoderConfig(new EncoderConfig().defaultContentCharset("UTF-8"));

		MockitoAnnotations.initMocks(this);
	}

	@DisplayName(value = "Testa Fluxo de Exceção E1. Não foi possível encontrar a proposta indicada")
	@Test
	@Order(1)
	public void fluxoExcecaoPropostaNaoExiste() {
		Response resposta = given().when().get("/licitacao/permissoes/-1").andReturn();

		assertEquals(HttpStatus.SC_NOT_FOUND, resposta.getStatusCode());

		String resultadoEsperado = "{\"data\":{\"severity\":\"ERROR\",\"codigo\":513493,\"message\":\"Não foi possível encontrar a proposta indicada.\"},\"status\":\"fail\"}";

		assertEquals(resultadoEsperado, resposta.getBody().asString());

	}

	@DisplayName(value = "Testa Fluxo de Exceção E2. Processo de Execução não existe")
	@Test
	@Order(1)
	public void fluxoExcecaoProcessoExecucaoNaoExiste() {
		Response resposta = given().when().get("/licitacao/permissoes/1293027").andReturn();

		assertEquals(HttpStatus.SC_NOT_FOUND, resposta.getStatusCode());

		String resultadoEsperado = "{\"data\":{\"severity\":\"ERROR\",\"codigo\":513493,\"message\":\"Processo de Execução não encontrado.\"},\"status\":\"fail\"}";

		assertEquals(resultadoEsperado, resposta.getBody().asString());

	}
}
