package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

public enum Stage {
	LOCAL, DEVELOPMENT, TEST, INTEGRATION_TEST, HOMOLOGACAO, PRODUCAO;

    public static Stage fromSystemStage(String systemStage) {
        if ("desenv".equalsIgnoreCase(systemStage)) {
            return DEVELOPMENT;
        } else if ("homologacao".equalsIgnoreCase(systemStage)) {
			return HOMOLOGACAO;
        } else {
            return Stage.valueOf(systemStage.toUpperCase());
        }
    }
}
