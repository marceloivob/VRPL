package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.GUEST;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.PROPONENTE;
import static io.restassured.RestAssured.given;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@Disabled
@TestMethodOrder(OrderAnnotation.class)
public class AuthenticateRestIT {

	RequestSpecification endpoint;

	@BeforeAll
	public static void initLog4J() {
		RestAssured.config = RestAssured.config().encoderConfig(new EncoderConfig().defaultContentCharset("UTF-8"));
	}

	@Nested
	@DisplayName("Testes de acesso para o próprio perfil")
	class ProprioPerfil {

		@DisplayName(value = "Guest pode acessar conteúdo de Guest")
		@Order(1)
		@Test
		public void euComoGuestPossoAcessarConteudoExclusivoAGuest() {
			euComo(GUEST).possoAcessar("/perfil/guestExclusive");
		}

		@DisplayName(value = "Concedente pode acessar conteúdo de Concedente")
		@Order(2)
		@Test
		public void euComoConcedentePossoAcessarConteudoExclusivoAConcedente() {
			euComo(CONCEDENTE).possoAcessar("/perfil/concedenteExclusive");
		}

		@DisplayName(value = "Proponente pode acessar conteúdo de Proponente")
		@Order(3)
		@Test
		public void euComoProponentePossoAcessarConteudoExclusivoAProponente() {
			euComo(PROPONENTE).possoAcessar("/perfil/proponenteExclusive");
		}

		@DisplayName(value = "Mandatária pode acessar conteúdo de Mandatária")
		@Order(4)
		@Test
		public void euComoMandatariaPossoAcessarConteudoExclusivoAMandataria() {
			euComo(MANDATARIA).possoAcessar("/perfil/mandatariaExclusive");
		}
	}

	@Nested
	@DisplayName("Todo perfil (Mandatária, Concedente, Proponente) acessa conteúdo liberado para GUEST")
	class TodosPodemAcessarGuest {

		@DisplayName(value = "Mandatária pode acessar conteúdo de Guest")
		@Order(5)
		@Test
		public void euComoMandatariaPossoAcessarConteudoExclusivoAGuest() {
			euComo(MANDATARIA).possoAcessar("/perfil/guestExclusive");
		}

		@DisplayName(value = "Concedente pode acessar conteúdo de Guest")
		@Order(6)
		@Test
		public void euComoConcedentePossoAcessarConteudoExclusivoAGuest() {
			euComo(CONCEDENTE).possoAcessar("/perfil/guestExclusive");
		}

		@DisplayName(value = "Proponente pode acessar conteúdo de Guest")
		@Order(7)
		@Test
		public void euComoProponentePossoAcessarConteudoExclusivoAGuest() {
			euComo(PROPONENTE).possoAcessar("/perfil/guestExclusive");
		}
	}

	@Nested
	@DisplayName("Testes de acesso cruzado")
	class TestesCruzado {
		@Nested
		@DisplayName("Guest")
		class Guest {

			@DisplayName(value = "Guest não pode acessar conteúdo restrito a Mandatarias")
			@Order(8)
			@Test
			public void euComoGuestNaoPossoAcessarConteudoExclusivoAConcedente() {
				euComo(GUEST).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/concedenteExclusive");
			}

			@DisplayName(value = "Guest não pode acessar conteúdo restrito a Proponente")
			@Order(9)
			@Test
			public void euComoGuestNaoPossoAcessarConteudoExclusivoAProponente() {
				euComo(GUEST).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/proponenteExclusive");
			}

			@DisplayName(value = "Guest não pode acessar conteúdo restrito a Concedente")
			@Order(10)
			@Test
			public void euComoGuestNaoPossoAcessarConteudoExclusivoAMandataria() {
				euComo(GUEST).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/mandatariaExclusive");
			}
		}

		@Nested
		@DisplayName("Mandatária")
		class Mandatarias {

			@DisplayName(value = "Mandatária não pode acessar conteúdo restrito a Concedente")
			@Order(11)
			@Test
			public void euComoMandatariasNaoPossoAcessarConteudoExclusivoAConcedente() {
				euComo(MANDATARIA).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/concedenteExclusive");
			}

			@DisplayName(value = "Mandatária não pode acessar conteúdo restrito a Proponente")
			@Order(12)
			@Test
			public void euComoMandatariasNaoPossoAcessarConteudoExclusivoAProponente() {
				euComo(MANDATARIA).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/proponenteExclusive");
			}
		}

		@Nested
		@DisplayName("Concedente")
		class Concedente {
			@DisplayName(value = "Concedente não pode acessar conteúdo restrito a Mandatária")
			@Order(13)
			@Test
			public void euComoConcedenteNaoPossoAcessarConteudoExclusivoAMandatarias() {
				euComo(CONCEDENTE).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/mandatariaExclusive");
			}

			@DisplayName(value = "Concedente não pode acessar conteúdo restrito a Proponente")
			@Order(14)
			@Test
			public void euComoConcedenteNaoPossoAcessarConteudoExclusivoAProponente() {
				euComo(CONCEDENTE).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/proponenteExclusive");
			}
		}

		@Nested
		@DisplayName("Proponente")
		class Proponente {
			@DisplayName(value = "Proponente não pode acessar conteúdo restrito a Concedente")
			@Order(15)
			@Test
			public void euComoProponenteNaoPossoAcessarConteudoExclusivoAConcedente() {
				euComo(PROPONENTE).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/concedenteExclusive");
			}

			@DisplayName(value = "Proponente não pode acessar conteúdo restrito a Mandatária")
			@Order(16)
			@Test
			public void euComoProponenteNaoPossoAcessarConteudoExclusivoAMandatarias() {
				euComo(PROPONENTE).naoTenhoAutorizacaoParaAcessarRecursoExclusivoA("/perfil/mandatariaExclusive");
			}
		}
	}

	@DisplayName(value = "Acesso sem Token é bloqueado")
	@Test
	public void acessarSemTokenEhBloqueado() {
		given().when().get("/perfil/withoutToken").then().statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
	}

	private AuthenticateRestIT euComo(String token) {
		this.endpoint = given().auth().oauth2(token).when();

		return this;
	}

	private ValidatableResponse possoAcessar(String endpoint) {
		return this.endpoint.get(endpoint).then().statusCode(Response.Status.OK.getStatusCode());
	};

	private ValidatableResponse naoTenhoAutorizacaoParaAcessarRecursoExclusivoA(String endpoint) {
		return this.endpoint.get(endpoint).then().statusCode(Response.Status.FORBIDDEN.getStatusCode());
	};

}
