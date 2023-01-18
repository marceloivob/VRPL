package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataInicioObraInvalidaException extends BusinessException {

	private static final long serialVersionUID = -6011968847741747744L;

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

	public DataInicioObraInvalidaException(YearMonth previsaoInicioDaObraAnalise, YearMonth previsaoDeInicio) {
		super(ErrorInfo.DATA_INICIO_OBRA_INVALIDA, previsaoInicioDaObraAnalise.format(formatter));

		log.info("Mensagem: [{}], PrevisaoInicioDaObraAnalise: [{}], PrevisaoDeInicio: [{}]",
				ErrorInfo.DATA_INICIO_OBRA_INVALIDA, previsaoInicioDaObraAnalise.format(formatter),
				previsaoDeInicio.format(formatter));

    }
}
