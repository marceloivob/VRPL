package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;

public class CancelamentoAceiteExisteInstrumentoContratualException extends BusinessException {
	

	public CancelamentoAceiteExisteInstrumentoContratualException(List<String> listaNumerosInstrumentosContratuais) {
		super(ErrorInfo.CANCELAMENTO_ACEITE_EXISTE_INSTRUMENTO_CONTRATUAL, buildMessage(listaNumerosInstrumentosContratuais));
    }
	
	private static String buildMessage(List<String> listaInstrumentosContratuais) {

		String instrumentoContratual = "";
		
		if(listaInstrumentosContratuais != null) {

			for (int i = 0; i < listaInstrumentosContratuais.size(); i++) {

				if (i == 0) {
					instrumentoContratual = listaInstrumentosContratuais.get(i);
				} else {
					if(i == listaInstrumentosContratuais.size() - 1) {
						instrumentoContratual = instrumentoContratual + " e " + listaInstrumentosContratuais.get(i);
					} else {
						instrumentoContratual = instrumentoContratual + ", " + listaInstrumentosContratuais.get(i); 
					}
				}

			}		
			
		}
				
		return instrumentoContratual;		
	}

}
