package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.CPF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import lombok.Data;

@Data
public class HistoricoLicitacaoDTO {

	private Long id;
	private Long versaoDoHistorico;

	private Long identificadorDaLicitacao;
	private Long versaoDaLicitacao;

	private EventoQuadroResumoEnum eventoGerador;
	private SituacaoLicitacaoEnum situacaoDaLicitacao;
	private String consideracoes;
	private Date dataDeRegistro;
	private String nomeDoResponsavel;
	private CPF cpfDoResponsavel;
	
	private String papelDoResponsavel;

	@JsonIgnore
	public boolean isValid() {
		return (this.getEventoGerador() != null && this.getIdentificadorDaLicitacao() != null);
	}

	public void setEventoGerador(String sigla) {
		this.eventoGerador = EventoQuadroResumoEnum.fromSigla(sigla);
	}

	public void setSituacaoDaLicitacao(String sigla) {
		this.situacaoDaLicitacao = SituacaoLicitacaoEnum.fromSigla(sigla);
	}
}
