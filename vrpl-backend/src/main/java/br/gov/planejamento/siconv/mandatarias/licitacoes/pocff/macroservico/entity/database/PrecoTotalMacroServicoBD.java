package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database;

import java.math.BigDecimal;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class PrecoTotalMacroServicoBD {

    @ColumnName("id")
    public Long macroservicoFK;

    @ColumnName("preco_total")
    public BigDecimal precoTotal;

}
