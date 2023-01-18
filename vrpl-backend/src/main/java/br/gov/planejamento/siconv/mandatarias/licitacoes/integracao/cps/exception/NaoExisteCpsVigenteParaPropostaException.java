package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaoExisteCpsVigenteParaPropostaException extends BusinessException {

	private static final long serialVersionUID = -7279896978851245049L;

	public NaoExisteCpsVigenteParaPropostaException(PropostaBD proposta) {
		super(ErrorInfo.ERRO_NAO_EXISTE_CPS_VIGENTE_PARA_PROPOSTA);
		
		log.info("Mensagem: [{}], detalhe: [Proposta: {}]",
				ErrorInfo.ERRO_NAO_EXISTE_CPS_VIGENTE_PARA_PROPOSTA, proposta);

	}

}
