package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaoExisteCargaSinapiParaDataBaseException extends BusinessException {

	private static final long serialVersionUID = -2410470632536344579L;

	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

	public NaoExisteCargaSinapiParaDataBaseException(LocalDate dataBase) {
		super(ErrorInfo.SEM_CARGA_SINAPI_PARA_DATA_BASE, dataBase.format(formatter));

		log.info(dataBase.format(formatter), ErrorInfo.SEM_CARGA_SINAPI_PARA_DATA_BASE);
    }

}
