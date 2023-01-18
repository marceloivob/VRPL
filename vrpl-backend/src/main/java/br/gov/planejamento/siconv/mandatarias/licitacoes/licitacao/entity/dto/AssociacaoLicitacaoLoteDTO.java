package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AssociacaoLicitacaoLoteDTO {

    private Long idLicitacao;
    private Long idFornecedor;
    private List<LoteAssociarDTO> lotes = new ArrayList<>();
    private List<SubmetasAlteradasDTO> submetasAlteradas = new ArrayList<>();

    @Data
    public static class SubmetasAlteradasDTO {
        private Long idSubmeta;
        private Long versaoSubmeta;
        private Long idNovoLote;
        private Long numeroNovoLote;
        private Long idAntigoLote;
    }

    @Data
    public static class LoteAssociarDTO {
        private Long id;
        private Long numero;
		private Long versao;
    }
}
