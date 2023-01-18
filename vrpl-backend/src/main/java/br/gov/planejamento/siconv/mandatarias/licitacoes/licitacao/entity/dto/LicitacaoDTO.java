package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import lombok.Data;

@Data
public class LicitacaoDTO {

    private Long id;
    private BigDecimal valorProcesso;

    private Long idIntegracao;
    private String numeroAno;
	private String processoDeExecucao;
	private String regimeContratacao;
    private String modalidade;

    private String objeto;
    private String situacaoProcesso;

    private String situacaoVerificacao;

    private String situacaoDaLicitacao;
    private String situacaoDaLicitacaoDescricao;

    private Integer maiorVersao;
    private Boolean versaoAtual;
    private Integer numeroVersao;
	private Long versao;

    private List<LoteDTO> lotes = new ArrayList<>();
    private List<FornecedorDTO> fornecedores = new ArrayList<>();

    public BigDecimal getValorSomatorioSubmeta() {
        return lotes.stream()
                .map(LoteDTO::getValorAceitoAnalise)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getValorSomatorioSubmetaLicitado() {
        return lotes.stream()
                .map(LoteDTO::getValorVerificadoLicitado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static LicitacaoDTO from(LicitacaoBD fromBD) {
        LicitacaoDTO licitacao = new LicitacaoDTO();
        licitacao.id = fromBD.getId();
        licitacao.numeroAno = fromBD.getNumeroAno();
        licitacao.objeto = fromBD.getObjeto();
        licitacao.valorProcesso = fromBD.getValorProcessoLicitatorio();
        licitacao.idIntegracao = fromBD.getIdLicitacaoFk();
		licitacao.modalidade = fromBD.getModalidade();
		licitacao.processoDeExecucao = fromBD.getProcessoDeExecucao();
		licitacao.regimeContratacao = fromBD.getRegimeContratacao();
        licitacao.situacaoDaLicitacao = fromBD.getSituacaoDaLicitacao();
        licitacao.situacaoDaLicitacaoDescricao = SituacaoLicitacaoEnum
                .fromSigla(licitacao.situacaoDaLicitacao).getDescricao();
        licitacao.situacaoVerificacao = fromBD.getSituacaoDoProcessoLicitatorio();
        licitacao.numeroVersao = fromBD.getNumeroVersao();
        licitacao.maiorVersao = fromBD.getMaiorVersao();
        licitacao.versaoAtual = licitacao.numeroVersao != null ? licitacao.numeroVersao.equals(licitacao.maiorVersao) : null;
		licitacao.versao = fromBD.getVersao();

        fromBD.getLotesAssociados().forEach(
            loteBD -> licitacao.lotes.add( new LoteDTO().from(loteBD) )
        );

        licitacao.getFornecedores().addAll(
            fromBD.getFornecedores().stream().map( FornecedorDTO::from ).collect(Collectors.toList())
        );

        
        associarFornecedoresALotes(licitacao.lotes, licitacao.fornecedores);
        return licitacao;
    }

    private static void associarFornecedoresALotes(List<LoteDTO> lotes, List<FornecedorDTO> fornecedores) {
		Map<Long, FornecedorDTO> mapaFornecedores = new HashMap<>();
        for (FornecedorDTO f : fornecedores) {
            mapaFornecedores.put(f.getId(), f);
        }
        for (LoteDTO lote : lotes) {
            if (mapaFornecedores.containsKey(lote.getIdFornecedor())) {
                lote.setFornecedor(mapaFornecedores.get(lote.getIdFornecedor()));
            }
        }
        
    }

    public List<LoteDTO> getLotes() {
        return this.lotes.stream()
                .sorted(Comparator.comparing(LoteDTO::getNumero))
                .collect(Collectors.toList());
    }
}
