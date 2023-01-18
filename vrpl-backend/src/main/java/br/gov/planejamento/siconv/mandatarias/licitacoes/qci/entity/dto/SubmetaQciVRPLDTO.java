package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SubmetaQciVRPLDTO {
	Long idMeta;
    Long idSubmeta;
	Long versao;
    BigDecimal vlRepasse;
	BigDecimal vlContrapartida;
    BigDecimal vlOutros;
}
