package br.gov.serpro.vrpl.grpc.submeta;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Servico {
	private Long id;
	private Integer numero;
	private String descricao;
	private BigDecimal qtdeItens;
	private BigDecimal preco;
	private String sgUnidade;
}
