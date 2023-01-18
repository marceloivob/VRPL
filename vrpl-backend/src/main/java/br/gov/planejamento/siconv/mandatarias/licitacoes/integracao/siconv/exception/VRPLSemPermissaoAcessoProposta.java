package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VRPLSemPermissaoAcessoProposta extends BusinessException{

	private static final long serialVersionUID = 829136100045717464L;

	public VRPLSemPermissaoAcessoProposta(SiconvPrincipal usuarioLogado) {
		
        super(ErrorInfo.ERRO_VRPL_SEM_ACESSO_PROPOSTA);

		log.debug("Mensagem: [{}], IdProposta: {}, Usuario: {}]", ErrorInfo.ERRO_VRPL_SEM_ACESSO_PROPOSTA,
				usuarioLogado.getIdProposta(), usuarioLogado.getCpf());

	}

}
