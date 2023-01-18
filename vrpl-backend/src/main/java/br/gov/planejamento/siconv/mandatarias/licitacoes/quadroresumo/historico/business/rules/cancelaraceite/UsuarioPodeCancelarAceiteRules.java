package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelaraceite;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.MudancaDeEstadoNaoPermitidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.UsuarioPodeRealizarAcaoRules;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class UsuarioPodeCancelarAceiteRules extends UsuarioPodeRealizarAcaoRules {

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

		Boolean estaAceita = SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla()
				.equals(licitacao.getSituacaoDaLicitacao());

		Boolean podeCancelar = ehVersaoAtual && estaAceita
				&& usuarioTemPerfilMandatariaOuConcedente(propostaAtual, usuarioLogado);

		return podeCancelar;
	}
}