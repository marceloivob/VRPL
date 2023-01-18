package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.TipoFonteEnum;

public class ItensDePoDeUmLoteComPrecosDiferenteException extends BusinessException {

	private static final long serialVersionUID = 7328611018039059123L;

	public ItensDePoDeUmLoteComPrecosDiferenteException(String lote, String fonte, String codigo, String itens) {
		super(ErrorInfo.ITENS_PO_DE_UM_LOTE_COM_PRECO_DIFERENTE, lote, TipoFonteEnum.fromSigla(fonte), codigo, itens);
	}
}
