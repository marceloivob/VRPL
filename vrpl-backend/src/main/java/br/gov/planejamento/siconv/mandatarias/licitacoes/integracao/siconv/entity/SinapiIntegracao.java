package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class SinapiIntegracao {
    private Long id;
    private String cdItem;
    private String sgLocalidade;
    private BigDecimal vlDesonerado;
    private BigDecimal vlNaoDesonerado;
    private String tpSinapi;
    private Date dtReferencia;
    private Date dtRegistro;
    private String sgUnidade;
    private String txDescricaoItem;
}
