package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class EventoFrenteObraDetalheBD {
    
    @NotNull
    @ColumnName("id_evento")
	private Long idEvento;

    @ColumnName("nm_evento")
	private String nomeEvento;

    @ColumnName("nr_evento")
	private Integer numeroEvento;

    @NotNull
    @ColumnName("id_frente_obra")
	private Long idFrenteObra;

    @ColumnName("nm_frente_obra")
	private String nomefrenteObra;

    @ColumnName("nr_frente_obra")
	private Integer numerofrenteObra;
    
    @ColumnName("versao_frente_obra")
	private Long versaoFrenteObra;

    @ColumnName("nr_mes_conclusao")
	private Integer numeroMesConclusao;
    
    @ColumnName("versao")
    private Long versao;

}
