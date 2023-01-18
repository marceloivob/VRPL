package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.EventoFrenteObraDTO;
import lombok.Data;

@Data
public class EventoFrenteObraBD {

    @NotNull
    @ColumnName("frente_obra_fk")
	private Long frenteObraFk;

    @ColumnName("evento_fk")
	private Long eventoFk;

    @NotNull
    @ColumnName("nr_mes_conclusao")
	private Integer nrMesConclusao;

	@ColumnName("versao_nr")
	private Long versaoNr;

	@ColumnName("versao")
	private Long versao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    public EventoFrenteObraDTO converterParaDTO() {
        EventoFrenteObraDTO dto = new EventoFrenteObraDTO();
        dto.setEventoFk(this.getEventoFk());
        dto.setFrenteObraFk(this.getFrenteObraFk());
        dto.setNrMesConclusao(this.getNrMesConclusao());
        return dto;
    }
}
