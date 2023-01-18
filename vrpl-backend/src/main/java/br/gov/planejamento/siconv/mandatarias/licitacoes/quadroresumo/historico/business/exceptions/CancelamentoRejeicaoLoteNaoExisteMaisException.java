package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import static java.util.stream.Collectors.joining;

import java.util.List;
import java.util.stream.Stream;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CancelamentoRejeicaoLoteNaoExisteMaisException extends BusinessException {

	private static final long serialVersionUID = 7489675754397100234L;

	public CancelamentoRejeicaoLoteNaoExisteMaisException(List<Long> listaNumeroLote) {
        super(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_NAO_EXISTE_MAIS, format(listaNumeroLote.stream().map(String::valueOf)));
    }
	
	private static String format(Stream<String> lista) {
		return lista.collect(joining(", ")).replaceAll(",([^,]*)$", " e$1");
	}
}
