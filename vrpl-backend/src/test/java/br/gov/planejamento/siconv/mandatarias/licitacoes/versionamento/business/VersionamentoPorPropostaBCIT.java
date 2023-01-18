package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@Disabled
@EnableWeld
@TestInstance(Lifecycle.PER_CLASS)
public class VersionamentoPorPropostaBCIT {

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	@Spy
	private VersionamentoPorPropostaBC versionamento;

	private final static String cpfUsuarioLogado = "00000000000";

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		return new MockSiconvPrincipal(Profile.MANDATARIA, cpfUsuarioLogado);
	}

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(cpfUsuarioLogado);
		});

	}

	@Test
	@DisplayName(value = "Versionar Proposta")
	public void versionarProposta() {

		PropostaBD proposta = new PropostaBD();
		proposta.setId(1L);
		proposta.setVersao(1L);

		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.INICIAR_COMPLEMENTACAO_CONVENENTE;

		HistoricoLicitacaoDTO novoHistorico = new HistoricoLicitacaoDTO();
		novoHistorico.setEventoGerador(evento.getSigla());

		daoFactory.getJdbi().useTransaction(transaction -> {

			versionamento.criarVersaoDaProposta(proposta, novoHistorico, transaction);

		});
	}

}
