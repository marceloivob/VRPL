package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.junit5.EnableWeld;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.ConcreteDAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import br.gov.planejamento.siconv.mandatarias.test.core.DataFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.JDBIFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
@DisplayName(value = "Testes de transação")
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TransactionIT {
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Configuração dos testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@InjectMocks
	private JDBIFactory jdbiFactory;

	@InjectMocks
	private ConcreteDAOFactory daoFactory;

	private final static String usuarioLogado = "00000000000";

	@ApplicationScoped
	@Produces
	SiconvPrincipal produceSiconvPrincipal() {
		// https://github.com/weld/weld-junit/blob/master/junit5/README.md
		return new MockSiconvPrincipal(Profile.MANDATARIA, "1123213");
	}

	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		daoFactory.setJdbi(jdbiFactory.createJDBI());

		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

			handle.execute(DataFactory.INSERT_PROPOSTA);
		});
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Testes
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Test
	@DisplayName(value = "Insere uma Licitação")
	@Order(1)
	public void insereLicitacao() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);
			LicitacaoBD novaLicitacao = new LicitacaoBD();

			novaLicitacao.setIdLicitacaoFk(632761L);
			novaLicitacao.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.HOMOLOGADA.getSigla());
			novaLicitacao.setIdentificadorDaProposta(1L);
			novaLicitacao.setNumeroAno("006/2018");
			novaLicitacao.setValorProcessoLicitatorio(new BigDecimal("12.20"));
			novaLicitacao.setSituacaoDoProcessoLicitatorio("Concluído");
			novaLicitacao.setObjeto("objeto");

			LicitacaoBD licitacaoInserida = handle.attach(LicitacaoDAO.class).insertLicitacao(novaLicitacao);

			assertNotNull(licitacaoInserida);

			Long idLicitacaoEsperado = 1L;
			assertEquals(idLicitacaoEsperado, licitacaoInserida.getId());
		});
	}

	@Test
	@DisplayName(value = "Insere um Lote associado a uma Licitação")
	@Order(2)
	public void inserirLicitacaoLote() {
		daoFactory.getJdbi().useTransaction(handle -> {
			handle.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);
			Long idLicitacao = 1L;
			LicitacaoBD licitacaoRecuperada = handle.attach(LicitacaoDAO.class).findLicitacaoById(idLicitacao);

			assertNotNull(licitacaoRecuperada);

			LoteBD novoLote = new LoteBD();
			novoLote.setIdentificadorDaLicitacao(1L);
			novoLote.setNumeroDoLote(1L);

			List<LoteBD> listaDeLotes = new ArrayList<>();
			listaDeLotes.add(novoLote);

			List<LoteBD> resultado = handle.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(listaDeLotes);
			assertNotNull(resultado);
			assertTrue(resultado.size() == 1);
		});
	}

	@Test
	@DisplayName(value = "Confirma que lançamento de exceção faz Rollback na transação")
	@Order(3)
	public void atualiza() {

		try {
			daoFactory.getJdbi().useTransaction(transaction -> {
				transaction.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

				Long idLicitacao = 1L;

				LicitacaoBD licitacaoAntesDaAlteracao = transaction.attach(LicitacaoDAO.class)
						.findLicitacaoById(idLicitacao);

				// Confirma que registro está na situação - Homologada
				assertTrue(licitacaoAntesDaAlteracao.getSituacaoDaLicitacao()
						.equals(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.HOMOLOGADA.getSigla()));

				licitacaoAntesDaAlteracao
						.setSituacaoDaLicitacao(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.DOCUMENTACAO_ACEITA.getSigla());

				// Atualiza situação do registro para Documentação Aceita
				boolean registroAtualizado = transaction.attach(LicitacaoDAO.class)
						.updateSituacaoDaLicitacao(licitacaoAntesDaAlteracao);

				// Confirma que houve a atualização
				assertTrue(registroAtualizado);

				// Recupera o registro atualizado
				LicitacaoBD licitacaoRecuperada = transaction.attach(LicitacaoDAO.class).findLicitacaoById(idLicitacao);

				// Confirma que a situação do documento foi atualizada
				assertTrue(licitacaoRecuperada.getSituacaoDaLicitacao()
						.equals(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.DOCUMENTACAO_ACEITA.getSigla()));

				// Lança exceção para forçar o rollback
				throw new NullPointerException();

			});
		} catch (NullPointerException npe) {

			// Consulta a registro
			Long idLicitacao = 1L;
			LicitacaoBD licitacaoRecuperada = daoFactory.get(LicitacaoDAO.class).findLicitacaoById(idLicitacao);

			// Confirma que alteração não foi realizada
			assertTrue(licitacaoRecuperada.getSituacaoDaLicitacao()
					.equals(SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.HOMOLOGADA.getSigla()));
		}
	}

}
