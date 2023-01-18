package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.module.SimpleModule;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class CPFSerializerTest {

	private ObjectMapperContextResolver mapper = new ObjectMapperContextResolver();

	private MockInscricaoGenericaSerializer inscricaoGenericaSerializer = new MockInscricaoGenericaSerializer();

	private MockCPFSerializer cpfSerializer = new MockCPFSerializer();

	@DisplayName(value = "Teste serialização de CPF no Acesso Livre com usuário GUEST")
	@Test
	public void testeCPFAcessoLivreComGuest() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.GUEST, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		cpfSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);
		mapper.setCpfSerializer(cpfSerializer);

		CPF cpf = new CPF("11111111111");
		String encontrado = mapper.getContext(CPF.class).writeValueAsString(cpf);
		String esperado = "\"11111111111\"";

		assertEquals(esperado, encontrado);
	}

	@DisplayName(value = "Teste serialização de CPF no Acesso Livre com usuário MANDATARIA")
	@Test
	public void testeCPFAcessoLivreComMandataria() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.MANDATARIA, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		cpfSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);
		mapper.setCpfSerializer(cpfSerializer);

		CPF cpf = new CPF("11111111111");
		String encontrado = mapper.getContext(CPF.class).writeValueAsString(cpf);
		String esperado = "\"11111111111\"";

		assertEquals(esperado, encontrado);
	}

	@DisplayName(value = "Teste serialização de CPF no Acesso Livre com usuário PROPONENTE")
	@Test
	public void testeCPFAcessoLivreComProponente() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		cpfSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);
		mapper.setCpfSerializer(cpfSerializer);

		CPF cpf = new CPF("11111111111");
		String encontrado = mapper.getContext(CPF.class).writeValueAsString(cpf);
		String esperado = "\"11111111111\"";

		assertEquals(esperado, encontrado);
	}

	@DisplayName(value = "Teste serialização de CPF no Acesso Livre com usuário CONCEDENTE")
	@Test
	public void testeCPFAcessoLivreComConcedente() throws IOException {
		SiconvPrincipal usuarioLogadoMockado = new MockSiconvPrincipal(Profile.CONCEDENTE, "11111111111");

		mapper.setSimpleModule(new SimpleModule("AcessoLivreTest"));

		cpfSerializer.setUsuarioLogado(usuarioLogadoMockado);

		mapper.setInscricaoGenericaSerializer(inscricaoGenericaSerializer);
		mapper.setCpfSerializer(cpfSerializer);

		CPF cpf = new CPF("11111111111");
		String encontrado = mapper.getContext(CPF.class).writeValueAsString(cpf);
		String esperado = "\"11111111111\"";

		assertEquals(esperado, encontrado);
	}

}
