package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ContratoDocLiquidacaoResponse implements Serializable {

	private static final long serialVersionUID = -4974117081939510950L;

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	public List<String> Contrato = new ArrayList<>();

	@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	public List<String> DocLiquidacao = new ArrayList<>();

	@Override
	public String toString() {
		return "ContratoDocLiquidacaoResponse [contrato=" + Contrato + ", docLiquidacao=" + DocLiquidacao + "]";
	}

	/**
	 * Construtor Padrão
	 */
	public ContratoDocLiquidacaoResponse() {
	}

	public ContratoDocLiquidacaoResponse(List<String> contrato, List<String> docLiquidacao) {
		if (contrato == null) {
			contrato = Collections.emptyList();
		}
		if (docLiquidacao == null) {
			docLiquidacao = Collections.emptyList();
		}

		this.Contrato = contrato;
		this.DocLiquidacao = docLiquidacao;
	}

	/**
	 * RN: 595818 -
	 * SICONV-DocumentosOrcamentarios-ManterQuadroResumo-VRPL-RN-ValidacaoCancelamentoRejeicao
	 * <p>
	 * Impedir o cancelamento da rejeição de uma licitação se pelo menos um dos
	 * pontos indicados a seguir for detectado:
	 * <p>
	 * Existência de Contrato ou Documento de Liquidação associada ao Lote
	 */
	@JsonIgnore
	public boolean existeInstrumentoContratualOuDocumentoDeLiquidacao() {
		boolean temContratoAceito = !(this.Contrato == null || this.Contrato.isEmpty());
		boolean temDocumentoDeLiquidacao = !(this.DocLiquidacao == null || this.DocLiquidacao.isEmpty());

		return temContratoAceito || temDocumentoDeLiquidacao;
	}

}
