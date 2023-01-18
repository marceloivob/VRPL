package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MacroServicoIntegracao {
    private Long id;
    private String txDescricao;
    private Long poFk;
	private Long nrMacroServico;
    private List<MacroServicoParcelaIntegracao> parcelas = new ArrayList<>();

    public void addParcela(MacroServicoParcelaIntegracao parcela) {
        this.parcelas.add(parcela);
    }

}
