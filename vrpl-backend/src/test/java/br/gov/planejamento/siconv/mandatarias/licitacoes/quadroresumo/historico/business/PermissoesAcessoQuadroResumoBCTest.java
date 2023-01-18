package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarcomplementacaoconvenente.UsuarioPodeCancelarComplementacaoConvenenteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenvioanalise.UsuarioPodeCancelarEnvioAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenviocomplementacao.UsuarioPodeCancelarEnvioComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.UsuarioPodeEnviarParaAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciaranalise.UsuarioPodeIniciarAnaliseRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciarcomplementacaoconvenente.UsuarioPodeIniciarComplementacaoConvenenteRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.rejeitardocumentacao.UsuarioPodeRejeitarDocumentacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.solicitarcomplementacao.UsuarioPodeSolicitarComplementacaoRules;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class PermissoesAcessoQuadroResumoBCTest {

	@InjectMocks
	private QuadroResumoBC quadroResumoBC;

	@InjectMocks
	private UsuarioPodeCancelarEnvioAnaliseRules usuarioPodeCancelarEnvioAnaliseRules;
	
	@InjectMocks
	private UsuarioPodeIniciarAnaliseRules usuarioPodeIniciarAnaliseRules;
	
	@InjectMocks
	private UsuarioPodeSolicitarComplementacaoRules usuarioPodeSolicitarComplementacaoRules;
	
	@InjectMocks
	private UsuarioPodeCancelarEnvioComplementacaoRules usuarioPodeCancelarEnvioComplementacaoRules;
	
	@InjectMocks
	private UsuarioPodeCancelarComplementacaoConvenenteRules usuarioPodeCancelarComplementacaoConvenenteRules;
	
	@InjectMocks
	private UsuarioPodeIniciarComplementacaoConvenenteRules usuarioPodeIniciarComplementacaoConvenenteRules;
	
	@InjectMocks
	private UsuarioPodeRejeitarDocumentacaoRules usuarioPodeRejeitarDocumentacaoRules;
	
	@InjectMocks
	private UsuarioPodeEnviarParaAnaliseRules usuarioPodeEnviarParaAnaliseRules;
	
	private SiconvPrincipal usuarioLogado;

	@Spy
	private LaudoBC laudo;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		this.quadroResumoBC.setLaudo(laudo);
	}

	@Test
	public void ComoProponenteEmPreenchimentoVersaoAtualPossoEnviarParaAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeEnviarParaAnalise(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmComplementacaoVersaoAtualPossoEnviarParaAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeEnviarParaAnalise(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEmComplementacaoVersaoAtualPossoEnviarParaAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeEnviarParaAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmComplementacaoVersaoAnteriorPossoEnviarParaAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeEnviarParaAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmAnaliseVersaoAtualPossoEnviarParaAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeEnviarParaAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteSolicitadaComplementacaoProponenteVersaoAtualPossoIniciarComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeIniciarComplementacaoConvenente(propostaAtual,
				licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteSolicitadaComplementacaoProponenteVersaoAnteriorPossoIniciarComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeIniciarComplementacaoConvenente(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmAnaliseVersaoAtualPossoIniciarComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeIniciarComplementacaoConvenente(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaSolicitadaComplementacaoProponenteVersaoAtualPossoIniciarComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeIniciarComplementacaoConvenente(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmPreenchimentoVersaoAtualPossoCancelarEnvio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_PREENCHIMENTO;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvio(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmPreenchimentoVersaoAnteriorPossoCancelarEnvio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_PREENCHIMENTO;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvio(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEnviadaParaAnaliseVindoEmPreenchimentoVersaoAtualPossoCancelarEnvio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_PREENCHIMENTO;

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeCancelarEnvio(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmComplementacaoVersaoAtualPossoCancelarEnvio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvio(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmPreenchimentoVindoEnviadaAnaliseVersaoAtualPossoCancelarEnvio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvio(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmComplementacaoVersaoAtualPossoCancelarEnvioComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvioComplementacao(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmComplementacaoVersaoAnteriorPossoCancelarEnvioComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvioComplementacao(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEnviadaParaAnaliseVindoEmComplementacaoVersaoAtualPossoCancelarEnvioComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeCancelarEnvioComplementacao(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaParaAnaliseVindoEmAnaliseVersaoAtualPossoCancelarEnvioComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.EM_ANALISE;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvioComplementacao(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmComplementacaoVindoEnviadaAnaliseVersaoAtualPossoCancelarEnvioComplementacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla());
		SituacaoLicitacaoEnum situacaoAnterior = SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE;

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarEnvioComplementacao(propostaAtual, licitacao,
				situacaoAnterior);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEnviadaAnaliseVersaoAtualPossoIniciarAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeIniciarAnalise(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoConcedenteEnviadaAnaliseVersaoAtualPossoIniciarAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO_CONTRATO_DE_REPASSE.getCodigo());
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.CONCEDENTE).usuarioPodeIniciarAnalise(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);

	}

	@Test
	public void ComoMandatariaEnviadaAnaliseVersaoAnteriorPossoIniciarAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeIniciarAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaComPropostaEmAnalisePossoSolicitacaoComplementacao() {
		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Mockito.doReturn(false).when(this.laudo).existeParecerEmitidoParaALicitacao(licitacao);

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeSolicitarComplementacao(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoConcedenteComPropostaEmAnalisePossoSolicitacaoComplementacao() {
		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO.getCodigo());
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Mockito.doReturn(false).when(this.laudo).existeParecerEmitidoParaALicitacao(licitacao);

		Boolean obtido = euComo(Profile.CONCEDENTE).usuarioPodeSolicitarComplementacao(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteComPropostaEmAnalisePossoSolicitacaoComplementacao() {
		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Mockito.doReturn(true).when(this.laudo).existeParecerEmitidoParaALicitacao(licitacao);

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeSolicitarComplementacao(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEmAnaliseVersaoAtualPossoIniciarAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeIniciarAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEnviadaAnaliseVersaoAtualPossoIniciarAnalise() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeIniciarAnalise(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEmAnaliseVersaoAtualPossoRejeitar() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeRejeitarDocumentacao(propostaAtual, licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEmAnaliseVersaoAnteriorPossoRejeitar() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeRejeitarDocumentacao(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEnviadaParaAnaliseVersaoAtualPossoRejeitar() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeRejeitarDocumentacao(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteEmAnaliseVersaoAtualPossoRejeitar() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeRejeitarDocumentacao(propostaAtual, licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaSolicitadaComplementacaoVersaoAtualPossoCancelarSolicitacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeCancelarSolicitacaoComplementacao(propostaAtual,
				licitacao);
		Boolean esperado = true;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaSolicitadaComplementacaoVersaoAnteriorPossoCancelarSolicitacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeCancelarSolicitacaoComplementacao(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoMandatariaEmAnaliseVersaoAtualPossoCancelarSolicitacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());

		Boolean obtido = euComo(Profile.MANDATARIA).usuarioPodeCancelarSolicitacaoComplementacao(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	@Test
	public void ComoProponenteSolicitadaComplementacaoVersaoAtualPossoCancelarSolicitacao() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla());

		Boolean obtido = euComo(Profile.PROPONENTE).usuarioPodeCancelarSolicitacaoComplementacao(propostaAtual,
				licitacao);
		Boolean esperado = false;

		assertEquals(esperado, obtido);
	}

	private PermissoesAcessoQuadroResumoBCTest euComo(Profile perfilDoUsuario) {
		this.usuarioLogado = new MockSiconvPrincipal(perfilDoUsuario);
		quadroResumoBC.setUsuarioLogado(this.usuarioLogado);

		return this;
	}

	private boolean usuarioPodeEnviarParaAnalise(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return this.usuarioPodeEnviarParaAnaliseRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado);
	}

	private boolean usuarioPodeIniciarComplementacaoConvenente(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return this.usuarioPodeIniciarComplementacaoConvenenteRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual,
				licitacao, usuarioLogado);
	}

	private boolean usuarioPodeCancelarEnvio(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SituacaoLicitacaoEnum situacaoAnterior) {
		return this.usuarioPodeCancelarEnvioAnaliseRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado, situacaoAnterior);
	}

	private boolean usuarioPodeCancelarEnvioComplementacao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SituacaoLicitacaoEnum situacaoAnterior) {
		return this.usuarioPodeCancelarEnvioComplementacaoRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado, situacaoAnterior);
	}

	private boolean usuarioPodeIniciarAnalise(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return this.usuarioPodeIniciarAnaliseRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado);
	}

	private boolean usuarioPodeSolicitarComplementacao(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return this.usuarioPodeSolicitarComplementacaoRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado);
	}

	private boolean usuarioPodeRejeitarDocumentacao(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return usuarioPodeRejeitarDocumentacaoRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao,
				usuarioLogado);
	}

	private boolean usuarioPodeCancelarSolicitacaoComplementacao(PropostaBD propostaAtual, LicitacaoBD licitacao) {
		return this.usuarioPodeCancelarComplementacaoConvenenteRules.usuarioTemPermissaoParaExecutarAcao(propostaAtual,
				licitacao, usuarioLogado);
	}

}
