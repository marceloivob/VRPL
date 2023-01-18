package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class MacroServicoCFFDTO {

	private Long idPO;
	private Long id;
	private String txDescricao;
	private Long nrMacroServico;
	private BigDecimal precoMacroservico;
	private List<MacroServicoParcelaDTO> parcelas;

}
