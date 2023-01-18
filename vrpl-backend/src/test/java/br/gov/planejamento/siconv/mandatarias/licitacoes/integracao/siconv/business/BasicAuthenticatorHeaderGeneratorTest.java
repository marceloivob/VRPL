package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestConfig;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class BasicAuthenticatorHeaderGeneratorTest {

	@InjectMocks
	private BasicAuthenticatorHeaderGenerator basicAuthenticatorHeaderGenerator;

	@Spy
	private SiconvRestConfig siconvRestConfig;

	private final String secretKey = "aW50ZWdyYWNhb192cnBsX3NpY29udg==";

	private final String cpfGenerico = "15327205487";
	private final String cpfEmBase64Generico = "w79hw7UENMKWQMKpIGIZGHfDijnClA==";
	private final String variavelDeControleGenerica = "Basic " + cpfGenerico + ":" + cpfEmBase64Generico;

	private final String cpf_0009 = "00009248145";
	private final String cpfEmBase64_0009 = "Xh0Bw4kfG8KcHsO4wr8TwpIpF8OrSA==";
	private final String variavelDeControle_0009 = "Basic " + cpf_0009 + ":" + cpfEmBase64_0009;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		Mockito.when(siconvRestConfig.getSecretKey()).thenReturn(secretKey);
	}

	@Test
	public void encode() {

		// Caso de Teste 1
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE, cpfGenerico);

		String resultado = basicAuthenticatorHeaderGenerator.create(usuarioLogado);

		assertEquals(variavelDeControleGenerica, resultado);

		// Caso de Teste 2
		SiconvPrincipal usuarioLogado2 = new MockSiconvPrincipal(Profile.PROPONENTE, cpf_0009);

		String resultado2 = basicAuthenticatorHeaderGenerator.create(usuarioLogado2);

		assertEquals(variavelDeControle_0009, resultado2);

	}

	@Test
	public void decode() throws UnsupportedEncodingException {

		try {

			// Caso de Teste 1

			byte[] decoded = Base64.getDecoder().decode(cpfEmBase64Generico.getBytes("UTF-8"));

			String cpfEmMD5 = new String(decoded, "UTF-8");

			String novoMD5Calculado = basicAuthenticatorHeaderGenerator.getMD5(cpfGenerico.concat(secretKey));

			assertEquals(novoMD5Calculado, cpfEmMD5);

			// Caso de Teste 2

			byte[] decoded_009 = Base64.getDecoder().decode(cpfEmBase64_0009.getBytes("UTF-8"));

			String cpfEmMD5_009 = new String(decoded_009, "UTF-8");

			String novoMD5Calculado_009 = basicAuthenticatorHeaderGenerator.getMD5(cpf_0009.concat(secretKey));

			assertEquals(novoMD5Calculado_009, cpfEmMD5_009);

		} catch (NoSuchAlgorithmException algorithmException) {
			throw new IllegalArgumentException("Não foi possível gerar o HASH.", algorithmException);
		}

	}

}
