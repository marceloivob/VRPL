package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitardocumentacao;

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

public class UsuarioPodeAceitarDocumentacaoRules extends UsuarioPodeRealizarAcaoRules {

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
	private boolean existeParecerTecnicoEmitidoEViavel;
	
	@Override
	public void validate() {
		this.verificaSeUsuarioTemPermissaoParaExecutarAcao(proposta, licitacao, existeParecerTecnicoEmitidoEViavel, usuarioLogado);
	}
	
	public void verificaSeUsuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			boolean existeParecerTecnicoEmitidoEViavel, SiconvPrincipal usuarioLogado) {

		if (!this.usuarioTemPermissaoParaExecutarAcao(propostaAtual, licitacao, existeParecerTecnicoEmitidoEViavel, usuarioLogado)) {
			throw new MudancaDeEstadoNaoPermitidaException(propostaAtual, licitacao, usuarioLogado);
		}
	}
	
	public boolean usuarioTemPermissaoParaExecutarAcao(PropostaBD propostaAtual, LicitacaoBD licitacao,
			boolean existeParecerTecnicoEmitidoEViavel, SiconvPrincipal usuarioLogado) {
		Boolean ehVersaoAtual = propostaAtual.ehVersaoAtual();
		Boolean estaEmAnalise = SituacaoLicitacaoEnum.EM_ANALISE.getSigla().equals(licitacao.getSituacaoDaLicitacao());
		Boolean podeIniciarAceitarDocumentacao = ehVersaoAtual;
		podeIniciarAceitarDocumentacao = podeIniciarAceitarDocumentacao && estaEmAnalise;
		podeIniciarAceitarDocumentacao = podeIniciarAceitarDocumentacao
				&& usuarioTemPerfilMandatariaOuConcedente(propostaAtual, usuarioLogado);

		// RN 586279:
		// SICONV-DocumentosOrcamentarios-Geral-RN-FaseVRPLOpcoesSituacaoAnaliseMandatariaConcedente
		// Aceitar: exibir APENAS se parecer técnico emitido com a situação Viável
		podeIniciarAceitarDocumentacao = podeIniciarAceitarDocumentacao && existeParecerTecnicoEmitidoEViavel;

		return podeIniciarAceitarDocumentacao;
	}
}
