package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ServicoPLQDTO {
    private Long id;
    private Integer numero;
    private String descricao;
    private BigDecimal quantidade;
    private String unidade;
    private Integer numeroEvento;
    private String descricaoEvento;
    private BigDecimal precoTotalServico = new BigDecimal(0);
    private BigDecimal precoUnitarioServico = new BigDecimal(0);
    private Long macroServicoFk;
    private List<FrenteObraPLQDTO> frentesObra;
}
