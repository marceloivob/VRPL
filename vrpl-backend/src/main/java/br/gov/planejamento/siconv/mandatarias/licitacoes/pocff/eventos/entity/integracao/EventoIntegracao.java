package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.integracao;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class EventoIntegracao {

    @ColumnName("id")
    public Long id;

    @ColumnName("nm_evento")
    public String nomeEvento;

    @ColumnName("po_fk")
    public Long idPo;

    @ColumnName("nr_evento")
    public Integer numeroEvento;
}
