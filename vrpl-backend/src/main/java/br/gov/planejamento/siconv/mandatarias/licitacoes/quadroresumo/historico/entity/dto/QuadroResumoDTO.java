package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class QuadroResumoDTO {

	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeEnviarParaAnalise;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeIniciarComplementacao;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeCancelarEnvio;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeCancelarEnvioComplementacao;

	
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeIniciarAnalise;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeSolicitarComplementacao;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeAceitarDocumentacao;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeRejeitarDocumentacao;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeCancelarSolicitacaoComplementacao;
	
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeCancelarAceite;
	@JsonInclude(value = Include.NON_DEFAULT)
	private boolean podeCancelarRejeite;

	private List<HistoricoLicitacaoDTO> listaHistorico;
}
