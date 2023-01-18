package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class UF {
    @ColumnName("id")
    private Long id;

    @ColumnName("sigla")
    private String sigla;

}
