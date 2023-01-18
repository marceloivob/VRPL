package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;

public class LicitacaoReducer implements LinkedHashMapRowReducer<Long, LicitacaoBD> {

	@Override
    public void accumulate(Map<Long, LicitacaoBD> container, RowView rowView) {

        LicitacaoBD licitacao = container.computeIfAbsent(rowView.getColumn("lic_id", Long.class),
                id -> rowView.getRow(LicitacaoBD.class));

        if (rowView.getColumn("lote_id", Long.class) != null) {
            licitacao.addLote(rowView.getRow(LoteBD.class));
        }
	}
}
