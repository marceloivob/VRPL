package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.MockDaoFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class ExibeParecerBCTest {

	@Spy
	private ExibeParecerBC exibeParecer;

	@Mock
	private MockDaoFactory<?> daoFactoryMock;

	@Mock
	private LaudoDAO laudoDAO;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.exibeParecer.setDaoFactory(daoFactoryMock);
		Mockito.when(exibeParecer.getLaudoDAO()).thenReturn(laudoDAO);
	}

	@Test
	public void cenarioUsuarioProponenteComLicitacaoHomologadaEParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.HOMOLOGADA.getSigla());

		Mockito.doReturn(true).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioProponenteComLicitacaoHomologadaSEMParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.HOMOLOGADA.getSigla());

		Mockito.doReturn(false).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertFalse(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioProponenteComLicitacaoAceitaSEMParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.doReturn(false).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioProponenteComLicitacaoAceitaEParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.doReturn(true).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioMandatariaComLicitacaoAceitaEParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.doReturn(true).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioConcedenteComLicitacaoAceitaEParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.CONCEDENTE, "11111111111");
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.doReturn(true).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}

	@Test
	public void cenarioUsuarioGuestComLicitacaoAceitaEParecerEmitido() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.GUEST, "guest");
		
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.doReturn(true).when(exibeParecer).existeParecerEmitido();

		Boolean resultadoEncontrado = exibeParecer.dadaALicitacao(licitacao).eOUsuario(usuarioLogado)
				.devoExibirOParecer();

		assertTrue(resultadoEncontrado);
	}
}
