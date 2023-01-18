package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.rules;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.SalvarLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class SalvarLicitacaoRules {
	
	@Inject
	private SiconvPrincipal usuarioLogado;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PropostaBD proposta;
	
	
	public void validate() {
		this.verificaSePodeSalvarLicitacao(proposta, licitacao, usuarioLogado);
	}
	
	public void verificaSePodeSalvarLicitacao(PropostaBD proposta, LicitacaoBD licitacao, SiconvPrincipal usuarioLogado) {
		
		if( ! this.podeSalvarLicitacao(proposta, licitacao, usuarioLogado) ) {
			throw new SalvarLicitacaoException(proposta, licitacao, usuarioLogado);
		}
	}
	
	public boolean podeSalvarLicitacao(PropostaBD proposta, LicitacaoBD licitacao, SiconvPrincipal usuarioLogado) {
		Boolean ehUltimaVersao = proposta.ehVersaoAtual();
		Boolean ehProponente = usuarioLogado.isProponente();
		Boolean licitacaoEmPreenchimento =  SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla().equals( licitacao.getSituacaoDaLicitacao() );
		Boolean licitacaoEmComplementacao =  SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla().equals( licitacao.getSituacaoDaLicitacao() );
		Boolean licitacaoEmPreenchimentoOuComplementacao = licitacaoEmPreenchimento || licitacaoEmComplementacao;
		
		return ehUltimaVersao && ehProponente && licitacaoEmPreenchimentoOuComplementacao;
	}

}
