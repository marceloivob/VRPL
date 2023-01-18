package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitardocumentacao;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise.EnviarParaAnaliseEAceiteRules;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class AceitarDocumentacaoRules implements QuadroResumoValidator {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Inject
	private EnviarParaAnaliseEAceiteRules validator;
	
	@Override
	public void validate() {
		this.validator.realizarValidacoesParaAceite(getLicitacao());
	}
}

