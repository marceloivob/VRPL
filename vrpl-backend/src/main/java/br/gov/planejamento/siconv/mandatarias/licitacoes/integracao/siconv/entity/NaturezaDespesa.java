package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

/**
 * Tabela: natureza_despesa_sub_it
 */
@Data
public class NaturezaDespesa {

	@ColumnName("id")
	private Long id;

	@ColumnName("descricao_sub_item")
	private String descricao;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NaturezaDespesa other = (NaturezaDespesa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

}
