package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database;

import java.math.BigDecimal;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;
@Data
public class TotalizadorMesBD {

    @ColumnName("total")
	private BigDecimal total;

    @ColumnName("mes")
	private Integer mes;

}


