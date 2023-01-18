package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import lombok.Data;

@Data
public class SubitemInvestimentoIntegracao {
    private Long id;
    private String descricao;
    private String inProjetoSocial;
    private String codigoUnd;
    private String descricaoUnd;
}
