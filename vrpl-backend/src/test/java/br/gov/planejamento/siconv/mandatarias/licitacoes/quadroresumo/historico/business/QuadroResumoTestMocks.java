package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.mockito.Mockito.mock;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.GeradorDeTicket;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class QuadroResumoTestMocks {

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		return new MockSiconvPrincipal(Profile.MANDATARIA, "1123213");
	}

	@ApplicationScoped
	@Produces
	PoBC producePO() {
		return mock(PoBC.class);
	}

	@ApplicationScoped
	@Produces	
	DAOFactory produceDaoFactory() {
		return mock(DAOFactory.class);
	}

	@ApplicationScoped
	@Produces	
	ValidarSituacaoConcluidaDaLicitacao produceValidar() {
		return mock(ValidarSituacaoConcluidaDaLicitacao.class);
	}

	@ApplicationScoped
	@Produces
	SiconvBC produceSiconvBC() {
		return mock(SiconvBC.class);
	}

	@ApplicationScoped
	@Produces
	MailerUtils produceMailUtils() {
		return mock(MailerUtils.class);
	}
	
	@ApplicationScoped
	@Produces
	GeradorDeTicket produceGeradorDeTicket() {
		return mock(GeradorDeTicket.class);
	}
	
	@ApplicationScoped
	@Produces
	HistoricoLicitacaoBC produceHistorico() {
		return mock(HistoricoLicitacaoBC.class);
	}
}
