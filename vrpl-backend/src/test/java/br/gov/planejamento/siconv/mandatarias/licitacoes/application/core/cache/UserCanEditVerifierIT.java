package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache;

import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.PROPONENTE;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
public class UserCanEditVerifierIT {

	@BeforeAll
	public void setUp() {
		RestAssured.config = RestAssured.config().encoderConfig(new EncoderConfig().defaultContentCharset("UTF-8"));

		MockitoAnnotations.initMocks(this);
	}

	@DisplayName(value = "Verifica o controle de acesso por registro")
	@Test
	public void verificaControleDeAcessoPorRegistro() {

		String string = "ArquivoDeTeste";
		Long tamanhoDoArquivo = 14L;
		Long idDeAnexoQueNaoPertenceAUsuario = 987987987987L;

		// use ByteArrayInputStream to get the bytes of the String and convert them to
		// InputStream.
		InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));

		Response resposta = given().auth().oauth2(PROPONENTE).when().multiPart("arquivo", "arquivo2", inputStream)
				.formParam("nomeArquivo", LocalDateTime.now() + "Nome do Arquivo Anexado.PDF")
				.formParam("descricao", "Anexado" + LocalDateTime.now()).formParam("tipoAnexo", "OUTROS")
				.formParam("tamanhoArquivo", tamanhoDoArquivo).formParam("versao", 0)
				.post("anexos/anexo/{idAnexo}/update", idDeAnexoQueNaoPertenceAUsuario).andReturn();

		assertEquals(
				"{\"data\":{\"severity\":\"ERROR\",\"codigo\":2,\"message\":\"Usuário está tentando acessar recurso que não tem permissão.\"},\"status\":\"fail\"}",
				resposta.getBody().asString());

		assertEquals(HttpStatus.SC_FORBIDDEN, resposta.statusCode());
	}

}
