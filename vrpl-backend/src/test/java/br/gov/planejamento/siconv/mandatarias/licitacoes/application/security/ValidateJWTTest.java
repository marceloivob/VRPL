package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.GUEST;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.TOKEN_JWT_ASSINATURA_INVALIDA;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.TOKEN_JWT_EXPIRADO;
import static br.gov.planejamento.siconv.mandatarias.test.core.DataFactory.TOKEN_JWT_INVALIDO;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;

public class ValidateJWTTest {

	private final static String PUBLIC_KEY_JWT = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoIVolQydZAWYFnRwluqCNx8mkJ4g+XJmDWFnYwT6uX8PpUd3GGET9orNWQPDyX8iO32pasAWK2JjLFr2jm0+zjhvgTywGbHv5s/io7XeGtn5RrK9o5pdD4OFccvQKElmyQIROFNscNl2Ov53JN6J8aSGCBFWpFuM21jNV/bxx24zAM6AKJGtjQcRwgMiV/FWEzCBDybQ68LzqCDXaxbdPukUnEb5k/N5S9SQqkG+5FMPKwVvpaOXIyA3qyL5ipyNYX8KFa3ZKsPTyYmDqN3296g5unffd8f+r2r4AdrtazkTXlzoHLOh/v2GORw8N73DvFtqjYKWX3bHP/wDPXSTwQIDAQAB";

	private ValidateJWT validateJWT;

	@InjectMocks
	private ApplicationProperties applicationProperties;

	@BeforeAll
	public static void setup() {
		System.setProperty("publickey.jwt", PUBLIC_KEY_JWT);
	}

	@BeforeEach
	public void beforeEach() throws NoSuchAlgorithmException, InvalidKeySpecException {
		MockitoAnnotations.initMocks(this);

		applicationProperties.setPublicKeyJWT(PUBLIC_KEY_JWT);

		this.validateJWT = new ValidateJWT(applicationProperties);
	}

	@Test
	public void testaAssinaturaInvalidaDoToken() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		assertThrows(JWTVerificationException.class, () -> validateJWT.validaToken(TOKEN_JWT_ASSINATURA_INVALIDA));
	}

	@Test
	public void testaTokenInvalido() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		assertThrows(JWTDecodeException.class, () -> validateJWT.validaToken(TOKEN_JWT_INVALIDO));
	}

	@Test
	public void testaTokenExpirado() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		assertThrows(TokenExpiredException.class, () -> validateJWT.validaToken(TOKEN_JWT_EXPIRADO));
	}

	@Test
	public void testaTokenConcedenteValido() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		validateJWT.validaToken(CONCEDENTE);
	}

	@Test
	public void testaTokenProponenteValido() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		validateJWT.validaToken(PROPONENTE);
	}

	@Test
	public void testaTokenMandatariaValido() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		validateJWT.validaToken(MANDATARIA);
	}

	@Test
	public void testaTokenGuestValido() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		validateJWT.validaToken(GUEST);
	}

}
