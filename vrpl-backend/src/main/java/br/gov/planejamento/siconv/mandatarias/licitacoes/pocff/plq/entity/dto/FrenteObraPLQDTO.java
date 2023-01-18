package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class FrenteObraPLQDTO {
    
    private Long id;
    private Long servicoFK;
    private Integer numero;
    private String descricao;
    private BigDecimal quantidade = new BigDecimal(0);
    private BigDecimal valorLicitado = new BigDecimal(0); 
}
