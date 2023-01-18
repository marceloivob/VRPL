package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SemCargaSinapiEExisteDataBaseDiferenteException extends BusinessException {
	
	private static final long serialVersionUID = -3334038005109137597L;
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

	public SemCargaSinapiEExisteDataBaseDiferenteException(LocalDate dataBase, String lote) {
		super(ErrorInfo.SEM_CARGA_SINAPI_PARA_DATA_BASE_DIFERENTE_MESMO_LOTE, dataBase.format(formatter), lote);

		log.info(dataBase.format(formatter) + " lote " + lote,
				ErrorInfo.SEM_CARGA_SINAPI_PARA_DATA_BASE_DIFERENTE_MESMO_LOTE);
    }

}
