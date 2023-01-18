package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.dto;

import lombok.Data;

@Data
public class EventoDTO {

    private Long id;
    private Integer numeroEvento;
    private String tituloEvento;
    private Long idPO;
	private Long versao;
}
