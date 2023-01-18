package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class QciDTO {

	private List<MetaDTO> metas = new ArrayList<>();

	private Boolean possuiSubmetaSocial;

	private BigDecimal somatorioVlRepasseParaSubmetaSocial;

	private BigDecimal somatorioVlContrapartidaParaSubmetaSocial;

	private BigDecimal somatorioVlTotalLicitadoParaSubmetaSocial;

	private String modalidade;

	@NotNull
	private BigDecimal valorGlobalLicitado;

	@NotNull
	private BigDecimal valorGlobalRepasseLicitado;

	@NotNull
	private BigDecimal valorGlobalContrapartidaLicitado;

	private BigDecimal saldoDemaisMetasVlRepasse;

	private BigDecimal saldoDemaisMetasContrapartida;

	private BigDecimal saldoDemaisMetasTotal;

	public QciDTO(DadosBasicosIntegracao dadosBasicos, List<MetaBD> metasBD) {
		this.valorGlobalContrapartidaLicitado = dadosBasicos.getValorContrapartida();
		this.valorGlobalLicitado = dadosBasicos.getValorGlobal();
		this.valorGlobalRepasseLicitado = dadosBasicos.getValorRepasse();
		this.modalidade = ModalidadePropostaEnum.fromCodigo(dadosBasicos.getModalidade(), dadosBasicos.getTermoCompromissoTemMandataria()).getDescricao();
		this.metas.addAll(metasBD.stream().map(MetaDTO::from).collect(Collectors.toList()));

		Boolean existeMetaSocial = metasBD.stream().filter(MetaBD::getSocial).collect(Collectors.toList())
				.isEmpty() == false;

		this.setPossuiSubmetaSocial(existeMetaSocial);

		this.setSomatorioVlRepasseParaSubmetaSocial(
				metasBD.stream().filter(MetaBD::getSocial).flatMap(meta -> meta.getSubmetas().stream())
						.map(SubmetaBD::getVlRepasse).reduce(BigDecimal.ZERO, BigDecimal::add));

		this.setSomatorioVlContrapartidaParaSubmetaSocial(
				metasBD.stream().filter(MetaBD::getSocial).flatMap(meta -> meta.getSubmetas().stream())
						.map(SubmetaBD::getVlContrapartida).reduce(BigDecimal.ZERO, BigDecimal::add));

		this.setSomatorioVlTotalLicitadoParaSubmetaSocial(
				metasBD.stream().filter(MetaBD::getSocial).flatMap(meta -> meta.getSubmetas().stream())
						.map(SubmetaBD::getVlTotalLicitado).reduce(BigDecimal.ZERO, BigDecimal::add));

		BigDecimal todosValoresDeRepasse = metasBD.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaBD::getVlRepasse).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal todosValoresDeContrapartida = metasBD.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaBD::getVlContrapartida).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal todosValoresDeTotalLicitado = metasBD.stream().flatMap(meta -> meta.getSubmetas().stream())
				.map(SubmetaBD::getVlTotalLicitado).reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal somatorioVlRepasseParaSubmetaDeEngenharia = todosValoresDeRepasse
				.subtract(somatorioVlRepasseParaSubmetaSocial);

		BigDecimal somatorioVlContrapartidaParaSubmetaDeEngenharia = todosValoresDeContrapartida
				.subtract(somatorioVlContrapartidaParaSubmetaSocial);

		BigDecimal somatorioVlTotalLicitadoParaSubmetaDeEngenharia = todosValoresDeTotalLicitado
				.subtract(somatorioVlTotalLicitadoParaSubmetaSocial);

		this.saldoDemaisMetasVlRepasse = valorGlobalRepasseLicitado.subtract(somatorioVlRepasseParaSubmetaDeEngenharia);

		this.saldoDemaisMetasContrapartida = valorGlobalContrapartidaLicitado
				.subtract(somatorioVlContrapartidaParaSubmetaDeEngenharia);

		this.saldoDemaisMetasTotal = valorGlobalLicitado.subtract(somatorioVlTotalLicitadoParaSubmetaDeEngenharia);

	}

	public BigDecimal getVlRepasse() {
		BigDecimal vlRepasse = metas.stream()
				.map(meta -> meta.getVlRepasse() == null ? BigDecimal.ZERO : meta.getVlRepasse())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return vlRepasse;
	}

	public BigDecimal getVlContrapartida() {
		BigDecimal vlContrapartida = metas.stream()
				.map(meta -> meta.getVlContrapartida() == null ? BigDecimal.ZERO : meta.getVlContrapartida())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return vlContrapartida;
	}

	public BigDecimal getVlOutros() {
		BigDecimal vlOutros = metas.stream()
				.map(meta -> meta.getVlOutros() == null ? BigDecimal.ZERO : meta.getVlOutros())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return vlOutros;
	}

	public BigDecimal getVlTotal() {
		BigDecimal vlTotal = metas.stream().map(meta -> meta.getVlTotal() == null ? BigDecimal.ZERO : meta.getVlTotal())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
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
		return getValorGlobalRepasseLicitado().subtract(getSomatorioVlRepasseParaSubmetaSocial());
	}

	/**
	 * A diferença entre Contrapartida Global e Total Contrapartida do QCI na VRPL
	 * (das submetas do tipo Social)
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlContrapartidaParaSubmetasSociais() {
		return getValorGlobalContrapartidaLicitado().subtract(getSomatorioVlContrapartidaParaSubmetaSocial());
	}

	/**
	 * A diferença entre Valor Global e Total do QCI na VRPL (das submetas do tipo
	 * Social).
	 * <p>
	 * RN: Manter QCI (Quadro de Composição de Investimento) > 533590 -
	 * SICONV-DocumentosOrcamentarios-ManterQCI-RN-TotalizacaoListagemQCI
	 */
	public BigDecimal getDiferencaVlTotalLicitadoParaSubmetasSociais() {
		return getValorGlobalLicitado().subtract(getSomatorioVlTotalLicitadoParaSubmetaSocial());
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
		return getValorGlobalLicitado().subtract(getVlTotal());
	}

	public BigDecimal getDifValorOrcadoRepasseLicitado() {
		return getValorGlobalRepasseLicitado().subtract(getVlRepasse());
	}

	public BigDecimal getDifValorOrcadoContrapartidaLicitada() {
		return getValorGlobalContrapartidaLicitado().subtract(getVlContrapartida());
	}

}
