package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.module.SimpleModule;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class InscricaoGenericaSerializerTest {

	private ObjectMapperContextResolver mapper = new ObjectMapperContextResolver();

	private MockInscricaoGenericaSerializer inscricaoGenericaSerializer = new MockInscricaoGenericaSerializer();
	private MockCPFSerializer cpfSerializer = new MockCPFSerializer();

	@DisplayName(value = "Teste serialização de Inscrição Genérica no Acesso Livre com usuário GUEST")
	@Test
	public void testeInscricaoGenericaTamanhoNormalAcessoLivreComGuest() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.GUEST, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		inscricaoGenericaSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setCpfSerializer(cpfSerializer);
		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);

		IG casoDeTeste01 = new IG("EX0250067");
		String encontrado01 = mapper.getContext(IG.class).writeValueAsString(casoDeTeste01);
		String esperado01 = "{\"tipo\":\"IG\",\"valor\":\"EX0250067\"}";

		assertEquals(esperado01, encontrado01);

		IG casoDeTeste02 = new IG("7");
		String encontrado02 = mapper.getContext(IG.class).writeValueAsString(casoDeTeste02);
		String esperado02 = "{\"tipo\":\"IG\",\"valor\":\"7\"}";

		assertEquals(esperado02, encontrado02);

		IG casoDeTeste03 = new IG("71");
		String encontrado03 = mapper.getContext(IG.class).writeValueAsString(casoDeTeste03);
		String esperado03 = "{\"tipo\":\"IG\",\"valor\":\"71\"}";

		assertEquals(esperado03, encontrado03);

		IG casoDeTeste04 = new IG("21761 OAB/PE");
		String encontrado04 = mapper.getContext(IG.class).writeValueAsString(casoDeTeste04);
		String esperado04 = "{\"tipo\":\"IG\",\"valor\":\"21761 OAB/PE\"}";

		assertEquals(esperado04, encontrado04);

	}

	@DisplayName(value = "Teste serialização de Inscrição Genérica no Acesso Livre com usuário MANDATARIA")
	@Test
	public void testeInscricaoGenericaAcessoLivreComMandataria() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.MANDATARIA, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		inscricaoGenericaSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setCpfSerializer(cpfSerializer);
		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);

		IG casoDeTeste = new IG("7");
		String encontrado = mapper.getContext(IG.class).writeValueAsString(casoDeTeste);
		String esperado = "{\"tipo\":\"IG\",\"valor\":\"7\"}";

		assertEquals(esperado, encontrado);
	}

	@DisplayName(value = "Teste serialização de Inscrição Genérica no Acesso Livre com usuário PROPONENTE")
	@Test
	public void testeInscricaoGenericaAcessoLivreComProponente() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		inscricaoGenericaSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setCpfSerializer(cpfSerializer);
		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);

		IG casoDeTeste = new IG("Passaporte 6762624-9");
		String encontrado = mapper.getContext(IG.class).writeValueAsString(casoDeTeste);
		String esperado = "{\"tipo\":\"IG\",\"valor\":\"Passaporte 6762624-9\"}";

		assertEquals(esperado, encontrado);
	}

	@DisplayName(value = "Teste serialização de Inscrição Genérica no Acesso Livre com usuário CONCEDENTE")
	@Test
	public void testeInscricaoGenericaAcessoLivreComConcedente() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.CONCEDENTE, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		inscricaoGenericaSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setCpfSerializer(cpfSerializer);
		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);

		IG casoDeTeste = new IG("C9T219YUk");
		String encontrado = mapper.getContext(IG.class).writeValueAsString(casoDeTeste);
		String esperado = "{\"tipo\":\"IG\",\"valor\":\"C9T219YUk\"}";

		assertEquals(esperado, encontrado);
	}

}
