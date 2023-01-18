package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class EventoCFFcomEventosDTO {
    Long idEvento;
    Integer numeroEvento;
    String tituloEvento;
    List<FrenteDeObraCFFcomEventosDTO> listaFrenteObras;
    Long versao;
}
