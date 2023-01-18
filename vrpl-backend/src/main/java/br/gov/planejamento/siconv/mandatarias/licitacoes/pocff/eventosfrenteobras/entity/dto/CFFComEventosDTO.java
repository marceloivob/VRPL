package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CFFComEventosDTO {
    Long idPO;
	Long qtdMeses;
    BigDecimal totalPO;
    Map<Integer, BigDecimal> mapaMesValorParcela;
    List<EventoCFFcomEventosDTO> listaDeEventoCFF;
}
