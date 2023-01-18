package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PoIntegracao {
    private Long id;
    private LocalDate dataBase;
    private String descricaoMeta;
    private Long numeroMeta;
    private Long idSubmeta;
    private Boolean indicadorDesonerado;
    private String siglaLocalidade;
    private LocalDate dataPrevisaoInicioObra;
	private Long quantidadeMesesDuracaoObra;
    private Boolean indicadorAcompanhamentoPorEvento;
}
