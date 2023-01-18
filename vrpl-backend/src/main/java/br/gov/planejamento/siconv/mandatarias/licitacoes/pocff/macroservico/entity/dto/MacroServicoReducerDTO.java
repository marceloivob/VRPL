package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoReducerDTO;
import lombok.Data;

@Data
public class MacroServicoReducerDTO {

	@NotNull
	@ColumnName("tx_descricao")
	private String txDescricao;

	@ColumnName("id")
	private Long id;

	@NotNull
	@ColumnName("po_fk")
	private Long poFk;

	@NotNull
	@ColumnName("nr_macro_servico")
	private Long nrMacroServico;

	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	private List<ServicoReducerDTO> servicos = new ArrayList<>();

	public MacroServicoDTO convert() {
		MacroServicoDTO dto = new MacroServicoDTO();
		dto.setId(this.getId());
		dto.setNrMacroServico(this.getNrMacroServico());
		dto.setPoFk(this.getPoFk());
		dto.setTxDescricao(this.getTxDescricao());
		dto.setVersao(this.getVersao());
		dto.setVersaoNr(this.getVersaoNr());
		dto.setNrVersao(this.getVersaoNr());
		
		return dto;
	}

	public List<ServicoDTO> convertListaServicos() {
		
		return servicos.stream().map( ServicoReducerDTO::converterParaDTO ).collect(Collectors.toList());
	}
	
}
