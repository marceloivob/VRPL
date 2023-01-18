package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail.SenderEmail;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.TipoFonteEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.ValidarSituacaoConcluidaDaLicitacao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CffComEventoFaltandoAssociacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.DataBaseAnaliseInvalidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.DataBaseLicitacaoInvalidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.EventoSemServicoAssociadoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExisteLoteComPosDeOrcamentosDiferentesException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExistePoSemEventosNaLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExistePoSemFrenteDeObraNaLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExisteSubmetaComContrapartidaNegativaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.FrenteDeObraSemQuantidadeException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ItensDePoDeUmLoteComPrecosDiferenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.MacroServicoSemPercentualParcelaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.PlanilhaOrcamentariaNaoCadastrada;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.PosDeUmLoteComDataBaseDiferenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemEventoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemFrenteObraException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemQuantidadesInformadasException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.SomatorioLicitacaoExcedeValorAprovadoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.CodigoEFonteDeServico;
import br.gov.planejamento.siconv.mandatarias.test.core.MockDaoFactory;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;

@TestMethodOrder(OrderAnnotation.class)
@DisplayName("Cenários de teste para Enviar Para Análise")
public class EnviarParaAnaliseEAceiteRulesTest {

	@InjectMocks
	private EnviarParaAnaliseEAceiteRules regrasParaEnviarParaAnaliseEAceite;

	@Mock
	private PoBC poBC;

	@Mock
	private ValidarSituacaoConcluidaDaLicitacao validarSituacaoConcluidaDaLicitacao;

	@Mock
	private SiconvBC siconvBC;

	@Mock
	private MockDaoFactory<?> daoFactoryMock;

	@Mock
	private PropostaDAO propostaDAOMock;

	@Mock
	private EventoDAO eventoDAOMock;

	@Mock
	private ServicoDAO servicoDAOMock;

	@Mock
	private FrenteObraDAO frenteDeObraDAOMock;

	@Mock
	private MacroServicoDAO macroServicoDAOMock;

	@Mock
	private MacroServicoParcelaDAO macroServicoParcelaDAOMock;

	@Mock
	private ServicoFrenteObraDAO servicoFrenteObraDAOMock;

	@Mock
	private PoDAO poDAOMock;

	@Mock
	private LicitacaoDAO licitacaoDAOMock;

	@Mock
	private LoteLicitacaoDAO loteLicitacaoDAOMock;

	@Mock
	private ClientLicitacoesInterface clientLicitacoesMock;

	@Mock
	private EventoFrenteObraDAO eventoFrenteObraMock;

	@Mock
	private SenderEmail senderEmail;
	
	@Mock
	private BusinessExceptionContext businessExceptionContext;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		regrasParaEnviarParaAnaliseEAceite.setDao(daoFactoryMock);
		regrasParaEnviarParaAnaliseEAceite.setPoBC(poBC);
		regrasParaEnviarParaAnaliseEAceite.setValidarLicitacaoRules(validarSituacaoConcluidaDaLicitacao);

		when(regrasParaEnviarParaAnaliseEAceite.getServicoFrenteObraDAO()).thenReturn(servicoFrenteObraDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getMacroServicoDAO()).thenReturn(macroServicoDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getMacroServicoParcelaDAO())
				.thenReturn(macroServicoParcelaDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getServicoDAO()).thenReturn(servicoDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getFrenteDeObraDAO()).thenReturn(frenteDeObraDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getEventoDAO()).thenReturn(eventoDAOMock);
		when(regrasParaEnviarParaAnaliseEAceite.getLicitacaoDAO()).thenReturn(licitacaoDAOMock);
	}

	@Order(1)
	@DisplayName("Regra de Validação Número ? - Enviar para Análise Licitação Concluída sem Planilha Orçamentária")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaSemPlanilhaOrcamentaria() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();
		
		definirMockAssertThrow(PlanilhaOrcamentariaNaoCadastrada.class);
		
		definirPlanilhasOrcamentariasVazia(licitacao);

		assertThrows(PlanilhaOrcamentariaNaoCadastrada.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(2)
	@DisplayName("Regra de Validação Número 2 - Existe pelo menos uma PO na licitação, sem evento cadastrado")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaSemEventoAssociadoAPlanilhaOrcamentaria() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ExistePoSemEventosNaLicitacaoException.class);

		definirPlanilhaOrcamentariaSemEvento(licitacao);

		assertThrows(ExistePoSemEventosNaLicitacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(3)
	@DisplayName("Regra de Validação Número 3 - Existe pelo menos uma PO na licitação, sem frente de obra cadastrada")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComPlanilhaOrcamentariaSemFrenteDeObra() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ExistePoSemFrenteDeObraNaLicitacaoException.class);

		definirPlanilhaOrcamentariaComEvento(licitacao);

		definirPlanilhaOrcamentariaSemFrenteDeObra(licitacao);

		assertThrows(ExistePoSemFrenteDeObraNaLicitacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(4)
	@DisplayName("Regra de Validação Número 4 - Existe serviço sem Evento associado")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComPlanilhaOrcamentariaComEventoSemServicoAssociado() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ServicoSemEventoException.class);
		
		definirPlanilhaOrcamentariaComEventoComPrecoLicitadoIgualAoAprovado(licitacao);

		definirPlanilhaOrcamentariaComFrenteDeObra(licitacao);

		// Caso de Teste 1 do Cenário
		definirPlanilhaOrcamentariaComServicosSemEventos(licitacao);

		definirPOcomEventoFrenteObra(licitacao);

		assertThrows(ServicoSemEventoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	private void definirPOcomEventoFrenteObra(LicitacaoBD licitacao) {

		when(regrasParaEnviarParaAnaliseEAceite.getEventoFrenteObraDAO()).thenReturn(eventoFrenteObraMock);
		List<EventoFrenteObraBD> listaEventoFrenteObra = new ArrayList<>();
		when(eventoFrenteObraMock.recuperarEventoFrenteObraPorIdFrenteObra(Mockito.any()))
				.thenReturn(listaEventoFrenteObra);

	}

	@Order(5)
	@DisplayName("Regra de Validação Número 5 - Existe evento sem serviço associado")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaMasSemServiçoAssociadoAoEvento() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ExistePoSemEventosNaLicitacaoException.class);
		
		definirPlanilhaOrcamentariaComEvento(licitacao);

		assertThrows(EventoSemServicoAssociadoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}


	@Order(6)
	@DisplayName("Regra de Validação Número 6 - Existe serviço sem Frente de Obra associada")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaMasSemFrenteDeObraAssociadoAServico() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ServicoSemFrenteObraException.class);

		definirPlanilhaOrcamentariaComEventoComServicoSemFrenteDeObra(licitacao);

		assertThrows(ServicoSemFrenteObraException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(7)
	@DisplayName("Regra de Validação Número 7 - O total (somatório dos quantitativos) dos serviços informados nas  frentes de obra difere do total (somatório dos quantitativos) aceito na análise.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformada() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise.class);

		definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObra2(licitacao);

		assertThrows(QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(8)
	@DisplayName("Regra de Validação Número 8 - Existe Frente de Obra sem quantidade de serviço informada.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoSemQuantidadeInformada() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(FrenteDeObraSemQuantidadeException.class);
		
		definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObra(licitacao);

		assertThrows(FrenteDeObraSemQuantidadeException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(9)
	@DisplayName("Regra de Validação Número 9 - Existe serviço sem quantidades informadas por frente de obra")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaSemQuantidadePorFrenteDeObra() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ServicoSemQuantidadesInformadasException.class);
		
		definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObraSemQuantidadeInformada(licitacao);

		assertThrows(ServicoSemQuantidadesInformadasException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));
	}

	@Order(10)
	@DisplayName("Regra de Validação Número 10 - Na tabela 'Visão Frentes de Obra por Evento' devem estar associados todos os eventos cadastrados.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraSemTodosOsEventosCadastrados() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(CffComEventoFaltandoAssociacaoException.class);
		
		definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObraComQuantidadeInformada(licitacao);

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		when(eventoDAOMock.recuperarEventosPorPo(Mockito.any())).thenReturn(null);

		assertThrows(CffComEventoFaltandoAssociacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

		definirServicoPorMacroServicoComEventoNulo();

		assertThrows(CffComEventoFaltandoAssociacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

		definirServicoPorMacroServicoComEventoNaoNulo();

		assertThrows(CffComEventoFaltandoAssociacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

		definirServicoPorMacroServicoComEventoComServicoFrenteObra();

		assertThrows(CffComEventoFaltandoAssociacaoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(11)
	@DisplayName("Regra de Validação Número 11 - No CFF (sem evento), na tabela 'Visão Parcelas por Macrosserviço' deve existir uma percentual de parcela cadastrado para cada macrosserviço cadastrado para o respectivo PO.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraComTodosOsEventosCadastrados() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(MacroServicoSemPercentualParcelaException.class);

		definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformada(licitacao);

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirServicoPorMacroServicoComEvento();

		assertThrows(MacroServicoSemPercentualParcelaException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(12)
	@DisplayName("Regra de Validação Número 12 - O valor de repasse da proposta foi excedido pelo somatório dos totais de repasses de todas as metas do QCI.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraComTodosOsEventosCadastradosComValorDeRepasseExcedeProposta() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		
		definirMockAssertThrow(SomatorioLicitacaoExcedeValorAprovadoException.class);
		
		definirProcessoDeExecucaoConcluido();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(1));

		definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComValorDeRepasseExcedido(
				licitacao);

		assertThrows(SomatorioLicitacaoExcedeValorAprovadoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(13)
	@DisplayName("Regra de Validação Número 13 - O valor global da proposta foi excedido pelo somatório de todas as metas do QCI.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraComTodosOsEventosCadastradosComValorGlobalExcedeProposta() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();

		definirMockAssertThrow(SomatorioLicitacaoExcedeValorAprovadoException.class);
		
		definirProcessoDeExecucaoConcluido();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(1));

		definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComValorGlobalExcedido(
				licitacao);

		assertThrows(SomatorioLicitacaoExcedeValorAprovadoException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(14)
	@DisplayName("Regra de Validação Número 14 - Planilhas orçamentárias cujas submetas estejam associadas a um mesmo lote não podem ter valores diferentes para a data base.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraComTodosOsEventosCadastradosComDatabaseInconsistenteNumLote() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		
		definirMockAssertThrow(PosDeUmLoteComDataBaseDiferenteException.class);
		
		definirProcessoDeExecucaoConcluido();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(1));

		definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComDataBaseDiferentes(
				licitacao);

		assertThrows(PosDeUmLoteComDataBaseDiferenteException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(15)
	@DisplayName("Regra de Validação Número 15 - Para itens de PO de um mesmo lote (mesmo que de PO diferentes no lote), com mesmos valores de Fonte e Código, os preços unitários não podem ser diferentes.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComEventoNAPlanilhaOrcamentariaComFrenteDeObraAssociadoAServicoComQuantidadeInformadaComQuantidadePorFrenteDeObraComTodosOsEventosCadastradosComItensPoComPrecosDiferentesNumLote() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ItensDePoDeUmLoteComPrecosDiferenteException.class);
		
		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(1));

		definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComPrecosDiferentes(
				licitacao);

		assertThrows(ItensDePoDeUmLoteComPrecosDiferenteException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(16)
	@DisplayName("Regra de Validação Número 16 - Para orçamento de referencia utilizado para comparação selecionado é o \"Aceito na Análise\".\n"
			+ "\n" + "E a data base da licitação é diferente da data base da fase de análise.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComDataAnaliseDiferenteDataBaseVrplEOrcamentoAceitoNaAnalise() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(DataBaseAnaliseInvalidaException.class);
		
		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(-1));

		definirPlanilhaOrcamentariaComDataBaseAnaliseDiferenteDataBaseVrplEOrcamentoAceitoNaAnalise(licitacao);

		assertThrows(DataBaseAnaliseInvalidaException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(17)
	@DisplayName("Regra de Validação Número 17 - Para orçamento de referencia utilizado para comparação selecionado é o \"Atualizado na Data Base da Licitação\"\n"
			+ "\n" + "E a data base da licitação é mesma da data base da fase de análise.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComDataAnaliseIgualDataBaseVrplEOrcamentoDataBaseLicitacao() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(DataBaseLicitacaoInvalidaException.class);
		
		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(-1));

		definirPlanilhaOrcamentariaComDataBaseAnaliseIgualDataBaseVrplEOrcamentoDataBaseLicitacao(licitacao);

		assertThrows(DataBaseLicitacaoInvalidaException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(18)
	@DisplayName("Regra de Validação Número 18 - Existe Submeta com contrapartida negativa")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComContrapartidaNegativa() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ExisteSubmetaComContrapartidaNegativaException.class);
		
		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(-1));

		definirPlanilhaOrcamentariaComContrapartidaNegativa(licitacao);

		assertThrows(ExisteSubmetaComContrapartidaNegativaException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	@Order(19)
	@DisplayName("Regra de Validação Número 19 - Orçamento de Referência - Não pode haver orçamentos de referencia diferentes em uma mesma licitação.")
	@Test
	public void enviaParaAnaliseLicitacaoConcluidaComPosDeOrcamentoDiferentes() {
		// Preparação dos Dados do Cenário a ser testado
		logarComoProponente();
		definirPropostaEmVersaoAtual();
		LicitacaoBD licitacao = definirSituacaoDaLicitacaoComoEmPreenchimento();
		definirProcessoDeExecucaoConcluido();

		definirMockAssertThrow(ExisteLoteComPosDeOrcamentosDiferentesException.class);

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirServicoPorMacroServicoSemEventoFK();

		definirMacroServicoPorPlanilhaOrcamentaria();

		definirParcelaMacroServicoPorPlanilhaOrcamentaria();

		definirLotesPorLicitacao();

		definirValorContrapartidaSubmetaPorPo(new BigDecimal(1));

		definirPlanilhaOrcamentariaComOrcamentosDiferentes(licitacao);

		assertThrows(ExisteLoteComPosDeOrcamentosDiferentesException.class,
				() -> regrasParaEnviarParaAnaliseEAceite.realizarValidacoesParaAceite(licitacao));

	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Dados usados nos cenários
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void definirServicoPorMacroServicoSemEventoFK() {
		List<ServicoBD> servicos = new ArrayList<>();
		ServicoBD servico = new ServicoBD();

		servicos.add(servico);

		when(servicoDAOMock.recuperarServicoPorMacroServico(Mockito.any())).thenReturn(servicos);

	}

	private void definirServicoPorMacroServicoComEventoNulo() {
		List<ServicoBD> servicos = new ArrayList<>();
		ServicoBD servico = new ServicoBD();
		servico.setTxDescricao("nome do servico");
		servico.setEventoFk(4L);

		servicos.add(servico);

		when(servicoDAOMock.recuperarServicoPorMacroServico(Mockito.any())).thenReturn(servicos);

		EventoBD evento = null;

		when(eventoDAOMock.recuperarEventoPorId(servico.getEventoFk())).thenReturn(evento);

	}

	private void definirServicoPorMacroServicoComEventoNaoNulo() {
		List<ServicoBD> servicos = new ArrayList<>();
		ServicoBD servico = new ServicoBD();
		servico.setTxDescricao("nome do servico");
		servico.setEventoFk(4L);

		servicos.add(servico);

		when(servicoDAOMock.recuperarServicoPorMacroServico(Mockito.any())).thenReturn(servicos);

		EventoBD evento = new EventoBD();

		when(eventoDAOMock.recuperarEventoPorId(servico.getEventoFk())).thenReturn(evento);

		List<ServicoFrenteObraBD> listaServicoFrenteObra = null;

		when(servicoFrenteObraDAOMock.recuperarServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteObra);

	}

	private void definirServicoPorMacroServicoComEventoComServicoFrenteObra() {
		List<ServicoBD> servicos = new ArrayList<>();
		ServicoBD servico = new ServicoBD();
		servico.setTxDescricao("nome do servico");
		servico.setEventoFk(4L);

		servicos.add(servico);

		when(servicoDAOMock.recuperarServicoPorMacroServico(Mockito.any())).thenReturn(servicos);

		EventoBD evento = new EventoBD();

		when(eventoDAOMock.recuperarEventoPorId(servico.getEventoFk())).thenReturn(evento);

		List<ServicoFrenteObraBD> listaServicoFrenteObra = new ArrayList<>();

		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("-0.1"));

		listaServicoFrenteObra.add(servicoFrenteObra);

		when(servicoFrenteObraDAOMock.recuperarServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteObra);

	}

	private void definirServicoPorMacroServicoComEvento() {
		List<ServicoBD> servicos = new ArrayList<>();
		ServicoBD servico = new ServicoBD();
		servico.setTxDescricao("nome do servico");
		servico.setEventoFk(4L);

		servicos.add(servico);

		when(servicoDAOMock.recuperarServicoPorMacroServico(Mockito.any())).thenReturn(servicos);

		EventoBD evento = new EventoBD();

		when(eventoDAOMock.recuperarEventoPorId(servico.getEventoFk())).thenReturn(evento);

		List<ServicoFrenteObraBD> listaServicoFrenteObra = new ArrayList<>();

		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("0.1"));

		listaServicoFrenteObra.add(servicoFrenteObra);

		when(servicoFrenteObraDAOMock.recuperarServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteObra);

		List<MacroServicoParcelaBD> lista = new ArrayList<>();

		when(macroServicoParcelaDAOMock.recuperarParcelasDoMacroServicoPorIdsMacroServico(Mockito.any()))
				.thenReturn(lista);

	}

	private void definirMacroServicoPorPlanilhaOrcamentaria() {

		List<MacroServicoBD> macroServicos = new ArrayList<>();

		MacroServicoBD macroServicoBD = new MacroServicoBD();
		macroServicoBD.setTxDescricao("Descricao Macro Servico");

		macroServicos.add(macroServicoBD);

		when(macroServicoDAOMock.recuperarMacroServicosPorIdPo(Mockito.any())).thenReturn(macroServicos);

	}

	private void definirParcelaMacroServicoPorPlanilhaOrcamentaria() {

		List<MacroServicoParcelaBD> msParcelas = new ArrayList<>();

		MacroServicoParcelaBD macroServicoParcelaBD = new MacroServicoParcelaBD();
		macroServicoParcelaBD.setPcParcela(BigDecimal.TEN);

		msParcelas.add(macroServicoParcelaBD);

		when(macroServicoParcelaDAOMock.recuperarParcelasDoMacroServicoPorIdsMacroServico(Mockito.any()))
				.thenReturn(msParcelas);

	}

	private void definirPlanilhaOrcamentariaSemFrenteDeObra(LicitacaoBD licitacao) {

		List<ServicoBD> servicos = new ArrayList<>();
		servicos.add(new ServicoBD());

		when(servicoDAOMock.recuperarServicosPorEvento(Mockito.any())).thenReturn(servicos);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);
	}

	private void definirPlanilhaOrcamentariaComServicosSemEventos(LicitacaoBD licitacao) {
		List<ServicoBD> servicos = new ArrayList<>();
		servicos.add(new ServicoBD());

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();
		frentesDeObraPo.add(new FrenteObraBD());

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);
	}

//	private void definirPlanilhaOrcamentariaComServicosComEventoNaoEncontrado(LicitacaoBD licitacao) {
//		List<ServicoBD> servicos = new ArrayList<>();
//		ServicoBD servicoBD = new ServicoBD();
//		servicoBD.setEventoFk(1L);
//		servicos.add(servicoBD);
//
//		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos);
//
//		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();
//		frentesDeObraPo.add(new FrenteObraBD());
//
//		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);
//	}

	private void definirPlanilhaOrcamentariaComFrenteDeObra(LicitacaoBD licitacao) {

		List<ServicoBD> servicos = new ArrayList<>();
		servicos.add(new ServicoBD());

		when(servicoDAOMock.recuperarServicosPorEvento(Mockito.any())).thenReturn(servicos);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();
		frentesDeObraPo.add(new FrenteObraBD());

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);
	}

	private void definirPlanilhaOrcamentariaComEvento(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setIndicadorAcompanhamentoPorEvento(true);
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.50"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);
		evento1.setNomeEvento("Nome Evento1");

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);
		evento2.setNomeEvento("Nome Evento2");

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();

		List<ServicoBD> servicos2 = new ArrayList<>();
		servicos2.add(new ServicoBD());

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComServicoSemFrenteDeObra(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();
		frentesDeObraPo.add(new FrenteObraBD());

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObra2(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setTxDescricao("Descrição Serviço");

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1"));

		listaServicoFrenteDeObra.add(servicoFrenteObra);

		SubmetaBD submeta = new SubmetaBD();
		submeta.setDescricao("Descrição Submeta");

		when(poDAOMock.recuperarSubmetaPorPO(Mockito.any())).thenReturn(submeta);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObra(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setTxDescricao("Descrição Serviço");

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra1 = new ServicoFrenteObraBD();
		servicoFrenteObra1.setQtItens(new BigDecimal("1"));


		ServicoFrenteObraBD servicoFrenteObra2 = new ServicoFrenteObraBD();
		servicoFrenteObra2.setQtItens(new BigDecimal("0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra1);
		listaServicoFrenteDeObra.add(servicoFrenteObra2);

		SubmetaBD submeta = new SubmetaBD();
		submeta.setDescricao("Descrição Submeta");

		when(poDAOMock.recuperarSubmetaPorPO(Mockito.any())).thenReturn(submeta);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObraSemQuantidadeInformada(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();

		listaServicoFrenteDeObra.add(servicoFrenteObra);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComServicoComFrenteDeObraComQuantidadeInformada(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setIndicadorAcompanhamentoPorEvento(true);
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);
		evento1.setNomeEvento("Nome Evento1");

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);
		evento2.setNomeEvento("Nome Evento2");

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformada(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComEventoComPrecoLicitadoIgualAoAprovado(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setIndicadorAcompanhamentoPorEvento(true);
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();

		List<ServicoBD> servicos2 = new ArrayList<>();
		servicos2.add(new ServicoBD());

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaSemEvento(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setIndicadorAcompanhamentoPorEvento(true);

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);

		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhasOrcamentariasVazia(LicitacaoBD licitacao) {
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(Collections.emptyList());
	}

	private void definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComValorDeRepasseExcedido(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now().minusMonths(3));

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.ONE);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(regrasParaEnviarParaAnaliseEAceite.getLoteLicitacaoDAO()).thenReturn(loteLicitacaoDAOMock);

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComValorGlobalExcedido(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now().minusMonths(3));

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setVersao(1L);

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setVersao(1L);

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(BigDecimal.ONE);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("9.0"));

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComDataBaseDiferentes(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now().minusMonths(3));

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setQtTotalItensAnalise(new BigDecimal("10"));
		servico1.setVlPrecoUnitario(new BigDecimal("10.0"));

		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setQtTotalItensAnalise(new BigDecimal("10"));
		servico2.setVlPrecoUnitario(new BigDecimal("10.0"));

		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperarServicosPorPo(Mockito.any())).thenReturn(servicos1);
		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaSemEventoComServicoComFrenteDeObraComQuantidadeInformadaComPrecosDiferentes(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento1.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento1.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setId(1L);
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setReferencia("analise");

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento2.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setId(2L);
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setReferencia("analise");

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setCdServico("1");
		servico1.setInFonte(TipoFonteEnum.SINAPI.getSigla());
		servico1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setCdServico("1");
		servico2.setInFonte(TipoFonteEnum.SINAPI.getSigla());
		servico2.setVlPrecoUnitarioLicitado(BigDecimal.TEN);
		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		CodigoEFonteDeServico cf = new CodigoEFonteDeServico();
		cf.setCdServico("1");
		cf.setInFonte("1");

		listaCodigoEFonte.add(cf);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD1)))
				.thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD2)))
				.thenReturn(servicos2);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComDataBaseAnaliseDiferenteDataBaseVrplEOrcamentoAceitoNaAnalise(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento1.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEvento1.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setId(1L);
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEventoBD1.setReferencia("analise");

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento2.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEvento2.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setId(2L);
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEventoBD2.setReferencia("analise");

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setCdServico("1");
		servico1.setInFonte("1");
		servico1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setCdServico("1");
		servico2.setInFonte("1");
		servico2.setVlPrecoUnitarioLicitado(BigDecimal.TEN);
		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		CodigoEFonteDeServico cf = new CodigoEFonteDeServico();
		cf.setCdServico("1");
		cf.setInFonte("1");

		listaCodigoEFonte.add(cf);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD1)))
				.thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD2)))
				.thenReturn(servicos2);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComDataBaseAnaliseIgualDataBaseVrplEOrcamentoDataBaseLicitacao(
			LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento1.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento1.setReferencia("database");

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setId(1L);
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setReferencia("database");

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setReferencia("database");

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setId(2L);
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setReferencia("database");

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setCdServico("1");
		servico1.setInFonte("1");
		servico1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setCdServico("1");
		servico2.setInFonte("1");
		servico2.setVlPrecoUnitarioLicitado(BigDecimal.TEN);
		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		CodigoEFonteDeServico cf = new CodigoEFonteDeServico();
		cf.setCdServico("1");
		cf.setInFonte("1");

		listaCodigoEFonte.add(cf);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD1)))
				.thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD2)))
				.thenReturn(servicos2);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComContrapartidaNegativa(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento1.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento1.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setId(1L);
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setReferencia("analise");

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento2.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setId(2L);
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setReferencia("analise");

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setCdServico("1");
		servico1.setInFonte("1");
		servico1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setCdServico("1");
		servico2.setInFonte("1");
		servico2.setVlPrecoUnitarioLicitado(BigDecimal.TEN);
		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));


		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		CodigoEFonteDeServico cf = new CodigoEFonteDeServico();
		cf.setCdServico("1");
		cf.setInFonte("1");

		listaCodigoEFonte.add(cf);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD1)))
				.thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD2)))
				.thenReturn(servicos2);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirPlanilhaOrcamentariaComOrcamentosDiferentes(LicitacaoBD licitacao) {
		List<PoVRPLDTO> planilhasOrcamentarias = new ArrayList<>();

		List<PoBD> planilhasOrcamentariasBD = new ArrayList<>();

		PoVRPLDTO poAcompanhadaPorEvento1 = new PoVRPLDTO();
		poAcompanhadaPorEvento1.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento1.setDataBaseVrpl(java.time.LocalDate.now());
		poAcompanhadaPorEvento1.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEvento1.setReferencia("database");

		PoBD poAcompanhadaPorEventoBD1 = new PoBD();
		poAcompanhadaPorEventoBD1.setId(1L);
		poAcompanhadaPorEventoBD1.setDtBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD1.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl().minusMonths(2));
		poAcompanhadaPorEventoBD1.setReferencia("database");

		PoVRPLDTO poAcompanhadaPorEvento2 = new PoVRPLDTO();
		poAcompanhadaPorEvento2.setPrecoTotalAnalise(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setPrecoTotalLicitacao(new BigDecimal("1.00"));
		poAcompanhadaPorEvento2.setDataBaseVrpl(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setDataBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEvento2.setReferencia("analise");

		PoBD poAcompanhadaPorEventoBD2 = new PoBD();
		poAcompanhadaPorEventoBD2.setId(2L);
		poAcompanhadaPorEventoBD2.setDtBaseVrpl(poAcompanhadaPorEvento2.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setDtBaseAnalise(poAcompanhadaPorEvento1.getDataBaseVrpl());
		poAcompanhadaPorEventoBD2.setReferencia("analise");

		planilhasOrcamentarias.add(poAcompanhadaPorEvento1);
		planilhasOrcamentarias.add(poAcompanhadaPorEvento2);

		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD1);
		planilhasOrcamentariasBD.add(poAcompanhadaPorEventoBD2);

		List<EventoBD> eventosPo = new ArrayList<>();
		EventoBD evento1 = new EventoBD();
		evento1.setId(1L);

		EventoBD evento2 = new EventoBD();
		evento2.setId(2L);

		eventosPo.add(evento1);
		eventosPo.add(evento2);

		List<ServicoBD> servicos1 = new ArrayList<>();
		ServicoBD servico1 = new ServicoBD();
		servico1.setEventoFk(1L);
		servico1.setCdServico("1");
		servico1.setInFonte("1");
		servico1.setVlPrecoUnitarioLicitado(BigDecimal.ONE);
		servicos1.add(servico1);

		List<ServicoBD> servicos2 = new ArrayList<>();
		ServicoBD servico2 = new ServicoBD();
		servico2.setEventoFk(2L);
		servico2.setCdServico("1");
		servico2.setInFonte("1");
		servico2.setVlPrecoUnitarioLicitado(BigDecimal.TEN);
		servicos2.add(servico2);

		List<FrenteObraBD> frentesDeObraPo = new ArrayList<>();

		FrenteObraBD frenteObraBD = new FrenteObraBD();
		frenteObraBD.setNmFrenteObra("NomeFrenteObra1");

		frentesDeObraPo.add(frenteObraBD);

		List<ServicoFrenteObraBD> listaServicoFrenteDeObra = new ArrayList<>();
		ServicoFrenteObraBD servicoFrenteObra = new ServicoFrenteObraBD();
		servicoFrenteObra.setQtItens(new BigDecimal("1.0"));

		listaServicoFrenteDeObra.add(servicoFrenteObra);

		List<LoteBD> lotes = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);
		lote.setNumeroDoLote(1L);

		lotes.add(lote);

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		CodigoEFonteDeServico cf = new CodigoEFonteDeServico();
		cf.setCdServico("1");
		cf.setInFonte("1");

		listaCodigoEFonte.add(cf);

		DadosBasicosIntegracao dadosBasicos = new DadosBasicosIntegracao();
		dadosBasicos.setValorRepasse(BigDecimal.TEN);
		dadosBasicos.setValorGlobal(new BigDecimal("20.0"));

		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD1)))
				.thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorPo(PoVRPLDTO.from(poAcompanhadaPorEventoBD2)))
				.thenReturn(servicos2);

		when(siconvBC.recuperarDadosBasicos(Mockito.any())).thenReturn(dadosBasicos);

		when(licitacaoDAOMock.recuperarValorRepassePorListaLicitacoes(Mockito.any()))
				.thenReturn(BigDecimal.TEN);

		when(poDAOMock.recuperarValorTotalVRPL(Mockito.any())).thenReturn(new BigDecimal("10.0"));
		when(poDAOMock.recuperarValorTotalAnalisePorPO(Mockito.any())).thenReturn(new BigDecimal("10.0"));

		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotes);

		when(regrasParaEnviarParaAnaliseEAceite.getPoDAO()).thenReturn(poDAOMock);

		when(poDAOMock.recuperarPosPorLicitacaoELote(Mockito.any(), Mockito.any()))
				.thenReturn(planilhasOrcamentariasBD);

		when(frenteDeObraDAOMock.recuperarListaFrentesDeObraIdPo(Mockito.any())).thenReturn(frentesDeObraPo);

		when(frenteDeObraDAOMock.recuperarFrenteObraPorId(Mockito.any())).thenReturn(frenteObraBD);

		when(servicoDAOMock.recuperaListaServicoFrenteObraPorIdServico(Mockito.any()))
				.thenReturn(listaServicoFrenteDeObra);

		when(servicoDAOMock.recuperarServicosPorEvento(1L)).thenReturn(servicos1);
		when(servicoDAOMock.recuperarServicosPorEvento(2L)).thenReturn(servicos2);

		when(eventoDAOMock.recuperarEventoPorId(1L)).thenReturn(evento1);
		when(eventoDAOMock.recuperarListaEventosVRPLIdPo(Mockito.any())).thenReturn(eventosPo);
		when(regrasParaEnviarParaAnaliseEAceite.recuperaPOsDaLicitacao(licitacao))
				.thenReturn(planilhasOrcamentarias);
	}

	private void definirLotesPorLicitacao() {

		List<LoteBD> lotesDaLicitacao = new ArrayList<>();
		LoteBD lote = new LoteBD();
		lote.setId(1L);

		lotesDaLicitacao.add(lote);

		when(regrasParaEnviarParaAnaliseEAceite.getLoteLicitacaoDAO()).thenReturn(loteLicitacaoDAOMock);
		when(loteLicitacaoDAOMock.findLotesByIdLicitacao(Mockito.any())).thenReturn(lotesDaLicitacao);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Métodos auxiliares para os casos de teste
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void definirProcessoDeExecucaoConcluido() {
		ProcessoExecucaoResponse processoDeExecucao = ProcessoExecucaoResponse.newBuilder().setNumeroAno("01/2019")
				.setStatus("Concluído").build();

		when(clientLicitacoesMock.detalharProcessosExecucao(Mockito.anyLong())).thenReturn(processoDeExecucao);
	}

	private void logarComoProponente() {
		SiconvPrincipal proponente = new MockSiconvPrincipal(Profile.PROPONENTE, 1L);

		regrasParaEnviarParaAnaliseEAceite.setUsuarioLogado(proponente);
	}

	private LicitacaoBD definirSituacaoDaLicitacaoComoEmPreenchimento() {
		LicitacaoBD licitacaoEmPreenchimento = new LicitacaoBD();
		licitacaoEmPreenchimento.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());
		licitacaoEmPreenchimento.setIdLicitacaoFk(1L);
		licitacaoEmPreenchimento.setNumeroAno("0070");
		licitacaoEmPreenchimento.setId(1L);

		when(licitacaoDAOMock.findLicitacaoById(Mockito.any())).thenReturn(licitacaoEmPreenchimento);

		return licitacaoEmPreenchimento;
	}

	private void definirPropostaEmVersaoAtual() {
		PropostaBD propostaNaVersaoAtual = new PropostaBD();
		propostaNaVersaoAtual.setVersaoAtual(true);

		when(propostaDAOMock.recuperaUltimaVersaoDaProposta(Mockito.any())).thenReturn(propostaNaVersaoAtual);
	}

	private void definirValorContrapartidaSubmetaPorPo(BigDecimal valorContrapartida) {
		SubmetaBD submeta = new SubmetaBD();
		submeta.setDescricao("Descrição Submeta");
		submeta.setVlContrapartida(valorContrapartida);

		when(poDAOMock.recuperarSubmetaPorPO(Mockito.any())).thenReturn(submeta);

	}
	
	private void definirMockAssertThrow(Class<? extends BusinessException> classe) {
		Mockito.doThrow(classe)
	      .when(businessExceptionContext)
	      .add(Mockito.any(classe));
	}


}
