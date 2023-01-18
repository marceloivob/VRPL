package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class ComentarioObrigatorioParaSecao extends BusinessException {

	private static final long serialVersionUID = 8040274925462086254L;

	/**
	 * Regra 1 - Para Parecer Técnico de Engenharia: - Existe seção em que a
	 * resposta não esteja de acordo com o esperado, conforme regra de negócio:
	 * 566891: SICONV-DocumentosOrcamentarios-ManterVRPL-RN-Formulario E não existe
	 * o Comentário na respectiva seção.
	 * <p>
	 * O comentário é obrigatório na seção <descrição da seção>, pela existência de
	 * resposta(s) que não está(estão) de acordo com o esperado.
	 */
	public ComentarioObrigatorioParaSecao(String secao) {
		super(ErrorInfo.ERRO_COMENTARIO_OBRIGATORIO_PARA_SECAO, secao);
	}

}
