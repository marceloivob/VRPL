package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class MetaQCIExternoDTO implements Comparable<MetaQCIExternoDTO> {

	private Long numero;
	private String descricao;
	private String itemInvestimento;
	private BigDecimal quantidade = BigDecimal.ZERO;
	private String unidade;
	private Boolean metaSocial;

	private List<SubmetaQCIExternoDTO> submetas = new ArrayList<>();

	@JsonIgnore
	private Long idMetaAnalise;

	@JsonIgnore
	private Long nrMetaAnalise;

	public BigDecimal getVlRepasse() {
		BigDecimal valorRepasse = submetas.stream().map(SubmetaQCIExternoDTO::getValorRepasseLicitado)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		return valorRepasse;
	}

	public BigDecimal getVlContrapartida() {
		BigDecimal valorContrapartida = submetas.stream().map(SubmetaQCIExternoDTO::getValorContrapartidaLicitado)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorContrapartida;
	}

	public BigDecimal getVlOutros() {
		BigDecimal valorOutros = submetas.stream().map(SubmetaQCIExternoDTO::getValorOutrosLicitado)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorOutros;
	}

	public BigDecimal getVlTotal() {
		BigDecimal vlTotal = submetas.stream().map(SubmetaQCIExternoDTO::getValorTotalLicitado).reduce(BigDecimal.ZERO,
				BigDecimal::add);
		return vlTotal;
	}

	@Override
	public int compareTo(MetaQCIExternoDTO o) {
		return this.idMetaAnalise.compareTo(o.idMetaAnalise);
	}
}
