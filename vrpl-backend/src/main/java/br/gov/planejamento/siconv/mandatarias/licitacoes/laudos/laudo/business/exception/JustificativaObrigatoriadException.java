package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

/**
 * RN: 566904 - SICONV-DocumentosOrcamentarios-Pareceres-RN-Validacao
 */
public class JustificativaObrigatoriadException extends BusinessException {

	private static final long serialVersionUID = 1L;

	/**
	 * Regra 4
	 * <p>
	 * - Existe seção com a opção "Não se aplica" marcada;
	 * <p>
	 * - E não existe Justificativas na Conclusão.
	 * <p>
	 * Apresentar a mensagem de alerta: A justificativa é obrigatória devido à
	 * existência da opção \"Não se aplica\" marcada na(s) seção(ões) {0}.
	 */
	public JustificativaObrigatoriadException(List<String> secoes) {
		super(ErrorInfo.ERRO_JUSTIFICATIVA_OBRIGATORIA_COM_RESPOSTA_NAO_SE_APLICA,
				secoes.stream().map(Object::toString).collect(Collectors.joining(", ")));
	}

}
