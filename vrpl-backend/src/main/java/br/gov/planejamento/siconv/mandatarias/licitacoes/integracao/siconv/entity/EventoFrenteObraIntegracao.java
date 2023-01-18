package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import lombok.Data;

@Data
public class EventoFrenteObraIntegracao {
    private Long eventoFk;
    private Long frenteObraFk;
    private Integer nrMesConclusao;

}
