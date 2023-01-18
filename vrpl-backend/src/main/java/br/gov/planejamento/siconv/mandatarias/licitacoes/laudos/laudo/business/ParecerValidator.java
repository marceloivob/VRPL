package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.Objects;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.StatusParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;

public class ParecerValidator {

	@Inject
	private ParecerVRPLConstraintValidator vrplConstraintValidator;

	@Inject
	private ParecerVRPLBusinessRuleValidator vrplBusinessRuleValidator;

	@Inject
	private ParecerVRPLSConstraintValidator vrplsConstraintValidator;

	@Inject
	private ParecerVRPLSBusinessRuleValidator vrplsBusinessRuleValidator;

	@Inject
	private ParecerGeneralBusinessRuleValidator generalBusinessRuleValidator;

	public boolean isValid(LaudoDTO laudo) {
		Objects.requireNonNull(laudo);
		Objects.requireNonNull(laudo.getTemplateFk());

		if (laudo.ehVRPL()) {
			vrplConstraintValidator.validate(laudo);

			if (!laudoEstaEmRascunho(laudo)) {
				vrplBusinessRuleValidator.validate(laudo);
			}

		} else if (laudo.ehVRPLS()) {
			vrplsConstraintValidator.validate(laudo);

			if (!laudoEstaEmRascunho(laudo)) {
				vrplsBusinessRuleValidator.validate(laudo);
			}
		} else {
			throw new IllegalArgumentException("Tipo de Parecer inválido: " + laudo.getTemplateFk());
		}

		if (!laudoEstaEmRascunho(laudo)) {
			generalBusinessRuleValidator.validate(laudo);
		}

		return true;
	}

	protected boolean laudoEstaEmRascunho(LaudoDTO laudo) {
		return laudo.getInStatus().equals(StatusParecerEnum.RASCUNHO.getCodigo());
	}


}
