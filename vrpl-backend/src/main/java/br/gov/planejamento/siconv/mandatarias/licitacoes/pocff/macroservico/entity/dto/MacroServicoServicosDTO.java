package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import lombok.Data;

@Data
public class MacroServicoServicosDTO {

	private MacroServicoDTO macroServico;
	private List<ServicoDTO> servicos;
	private BigDecimal precoTotalLicitado = BigDecimal.ZERO;
	private BigDecimal precoTotalAnalise = BigDecimal.ZERO;
	private BigDecimal precoTotalAceitoNaAnalise = BigDecimal.ZERO;
	private BigDecimal precoTotalNaDataBaseDaLicitacao = BigDecimal.ZERO;
	private Long submetaId;
	
}
