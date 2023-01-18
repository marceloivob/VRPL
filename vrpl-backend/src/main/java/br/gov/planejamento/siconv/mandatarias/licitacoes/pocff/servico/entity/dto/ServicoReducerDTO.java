package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import lombok.Data;

@Data
public class ServicoReducerDTO {

    @NotNull
    @ColumnName("macro_servico_fk")
	private Long macroServicoFk;

	private BigDecimal vlPrecoTotal;

    @NotNull
    @ColumnName("vl_preco_unitario_licitado")
	private BigDecimal vlPrecoUnitarioLicitado;

    @ColumnName("evento_fk")
	private Long eventoFk;


    private BigDecimal pcBdi;

    @NotNull
    @ColumnName("pc_bdi_licitado")
	private BigDecimal pcBdiLicitado;

	private BigDecimal vlPrecoUnitario;

    @ColumnName("tx_observacao")
	private String txObservacao;

    @NotNull
    @ColumnName("in_fonte")
	private String inFonte;

	private BigDecimal vlCustoUnitarioRef;

	private BigDecimal vlCustoUnitario;
	
	private BigDecimal vlCustoUnitarioDatabase;

    @NotNull
	private BigDecimal qtTotalItensAnalise;

    @NotNull
	private String cdServico;

    @NotNull
	private String txDescricao;

    @NotNull
	private String sgUnidade;

	private Long id;

	private Long idServicoAnalise;

    @NotNull
	private Integer nrServico;

	private Long versaoNr;

	private Long versao;

	private String versaoId;

	private String versaoNmEvento;

    private BigDecimal custoUnitarioDataBase;

    private List<ServicoFrenteObraDTO> frentesObra = new ArrayList<>();

    private List<ServicoFrenteObraAnaliseDTO> frentesObraAnalise = new ArrayList<>();

    public ServicoDTO converterParaDTO() {
        ServicoDTO retorno = new ServicoDTO();
        retorno.setId(id);
        retorno.setCdServico(cdServico);
        retorno.setEventoFk(eventoFk);
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
        retorno.setVlCustoUnitarioRef(vlCustoUnitarioRef);
        retorno.setVlCustoUnitarioDataBase(vlCustoUnitarioDatabase);
		retorno.setVlPrecoUnitarioAceitoNaAnalise(vlPrecoUnitario);
		retorno.setVlPrecoUnitarioDaLicitacao(vlPrecoUnitarioLicitado);
		retorno.setVlPrecoTotalAceitoNaAnalise(vlPrecoTotal);
		retorno.setCustoUnitarioDataBase(custoUnitarioDataBase);
		retorno.setFrentesObra(frentesObra);
		retorno.setFrentesObraAnalise(frentesObraAnalise);

        return retorno;
    }
}
