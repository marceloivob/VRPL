package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise extends BusinessException {

	private static final long serialVersionUID = 1481654711418894719L;

	public QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise(ServicoBD servico, SubmetaBD submeta) {
		super(ErrorInfo.QUANTIDADES_TOTAL_SERVICO_DIFERENTE_TOTAL_ACEITO_ANALISE_EXCEPTION, servico.getTxDescricao(),
				submeta.getDescricao());

		log.info("Mensagem: [{}], detalhe: [Serviço: {} - Submeta: {}]",
				ErrorInfo.QUANTIDADES_TOTAL_SERVICO_DIFERENTE_TOTAL_ACEITO_ANALISE_EXCEPTION, servico, submeta);
	}
}
