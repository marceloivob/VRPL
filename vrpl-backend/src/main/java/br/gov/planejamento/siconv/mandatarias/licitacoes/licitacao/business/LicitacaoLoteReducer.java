package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import java.util.Map;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

public class LicitacaoLoteReducer implements LinkedHashMapRowReducer<Long, LicitacaoBD> {

    @Override
    public void accumulate(Map<Long, LicitacaoBD> container, RowView rowView) {

        LicitacaoBD licitacao = container.computeIfAbsent(rowView.getColumn("lic_id", Long.class),
                id -> rowView.getRow(LicitacaoBD.class));

        LoteBD lote = null;
        if (rowView.getColumn("lote_id", Long.class) == null) {
            lote = new LoteBD();
        } else {
            lote = licitacao.addLote(rowView.getRow(LoteBD.class));
        }

        SubmetaBD submetaBD = null;
        if (rowView.getColumn("submeta_id", Long.class) == null) {
            submetaBD = new SubmetaBD();
        } else {
            submetaBD = lote.addSubmeta(rowView.getRow(SubmetaBD.class));
        }

        if (rowView.getColumn("meta_id", Long.class) != null) {
            submetaBD.setMetaBD(rowView.getRow(MetaBD.class));
        }

        if (rowView.getColumn("for_id", Long.class) != null) {
            FornecedorBD fornecedor = rowView.getRow(FornecedorBD.class);
            if(!licitacao.getFornecedores().contains(fornecedor)) {
                licitacao.getFornecedores().add(fornecedor);
            }
        }

    }

}
