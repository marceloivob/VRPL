package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.TipoOrgao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;

public abstract class UsuarioPodeRealizarAcaoRules implements QuadroResumoValidator {

	protected boolean usuarioTemPerfilMandatariaOuConcedente(PropostaBD proposta, SiconvPrincipal usuario) {
		if (ModalidadePropostaEnum.fromCodigo(proposta.getModalidade(), proposta.getTermoCompromissoTemMandatar()).getTipoOrgao()
				.equals(TipoOrgao.INST_MANDATARIA)) {
			return usuario.hasProfile(Profile.MANDATARIA);
		}

		return usuario.hasProfile(Profile.CONCEDENTE);
	}
}
