package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EstornarIntegracao implements Serializable {
	private static final long serialVersionUID = 2133309575797711514L;

	@NotNull
	private String licitacao;

	@NotNull
	private String justificativa;

	public EstornarIntegracao(Long licitacao, String justificativa) {
		Objects.requireNonNull(licitacao);
		Objects.requireNonNull(justificativa);

		this.licitacao = licitacao.toString();
		this.justificativa = justificativa;
	}
}
