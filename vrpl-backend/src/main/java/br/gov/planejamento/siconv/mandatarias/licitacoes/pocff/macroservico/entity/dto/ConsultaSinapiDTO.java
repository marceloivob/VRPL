package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Data;


@Data
public class ConsultaSinapiDTO {
	
	private static final String QUERY_DATA_PATTERN = "MMyyyy";
	private String cdItem;
    private String sgLocalidade;
    private LocalDate dtReferencia;

	
	
	public ConsultaSinapiDTO() {
	}
	
	
	public ConsultaSinapiDTO(String cdItem, String sgLocalidade, LocalDate dtReferencia) {
		super();
		this.cdItem = cdItem;
		this.dtReferencia = dtReferencia;
		this.sgLocalidade = sgLocalidade;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsultaSinapiDTO other = (ConsultaSinapiDTO) obj;
		if (cdItem == null) {
			if (other.cdItem != null)
				return false;
		} else if (!cdItem.equals(other.cdItem))
			return false;
		if (dtReferencia == null) {
			if (other.dtReferencia != null)
				return false;
		} else if (!dtReferencia.equals(other.dtReferencia))
			return false;
		if (sgLocalidade == null) {
			if (other.sgLocalidade != null)
				return false;
		} else if (!sgLocalidade.equals(other.sgLocalidade))
			return false;
		return true;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdItem == null) ? 0 : cdItem.hashCode());
		result = prime * result + ((dtReferencia == null) ? 0 : dtReferencia.hashCode());
		result = prime * result + ((sgLocalidade == null) ? 0 : sgLocalidade.hashCode());
		return result;
	}


	public String getDataFormatada () {
		
		return getDtReferencia().format(DateTimeFormatter.ofPattern(QUERY_DATA_PATTERN));
	}
	
}
