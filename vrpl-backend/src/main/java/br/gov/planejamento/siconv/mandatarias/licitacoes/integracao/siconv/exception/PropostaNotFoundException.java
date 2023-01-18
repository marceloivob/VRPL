package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropostaNotFoundException extends BusinessException {

	private static final long serialVersionUID = -6632005291913532011L;

	/**
	 * RN: 513493 -
	 * SICONV-DocumentosOrcamentarios-Geral-MSG-Erro-PropostaNaoEncontrada
	 */
	public PropostaNotFoundException(SiconvPrincipal usuarioLogado) {
		super(ErrorInfo.ERRO_PROPOSTA_NAO_ENCONTRADA);

		log.debug("Mensagem: [{}], IdProposta: {}, Usuario: {}]", ErrorInfo.ERRO_PROPOSTA_NAO_ENCONTRADA,
				usuarioLogado.getIdProposta(), usuarioLogado.getCpf());
	}

	/**
	 * RN: 513493 -
	 * SICONV-DocumentosOrcamentarios-Geral-MSG-Erro-PropostaNaoEncontrada
	 */
	public PropostaNotFoundException(SiconvPrincipal usuarioLogado, Long versaoDaProposta) {
		super(ErrorInfo.ERRO_PROPOSTA_NAO_ENCONTRADA);

		log.debug("Mensagem: [{}], IdProposta: {}, Versão: {}, Usuario: {}]", ErrorInfo.ERRO_PROPOSTA_NAO_ENCONTRADA,
				usuarioLogado.getIdProposta(), versaoDaProposta, usuarioLogado.getCpf());
	}

}
