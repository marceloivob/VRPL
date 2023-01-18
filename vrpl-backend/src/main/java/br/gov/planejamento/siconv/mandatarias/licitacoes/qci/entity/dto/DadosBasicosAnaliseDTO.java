package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DadosBasicosAnaliseDTO {
	
    private BigDecimal valorGlobal = BigDecimal.ZERO;

    private BigDecimal valorRepasse = BigDecimal.ZERO;;

    private BigDecimal valorContrapartida = BigDecimal.ZERO;
    
    private String modalidade;

}
