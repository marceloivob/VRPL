package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MetaIntegracao {
    private Long id;
    private String descricao;
	private BigDecimal qtItens;
    private Long subitemInvestimentoFk;
    private Long numero;
    private Boolean social;

    // campos para relatório qci da integração
    private String subitemDescricao;
    private String subitemTipoProjetoSocial;
    private String subitemCodigoUnidade;

    private List<SubmetaIntegracao> submetas = new ArrayList<>();

    public void addSubmeta(SubmetaIntegracao submeta) {
        this.submetas.add(submeta);
    }
}
