package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

/**
 * Representa uma ação a ser realizada para o Quadro Resumo. 
 * @author 61603945504
 *
 */
public interface QuadroResumoAction {
	
	void validar(@NotNull PropostaBD proposta,@NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico);
	
	void execute(@NotNull HistoricoLicitacaoDTO historicoRecebido, 
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao);

	/**
	 * Etapa opcional para integração.
	 */
	void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historicoRecebido,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao);

	List<QuadroResumoValidator> getValidators(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico);
	
 	EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao);

	void gerarHistorico(Handle transaction, HistoricoLicitacaoDTO historico, LicitacaoBD licitacao);

}
