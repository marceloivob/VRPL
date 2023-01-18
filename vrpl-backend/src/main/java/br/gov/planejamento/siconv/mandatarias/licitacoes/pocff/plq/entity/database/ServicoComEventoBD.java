package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class ServicoComEventoBD {

    @NotNull
    @ColumnName("macro_servico_fk")
    private Long macroServicoFk;

    @NotNull
    @ColumnName("vl_preco_total_analise")
    private BigDecimal vlPrecoTotal;

    @NotNull
    @ColumnName("vl_preco_unitario_licitado")
    private BigDecimal vlPrecoUnitarioLicitado;

    @ColumnName("evento_fk")
    private Long eventoFk;

    @NotNull
    @ColumnName("pc_bdi_analise")
    private BigDecimal pcBdi;

    @NotNull
    @ColumnName("pc_bdi_licitado")
    private BigDecimal pcBdiLicitado;

    @NotNull
    @ColumnName("vl_preco_unitario_analise")
    private BigDecimal vlPrecoUnitario;

    @ColumnName("tx_observacao")
    private String txObservacao;

    @NotNull
    @ColumnName("in_fonte")
    private String inFonte;

    @NotNull
    @ColumnName("vl_custo_unitario_ref_analise")
    private BigDecimal vlCustoUnitarioRef;

    @NotNull
    @ColumnName("vl_custo_unitario_analise")
    private BigDecimal vlCustoUnitario;

    @NotNull
    @ColumnName("qt_total_itens_analise")
    private BigDecimal qtTotalItensAnalise;

    @NotNull
    @ColumnName("cd_servico")
    private String cdServico;

    @NotNull
    @ColumnName("tx_descricao")
    private String txDescricao;

    @NotNull
    @ColumnName("sg_unidade")
    private String sgUnidade;

    @ColumnName("id")
    private Long id;

    @ColumnName("id_servico_analise")
    private Long idServicoAnalise;

    @NotNull
    @ColumnName("nr_servico")
    private Integer nrServico;

    @ColumnName("versao_nr")
    @NotNull
    private Long versaoNr;

	@ColumnName("versao")
    private Long versao;

    @ColumnName("versao_id")
    private String versaoId;

    @ColumnName("versao_nm_evento")
    private String versaoNmEvento;
    
    @ColumnName("evento_numero")
    private Integer numeroEvento;
    
    @ColumnName("evento_nome")
    private String eventoNome;
    

}
