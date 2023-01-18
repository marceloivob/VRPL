package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExisteDataBaseDiferenteNoMesmoLoteException extends BusinessException {
	
	private static final long serialVersionUID = -4432826335295873681L;
	
	public ExisteDataBaseDiferenteNoMesmoLoteException(String lote) {
		super(ErrorInfo.DATA_BASE_DIFERENTE_NO_MESMO_LOTE, lote);
		log.info(lote, ErrorInfo.DATA_BASE_DIFERENTE_NO_MESMO_LOTE);

    }

}
