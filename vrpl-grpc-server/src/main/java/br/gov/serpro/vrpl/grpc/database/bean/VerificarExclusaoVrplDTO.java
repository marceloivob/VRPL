package br.gov.serpro.vrpl.grpc.database.bean;

import lombok.Data;

@Data
public class VerificarExclusaoVrplDTO {
    Integer situacao;
	String mensagem;
}
