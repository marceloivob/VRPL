package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.RegimeExecucaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaQciVRPLDTO;
import lombok.Data;

@Data
public class SubmetaBD {

    private Long id;

    @NotNull
    private Long idSubmetaAnalise;

    @ColumnName("vrpl_lote_licitacao_fk")
    private Long vrplLoteLicitacaoFk;

    @NotNull
    @ColumnName("meta_fk")
    private Long idMeta;

    @NotNull
    @ColumnName("proposta_fk")
    private Long idProposta;

    @NotNull
    @ColumnName("in_regime_execucao")
    private RegimeExecucaoEnum regimeExecucao;

    @NotNull
    private BigDecimal vlRepasse = BigDecimal.ZERO;

    @NotNull
    private BigDecimal vlContrapartida = BigDecimal.ZERO;

    @NotNull
    private BigDecimal vlOutros = BigDecimal.ZERO;

    @NotNull
    @ColumnName("vl_total_licitado")
    private BigDecimal vlTotalLicitado = BigDecimal.ZERO;

    @ColumnName("in_situacao")
    private String situacao;

    @NotNull
    @ColumnName("tx_descricao_analise")
    private String descricao;

    @NotNull
    @ColumnName("nr_lote_analise")
    private Long lote;

    @NotNull
    private BigDecimal vlRepasseAnalise;

    @NotNull
    private BigDecimal vlContrapartidaAnalise;

    @NotNull
    private BigDecimal vlOutrosAnalise;

    @NotNull
    private BigDecimal vlTotalAnalise;

    @NotNull
    @ColumnName("nr_submeta_analise")
    private Long numero;

    @NotNull
    @ColumnName("in_situacao_analise")
    private String situacaoAnalise;

    @NotNull
    @ColumnName("in_regime_execucao_analise")
    private String regimeExecucaoAnalise;

    @ColumnName("natureza_despesa_sub_it_fk_analise")
    private Long itemPad;

    @NotNull
    private Long versao;

	@ColumnName("versao_nr")
	@NotNull
	private Integer numeroVersao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    private MetaBD metaBD;

    private Boolean inAcompanhamentoEventos; // info associar licitacao lote

	/**
	 * Construtor Padrão
	 */
	public SubmetaBD() {
		// noop
	}

    public static SubmetaBD from(SubmetaIntegracao submetaIntegracao) {
        SubmetaBD submetaBD = new SubmetaBD();
        submetaBD.setIdSubmetaAnalise(submetaIntegracao.getId());
        submetaBD.setDescricao(submetaIntegracao.getDescricao());
        submetaBD.setLote(submetaIntegracao.getNumeroLote());
        submetaBD.setVlRepasse(submetaIntegracao.getValorRepasseAnalise());
        submetaBD.setVlContrapartida(submetaIntegracao.getValorContrapartidaAnalise());
        submetaBD.setVlOutros(submetaIntegracao.getValorOutrosAnalise());
        submetaBD.setVlTotalLicitado(submetaIntegracao.getValorAceitoNaAnalise());
        submetaBD.setVlRepasseAnalise(submetaIntegracao.getValorRepasseAnalise());
        submetaBD.setVlContrapartidaAnalise(submetaIntegracao.getValorContrapartidaAnalise());
        submetaBD.setVlOutrosAnalise(submetaIntegracao.getValorOutrosAnalise());
        submetaBD.setVlTotalAnalise(submetaIntegracao.getValorAceitoNaAnalise());
        submetaBD.setNumero(submetaIntegracao.getNumero());
        submetaBD.setRegimeExecucaoAnalise(submetaIntegracao.getRegimeExecucao().name());
        submetaBD.setItemPad(submetaIntegracao.getNaturezaDespesaFk());
        submetaBD.setSituacaoAnalise(submetaIntegracao.getSituacaoAnalise());
        submetaBD.setSituacao(submetaIntegracao.getSituacao());
        submetaBD.setRegimeExecucao(submetaIntegracao.getRegimeExecucao());

        return submetaBD;
    }


	public SubmetaBD(SubmetaQciVRPLDTO submeta) {
		this.id = submeta.getIdSubmeta();
		this.idMeta = submeta.getIdMeta();

		this.vlContrapartida = submeta.getVlContrapartida() == null ? BigDecimal.ZERO : submeta.getVlContrapartida();
		this.vlOutros = submeta.getVlOutros() == null ? BigDecimal.ZERO : submeta.getVlOutros();
		this.vlRepasse = submeta.getVlRepasse() == null ? BigDecimal.ZERO : submeta.getVlRepasse();
		this.versao = submeta.getVersao();
	}

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SubmetaBD submetaBD = (SubmetaBD) o;
        return Objects.equals(id, submetaBD.id) && Objects.equals(idSubmetaAnalise, submetaBD.idSubmetaAnalise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idSubmetaAnalise);
    }
    
}
