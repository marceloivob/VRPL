package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PoMacroServicoServicosDTO {
	
	private List<MacroServicoServicosDTO> macroservicos;
	private BigDecimal totalGeralLicitado = BigDecimal.ZERO;
	private BigDecimal totalGeralAceitoNaAnalise = BigDecimal.ZERO;
	private BigDecimal totalGeralNaDataBaseDaLicitacao = BigDecimal.ZERO;
	private Long poId;
	
}
