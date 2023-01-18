package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class EnviarParaAnaliseRules implements QuadroResumoValidator {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Inject
	private EnviarParaAnaliseEAceiteRules validator;
	
	@Override
	public void validate() {
		this.validator.realizarValidacoesParaEnviarParaAnalise(getLicitacao());
	}
}
