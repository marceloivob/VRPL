package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LicitacaoNaoEncontradaException extends BusinessException {

	private static final long serialVersionUID = 294160191192042404L;

	/**
	 * RN: 543243 -
	 * SICONV-DocumentosOrcamentarios-ManterAssociacaoDeLotesALicitacao-MSG-Alerta-NenhumaLicitacaoRetornada
	 */
	public LicitacaoNaoEncontradaException(LicitacaoBD licitacao) {
		super(ErrorInfo.ERRO_LICITACAO_NAO_ENCONTRADA);

		log.info("Mensagem: [{}], detalhe: [{}]", ErrorInfo.ERRO_LICITACAO_NAO_ENCONTRADA, licitacao);
	}
}
