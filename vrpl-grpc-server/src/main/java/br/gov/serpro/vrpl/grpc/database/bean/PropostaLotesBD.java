package br.gov.serpro.vrpl.grpc.database.bean;

import lombok.Data;

@Data
public class PropostaLotesBD {
    
    private Integer numero;

	private Integer ano;

	private Integer versao;

    private Integer qtdLotes;
    
}