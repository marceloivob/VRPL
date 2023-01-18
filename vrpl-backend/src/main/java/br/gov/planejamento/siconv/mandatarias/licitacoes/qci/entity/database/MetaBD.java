package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import lombok.Data;

@Data
public class MetaBD {

    @ColumnName("id")
    private Long id;

    @NotNull
    @ColumnName("id_meta_analise")
    private Long idMetaAnalise;

    @ColumnName("tx_descricao_analise")
    private String descricao;

    @ColumnName("qt_itens_analise")
    private BigDecimal quantidade;

    @ColumnName("nr_meta_analise")
    private Long numero;

    @ColumnName("in_social")
    private Boolean social;

    @ColumnName("subitem_fk")
    private Long subItemFk;

    @ColumnName("versao_nr")
    @NotNull
    private Integer numeroVersao;

	@ColumnName("versao")
	private Long versao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    private List<SubmetaBD> submetas = new ArrayList<>();
    
    private SubitemInvestimentoBD subitem;

    public void addSubmeta(SubmetaBD submeta) {
        this.submetas.add(submeta);
    }

    public static MetaBD from(MetaIntegracao metaIntegracao) {
        MetaBD meta = new MetaBD();
        meta.setIdMetaAnalise(metaIntegracao.getId());
        meta.setDescricao(metaIntegracao.getDescricao());
        meta.setQuantidade(metaIntegracao.getQtItens());
        meta.setNumero(metaIntegracao.getNumero());
        meta.setSocial(metaIntegracao.getSocial());

        return meta;
    }
}
