package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.iniciarcomplementacaoconvenente;

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

public class UsuarioPodeIniciarComplementacaoConvenenteRules implements QuadroResumoValidator {

	@Inject
	private SiconvPrincipal usuarioLogado;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PropostaBD proposta;
	
	@Override
	public void validate() {
		this.verificaSeUsuarioTemPermissaoParaExecutarAcao(proposta, licitacao, usuarioLogado);
	}
	
	public void verificaSeUsuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SiconvPrincipal usuarioLogado) {

		if (!this.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, usuarioLogado)) {
			throw new MudancaDeEstadoNaoPermitidaException(propostaAtual, licitacao, usuarioLogado);
		}
	}
	
	public boolean usuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SiconvPrincipal usuarioLogado) {
		Boolean ehVersaoAtual = propostaAtual.ehVersaoAtual();

		Boolean foiSolicitadaComplementacao = SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla()
				.equals(licitacao.getSituacaoDaLicitacao());

		// Lembrar que na fase de Licitação (VRPL) o Proponente é chamado de Convenente
		Boolean ehProponente = usuarioLogado.hasProfile(Profile.PROPONENTE);

		Boolean podeIniciarComplementacao = ehVersaoAtual && foiSolicitadaComplementacao && ehProponente;

		return podeIniciarComplementacao;
	}
}
