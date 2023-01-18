package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto;

public enum OrcamentoReferenciaEnum {
	ANALISE {
		@Override
		public String getDescricao() {
			return "Aceito na Análise";
		}
	},
	DATABASE {
		@Override
		public String getDescricao() {
			return "Atualizado na Data Base da Licitação";
		}
	};

	public abstract String getDescricao();

}
