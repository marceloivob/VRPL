package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoDTO;
import lombok.Data;

@Data
public class MacroServicoBD {

	@NotNull
	@ColumnName("tx_descricao")
	private String txDescricao;

	@ColumnName("id")
	private Long id;

	@ColumnName("id_macro_servico_analise")
	private Long idMacroServicoAnalise;

	@NotNull
	@ColumnName("po_fk")
	private Long poFk;

	@NotNull
	@ColumnName("nr_macro_servico")
	private Long nrMacroServico;

	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

	@ColumnName("versao")
	private Long versao;
	
	public MacroServicoDTO converterParaDTO() {
		MacroServicoDTO retorno = new MacroServicoDTO();
		retorno.setId(id);
		retorno.setNrMacroServico(nrMacroServico);
		retorno.setNrVersao(versaoNr);
		retorno.setPoFk(poFk);
		retorno.setTxDescricao(txDescricao);
		retorno.setVersao(versao);
		retorno.setVersaoNr(versaoNr);

		return retorno;
	}
	
	public String getTxIdentificador() {
		return this.getNrMacroServico() + " - " + this.getTxDescricao();
	}
}
