package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException extends BusinessException {

	private static final long serialVersionUID = -6612682070531103453L;

	public CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException(Map<String, List<Long>> mapLicitacaoLotesAssociados) {
        super(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_JA_ASSOCIADO_OUTRA_LICITACAO, 
        		buildMessage(mapLicitacaoLotesAssociados));
    }

	private static String buildMessage(Map<String, List<Long>> mapLicitacaoLotesAssociados) {
		List<String> texto = new ArrayList<String>();
		
		mapLicitacaoLotesAssociados.forEach((licitacao, lote) -> {
			texto.add("Licitação " + licitacao + " associada ao(s) lote(s) " + format(lote.stream().map(String::valueOf)));
		});
		
		return texto.stream().collect(joining(System.lineSeparator().repeat(2)));
	}

	private static String format(Stream<String> lista) {
		return lista.collect(joining(", ")).replaceAll(",([^,]*)$", " e$1");
	}
}
