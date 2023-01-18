package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropostaSemNivelCPSCorrespondenteException extends BusinessException {

	private static final long serialVersionUID = 6924500087853486796L;

	public PropostaSemNivelCPSCorrespondenteException(PropostaBD proposta) {
		super(ErrorInfo.PROPOSTA_SEM_NIVEL_CPS_CORRESPONDENTE);
		
		log.info("Mensagem: [{}], detalhe: [Proposta: {}]",
				ErrorInfo.PROPOSTA_SEM_NIVEL_CPS_CORRESPONDENTE, proposta);

	}

}
