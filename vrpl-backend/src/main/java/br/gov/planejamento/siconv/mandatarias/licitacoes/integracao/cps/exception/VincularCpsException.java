package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VincularCpsException extends BusinessException {

	private static final long serialVersionUID = -8150127840044488890L;

	private static final String CPS_PROPOSTA_DIFERENTE = "quadro.resumo.cps.proposta.diferente";
	private static final String CPS_EM_ADITIVACAO = "quadro.resumo.cps.aditivacao";
	private static final String NAO_EXISTE_CPS_ATIVO_VIGENTE = "quadro.resumo.cps.ativo.vigente";

	private static final String NAO_EXISTE_CPS_VIGENTE = "quadro.resumo.cps.vigente";

	private static final String ERRO_INTEGRACAO_SERVICO_SICONV = "quadro.resumo.erro.integracao.servico.siconv";

	public VincularCpsException() {
		super(ErrorInfo.ERRO_AO_ACIONAR_SERVICO_GPRC_CPS);
	}

	public VincularCpsException(ErrorInfo error, PropostaBD proposta) {
		super(error);

		log.info("Mensagem: [{}], detalhe: [Proposta: {}]",	error, proposta);
	}

	public static ErrorInfo getErrorInfo(String mensagemCpsGrpc) {
		if(CPS_PROPOSTA_DIFERENTE.equals(mensagemCpsGrpc)) {
			return ErrorInfo.CPS_PROPOSTA_DIFERENTE;
		}

		if(CPS_EM_ADITIVACAO.equals(mensagemCpsGrpc)) {
			return ErrorInfo.CPS_EM_ADITIVACAO;
		}

		if (NAO_EXISTE_CPS_VIGENTE.equals(mensagemCpsGrpc)) {
			return ErrorInfo.NAO_EXISTE_CPS_VIGENTE;
		}

		if(NAO_EXISTE_CPS_ATIVO_VIGENTE.equals(mensagemCpsGrpc)) {
			return ErrorInfo.CPS_NAO_ATIVO_VIGENTE;
		}

		if(ERRO_INTEGRACAO_SERVICO_SICONV.equals(mensagemCpsGrpc)) {
			return ErrorInfo.CPS_ERRO_INTEGRACAO_SICONV;
		}

		return ErrorInfo.ERRO_AO_ACIONAR_SERVICO_GPRC_CPS;
	}
}
