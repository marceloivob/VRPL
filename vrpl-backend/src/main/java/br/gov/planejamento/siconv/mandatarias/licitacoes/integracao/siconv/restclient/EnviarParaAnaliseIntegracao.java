package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EnviarParaAnaliseIntegracao implements Serializable {
	private static final long serialVersionUID = 5270060607528733802L;

	@NotNull
	private String licitacao;

	@NotNull
	private String justificativa;

	public EnviarParaAnaliseIntegracao(Long licitacao, String justificativa) {
		Objects.requireNonNull(licitacao);

		this.licitacao = licitacao.toString();
		this.justificativa = justificativa;
	}

}
