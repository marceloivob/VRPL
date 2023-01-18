package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.rest;

import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.GUEST;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.PROPONENTE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
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
public class AnexoRestIT {

	@BeforeAll
	public void setUp() {
		RestAssured.config = RestAssured.config().encoderConfig(new EncoderConfig().defaultContentCharset("UTF-8"));

		MockitoAnnotations.initMocks(this);
	}

	@DisplayName(value = "Verifica que o serviço de Recuperação dos Tipos de Anexos está OK")
	@Test
	@Order(1)
	public void sucessoRecuperacaoDeTiposDeAnexo() {
		Response resposta = given().auth().oauth2(GUEST).when().get("/anexos/tipos/").andReturn();
		assertEquals(HttpStatus.SC_OK, resposta.getStatusCode());
	}

	@DisplayName(value = "Verifica que o contrato do serviço de Recuperação dos Tipos de Anexos está mantido")
	@Test
	@Order(2)
	public void contratoIgualDeTiposDeAnexo() {
		Response resposta = given().auth().oauth2(GUEST).when().get("/anexos/tipos/").andReturn();

		JsonObject tiposDeAnexos = Json.createObjectBuilder()
				.add("ATA_HOMOLOGACAO_LICITACAO", "Ata de Homologação da Licitação")
				.add("DESPACHO_ADJUDICACAO", "Despacho de Adjudicação").add("OUTROS", "Outros")
				.add("QUADRO_RESUMO", "Quadro Resumo").add("RESUMO_EDITAL", "Publicado Resumo do Edital")
				.add("VRPL", "VRPL").build();

		JsonObject json = Json.createObjectBuilder().add("data", tiposDeAnexos).add("status", "SUCCESS").build();

		String valorEsperado = json.toString();

		String valorEncontrado = resposta.body().asString();

		assertEquals(valorEsperado, valorEncontrado);
	}


	@Nested
	@DisplayName("Verifica que o serviço de Listar Anexos está liberado para:")
	class TestaServicoListaAnexo {

		@DisplayName(value = "GUEST")
		@Test
		public void sucessoListarAnexosParaGuest() {
			Response resposta = given().auth().oauth2(GUEST).when().get("/anexos/1/").andReturn();
			assertEquals(HttpStatus.SC_OK, resposta.getStatusCode());
		}

		@DisplayName(value = "MANDATÁRIA")
		@Test
		public void sucessoListarAnexosParaMandataria() {
			Response resposta = given().auth().oauth2(MANDATARIA).when().get("/anexos/1/").andReturn();
			assertEquals(HttpStatus.SC_OK, resposta.getStatusCode());
		}

		@DisplayName(value = "PROPONENTE")
		@Test
		public void sucessoListarAnexosParaProponente() {
			Response resposta = given().auth().oauth2(PROPONENTE).when().get("/anexos/1/").andReturn();
			assertEquals(HttpStatus.SC_OK, resposta.getStatusCode());
		}

		@DisplayName(value = "CONCEDENTES")
		@Test
		public void sucessoListarAnexosParaConcedente() {
			Response resposta = given().auth().oauth2(CONCEDENTE).when().get("/anexos/1/").andReturn();
			assertEquals(HttpStatus.SC_OK, resposta.getStatusCode());
		}

	}

	@Nested
	@DisplayName("Testa o Serviço de Upload(Anexar)")
	class TestaServicoAnexar {

		@DisplayName(value = "O serviço de Anexar está bloqueado para GUEST")
		@Test
		public void bloqueioParaAnexarPorGuest() {
			Response resposta = given().auth().oauth2(GUEST).when().multiPart("Qualquer conteúdo", "Qualquer conteúdo")
					.post("/anexos/1/anexo/").andReturn();
			assertEquals(HttpStatus.SC_FORBIDDEN, resposta.getStatusCode());
		}

		@DisplayName(value = "O serviço de Anexar está liberado para MANDATÁRIA")
		@Test
		public void anexarPorMandataria() {
			String string = "ArquivoDeTeste";
			Long tamanhoDoArquivo = 14L;
			Long idLicitacao = 615289L;

			// use ByteArrayInputStream to get the bytes of the String and convert them to
			// InputStream.
			InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));

			Response resposta = given().auth().oauth2(MANDATARIA).when().multiPart("arquivo", "arquivo2", inputStream)
					.formParam("nomeArquivo", LocalDateTime.now() + "Arquivo Anexado.PDF")
					.formParam("descricao", "Arquivo" + LocalDateTime.now()).formParam("tipoAnexo", "OUTROS")
					.formParam("tamanhoArquivo", tamanhoDoArquivo).post("anexos/{idLicitacao}/anexo/", idLicitacao)
					.andReturn();

			assertEquals("{\"data\":\"ok\",\"status\":\"SUCCESS\"}", resposta.getBody().asString());

			assertEquals(HttpStatus.SC_OK, resposta.statusCode());

		}

		@DisplayName(value = "O serviço de Anexar está liberado para PROPONENTE")
		@Test
		public void anexarPorProponente() {
			String string = "ArquivoDeTeste";
			Long tamanhoDoArquivo = 14L;
			Long idLicitacao = 615289L;

			// use ByteArrayInputStream to get the bytes of the String and convert them to
			// InputStream.
			InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));

			Response resposta = given().auth().oauth2(PROPONENTE).when().multiPart("arquivo", "arquivo2", inputStream)
					.formParam("nomeArquivo", LocalDateTime.now() + "Arquivo Anexado.PDF")
					.formParam("descricao", "Arquivo" + LocalDateTime.now()).formParam("tipoAnexo", "OUTROS")
					.formParam("tamanhoArquivo", tamanhoDoArquivo).post("anexos/{idLicitacao}/anexo/", idLicitacao)
					.andReturn();

			assertEquals("{\"data\":\"ok\",\"status\":\"SUCCESS\"}", resposta.getBody().asString());

			assertEquals(HttpStatus.SC_OK, resposta.statusCode());
		}

		@DisplayName(value = "O serviço de Anexar está liberado para CONCEDENTES")
		@Test
		public void anexarPorConcedente() {
			String string = "ArquivoDeTeste";
			Long tamanhoDoArquivo = 14L;
			Long idLicitacao = 615289L;

			// use ByteArrayInputStream to get the bytes of the String and convert them to
			// InputStream.
			InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));

			Response resposta = given().auth().oauth2(CONCEDENTE).when().multiPart("arquivo", "arquivo2", inputStream)
					.formParam("nomeArquivo", LocalDateTime.now() + "Arquivo Anexado.PDF")
					.formParam("descricao", "Arquivo" + LocalDateTime.now()).formParam("tipoAnexo", "OUTROS")
					.formParam("tamanhoArquivo", tamanhoDoArquivo).post("anexos/{idLicitacao}/anexo/", idLicitacao)
					.andReturn();

			assertEquals("{\"data\":\"ok\",\"status\":\"SUCCESS\"}", resposta.getBody().asString());

			assertEquals(HttpStatus.SC_OK, resposta.statusCode());
		}

	}

}
