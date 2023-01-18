package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto;

import java.math.BigDecimal;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import lombok.Data;

@Data
public class MacroServicoParcelaDTO {

	private Long id;
	private Long macroServicoFk;
	private Integer nrParcela;
	private BigDecimal pcParcela;
	private Long nrVersao;
	private Long versao;

	public MacroServicoParcelaBD converterParaBD() {
		// implementar conversao
		MacroServicoParcelaBD mspBD = new MacroServicoParcelaBD();
		mspBD.setId(this.id);
		mspBD.setMacroServicoFk(this.macroServicoFk);
		mspBD.setNrParcela(this.nrParcela);
		mspBD.setPcParcela(this.pcParcela);
		mspBD.setNumeroVersao(this.nrVersao);
		mspBD.setVersao(this.getVersao());

		return mspBD;
	}
}
