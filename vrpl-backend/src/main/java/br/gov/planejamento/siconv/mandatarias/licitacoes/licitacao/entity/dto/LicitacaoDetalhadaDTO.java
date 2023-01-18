package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.RegimeExecucaoEnum;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class LicitacaoDetalhadaDTO {

    @JsonIgnore
    @Delegate
    private LicitacaoDTO licitacao;

    private CtefDTO ctef;

    private String descricaoProcessoExecucao;

    private LocalDate dataPublicacaoEdital;
    private BigDecimal valorTotalEdital;
    private FornecedorDTO fornecedor;
    private LocalDate dataHomologacao;

    private String regimeExecucao;
    private BigDecimal valorAprovadoFaseAnalise;
    
	private boolean existeParecer = false;
	private boolean existeParecerEmitido = false;

    private boolean inSocial = false;

    public LicitacaoDetalhadaDTO updateFrom(LicitacaoBD fromBD) {
        this.licitacao = new LicitacaoDTO().from(fromBD);

        // para detalhar sempre há lote e submeta associados
        if (!fromBD.getLotesAssociados().isEmpty()) {
            LoteBD loteBD = fromBD.getLotesAssociados().get(0);
            this.regimeExecucao = loteBD.getSubmetas().get(0).getRegimeExecucaoAnalise();
            this.regimeExecucao = RegimeExecucaoEnum.fromSigla(this.regimeExecucao).getDescricao();
        }

        this.valorAprovadoFaseAnalise = fromBD.getLotesAssociados().stream().map(LoteDTO::from)
				.map(LoteDTO::getValorAceitoAnalise).reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.setRegimeContratacao(fromBD.getRegimeContratacao());

        return this;
    }

    public LicitacaoDetalhadaDTO from(ProcessoExecucaoResponse detalheDaLicitacao) {
        this.descricaoProcessoExecucao = detalheDaLicitacao.getDescricaoProcesso();
		this.licitacao.setModalidade(detalheDaLicitacao.getModalidade());

        /**
         * Ver
         * https://stackoverflow.com/questions/3829029/how-to-convert-from-float-to-bigdecimal-in-java
         *
         *
         * Note: For values other float and double NaN and ±Infinity, this constructor
         * is compatible with the values returned by Float.toString(float) and
         * Double.toString(double). This is generally the preferred way to convert a
         * float or double into a BigDecimal, as it doesn't suffer from the
         * unpredictability of the BigDecimal(double) constructor.
         */
        this.valorTotalEdital = new BigDecimal(Double.toString(detalheDaLicitacao.getValor()));

        this.fornecedor = new FornecedorDTO().from(detalheDaLicitacao);

        if (isDataValida(detalheDaLicitacao.getDataPublicacaoEdital())) {
            this.dataPublicacaoEdital = LocalDate.parse(detalheDaLicitacao.getDataPublicacaoEdital());
        }

        if (isDataValida(detalheDaLicitacao.getDataHomologacao())) {
            this.dataHomologacao = LocalDate.parse(detalheDaLicitacao.getDataHomologacao());
        }

        return this;
    }

    private boolean isDataValida(String data) {
        return ((data != null) && (!data.isEmpty()));
    }

}
