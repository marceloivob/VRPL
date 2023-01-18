package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity;

public enum TipoDeParecerEnum {

	VRPL {
		@Override
		public Long getId() {
			return 1L;
		}
	},
	VRPLS {
		@Override
		public Long getId() {
			return 2L;
		}
	};

	public abstract Long getId();
}
