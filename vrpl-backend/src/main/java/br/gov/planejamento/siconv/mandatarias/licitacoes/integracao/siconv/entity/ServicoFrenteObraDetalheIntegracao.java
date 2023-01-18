package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class ServicoFrenteObraDetalheIntegracao {
		
	@ColumnName("servico_fk")
    private Long servicoFk;
		
	@ColumnName("frente_obra_fk")
    private Long frenteObraFk; 
	
	@ColumnName("qt_itens")
    private BigDecimal qtItens;
    
    @ColumnName("nr_frente_obra")
    private Integer nrFrenteObra;
    
    @ColumnName("nm_frente_obra")
	private String nmFrenteObra;
}
