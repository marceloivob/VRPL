package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class SubItemInvestimento {

    @ColumnName("id")
    private Long id;

    @ColumnName("descricao")
    private String descricao;

    @ColumnName("in_tipo_projeto_social")
    private String tipoProjetoSocial;

    @ColumnName("codigo")
    private String codigoUnidade;
}
