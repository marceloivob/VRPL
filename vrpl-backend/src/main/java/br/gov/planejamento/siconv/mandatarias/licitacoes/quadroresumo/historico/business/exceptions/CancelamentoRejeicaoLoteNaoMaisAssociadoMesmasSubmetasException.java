package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException extends BusinessException {

	private static final long serialVersionUID = 1445401072819844157L;

	public CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException(List<Long> listaNrLoteSubmetasDiferentes, Map<Long, List<String>> mapSubmetasDiferentesLoteLicitacaoAtual, Map<Long, List<String>> mapSubmetasDiferentesLoteAtivoProposta) {
		super(ErrorInfo.CANCELAMENTO_REJEICAO_LOTE_NAO_MAIS_ASSOCIADO_MESMAS_SUBMETAS,
				format(listaNrLoteSubmetasDiferentes.stream().map(String::valueOf)),
				buildMessage(listaNrLoteSubmetasDiferentes, mapSubmetasDiferentesLoteLicitacaoAtual, mapSubmetasDiferentesLoteAtivoProposta));
    }
	
	private static String buildMessage(List<Long> listaNrLoteSubmetasDiferentes, Map<Long, List<String>> mapSubmetasDiferentesLoteLicitacaoAtual, Map<Long, List<String>> mapSubmetasDiferentesLoteAtivoProposta) {
		List<String> texto = new ArrayList<String>();
		
		listaNrLoteSubmetasDiferentes.forEach(nrLote -> {
			StringBuilder sb = new StringBuilder();
			sb.append("Submeta(s) associada(s) ao lote " + nrLote + " no momento da rejeição:");
			sb.append(System.lineSeparator());			
			sb.append(mapSubmetasDiferentesLoteLicitacaoAtual.get(nrLote).stream().collect(joining(System.lineSeparator())));
			sb.append(System.lineSeparator());
			sb.append("Submeta(s) associada(s) ao lote " + nrLote + " atualmente:");
			sb.append(System.lineSeparator());
			sb.append(mapSubmetasDiferentesLoteAtivoProposta.get(nrLote).stream().collect(joining(System.lineSeparator())));
			texto.add(sb.toString());
		});
		
		return texto.stream().collect(joining(System.lineSeparator().repeat(2)));
	}
	
	private static String format(Stream<String> lista) {
		return lista.collect(joining(", ")).replaceAll(",([^,]*)$", " e$1");
	}
}
