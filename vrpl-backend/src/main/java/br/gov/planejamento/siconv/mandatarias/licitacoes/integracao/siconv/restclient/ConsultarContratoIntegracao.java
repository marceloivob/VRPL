package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;

import lombok.Data;

@Data
public class ConsultarContratoIntegracao implements Serializable {
	private static final long serialVersionUID = 8426364131706297311L;
	public String licitacao;
	public String responsavel;
}
