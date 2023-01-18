package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Clone<T> {

	T objetoOriginal;

	T objetoClonado;

	public Clone(T original, T clone) {
		this.objetoOriginal = original;
		this.objetoClonado = clone;
	}

}
