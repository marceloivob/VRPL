package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ServicoFrenteObraIntegracao {
    private Long servicoFk;
    private Long frenteObraFk;
    private BigDecimal qtItens;
}
