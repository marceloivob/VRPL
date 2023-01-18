package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business;

import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

public class MetaBDReducer implements LinkedHashMapRowReducer<Long, MetaBD> {

	@Override
	public void accumulate(Map<Long, MetaBD> container, RowView rowView) {
		MetaBD meta = container.computeIfAbsent(rowView.getColumn("meta_id", Long.class),
				id -> rowView.getRow(MetaBD.class));

		if (rowView.getColumn("submeta_id", Long.class) != null) {
			SubmetaBD submetaBD = rowView.getRow(SubmetaBD.class);

			meta.addSubmeta(submetaBD);
		}
		

		if (rowView.getColumn("item_id", Long.class) != null) {
			SubitemInvestimentoBD submetaBD = rowView.getRow(SubitemInvestimentoBD.class);

			meta.setSubitem(submetaBD);
		}
		
	}

}
