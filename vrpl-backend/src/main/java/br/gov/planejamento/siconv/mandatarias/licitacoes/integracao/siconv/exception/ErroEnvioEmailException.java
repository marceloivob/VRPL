package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import javax.mail.MessagingException;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErroEnvioEmailException extends BusinessException {

	private static final long serialVersionUID = 821265578561110015L;

	/**
     * RN: 515884 SICONV-DocumentosOrcamentarios-ManterQuadroResumo-MSG-Alerta-ErroAoEnviarEmails
     */
	public ErroEnvioEmailException(MessagingException e) {
		super(ErrorInfo.ERRO_ENVIO_EMAIL);

		log.error("Mensagem: [{}], detalhe: [{}-{}]", ErrorInfo.ERRO_ENVIO_EMAIL, e, e.getStackTrace());
	}

}
