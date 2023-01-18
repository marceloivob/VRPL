package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache;

public enum TabelasDoVRPLEnum {

	ANEXO("vrpl_anexo"), EVENTO("vrpl_evento"),
	EVENTOS_FRENTES_DE_OBRA("vrpl_evento_frente_obra"),
	FRENTES_DE_OBRA("vrpl_frente_obra"),
	GRUPO_PERTUNTA("vrpl_grupo_pergunta"),
	HISTORICO("vrpl_historico_licitacao"),
	LAUDO("vrpl_laudo"),
	LICITACAO("vrpl_licitacao"),
	LOTE_LICITACAO("vrpl_lote_licitacao"),
	MACRO_SERVICO("vrpl_macro_servico"),
	MACRO_SERVICO_PARCELA("vrpl_macro_servico_parcela"),
	META("vrpl_meta"),
	PENDENCIA("vrpl_pendencia"),
	PERGUNTA("vrpl_pergunta"),
	PO("vrpl_po"),
	PROPOSTA("vrpl_proposta"),
	RESPOSTA("vrpl_resposta"),
	SERVICO("vrpl_servico"),
	SERVICO_FRENTE_DE_OBRA("vrpl_servico_frente_obra"),
	SUBITEM_DE_INVESTIMENTO("vrpl_subitem_investimento"),
	SUBMETA("vrpl_submeta"),
	TEMPLATE_LAUDO("vrpl_template_laudo");

	private String nomeDaTabela;

	TabelasDoVRPLEnum(String nomeDaTabela) {
		this.nomeDaTabela = nomeDaTabela;
	}

	public String getNomeDaTabela() {
		return nomeDaTabela;
	}

}
