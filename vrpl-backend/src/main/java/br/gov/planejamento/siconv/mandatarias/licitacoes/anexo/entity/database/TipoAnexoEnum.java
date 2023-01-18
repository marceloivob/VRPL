package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database;

/**
 * RN 579357: DocumentosOrcamentarios-ManterAnexo-RN-TipoAnexoManterAnexo-VRPL
 */
public enum TipoAnexoEnum {
    ATA_HOMOLOGACAO_LICITACAO("Ata de Homologação da Licitação") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    DESPACHO_ADJUDICACAO("Despacho de Adjudicação") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    RESUMO_EDITAL("Publicação do Resumo do Edital") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    QUADRO_RESUMO("Quadro Resumo") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    OUTROS("Outros") {
        @Override
        public String getDescription() {
            return this.description;
        }
    },
    VRPL("Anexo Parecer") {
    	@Override
        public String getDescription() {
            return this.description;
        }
    },
    VRPLS("Anexo Parecer Social") {
    	@Override
        public String getDescription() {
            return this.description;
        }
    };

    protected String description;

    public abstract String getDescription();

    TipoAnexoEnum(String description) {
        this.description = description;
    }

}
