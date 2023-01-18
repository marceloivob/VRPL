package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import lombok.Data;

@Data
public class Alteracao<T> {
	private String campo;

	private T valorOriginal;

	private T valorAlterado;

	public Alteracao<T> oCampo(String campo) {
		this.campo = campo;
		return this;
	}

	public Alteracao<T> mudouDe(T valorOriginal) {
		this.valorOriginal = valorOriginal;
		return this;
	}

	public Alteracao<T> para(T valorAlterado) {
		this.valorAlterado = valorAlterado;

		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alteracao other = (Alteracao) obj;
		if (campo == null) {
			if (other.campo != null)
				return false;
		} else if (!campo.equals(other.campo))
			return false;
		if (valorAlterado == null) {
			if (other.valorAlterado != null)
				return false;
		} else if (!valorAlterado.equals(other.valorAlterado))
			return false;
		if (valorOriginal == null) {
			if (other.valorOriginal != null)
				return false;
		} else if (!valorOriginal.equals(other.valorOriginal))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campo == null) ? 0 : campo.hashCode());
		result = prime * result + ((valorAlterado == null) ? 0 : valorAlterado.hashCode());
		result = prime * result + ((valorOriginal == null) ? 0 : valorOriginal.hashCode());
		return result;
	}

}
