package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SalvarLicitacaoException extends BusinessException {

	private static final long serialVersionUID = 6196540616398419963L;
	
	public SalvarLicitacaoException(PropostaBD proposta, LicitacaoBD licitacao, SiconvPrincipal usuarioLogado) {
		super(ErrorInfo.NAO_EH_POSSIVEL_SALVAR_ASSOCIACAO, licitacao.getSituacaoDaLicitacao(), usuarioLogado.getProfiles(), proposta.ehVersaoAtual());
		
		log.debug(
				"Mensagem: Não é possível salvar a associação em uma licitação na situação [{}] por um usuário com perfil [{}] e proposta versao atual [{}].",
				licitacao.getSituacaoDaLicitacao(), usuarioLogado.getProfiles(), proposta.ehVersaoAtual()
		);
	}

}
