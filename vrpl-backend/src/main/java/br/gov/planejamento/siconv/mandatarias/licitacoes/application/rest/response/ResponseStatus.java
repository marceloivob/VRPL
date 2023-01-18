package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response;

public enum ResponseStatus {
    SUCCESS("success") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
	WARN("warn") {
		@Override
		public String getDescription() {
			return this.description;
		}
	},
    FAIL("fail") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    ERROR("error") {
        @Override
        public String getDescription() {
            return this.description;
        }
    };

    public abstract String getDescription();

    protected String description;

    private ResponseStatus(String description) {
        this.description = description;
    }
}
