package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo.PO_NAO_ENCONTRADA;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PoNaoEncontradaException extends BusinessException {

	private static final long serialVersionUID = -8005499402304492481L;

	public PoNaoEncontradaException(PoDetalhadaVRPLDTO po) {
		super(PO_NAO_ENCONTRADA);

		log.info("Mensagem: [{}], detalhe: [{}]", PO_NAO_ENCONTRADA, po);
	}

}
