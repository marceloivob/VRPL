package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoParcelaIntegracao;
import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.Map;

public class MacroServicoReducer  implements LinkedHashMapRowReducer<Long, MacroServicoIntegracao> {

    @Override
    public void accumulate(Map<Long, MacroServicoIntegracao> container, RowView rowView) {
        MacroServicoIntegracao macroServico = container.computeIfAbsent(
                        rowView.getColumn("servico_id", Long.class),
                        id -> rowView.getRow(MacroServicoIntegracao.class));

        if (rowView.getColumn("parcela_id", Long.class) != null) {
            macroServico.addParcela(rowView.getRow(MacroServicoParcelaIntegracao.class));
        }
    }
}