package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class ServicoFrenteObraAnaliseBD {
    
    @NotNull
    @ColumnName("id")
    private Long id;
    
    @NotNull  
    @ColumnName("servico_fk")
    private Long servicoFk;
    
    @NotNull   
    @ColumnName("qt_itens")
    private BigDecimal qtItens;
	
    @NotNull
    @ColumnName("nr_frente_obra")
    private Integer nrFrenteObra;

    @NotNull  
    @ColumnName("nm_frente_obra")
    private String nmFrenteObra;
 

    @ColumnName("versao_nr")
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;   

}
