package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class PLQDTO {
    private boolean porEvento;
    private List<MacroServicoPLQDTO> macroservicos;
}
    