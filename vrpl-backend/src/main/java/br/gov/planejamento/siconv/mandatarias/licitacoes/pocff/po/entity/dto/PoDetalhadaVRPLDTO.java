package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import lombok.Data;

@Data
public class PoDetalhadaVRPLDTO {
	private Long id;
	private Long idAnalise;
	private LocalDate dataBaseAnalise;
	private LocalDate dataBaseVrpl;

	private String localidade;

	private Long duracao;

	private LocalDate previsaoAnalise;
	private LocalDate previsaoInicioVRPL;

	private Long qtMesesDuracaoObra;
	private Long qtMesesDuracaoObraValorOriginal;

	private Boolean acompanhamentoEvento;
	private String referencia;
	private Long versao;

	private BigDecimal precoTotalAnalise;
	private BigDecimal precoTotalLicitado;

	private Long numeroMeta;
	private String descricaoMeta;

	private Long idSubmeta;
	private Long numeroSubmeta;
	private String descricao;

	private Boolean reaproveitaCFFSemEventos;

	private Boolean inTrabalhoSocial;


	public void from(PoBD po) {
		this.id = po.getId();
		this.idAnalise = po.getIdPoAnalise();

		this.dataBaseAnalise = po.getDtBaseAnalise();
		this.dataBaseVrpl = po.getDtBaseVrpl();

		this.localidade = po.getSgLocalidade();

		this.previsaoAnalise = po.getDtPrevisaoInicioObraAnalise();
		this.previsaoInicioVRPL = po.getDtPrevisaoInicioObra();

		this.qtMesesDuracaoObra = po.getQtMesesDuracaoObra();
		this.qtMesesDuracaoObraValorOriginal = po.getQtMesesDuracaoObra();

		this.acompanhamentoEvento = po.getInAcompanhamentoEventos();
		this.referencia = po.getReferencia();
		this.versao = po.getVersao();

		this.precoTotalAnalise = po.getPrecoTotalAnalise();
		this.precoTotalLicitado = po.getPrecoTotalLicitacao();


		this.numeroMeta = po.getMetaNumero();
		this.descricaoMeta = po.getMetaDescricao();

		this.idSubmeta = po.getSubmetaId();
		this.numeroSubmeta = po.getSubmetaNumero();
		this.descricao = po.getSubmetaDescricao();

		this.inTrabalhoSocial = po.getInTrabalhoSocial();
	}

}