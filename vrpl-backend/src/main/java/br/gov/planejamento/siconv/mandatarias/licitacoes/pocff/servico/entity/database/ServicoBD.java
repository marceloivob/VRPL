package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.TipoFonteEnum;
import lombok.Data;

@Data
public class ServicoBD {

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
    
    @ColumnName("vl_custo_unitario_database")
	private BigDecimal vlCustoUnitarioDatabase;

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
	private Long versaoNr;

	@ColumnName("versao")
	private Long versao;

    @ColumnName("versao_id")
	private String versaoId;

    @ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    public static ServicoBD from(ServicoIntegracao servicoIntegracao) {
        ServicoBD servicoBD = new ServicoBD();
        servicoBD.setIdServicoAnalise(servicoIntegracao.getId());
        servicoBD.setTxObservacao(servicoIntegracao.getTxObservacao());
        servicoBD.setNrServico(servicoIntegracao.getNrServico());
        servicoBD.setCdServico(servicoIntegracao.getCdServico());
        servicoBD.setTxDescricao(servicoIntegracao.getTxDescricao());
        servicoBD.setSgUnidade(servicoIntegracao.getSgUnidade());
        servicoBD.setVlCustoUnitarioRef(servicoIntegracao.getVlCustoUnitarioRef());
        servicoBD.setPcBdi(servicoIntegracao.getPcBdi());
        servicoBD.setPcBdiLicitado(servicoIntegracao.getPcBdi());
        servicoBD.setQtTotalItensAnalise(servicoIntegracao.getQtTotalItens());
        servicoBD.setVlCustoUnitario(servicoIntegracao.getVlCustoUnitario());
        servicoBD.setVlCustoUnitarioDatabase(servicoIntegracao.getVlCustoUnitarioDatabase());
        servicoBD.setVlPrecoUnitarioLicitado(servicoIntegracao.getVlPrecoUnitario());
        servicoBD.setVlPrecoUnitario(servicoIntegracao.getVlPrecoUnitario());
        servicoBD.setVlPrecoTotal(servicoIntegracao.getVlPrecoTotal());
        servicoBD.setInFonte(servicoIntegracao.getInFonte());

        return servicoBD;
    }

    public ServicoDTO converterParaDTO() {
        ServicoDTO retorno = new ServicoDTO();
        retorno.setCdServico(cdServico);
        retorno.setEventoFk(eventoFk);
        retorno.setId(id);
        retorno.setInFonte(TipoFonteEnum.fromSigla(inFonte).getDescricao());
        retorno.setMacroServicoFk(macroServicoFk);
        retorno.setNrServico(nrServico);
		retorno.setNrVersao(versaoNr);
        retorno.setPcBdi(pcBdi);
        retorno.setPcBdiLicitado(pcBdiLicitado);
        retorno.setQtTotalItensAnalise(qtTotalItensAnalise);
        retorno.setSgUnidade(sgUnidade);
        retorno.setTxDescricao(txDescricao);
        retorno.setTxObservacao(txObservacao);
		retorno.setVersao(versao);
        retorno.setVlCustoUnitario(vlCustoUnitario);
        retorno.setVlCustoUnitarioDataBase(vlCustoUnitarioDatabase);
        retorno.setVlCustoUnitarioRef(vlCustoUnitarioRef);
		retorno.setVlPrecoUnitarioAceitoNaAnalise(vlPrecoUnitario);
		retorno.setVlPrecoUnitarioDaLicitacao(vlPrecoUnitarioLicitado);
		retorno.setVlPrecoTotalAceitoNaAnalise(vlPrecoTotal);
		
        return retorno;
    }
}
