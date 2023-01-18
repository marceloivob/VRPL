package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CancelamentoAceiteExisteDocumentoLiquidacaoException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public CancelamentoAceiteExisteDocumentoLiquidacaoException(List<String> listaDocumentosLiquidacao) {
		super(ErrorInfo.CANCELAMENTO_ACEITE_EXISTE_DOCUMENTO_LIQUIDACAO, buildMessage(listaDocumentosLiquidacao));
    }
	
	private static String buildMessage(List<String> listaDocumentosLiquidacao) {

		String docLiquidacao = "";
		
		if(listaDocumentosLiquidacao != null) {

			for (int i = 0; i < listaDocumentosLiquidacao.size(); i++) {

				if (i == 0) {
					docLiquidacao = listaDocumentosLiquidacao.get(i);
				} else {
					if(i == listaDocumentosLiquidacao.size() - 1) {
						docLiquidacao = docLiquidacao + " e " + listaDocumentosLiquidacao.get(i);
					} else {
						docLiquidacao = docLiquidacao + ", " + listaDocumentosLiquidacao.get(i); 
					}
				}

			}					
			
		}		
		
		return docLiquidacao;
	}


}
