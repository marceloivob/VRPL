package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MacroServicoParcelaIntegracao {
    private Long id;
    private Integer nrParcela;
    private BigDecimal pcParcela;
    private Long macroServicoFk;
}
