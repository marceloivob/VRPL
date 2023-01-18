package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestConfig;

public class BasicAuthenticatorHeaderGenerator {

	@Inject
	private SiconvRestConfig siconvRestConfig;

	public String create(SiconvPrincipal usuarioLogado) {
		Objects.requireNonNull(usuarioLogado);
		Objects.requireNonNull(usuarioLogado.getCpf());

		try {
			String cpfEmTextoLivre = usuarioLogado.getCpf();

			String secretKeyEmBase64 = siconvRestConfig.getSecretKey();

			String cpfESecretKeyEmMD5 = getMD5(cpfEmTextoLivre.concat(secretKeyEmBase64));

			String cpfEmTextoLivreESecretKeyEmBase64 = transformInBase64(cpfESecretKeyEmMD5);

			String headerAuthenticator = String.format("Basic %s:%s", cpfEmTextoLivre,
					cpfEmTextoLivreESecretKeyEmBase64);

			return headerAuthenticator;
		} catch (NoSuchAlgorithmException algorithmException) {
			throw new IllegalArgumentException("Não foi possível gerar o Header.", algorithmException);
		}
	}

	protected String getMD5(String text) throws NoSuchAlgorithmException {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();

			md.update(text.getBytes("ISO-8859-1"));
			byte[] digest = md.digest();

			return new String(digest, "ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot generate MD5 with ISO-8859-1", ex);
		}
	}

	protected String transformInBase64(String text) {
		try {
			String encoded = Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));

			return encoded;
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode with ISO-8859-1", ex);
		}
	}

}
