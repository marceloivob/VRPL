package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import lombok.Data;

@Data
public class FornecedorIntegracao {
    private Long id;
    private Long idLicitacao;
    private String razaoSocial;
    private String tipoIdentificacao;
    private String identificacao;
}

