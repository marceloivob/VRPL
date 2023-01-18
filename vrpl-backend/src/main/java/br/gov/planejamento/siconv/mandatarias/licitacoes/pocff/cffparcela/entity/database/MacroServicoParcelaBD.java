package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoParcelaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.MacroServicoParcelaDTO;
import lombok.Data;

@Data
public class MacroServicoParcelaBD {

    @NotNull
    @ColumnName("macro_servico_fk")
	private Long macroServicoFk;

    @NotNull
    @ColumnName("nr_parcela")
	private Integer nrParcela;

    @NotNull
    @ColumnName("pc_parcela")
	private BigDecimal pcParcela;

    @ColumnName("id")
	private Long id;
    
    @NotNull
    @ColumnName("versao")
	private Long versao = 1L;
    
	@ColumnName("versao_nr")
	private Long numeroVersao;
	
	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    public static MacroServicoParcelaBD from(MacroServicoParcelaIntegracao parcelaIntegracao) {
        MacroServicoParcelaBD parcelaBD = new MacroServicoParcelaBD();
        parcelaBD.setNrParcela(parcelaIntegracao.getNrParcela());
        parcelaBD.setPcParcela(parcelaIntegracao.getPcParcela());
		parcelaBD.setMacroServicoFk(parcelaIntegracao.getMacroServicoFk());

        return parcelaBD;
    }

    public MacroServicoParcelaDTO converterParaDTO(){
        MacroServicoParcelaDTO dto = new MacroServicoParcelaDTO();
        dto.setId(this.id);
        dto.setMacroServicoFk(this.macroServicoFk);
        dto.setNrParcela(this.nrParcela);
        dto.setPcParcela(this.pcParcela);
		dto.setNrVersao(this.numeroVersao);
		dto.setVersao(this.versao);
        return dto;
    }
}
