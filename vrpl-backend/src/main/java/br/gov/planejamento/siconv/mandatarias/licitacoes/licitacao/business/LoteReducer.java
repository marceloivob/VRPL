package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Map;

public class LoteReducer implements LinkedHashMapRowReducer<Long, LoteBD> {

    @Override
    public void accumulate(Map<Long, LoteBD> container, RowView rowView) {

        LoteBD lote = container.computeIfAbsent(rowView.getColumn("lote_id", Long.class),
                id -> rowView.getRow(LoteBD.class));

        SubmetaBD submetaBD = null;
        if (rowView.getColumn("submeta_id", Long.class) == null) {
            submetaBD = new SubmetaBD();
        } else {
            submetaBD = lote.addSubmeta(rowView.getRow(SubmetaBD.class));
        }

        if (rowView.getColumn("meta_id", Long.class) != null) {
            submetaBD.setMetaBD(rowView.getRow(MetaBD.class));
        }

    }

}