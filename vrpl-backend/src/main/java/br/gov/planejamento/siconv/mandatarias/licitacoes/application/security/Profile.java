package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

public enum Profile {
    CONCEDENTE("Concedente") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    MANDATARIA("Mandatária") {

        @Override
        public String getDescription() {
            return this.description;
        }
    },
    PROPONENTE("Proponente") {

        @Override
        public String getDescription() {
            return this.description;
        }
    },
    GUEST("Acesso Livre") {

        @Override
        public String getDescription() {
            return this.description;
        }
    };

    protected String description;

    public abstract String getDescription();

    private Profile(String description) {
        this.description = description;
    }
}
