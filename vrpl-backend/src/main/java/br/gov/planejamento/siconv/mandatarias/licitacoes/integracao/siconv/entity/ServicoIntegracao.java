package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicoIntegracao {
    private Long id;
    private String txObservacao;
    private Long macroServicoFk;
    private Integer nrServico;
    private String cdServico;
    private String txDescricao;
    private String sgUnidade;
    private BigDecimal vlCustoUnitarioRef;
    private BigDecimal pcBdi;
    private BigDecimal qtTotalItens;
    private BigDecimal vlCustoUnitario;
    private BigDecimal vlCustoUnitarioDatabase;
    private BigDecimal vlPrecoUnitario;
    private BigDecimal vlPrecoTotal;
    private Long eventoFk;
    private String inFonte;

}
