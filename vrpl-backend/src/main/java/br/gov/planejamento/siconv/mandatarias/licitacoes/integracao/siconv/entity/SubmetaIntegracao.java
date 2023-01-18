package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;

import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.RegimeExecucaoEnum;
import lombok.Data;

@Data
public class SubmetaIntegracao {
    private Long id;
    private Long numero;
    private Long idMeta;
    private String descricao;
    private Long numeroLote;
    private String situacao;
    private String situacaoAnalise;
    private RegimeExecucaoEnum regimeExecucao;
    private Long naturezaDespesaFk;
    private BigDecimal valorRepasse;
    private BigDecimal valorContrapartida;
    private BigDecimal valorOutros;
    private BigDecimal valorTotal;
    private BigDecimal valorRepasseAnalise;
    private BigDecimal valorContrapartidaAnalise;
    private BigDecimal valorOutrosAnalise;
    private BigDecimal valorAceitoNaAnalise;

}
