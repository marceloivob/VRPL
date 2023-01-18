package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MacroServicoPLQDTO {
    
    private Long id;
	private Long numero;
    private String descricao;
    private BigDecimal precoTotalLicitado = new BigDecimal(0);
    private List<ServicoPLQDTO> servicos;
}
