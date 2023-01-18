package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaoExisteSPAHomologado extends BusinessException {

    private static final long serialVersionUID = -4508532064837057705L;

    /**
     * RN: 540930 - SICONV-DocumentosOrcamentarios-Geral-MSG-Info-NaoPermitirAcessoVerificacaoProcessoLicitatorio
     */
	public NaoExisteSPAHomologado(SiconvPrincipal usuarioLogado) {
        super(ErrorInfo.ERRO_PROPOSTA_SEM_SPA_HOMOLOGADO);

		log.debug("Mensagem: [{}], IdProposta: {}, Usuario: {}]", ErrorInfo.ERRO_PROPOSTA_SEM_SPA_HOMOLOGADO,
				usuarioLogado.getIdProposta(), usuarioLogado.getCpf());
    }

}
