package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

/**
 * RN: 566904 - SICONV-DocumentosOrcamentarios-Pareceres-RN-Validacao
 */
public class JustificarInviabilidadeException extends BusinessException {

	private static final long serialVersionUID = 1L;

	/**
	 * Regra 5 - Caso a Conclusão seja "Inviável" E não exista Justificativas na
	 * Conclusão.
	 * <p>
	 * Apresentar a mensagem de alerta: Justificar a causa de inviabilidade.
	 */
	public JustificarInviabilidadeException() {
		super(ErrorInfo.ERRO_JUSTIFICAR_CAUSA_INVIABILIDADE_LAUDO);
	}

}
