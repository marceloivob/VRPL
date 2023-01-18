package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException extends BusinessException {

	private static final long serialVersionUID = -6968262294351950188L;

	public CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException(Map<String, List<Long>> mapLicitacaoLotes) {
		super(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_FOI_ASSOCIADO_A_LICITACAO_REJEITADA_POSTERIORMENTE, 
				buildMessage(mapLicitacaoLotes));
    }
	
	private static String buildMessage(Map<String, List<Long>> mapLicitacaoLotes) {
		List<String> texto = new ArrayList<String>();
		texto.add("<ul>");
		mapLicitacaoLotes.forEach((licitacao, lote) -> {
			texto.add("<li> Licitação " + licitacao + " rejeitada, vinculada ao(s) lote(s) " + format(lote.stream().map(String::valueOf)) + " </li>");
		});
		texto.add("</ul>");
		return texto.stream().collect(joining(System.lineSeparator().repeat(2)));
	}
	
	private static String format(Stream<String> lista) {
		return lista.collect(joining(", ")).replaceAll(",([^,]*)$", " e$1");
	}
}
