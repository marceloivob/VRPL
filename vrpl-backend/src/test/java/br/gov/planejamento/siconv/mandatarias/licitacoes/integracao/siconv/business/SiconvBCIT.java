package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business.PropostaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;


@EnableWeld
public class SiconvBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private PropostaBC propostaBC;

	@Spy
	private SiconvBC siconvBC;

	private Long idProposta = 1L;
	private String cpfUsuarioLogado = "00000000000";//"12345678912";

	private SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA, idProposta, cpfUsuarioLogado);

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		return new MockSiconvPrincipal(Profile.MANDATARIA, cpfUsuarioLogado);
	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		daoFactory.setJdbi(jdbiFactory.createJDBI());
		daoFactory.getJdbi().useHandle(handle -> {
			handle.execute(DataFactory.getAcffo());
			handle.execute(DataFactory.getConvenioInsert());
			handle.execute(DataFactory.getPropostaInsert());
			handle.execute(DataFactory.getOrgAdministrativoInsert());
		});
		siconvBC.setDao(daoFactory);
	}

	@Test
	public void testRecuperarDadosBasicos() {
		DadosBasicosIntegracao dadosBasicos = siconvBC.recuperarDadosBasicos(usuarioLogado);
		assertNotNull(dadosBasicos);
	}

	@Test
	public void testRecuperarDadosProposta() {
		PropostaBD propostaBD = siconvBC.recuperarDadosProposta(usuarioLogado);
		assertNotNull(propostaBD);
	}

	@Disabled
	@Test
	public void testAtualizarDataAssinaturaConvenio() {
		PropostaBD proposta = new PropostaBD();
		assertNull(proposta.getDataAssinaturaConvenio());
		siconvBC.atualizarDadosProposta(proposta, usuarioLogado);
		assertNotNull(proposta.getDataAssinaturaConvenio());
	}

	@Disabled
	@Test
	public void testAtualizarNumeroConvenio() {
		PropostaBD proposta = new PropostaBD();
		siconvBC.atualizarDadosProposta(proposta, usuarioLogado);
		assertNotNull(proposta.getNumeroConvenio());
	}

	@Disabled
	@Test
	public void testAtualizarAnoConvenio() {
		PropostaBD proposta = new PropostaBD();
		siconvBC.atualizarDadosProposta(proposta, usuarioLogado);
		assertNotNull(proposta.getAnoConvenio());
	}

	@Disabled
	@Test
	public void testAtualizarMandataria() {
		PropostaBD proposta = new PropostaBD();
		siconvBC.atualizarDadosProposta(proposta, usuarioLogado);
		assertNotNull(proposta.getNomeMandataria());
	}

	@Test
	public void testAtualizarDadosDaPropostaComNull() {
		assertThrows(IllegalArgumentException.class, () -> siconvBC.atualizarDadosProposta(null, usuarioLogado));
	}

	@Disabled
	@Test
	public void testAtualizarDataAssinaturaNaoAlteraDataExistente() {
		PropostaBD proposta = new PropostaBD();
		proposta.setDataAssinaturaConvenio(LocalDate.of(2020, 05, 23));
		siconvBC.atualizarDadosProposta(proposta, usuarioLogado);
		assertEquals(LocalDate.of(2020, 05, 23), proposta.getDataAssinaturaConvenio());
	}

	@Test
	public void testAtualizarDadosProposta() {
		daoFactory.getJdbi().useHandle(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(cpfUsuarioLogado);
			handle.execute(DataFactory.getVrplProposta(null, null, null, null));
		});
		PropostaBD propostaBD = daoFactory.get(PropostaDAO.class).loadById(1L);
		assertNotNull(propostaBD);
		assertEquals(null, propostaBD.getAnoConvenio());
		assertEquals(null, propostaBD.getNumeroConvenio());
		assertEquals(null, propostaBD.getDataAssinaturaConvenio());
		assertEquals(null, propostaBD.getNomeMandataria());

		daoFactory.getJdbi().useTransaction(transaction -> {
			siconvBC.atualizarDadosProposta(propostaBD, transaction, usuarioLogado);
		});

		PropostaBD novaPropostaBD = daoFactory.get(PropostaDAO.class).loadById(1L);
		assertNotNull(novaPropostaBD.getNumeroConvenio());
		assertEquals(2020, novaPropostaBD.getAnoConvenio());
		assertEquals(Integer.valueOf(883675), novaPropostaBD.getNumeroConvenio());
		assertEquals(LocalDate.of(2020, 1, 9), novaPropostaBD.getDataAssinaturaConvenio());
		assertEquals("CAIXA ECONOMICA FEDERAL", novaPropostaBD.getNomeMandataria());

	}

	@Test
	public void testAtualizarDadosPropostaInfoNotNull() {
		daoFactory.getJdbi().useHandle(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(cpfUsuarioLogado);
			handle.execute(DataFactory.getVrplProposta(123, 1999, LocalDate.of(1999, 1, 9).toString()
					, "MINISTERIO DA EDUCACAO"));
		});
		PropostaBD propostaBD = daoFactory.get(PropostaDAO.class).loadById(1L);
		assertNotNull(propostaBD);
		assertEquals(1999, propostaBD.getAnoConvenio());
		assertEquals(123, propostaBD.getNumeroConvenio());
		assertEquals(LocalDate.of(1999, 1, 9).toString(), propostaBD.getDataAssinaturaConvenio().toString());
		assertEquals("MINISTERIO DA EDUCACAO", propostaBD.getNomeMandataria());

		daoFactory.getJdbi().useTransaction(transaction -> {
			siconvBC.atualizarDadosProposta(propostaBD, transaction, usuarioLogado);
		});

		// Verifica se os dados foram alterados
		PropostaBD novaPropostaBD = daoFactory.get(PropostaDAO.class).loadById(1L);
		assertNotNull(novaPropostaBD.getNumeroConvenio());
		assertEquals(1999, propostaBD.getAnoConvenio());
		assertEquals(123, propostaBD.getNumeroConvenio());
		assertEquals(LocalDate.of(1999, 1, 9).toString(), propostaBD.getDataAssinaturaConvenio().toString());
		assertEquals("MINISTERIO DA EDUCACAO", propostaBD.getNomeMandataria());
	}

}
