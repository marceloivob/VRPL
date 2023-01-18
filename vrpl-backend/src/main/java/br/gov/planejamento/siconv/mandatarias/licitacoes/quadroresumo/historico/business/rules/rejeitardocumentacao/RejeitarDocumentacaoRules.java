package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.rejeitardocumentacao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ConsideracoesEmBrancoRejeicaoParecerViavel;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class RejeitarDocumentacaoRules implements QuadroResumoValidator {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private HistoricoLicitacaoDTO historico;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private boolean existeParecerViavelEmitido;

	@Inject
	private BusinessExceptionContext businessExceptionContext;

	@Override
	public void validate() {
		this.verificarExistenciaParecerEmitidoViavelParaALicitacao(historico, licitacao, existeParecerViavelEmitido);
		this.validarFornecedoresObsoletos(licitacao);
	}

	public void verificarExistenciaParecerEmitidoViavelParaALicitacao(HistoricoLicitacaoDTO historico,
			LicitacaoBD licitacao, boolean existeParecerViavelEmitido) {
		if (existeParecerViavelEmitido) {
			if (historico.getConsideracoes() == null || "".equals(historico.getConsideracoes())) {
				businessExceptionContext.add(new ConsideracoesEmBrancoRejeicaoParecerViavel());
			}
		}
	}

	private void validarFornecedoresObsoletos(LicitacaoBD licitacaoEstadoAtual) {
		List<Long> ids = new ArrayList<>();
		ids.add(licitacaoEstadoAtual.getId());

		List<FornecedorBD> obsoletos = new ArrayList<>();
		List<FornecedorBD> fornecedores = getLicitacaoDAO().recuperaFornecedoresDasLicitacoes(ids);
		for (FornecedorBD fornecedorBD : fornecedores) {
			Boolean fornecedorObsoleto = getLoteLicitacaoDAO().existeLoteAssociadoAoFornecedor(fornecedorBD.getId());
			fornecedorObsoleto = fornecedorObsoleto && fornecedorBD.getObsoleto();
			if (fornecedorObsoleto) {
				obsoletos.add(fornecedorBD);
			}
		}

		if (!obsoletos.isEmpty()) {
			String erro = this.formatarStringFornecedores(licitacaoEstadoAtual, obsoletos);
			businessExceptionContext.add(new BusinessException(ErrorInfo.FORNECEDORES_OBSOLETOS, erro));
			businessExceptionContext.add(new BusinessException(ErrorInfo.FORNECEDORES_OBSOLETOS_REJEITAR));
		}

	}

	private String formatarStringFornecedores(LicitacaoBD licitacaoEstadoAtual, List<FornecedorBD> obsoletos) {
		StringBuffer sb = new StringBuffer();

		sb.append("Licitação: ");
		sb.append(licitacaoEstadoAtual.getNumeroAno());
		sb.append(" [");

		for (FornecedorBD fornecedorBD : obsoletos) {
			sb.append("Fornecedor: ");
			sb.append(fornecedorBD.getIdentificacao());
			sb.append(" - ");
			sb.append(fornecedorBD.getRazaoSocial());
			sb.append(", ");
		}

		sb.delete(sb.length() - 2, sb.length());
		sb.append("]");

		return sb.toString();
	}

	protected LicitacaoDAO getLicitacaoDAO() {
		return dao.get(LicitacaoDAO.class);
	}

	protected LoteLicitacaoDAO getLoteLicitacaoDAO() {
		return dao.get(LoteLicitacaoDAO.class);
	}

}
