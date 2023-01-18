package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarenvioanalise;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.MudancaDeEstadoNaoPermitidaException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class UsuarioPodeCancelarEnvioAnaliseRules implements QuadroResumoValidator {
	
	@Inject
	private SiconvPrincipal usuarioLogado;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PropostaBD proposta;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	SituacaoLicitacaoEnum situacaoAnterior;
	
	@Override
	public void validate() {
		this.verificaSeUsuarioTemPermissaoParaExecutarAcao(proposta, licitacao, usuarioLogado, situacaoAnterior);
	}
	
	public void verificaSeUsuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SiconvPrincipal usuarioLogado, SituacaoLicitacaoEnum situacaoAnterior) {

		if (!this.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado, situacaoAnterior)) {
			throw new MudancaDeEstadoNaoPermitidaException(propostaAtual, licitacao, usuarioLogado);
		}
	}
	
	public boolean usuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SiconvPrincipal usuarioLogado, SituacaoLicitacaoEnum situacaoAnterior) {
		Boolean ehVersaoAtual = propostaAtual.ehVersaoAtual();

		Boolean estaEnviadaParaAnalise = SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla()
				.equals(licitacao.getSituacaoDaLicitacao());

		Boolean situacaoAnteriorEstavaEmPreenchimento = SituacaoLicitacaoEnum.EM_PREENCHIMENTO.equals(situacaoAnterior);

		Boolean situacaoValida = situacaoAnteriorEstavaEmPreenchimento && estaEnviadaParaAnalise;

		Boolean ehProponente = usuarioLogado.hasProfile(Profile.PROPONENTE);

		Boolean podeCancelarEnvio = ehVersaoAtual && situacaoValida && ehProponente;

		return podeCancelarEnvio;
	}
}
