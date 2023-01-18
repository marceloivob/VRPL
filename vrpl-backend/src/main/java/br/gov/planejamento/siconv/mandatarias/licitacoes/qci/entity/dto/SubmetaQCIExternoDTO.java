package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import lombok.Data;

@Data
public class SubmetaQCIExternoDTO {

	private Long id;
	private Long numero;
	private String descricao;
	private SituacaoLicitacaoEnum situacao;
	private Long lote;
	
	private BigDecimal valorRepasseLicitado = BigDecimal.ZERO;
	private BigDecimal valorOutrosLicitado = BigDecimal.ZERO;
	private BigDecimal valorContrapartidaLicitado = BigDecimal.ZERO;
	private BigDecimal valorTotalLicitado = BigDecimal.ZERO;
	
	private BigDecimal valorRepasseAnalise = BigDecimal.ZERO;
	private BigDecimal valorOutrosAnalise = BigDecimal.ZERO;
	private BigDecimal valorContrapartidaAnalise = BigDecimal.ZERO;
	private BigDecimal valorTotalAnalise = BigDecimal.ZERO;

	@JsonIgnore
	private Long idMeta;

	@JsonIgnore
	private String situacaoAsString;

	@JsonIgnore
	private String situacaoAnaliseAsString;

	public SituacaoLicitacaoEnum getSituacao() {
		if (situacaoAsString == null) {
			this.situacao = SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.fromSigla(situacaoAnaliseAsString)
					.getEquivalenteNoVRPL();

		} else if (situacaoAnaliseAsString == null) {
			this.situacao = SituacaoLicitacaoEnum.fromSigla(situacaoAsString);
		}

		return situacao;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SubmetaQCIExternoDTO other = (SubmetaQCIExternoDTO) obj;
		if (idMeta == null) {
			if (other.idMeta != null)
				return false;
		} else if (!idMeta.equals(other.idMeta))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMeta == null) ? 0 : idMeta.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

}
