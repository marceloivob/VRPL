package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class QciExternoDTO {

	private List<MetaQCIExternoDTO> metas = new ArrayList<>();

	private Boolean possuiSubmetaSocial;

	private BigDecimal somatorioVlRepasseParaSubmetaSocial = BigDecimal.ZERO;

	private BigDecimal somatorioVlContrapartidaParaSubmetaSocial = BigDecimal.ZERO;

	private BigDecimal somatorioVlTotalLicitadoParaSubmetaSocial = BigDecimal.ZERO;

	private String modalidade;

	@NotNull
	private BigDecimal valorGlobal = BigDecimal.ZERO;

	@NotNull
	private BigDecimal valorRepasse = BigDecimal.ZERO;

	@NotNull
	private BigDecimal valorContrapartida = BigDecimal.ZERO;

	private BigDecimal saldoDemaisMetasVlRepasse = BigDecimal.ZERO;

	private BigDecimal saldoDemaisMetasContrapartida = BigDecimal.ZERO;

	private BigDecimal saldoDemaisMetasTotal = BigDecimal.ZERO;

	public QciExternoDTO(PropostaBD proposta, List<MetaQCIExternoDTO> metasQCIExterno) {
		this.valorContrapartida = proposta.getValorContrapartida();
		this.valorGlobal = proposta.getValorGlobal();
		this.valorRepasse = proposta.getValorRepasse();
		this.modalidade = proposta.getDescricaoModalidade();

		this.metas = metasQCIExterno;

		this.setSomatorioVlRepasseParaSubmetaSocial(
				metas.stream().filter(MetaQCIExternoDTO::getMetaSocial).flatMap(meta -> meta.getSubmetas().stream())
						.map(SubmetaQCIExternoDTO::getValorRepasseLicitado).reduce(BigDecimal.ZERO, BigDecimal::add));

		this.setSomatorioVlContrapartidaParaSubmetaSocial(metas.stream().filter(MetaQCIExternoDTO::getMetaSocial)
				.flatMap(meta -> meta.getSubmetas().stream()).map(SubmetaQCIExternoDTO::getValorContrapartidaLicitado)
				.reduce(BigDecimal.ZERO, BigDecimal::add));

		this.setSomatorioVlTotalLicitadoParaSubmetaSocial(
				metas.stream().filter(MetaQCIExternoDTO::getMetaSocial).flatMap(meta -> meta.getSubmetas().stream())
						.map(SubmetaQCIExternoDTO::getValorTotalLicitado).reduce(BigDecimal.ZERO, BigDecimal::add));

		BigDecimal todosValoresDeRepasse = metas.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaQCIExternoDTO::getValorRepasseLicitado).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal todosValoresDeContrapartida = metas.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaQCIExternoDTO::getValorContrapartidaLicitado).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal todosValoresDeTotalLicitado = metas.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaQCIExternoDTO::getValorTotalLicitado).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal somatorioVlRepasseParaSubmetaDeEngenharia = todosValoresDeRepasse
				.subtract(somatorioVlRepasseParaSubmetaSocial);

		BigDecimal somatorioVlContrapartidaParaSubmetaDeEngenharia = todosValoresDeContrapartida
				.subtract(somatorioVlContrapartidaParaSubmetaSocial);

		BigDecimal somatorioVlTotalLicitadoParaSubmetaDeEngenharia = todosValoresDeTotalLicitado
				.subtract(somatorioVlTotalLicitadoParaSubmetaSocial);

		this.saldoDemaisMetasVlRepasse = valorRepasse.subtract(somatorioVlRepasseParaSubmetaDeEngenharia);

		this.saldoDemaisMetasContrapartida = valorContrapartida
				.subtract(somatorioVlContrapartidaParaSubmetaDeEngenharia);

		this.saldoDemaisMetasTotal = valorGlobal.subtract(somatorioVlTotalLicitadoParaSubmetaDeEngenharia);
	}

	public BigDecimal getVlRepasse() {
		BigDecimal vlRepasse = metas.stream().map(MetaQCIExternoDTO::getVlRepasse).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return vlRepasse;
	}

	public BigDecimal getVlContrapartida() {
		BigDecimal vlContrapartida = metas.stream().map(MetaQCIExternoDTO::getVlContrapartida).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return vlContrapartida;
	}

	public BigDecimal getVlOutros() {
		BigDecimal vlOutros = metas.stream().map(MetaQCIExternoDTO::getVlOutros).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return vlOutros;
	}

	public BigDecimal getVlTotal() {
		BigDecimal vlTotal = metas.stream().map(MetaQCIExternoDTO::getVlTotal).reduce(BigDecimal.ZERO, BigDecimal::add);

		return vlTotal;
	}

	/**
	 * Diferença entre Repasse Global e Total Repasse do QCI na VRPL (das submetas
	 * do tipo Social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlRepasseParaSubmetasSociais() {
		return getValorRepasse().subtract(getSomatorioVlRepasseParaSubmetaSocial());
	}

	/**
	 * A diferença entre Contrapartida Global e Total Contrapartida do QCI na VRPL
	 * (das submetas do tipo Social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlContrapartidaParaSubmetasSociais() {
		return getValorContrapartida().subtract(getSomatorioVlContrapartidaParaSubmetaSocial());
	}

	/**
	 * A diferença entre Valor Global e Total do QCI na VRPL (das submetas do tipo
	 * Social).
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlTotalLicitadoParaSubmetasSociais() {
		return getValorGlobal().subtract(getSomatorioVlTotalLicitadoParaSubmetaSocial());
	}

	/**
	 * A diferença entre Repasse Global e Total Repasse do QCI na VRPL (das metas do
	 * tipo <> de Trabalho social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlRepasseParaDemaisMetas() {
		return saldoDemaisMetasVlRepasse;
	}

	/**
	 * A diferença entre Contrapartida Global e Total Contrapartida do QCI na VRPL
	 * (das metas do tipo <> de Trabalho social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlContrapartidaParaDemaisMetas() {
		return saldoDemaisMetasContrapartida;
	}

	/**
	 * A diferença entre Valor Global e Total do QCI na VRPL (das metas do tipo <>
	 * de Trabalho social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlTotalLicitadoParaDemaisMetas() {
		return saldoDemaisMetasTotal;
	}

	public BigDecimal getDifValorOrcadoTotalLicitado() {
		return getValorGlobal().subtract(getVlTotal());
	}

	public BigDecimal getDifValorOrcadoRepasseLicitado() {
		return getValorRepasse().subtract(getVlRepasse());
	}

	public BigDecimal getDifValorOrcadoContrapartidaLicitada() {
		return getValorContrapartida().subtract(getVlContrapartida());
	}

}
