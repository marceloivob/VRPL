package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EstornarEnvioParaAceite implements Serializable {
	private static final long serialVersionUID = 2133309575798597357L;

	@NotNull
	private String licitacao;

	@NotNull
	private String justificativa;

	public EstornarEnvioParaAceite(Long licitacao, String justificativa) {
		Objects.requireNonNull(licitacao);
		Objects.requireNonNull(justificativa);

		this.licitacao = licitacao.toString();
		this.justificativa = justificativa;
	}

}
