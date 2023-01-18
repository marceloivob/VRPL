package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class CFFSemEventosDTO {
	private Long idPO;
	private LocalDate dtPrevisaoInicioObra;
	private Long qtMesesDuracaoObra;
	private List<MacroServicoCFFDTO> listaMacroServicos;
}
