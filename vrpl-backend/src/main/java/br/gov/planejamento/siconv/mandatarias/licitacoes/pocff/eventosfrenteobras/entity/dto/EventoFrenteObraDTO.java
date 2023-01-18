package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto;

import lombok.Data;

@Data
public class EventoFrenteObraDTO {

	private Long frenteObraFk;

	private Long eventoFk;

	private Integer nrMesConclusao;
	
	private Long versao;

}
