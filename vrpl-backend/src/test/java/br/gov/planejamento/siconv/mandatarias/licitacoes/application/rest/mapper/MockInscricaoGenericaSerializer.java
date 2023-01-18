package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;

public class MockInscricaoGenericaSerializer extends InscricaoGenericaSerializer {

	private static final long serialVersionUID = 1L;

	private SiconvPrincipal usuarioLogado;

	@Override
	public SiconvPrincipal getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(SiconvPrincipal usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

}
