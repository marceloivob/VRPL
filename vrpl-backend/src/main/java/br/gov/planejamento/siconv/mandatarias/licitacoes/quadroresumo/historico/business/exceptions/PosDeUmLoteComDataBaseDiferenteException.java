package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class PosDeUmLoteComDataBaseDiferenteException extends BusinessException {

	private static final long serialVersionUID = -5592173451885804857L;
	
	public PosDeUmLoteComDataBaseDiferenteException(String lote, List<String> submetas) {
		super(ErrorInfo.POS_DE_UM_LOTE_COM_DATABASE_DIFERENTE, lote, formatSubmetas(submetas));
	}
	
	
	private static String formatSubmetas(List<String> submetasString) {
		StringBuilder sb = new StringBuilder();
		
		for (String s : submetasString) {
			sb.append( "<li> " + s + " </li>" );
		}
		
		return sb.toString();
	}
}
