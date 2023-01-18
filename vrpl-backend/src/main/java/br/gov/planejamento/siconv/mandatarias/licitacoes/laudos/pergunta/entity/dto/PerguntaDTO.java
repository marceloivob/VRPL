package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class PerguntaDTO {

	private Long perguntaId;

	private String titulo;

	private GrupoPerguntaDTO grupo;

	private String tipoResposta;

	private String valorResposta;

	private String valorEsperado;

	private Long numero;

	private Long versaoNr;

	private String versaoNmEvento;

	private Long versao;

	private String versaoId;

	private Long loteId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((loteId == null) ? 0 : loteId.hashCode());
		result = prime * result + ((perguntaId == null) ? 0 : perguntaId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerguntaDTO other = (PerguntaDTO) obj;
		if (loteId == null) {
			if (other.loteId != null)
				return false;
		} else if (!loteId.equals(other.loteId))
			return false;
		if (perguntaId == null) {
			if (other.perguntaId != null)
				return false;
		} else if (!perguntaId.equals(other.perguntaId))
			return false;
		return true;
	}

}
