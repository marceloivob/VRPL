package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.MudancaDeEstadoNaoPermitidaException;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class UsuarioPodeCancelarRejeicaoRulesTest {

	@Spy
	private UsuarioPodeCancelarRejeicaoRules usuarioPodeCancelarRejeicaoRules;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDePropostaQueNaoEhAAtual() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(false);

		LicitacaoBD licitacao = new LicitacaoBD();
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);

		assertThrows(MudancaDeEstadoNaoPermitidaException.class, () -> usuarioPodeCancelarRejeicaoRules
				.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado));

	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueNaoEstaRejeitada() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_ANALISE.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);

		assertThrows(MudancaDeEstadoNaoPermitidaException.class, () -> usuarioPodeCancelarRejeicaoRules
				.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado));

	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueEstaRejeitadaMasEhProponente() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE);

		assertThrows(MudancaDeEstadoNaoPermitidaException.class, () -> usuarioPodeCancelarRejeicaoRules
				.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado));
	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueEstaRejeitadaMasEhMandatariaEEhConvenio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);

		assertThrows(MudancaDeEstadoNaoPermitidaException.class, () -> usuarioPodeCancelarRejeicaoRules
				.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado));
	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueEstaRejeitadaMasEhMandatariaEEhContratoDeRepasse() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);

		assertDoesNotThrow(() -> usuarioPodeCancelarRejeicaoRules.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual,
				licitacao, usuarioLogado));
	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueEstaRejeitadaMasEhConcedenteEEhConvenio() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.CONCEDENTE);

		assertDoesNotThrow(() -> usuarioPodeCancelarRejeicaoRules.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual,
				licitacao, usuarioLogado));
	}

	@Test
	public void verificaSeUsuarioPodeCancelarRejeicaoDeLicitacaoQueEstaRejeitadaMasEhConcedenteEEhConvenioOuContratoDeRepasse() {

		PropostaBD propostaAtual = new PropostaBD();
		propostaAtual.setVersaoAtual(true);
		propostaAtual.setModalidade(ModalidadePropostaEnum.CONVENIO_CONTRATO_DE_REPASSE.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.REJEITADA.getSigla());
		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.CONCEDENTE);

		assertDoesNotThrow(() -> usuarioPodeCancelarRejeicaoRules.verificaSeUsuarioTemPermissaoParaExecutarAcao(propostaAtual,
				licitacao, usuarioLogado));
	}

}
