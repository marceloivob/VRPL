package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import lombok.Data;

@Data
public class PermissaoDTO {

	private Long idLicitacao;

	@JsonIgnore
	private String situacaoVRPL;

	@JsonIgnore
	private String situacaoAnalise;

	public SituacaoLicitacaoEnum getEstado() {
		if (situacaoVRPL != null) {
			return SituacaoLicitacaoEnum.fromSigla(this.situacaoVRPL);
		} else if (situacaoAnalise != null) {
			return SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum.fromSigla(this.situacaoAnalise)
					.getEquivalenteNoVRPL();
		}

		throw new IllegalArgumentException(
				String.format("Situação da Licitação inválida. situacaoVRPL: '{}',  situacaoAnalise: '{}'",
						situacaoVRPL, situacaoAnalise));
	}

	/**
	 * RN: 603439 - SICONV-DocumentosOrcamentarios-VRPL-RN-RetornoOperação
	 */
	public boolean getPodeAlterar() {
		final List<SituacaoLicitacaoEnum> situacoesQuePermitemAlteracao = Arrays
				.asList(SituacaoLicitacaoEnum.EM_PREENCHIMENTO, SituacaoLicitacaoEnum.EM_COMPLEMENTACAO);

		return situacoesQuePermitemAlteracao.contains(this.getEstado());
	}

	/**
	 * RN: 603439 - SICONV-DocumentosOrcamentarios-VRPL-RN-RetornoOperação
	 */
	public boolean getPodeExcluir() {
		final List<SituacaoLicitacaoEnum> situacoesQuePermitemExcluir = Arrays.asList(
				SituacaoLicitacaoEnum.HOMOLOGADA_AUTOMATICAMENTE_PELO_SISTEMA_NO_PROJETO_BASICO,
				SituacaoLicitacaoEnum.HOMOLOGADA_NO_PROJETO_BASICO);

		return situacoesQuePermitemExcluir.contains(this.getEstado());
	}

}
