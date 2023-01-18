package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto;

import lombok.Data;

@Data
public class FrenteDeObraCFFcomEventosDTO {
    Long idFrenteObra;
    Integer numeroFrenteObra;
    String nomeFrenteObra;
    Integer mesConclusao;
    Long versao;
}
