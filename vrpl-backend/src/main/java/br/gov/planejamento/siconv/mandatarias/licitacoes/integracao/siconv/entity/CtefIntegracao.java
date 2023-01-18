package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class CtefIntegracao {

	@ColumnName("numero")
	public String numeroDoCtef = "";

	@ColumnName("objeto")
	public String objetoDoContrato = "";

	@ColumnName("valor_global")
	public BigDecimal valorTotal;

	@ColumnName("data_assinatura")
	public LocalDate dataDeAssinatura;

	@ColumnName("data_fim_vigencia")
	public LocalDate dataFimDaVigencia;

}
