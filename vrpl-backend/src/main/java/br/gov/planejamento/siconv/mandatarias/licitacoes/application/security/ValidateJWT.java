package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties;
import lombok.Setter;

@ApplicationScoped
public class ValidateJWT {

	// thread safe? https://github.com/auth0/java-jwt/issues/108
	private JWTVerifier verifier;

	@Inject
	@Setter
	private ApplicationProperties applicationProperties;

	@PostConstruct
	public void createValidateJWT() throws NoSuchAlgorithmException, InvalidKeySpecException {
		Algorithm algorithm = Algorithm.RSA256(readPublicKey(), null);
		verifier = JWT.require(algorithm).withIssuer("siconvidp").build();
	}

	/**
	 * Construtor Padrão para o CDI
	 */
	public ValidateJWT() {
		// noop
	}

	public ValidateJWT(ApplicationProperties applicationProperties)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		this.applicationProperties = applicationProperties;

		Algorithm algorithm = Algorithm.RSA256(readPublicKey(), null);
		verifier = JWT.require(algorithm).withIssuer("siconvidp").build();
	}

	public DecodedJWT validaToken(String token) {
		return verifier.verify(token);
	}

	private RSAPublicKey readPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {

		String publicKeyJWT = applicationProperties.getPublicKeyJWT();

		byte[] publicKeyJWTDecodedWithBase64 = Base64.getDecoder().decode(publicKeyJWT);

		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKeyJWTDecodedWithBase64);

		KeyFactory kf = KeyFactory.getInstance("RSA");

		return (RSAPublicKey) kf.generatePublic(pubKeySpec);
	}

}
