package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database;

import java.math.BigDecimal;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class FrenteObraComDetalhesBD {
    
    @ColumnName("id_frente_obra")
    private Long idFrenteObra;
    
    @ColumnName("id_servico")
    private Long idServico;
    
    @ColumnName("nr_frente_obra")
    private Integer numeroFrenteObra;
    
    @ColumnName("nm_frente_obra")
    private String nomeFrenteObra;
    
    @ColumnName("qt_itens")
    private BigDecimal quantidadeItens;
}
