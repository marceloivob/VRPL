package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class EventoBD {

    @ColumnName("id")
	private Long id;

    @NotNull
    @ColumnName("nm_evento")
    @Size(max = 100)
	private String nomeEvento;

    @NotNull
    @ColumnName("po_fk")
	private Long idPo;

    @NotNull
    @Max(value = 999, message="Número do Evento não pode ser superior a 999")
    @Min(value = 1, message="Número do Evento não pode ser inferior a 1")
    @ColumnName("nr_evento")
	private Integer numeroEvento;

    @Max(32767)
	@Min(0)
	@ColumnName("versao")
    @NotNull
	private Long versao;

	@ColumnName("versao_nr")
	private Integer numeroVersao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    // transient utilizado na importação
    private Long idAnalise;
}
