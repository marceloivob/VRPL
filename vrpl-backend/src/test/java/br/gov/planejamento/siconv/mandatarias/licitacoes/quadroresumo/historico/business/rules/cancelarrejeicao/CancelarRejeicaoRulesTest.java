package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Date;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoActionFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoTestMocks;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.SiconvRestMock;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteNaoExisteMaisException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao.HistoricoLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.MockDaoFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
public class CancelarRejeicaoRulesTest {

	@WeldSetup
	public WeldInitiator weld = WeldInitiator
			.from(SiconvRestMock.class, QuadroResumoActionFactory.class, QuadroResumoTestMocks.class,
					BusinessExceptionContext.class, BusinessExceptionProducer.class, CancelarRejeicaoRules.class)
			.activate(RequestScoped.class, SessionScoped.class).build();

	@Inject
	private CancelarRejeicaoRules cancelarRejeicaoRules;

	@Mock
	private MockDaoFactory<?> daoFactoryMock;

	@Mock
	private LoteLicitacaoDAO loteLicitacaoDAOMock;

	@Mock
	private LicitacaoDAO licitacaoDAOMock;

	@Mock
	private HistoricoLicitacaoDAO historicoLicitacaoDAOMock;

	@Mock
	private SiconvRest siconvRestMock;

	@Inject
	private BusinessExceptionContext businessExceptionContext;

	private static MockSiconvPrincipal siconvPrincipal;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		cancelarRejeicaoRules.setDao(daoFactoryMock);

		Mockito.when(cancelarRejeicaoRules.getLoteLicitacaoDAO()).thenReturn(loteLicitacaoDAOMock);
		Mockito.when(cancelarRejeicaoRules.getLicitacaoDAO()).thenReturn(licitacaoDAOMock);
		Mockito.when(cancelarRejeicaoRules.getHistoricoLicitacaoDAO()).thenReturn(historicoLicitacaoDAOMock);
	}

	@Order(1)
	@DisplayName("Validação 1 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " pelo menos um número de lote não existir mais.\r\n" + " (Não existem todos números de lotes)")
	@Test
	public void validacao1NaoExistemTodosNumerosDeLotes() {

		Mockito.when(loteLicitacaoDAOMock.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("3", "2", "4", "1")));

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue())).thenReturn(getLoteBDFromNrLote(Arrays.asList("4")));

		cancelarRejeicaoRules.validarExistenciaTodosNumerosDeLotes(getPropostaAtual(), getLicitacaoEstadoAtual(),
				getUsuarioLogadoConcedente());

		assertThrows(BusinessException.class, () -> businessExceptionContext.throwException());

		String mensagemEsperada = MessageFormat
				.format(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_NAO_EXISTE_MAIS.getMensagem(), "1, 2 e 3");

		assertEquals(mensagemEsperada,
				businessExceptionContext.getException(CancelamentoRejeicaoLoteNaoExisteMaisException.class).stream()
						.findFirst().orElseGet(() -> new BusinessException(ErrorInfo.ERRO_GENERICO_NEGOCIO))
						.getMessage());

	}

	@Order(2)
	@DisplayName("Validação 1 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " pelo menos um número de lote não existir mais.\r\n" + " (Existem todos números de lotes)")
	@Test
	public void validacao1ExistemTodosNumerosDeLotes() {
		Mockito.when(loteLicitacaoDAOMock.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("3", "2", "4", "1")));

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4")));

		assertDoesNotThrow(() -> cancelarRejeicaoRules.validarExistenciaTodosNumerosDeLotes(getPropostaAtual(),
				getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente()));
	}

	@Order(3)
	@DisplayName("Validação 2 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " existir pelo menos um lote já associado a uma outra licitação.\r\n"
			+ " (Existe lote associado a outra licitação)")
	@Test
	public void validacao2ExisteLoteAssociadoOutraLicitacao() {
		Mockito.when(loteLicitacaoDAOMock.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("2", "3", "4", "1")));

		List<LoteBD> lotesAtivosProposta = getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4"));

		// Lotes 1 e 3 associados a Licitação A
		Long idLicitacaoAssociada1 = 1L;
		lotesAtivosProposta.get(0).setIdentificadorDaLicitacao(idLicitacaoAssociada1);
		lotesAtivosProposta.get(2).setIdentificadorDaLicitacao(idLicitacaoAssociada1);
		LicitacaoBD licitacao1 = new LicitacaoBD();
		licitacao1.setNumeroAno("Licitação A");
		Mockito.when(licitacaoDAOMock.findLicitacaoById(idLicitacaoAssociada1)).thenReturn(licitacao1);

		// Lote 2 associado a Licitação B
		Long idLicitacaoAssociada2 = 2L;
		lotesAtivosProposta.get(1).setIdentificadorDaLicitacao(idLicitacaoAssociada2);
		LicitacaoBD licitacao2 = new LicitacaoBD();
		licitacao2.setNumeroAno("Licitação B");
		Mockito.when(licitacaoDAOMock.findLicitacaoById(idLicitacaoAssociada2)).thenReturn(licitacao2);

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue())).thenReturn(lotesAtivosProposta);

		cancelarRejeicaoRules.validarNaoAssociacaoDeLoteComOutraLicitacao(getPropostaAtual(),
				getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente());

		assertThrows(BusinessException.class, () -> businessExceptionContext.throwException());

		assertEquals(getMenssagemErroCancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException(),
				businessExceptionContext.getException(CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException.class)
						.stream().findFirst().orElseGet(() -> new BusinessException(ErrorInfo.ERRO_GENERICO_NEGOCIO))
						.getDetailMessage());
	}
	

	private String getMenssagemErroCancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException() {
		StringBuilder mensagem = new StringBuilder();

		mensagem.append("Licitação Licitação A associada ao(s) lote(s) 1 e 3").append(System.lineSeparator().repeat(2));
		mensagem.append("Licitação Licitação B associada ao(s) lote(s) 2");

		return mensagem.toString();
	}

	@Order(4)
	@DisplayName("Validação 2 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " existir pelo menos um lote já associado a uma outra licitação.\r\n"
			+ " (Não existe lote associado a outra licitação)")
	@Test
	public void validacao2NaoExisteLoteAssociadoOutraLicitacao() {
		Mockito.when(loteLicitacaoDAOMock.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4")));

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue()))
				.thenReturn(getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4")));

		assertDoesNotThrow(() -> cancelarRejeicaoRules.validarNaoAssociacaoDeLoteComOutraLicitacao(getPropostaAtual(),
				getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente()));
	}

	@Order(5)
	@DisplayName("Validação 3 - \r\n" + 
			"	Impedir o cancelamento da rejeição se\r\n" + 
			" existir pelo menos um lote cujo conjunto de submetas não é o mesmo do momento da rejeição.\r\n" +
			" (Existe lote com conjunto de submetas diferente do momento da rejeição)")
	@Test
	public void validacao3ExisteLoteComConjuntoDeSubmetasDiferente() {
		List<LoteBD> lotesLicitacaoAtual = getLoteBDFromNrLote(Arrays.asList("3", "1", "2"));
		lotesLicitacaoAtual.get(0).setSubmetas(Arrays.asList(this.createSubmetaBD(2l, 1l, 2l, "Submeta 1.2"),
				this.createSubmetaBD(1l, 1l, 1l, "Submeta 1.1")));
		lotesLicitacaoAtual.get(1).setSubmetas(Arrays.asList(this.createSubmetaBD(4l, 1l, 4l, "Submeta 1.4"), 
				this.createSubmetaBD(3l, 1l, 3l, "Submeta 1.3")));
		lotesLicitacaoAtual.get(2).setSubmetas(Arrays.asList(this.createSubmetaBD(6l, 1l, 6l, "Submeta 1.6"), 
				this.createSubmetaBD(5l, 1l, 5l, "Submeta 1.5")));
		
		Mockito.when(loteLicitacaoDAOMock
				.findLotesComAssociacoesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(lotesLicitacaoAtual);
		
		List<LoteBD> lotesAtivosProposta = getLoteBDFromNrLote(Arrays.asList("1", "2", "3"));
		lotesAtivosProposta.get(0).setSubmetas(Arrays.asList(this.createSubmetaBD(1l, 1l, 1l, "Submeta 1.1"),
				this.createSubmetaBD(3l, 1l, 3l, "Submeta 1.3")));
		lotesAtivosProposta.get(1).setSubmetas(Arrays.asList(this.createSubmetaBD(2l, 1l, 2l, "Submeta 1.2"), 
				this.createSubmetaBD(4l, 1l, 4l, "Submeta 1.4")));
		lotesAtivosProposta.get(2).setSubmetas(Arrays.asList(this.createSubmetaBD(5l, 1l, 5l, "Submeta 1.5"), 
				this.createSubmetaBD(6l, 1l, 6l, "Submeta 1.6")));
		
		Mockito.when(loteLicitacaoDAOMock
				.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(), getPropostaAtual().getVersaoNr().longValue()))
				.thenReturn(lotesAtivosProposta);
		
		cancelarRejeicaoRules
		.validarAssociacaoDeLoteComMesmasSubmetas(getPropostaAtual(), getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente());
		
		assertThrows(BusinessException.class, () -> businessExceptionContext.throwException());
		
		String mensagemEsperada = MessageFormat
				.format(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_NAO_MAIS_ASSOCIADO_MESMAS_SUBMETAS.getMensagem(), "1, 2 e 3");

		assertEquals(mensagemEsperada,
				businessExceptionContext.getException(CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException.class)
						.stream()
						.findFirst().orElseGet(() -> new BusinessException(ErrorInfo.ERRO_GENERICO_NEGOCIO)).getMessage());
		
		assertEquals(getMenssagemErroCancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetas(),
				businessExceptionContext.getException(CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException.class)
						.stream()
						.findFirst().orElseGet(() -> new BusinessException(ErrorInfo.ERRO_GENERICO_NEGOCIO)).getDetailMessage());
		
	}


	@Order(6)
	@DisplayName("Validação 3 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " existir pelo menos um lote cujo conjunto de submetas não é o mesmo do momento da rejeição.\r\n"
			+ " (Não existe lote com conjunto de submetas diferente do momento da rejeição)")
	@Test
	public void validacao3NaoExisteLoteComConjuntoDeSubmetasDiferente() {
		List<LoteBD> lotesLicitacaoAtual = getLoteBDFromNrLote(Arrays.asList("1", "2", "3"));
		lotesLicitacaoAtual.get(0).setSubmetas(Arrays.asList(this.createSubmetaBD(1l, 1l, 1l, "Submeta 1.1"),
				this.createSubmetaBD(2l, 1l, 2l, "Submeta 1.2")));
		lotesLicitacaoAtual.get(1).setSubmetas(Arrays.asList(this.createSubmetaBD(3l, 1l, 3l, "Submeta 1.3"),
				this.createSubmetaBD(4l, 1l, 4l, "Submeta 1.4")));
		lotesLicitacaoAtual.get(2).setSubmetas(Arrays.asList(this.createSubmetaBD(5l, 1l, 5l, "Submeta 1.5"),
				this.createSubmetaBD(6l, 1l, 6l, "Submeta 1.6")));

		Mockito.when(loteLicitacaoDAOMock.findLotesComAssociacoesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(lotesLicitacaoAtual);

		List<LoteBD> lotesAtivosProposta = getLoteBDFromNrLote(Arrays.asList("1", "2", "3"));
		lotesAtivosProposta.get(0).setSubmetas(Arrays.asList(this.createSubmetaBD(1l, 1l, 1l, "Submeta 1.1"),
				this.createSubmetaBD(2l, 1l, 2l, "Submeta 1.2")));
		lotesAtivosProposta.get(1).setSubmetas(Arrays.asList(this.createSubmetaBD(3l, 1l, 3l, "Submeta 1.3"),
				this.createSubmetaBD(4l, 1l, 4l, "Submeta 1.4")));
		lotesAtivosProposta.get(2).setSubmetas(Arrays.asList(this.createSubmetaBD(5l, 1l, 5l, "Submeta 1.5"),
				this.createSubmetaBD(6l, 1l, 6l, "Submeta 1.6")));

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue())).thenReturn(lotesAtivosProposta);

		assertDoesNotThrow(() -> cancelarRejeicaoRules.validarAssociacaoDeLoteComMesmasSubmetas(getPropostaAtual(),
				getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente()));
	}

	private String getMenssagemErroCancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetas() {
		StringBuilder mensagem = new StringBuilder();

		mensagem.append("Submeta(s) associada(s) ao lote 1 no momento da rejeição:").append(System.lineSeparator());
		mensagem.append("1.3 - Submeta 1.3").append(System.lineSeparator());
		mensagem.append("1.4 - Submeta 1.4").append(System.lineSeparator());
		mensagem.append("Submeta(s) associada(s) ao lote 1 atualmente:").append(System.lineSeparator());
		mensagem.append("1.1 - Submeta 1.1").append(System.lineSeparator());
		mensagem.append("1.3 - Submeta 1.3").append(System.lineSeparator().repeat(2));
		mensagem.append("Submeta(s) associada(s) ao lote 2 no momento da rejeição:").append(System.lineSeparator());
		mensagem.append("1.5 - Submeta 1.5").append(System.lineSeparator());
		mensagem.append("1.6 - Submeta 1.6").append(System.lineSeparator());
		mensagem.append("Submeta(s) associada(s) ao lote 2 atualmente:").append(System.lineSeparator());
		mensagem.append("1.2 - Submeta 1.2").append(System.lineSeparator());
		mensagem.append("1.4 - Submeta 1.4").append(System.lineSeparator().repeat(2));
		mensagem.append("Submeta(s) associada(s) ao lote 3 no momento da rejeição:").append(System.lineSeparator());
		mensagem.append("1.1 - Submeta 1.1").append(System.lineSeparator());
		mensagem.append("1.2 - Submeta 1.2").append(System.lineSeparator());
		mensagem.append("Submeta(s) associada(s) ao lote 3 atualmente:").append(System.lineSeparator());
		mensagem.append("1.5 - Submeta 1.5").append(System.lineSeparator());
		mensagem.append("1.6 - Submeta 1.6");

		return mensagem.toString();
	}

	@Order(7)
	@DisplayName("Validação 4 - \r\n" + 
			"	Impedir o cancelamento da rejeição se\r\n" + 
			" o lote estiver disponível, mas já foi associado a uma outra licitação que também foi rejeitada " +
			"- em um momento posterior à licitação que se deseja cancelar a rejeição.\r\n" +
			" (Existe lote disponível já associado a outra licitação rejeitada posteriormente)")
	@Test
	public void validacao4ExisteLoteDisponivelAssociadoOutraLicitacaoRejeitadaPosteriormente() {
		List<LoteBD> lotesLicitacaoAtual = getLoteBDFromNrLote(Arrays.asList("3", "1", "2", "5", "4", "6")); 
		Mockito.when(loteLicitacaoDAOMock
				.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(lotesLicitacaoAtual);
		
		List<LoteBD> lotesAtivosProposta = getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4", "5", "6"));
		// Lote 2 não disponível
		lotesAtivosProposta.get(1).setIdentificadorDaLicitacao(2l);
		
		Mockito.when(loteLicitacaoDAOMock
				.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(), getPropostaAtual().getVersaoNr().longValue()))
				.thenReturn(lotesAtivosProposta);
		
		// Licitação B com Lotes 1 e 4
		LicitacaoBD licitacaoB = new LicitacaoBD();
		licitacaoB.setId(3l);
		licitacaoB.setNumeroAno("Licitação B");
		LoteBD lote1B = new LoteBD();
		lote1B.setNumeroDoLote(4l);
		LoteBD lote2B = new LoteBD();
		lote2B.setNumeroDoLote(1l);
		licitacaoB.setLotesAssociados(Arrays.asList(lote1B, lote2B));
		
		// Licitação A com Lotes 3 e 5
		LicitacaoBD licitacaoA = new LicitacaoBD();
		licitacaoA.setId(4l);
		licitacaoA.setNumeroAno("Licitação A");
		LoteBD lote1A = new LoteBD();
		lote1A.setNumeroDoLote(5l);
		LoteBD lote2A = new LoteBD();
		lote2A.setNumeroDoLote(3l);
		licitacaoA.setLotesAssociados(Arrays.asList(lote1A, lote2A));
		
		// Licitação C com Lote 6
		LicitacaoBD licitacaoC = new LicitacaoBD();
		licitacaoC.setId(5l);
		licitacaoC.setNumeroAno("Licitação C");
		LoteBD lote1C = new LoteBD();
		lote1C.setNumeroDoLote(6l);
		licitacaoC.setLotesAssociados(Arrays.asList(lote1C));

		// Licitações rejeitadas com os mesmos lotes: Licitação A, Licitação B,  Licitação C
		Mockito.when(licitacaoDAOMock
				.recuperarOutrasLicitacoesRejeitadasComMesmosNumerosLotes(getLicitacaoEstadoAtual(), 
						Arrays.asList(1l, 3l, 4l, 5l, 6l)))
				.thenReturn(Arrays.asList(licitacaoB, licitacaoA, licitacaoC));

		// Datas de rejeição da Licitação A
		HistoricoLicitacaoBD historicoLicitacao1A = new HistoricoLicitacaoBD();
		historicoLicitacao1A.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 12, 8)));
		historicoLicitacao1A.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao1A.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		HistoricoLicitacaoBD historicoLicitacao2A = new HistoricoLicitacaoBD();
		historicoLicitacao2A.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 9, 19)));
		historicoLicitacao2A.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao2A.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		
		Mockito.when(historicoLicitacaoDAOMock
				.findHistoricoLicitacaoByIdLicitacao(licitacaoA.getId()))
				.thenReturn(Arrays.asList(historicoLicitacao1A, historicoLicitacao2A));

		// Datas de rejeição da Licitação B
		HistoricoLicitacaoBD historicoLicitacao1B = new HistoricoLicitacaoBD();
		historicoLicitacao1B.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 12, 8)));
		historicoLicitacao1B.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao1B.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		HistoricoLicitacaoBD historicoLicitacao2B = new HistoricoLicitacaoBD();
		historicoLicitacao2B.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 10, 5)));
		historicoLicitacao2B.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao2B.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		
		Mockito.when(historicoLicitacaoDAOMock
				.findHistoricoLicitacaoByIdLicitacao(licitacaoB.getId()))
				.thenReturn(Arrays.asList(historicoLicitacao1B, historicoLicitacao2B));

		// Datas de rejeição da Licitação C
		HistoricoLicitacaoBD historicoLicitacao1C = new HistoricoLicitacaoBD();
		historicoLicitacao1C.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 6, 1)));
		historicoLicitacao1C.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao1C.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		HistoricoLicitacaoBD historicoLicitacao2C = new HistoricoLicitacaoBD();
		historicoLicitacao2C.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 6, 3)));
		historicoLicitacao2C.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao2C.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		
		Mockito.when(historicoLicitacaoDAOMock
				.findHistoricoLicitacaoByIdLicitacao(licitacaoC.getId()))
				.thenReturn(Arrays.asList(historicoLicitacao1C, historicoLicitacao2C));

		// Datas de rejeição da Licitação atual
		HistoricoLicitacaoBD historicoLicitacaoEstadoAtual = new HistoricoLicitacaoBD();
		historicoLicitacaoEstadoAtual.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 8, 1)));
		historicoLicitacaoEstadoAtual.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacaoEstadoAtual.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		
		Mockito.when(historicoLicitacaoDAOMock
				.findHistoricoLicitacaoByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(Arrays.asList(historicoLicitacaoEstadoAtual));

		cancelarRejeicaoRules
		.validarUltimaLicitacaoRejeitadaAssociadaALote(getPropostaAtual(), getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente());
		
		assertThrows(BusinessException.class,() -> businessExceptionContext.throwException());
		
		assertEquals(getMenssagemErroCancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException(),
				businessExceptionContext.getException(CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException.class)
						.stream()
						.findFirst().orElseGet(() -> new BusinessException(ErrorInfo.ERRO_GENERICO_NEGOCIO)).getDetailMessage());
	}

	@Order(8)
	@DisplayName("Validação 4 - \r\n" + "	Impedir o cancelamento da rejeição se\r\n"
			+ " o lote estiver disponível, mas já foi associado a uma outra licitação que também foi rejeitada "
			+ "- em um momento posterior à licitação que se deseja cancelar a rejeição.\r\n"
			+ " (Não existe lote disponível já associado a outra licitação rejeitada posteriormente)")
	@Test
	public void validacao4NaoExisteLoteDisponivelAssociadoOutraLicitacaoRejeitadaPosteriormente() {
		List<LoteBD> lotesLicitacaoAtual = getLoteBDFromNrLote(Arrays.asList("3", "1", "2", "5", "4", "6"));
		Mockito.when(loteLicitacaoDAOMock.findLotesByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(lotesLicitacaoAtual);

		List<LoteBD> lotesAtivosProposta = getLoteBDFromNrLote(Arrays.asList("1", "2", "3", "4", "5", "6"));
		// Lote 2 não disponível
		lotesAtivosProposta.get(1).setIdentificadorDaLicitacao(2l);

		Mockito.when(loteLicitacaoDAOMock.findLotesAtivosByIdPropostaSiconv(getUsuarioLogadoConcedente(),
				getPropostaAtual().getVersaoNr().longValue())).thenReturn(lotesAtivosProposta);

		// Licitação B com Lotes 1 e 4
		LicitacaoBD licitacaoB = new LicitacaoBD();
		licitacaoB.setId(3l);
		licitacaoB.setNumeroAno("Licitação B");
		LoteBD lote1B = new LoteBD();
		lote1B.setNumeroDoLote(4l);
		LoteBD lote2B = new LoteBD();
		lote2B.setNumeroDoLote(1l);
		licitacaoB.setLotesAssociados(Arrays.asList(lote1B, lote2B));

		// Licitações rejeitadas com os mesmos lotes: Licitação A, Licitação B,
		// Licitação C
		Mockito.when(licitacaoDAOMock.recuperarOutrasLicitacoesRejeitadasComMesmosNumerosLotes(
				getLicitacaoEstadoAtual(), Arrays.asList(1l, 3l, 4l, 5l, 6l))).thenReturn(Arrays.asList(licitacaoB));

		// Datas de rejeição da Licitação B
		HistoricoLicitacaoBD historicoLicitacao1B = new HistoricoLicitacaoBD();
		historicoLicitacao1B.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 6, 1)));
		historicoLicitacao1B.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao1B.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());
		HistoricoLicitacaoBD historicoLicitacao2B = new HistoricoLicitacaoBD();
		historicoLicitacao2B.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 3, 23)));
		historicoLicitacao2B.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacao2B.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());

		Mockito.when(historicoLicitacaoDAOMock.findHistoricoLicitacaoByIdLicitacao(licitacaoB.getId()))
				.thenReturn(Arrays.asList(historicoLicitacao1B, historicoLicitacao2B));

		// Datas de rejeição da Licitação atual
		HistoricoLicitacaoBD historicoLicitacaoEstadoAtual = new HistoricoLicitacaoBD();
		historicoLicitacaoEstadoAtual.setDataDeRegistro(Date.valueOf(LocalDate.of(2020, 8, 1)));
		historicoLicitacaoEstadoAtual.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		historicoLicitacaoEstadoAtual.setEventoGerador(EventoQuadroResumoEnum.REJEITAR.getSigla());

		Mockito.when(historicoLicitacaoDAOMock.findHistoricoLicitacaoByIdLicitacao(getLicitacaoEstadoAtual().getId()))
				.thenReturn(Arrays.asList(historicoLicitacaoEstadoAtual));

		assertDoesNotThrow(() -> cancelarRejeicaoRules.validarUltimaLicitacaoRejeitadaAssociadaALote(getPropostaAtual(),
				getLicitacaoEstadoAtual(), getUsuarioLogadoConcedente()));
	}

	private String getMenssagemErroCancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException() {
		StringBuilder mensagem = new StringBuilder();

		mensagem.append("Licitação Licitação A rejeitada, vinculada ao(s) lote(s) 3 e 5")
				.append(System.lineSeparator().repeat(2));
		mensagem.append("Licitação Licitação B rejeitada, vinculada ao(s) lote(s) 1 e 4");

		return mensagem.toString();
	}

	private LicitacaoBD getLicitacaoEstadoAtual() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(1L);

		return licitacao;
	}

	private PropostaBD getPropostaAtual() {
		PropostaBD proposta = new PropostaBD();
		proposta.setId(1L);
		proposta.setVersaoNr(1);

		return proposta;
	}

	private List<LoteBD> getLoteBDFromNrLote(List<String> listaNrLote) {
		List<LoteBD> listaLoteBD = new ArrayList<LoteBD>();
		listaNrLote.forEach(nrLote -> {
			LoteBD lote = new LoteBD();
			lote.setNumeroDoLote(Long.valueOf(nrLote));
			listaLoteBD.add(lote);
		});

		return listaLoteBD;
	}

	private SubmetaBD createSubmetaBD(Long idSubmetaAnalise, Long numeroMeta, Long numeroSubmeta, String descricao) {
		SubmetaBD submeta = new SubmetaBD();
		submeta.setIdSubmetaAnalise(idSubmetaAnalise);
		submeta.setNumero(numeroSubmeta);
		submeta.setDescricao(descricao);

		MetaBD meta = new MetaBD();
		meta.setNumero(numeroMeta);
		submeta.setMetaBD(meta);

		return submeta;
	}

	private SiconvPrincipal getUsuarioLogadoConcedente() {
		if (siconvPrincipal == null) {
			siconvPrincipal = new MockSiconvPrincipal(Profile.CONCEDENTE, 1L);
		}

		return siconvPrincipal;
	}
}
