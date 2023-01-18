package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import lombok.Data;

@Data
public class PoVRPLDTO {

	public static String ORCAMENTO_REFERENCIA_ANALISE = "analise";
	public static String ORCAMENTO_REFERENCIA_DATABASE = "database";
	
	public static String ORCAMENTO_REFERENCIA_ANALISE_TEXTO = "Aceito na Análise";
	public static String ORCAMENTO_REFERENCIA_DATABASE_TEXTO = "Atualizado na Data Base da Licitação";
	
	private Long idLicitacao;

	private Long id;
	private Long idAnalise;
	private LocalDate dataPrevisaoInicioObraAnalise;
	private LocalDate dataPrevisaoInicioObra;
	private Long qtMesesDuracaoObra;
	private Long qtMesesDuracaoObraValorOriginal;

	private LocalDate dataBaseAnalise;
	private LocalDate dataBaseVrpl;
	private String siglaLocalidade;
	private Boolean indicadorAcompanhamentoPorEvento;

	private Long idMeta;
	private String descricaoMeta;
	private Long numeroMeta;
	private Boolean inTrabalhoSocial;

	private Long idSubmeta;
	private Long numeroSubmeta;
	private String descricaoSubmeta;
	
	private String referencia;

	private BigDecimal precoTotalAnalise;
	private BigDecimal precoTotalLicitacao;

	private Long versao;

	@JsonIgnore
	private PoMacroServicoServicosDTO poMacroServicoServicos;

	public static PoVRPLDTO from(PoBD po) {
		PoVRPLDTO dto = new PoVRPLDTO();

		dto.id = po.getId();
		dto.dataPrevisaoInicioObraAnalise = po.getDtPrevisaoInicioObraAnalise();

		dto.dataPrevisaoInicioObra = po.getDtPrevisaoInicioObra();
		dto.qtMesesDuracaoObra = po.getQtMesesDuracaoObra();
		dto.qtMesesDuracaoObraValorOriginal = po.getQtMesesDuracaoObra();

		dto.numeroSubmeta = po.getSubmetaNumero();
		dto.descricaoSubmeta = po.getSubmetaDescricao();

		dto.numeroMeta = po.getMetaNumero();
		dto.descricaoMeta = po.getMetaDescricao();
		dto.inTrabalhoSocial = po.getInTrabalhoSocial();

		dto.indicadorAcompanhamentoPorEvento = po.getInAcompanhamentoEventos();

		dto.siglaLocalidade = po.getSgLocalidade();

		dto.precoTotalAnalise = po.getPrecoTotalAnalise();
		dto.precoTotalLicitacao = po.getPrecoTotalLicitacao();
		dto.versao = po.getVersao();
		dto.dataBaseAnalise = po.getDtBaseAnalise();
		
		dto.referencia = po.getReferencia();

		if (po.getDtBaseVrpl() == null) {
			dto.setDataBaseVrpl(po.getDtBaseAnalise());
		} else {
			dto.setDataBaseVrpl(po.getDtBaseVrpl());
		}

		return dto;

	}

	public String getNomeIdentificador() {
		return String.format("%d.%d - %s", this.numeroMeta, this.numeroSubmeta, this.descricaoSubmeta);
	}

	public Boolean getIndicadorAcompanhamentoPorEvento() {
		// https://stackoverflow.com/questions/25824269/java-lang-nullpointerexception-with-boolean
		return indicadorAcompanhamentoPorEvento != null && Boolean.TRUE.equals(indicadorAcompanhamentoPorEvento);
	}

	public String getNomeIdentificadorComDatabase() {
		return this.getNomeIdentificador() + " - Data Base: " + this.getDataBaseVrplString();
	}

	public String getDataBaseVrplString() {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
		
		return this.getDataBaseVrpl() == null ? "sem data base" : this.getDataBaseVrpl().format(formatter);
	}
	
	public boolean isOrcamentoReferenciaAceitoNaAnalise() {
		return ORCAMENTO_REFERENCIA_ANALISE.equals(this.getReferencia()); 
	}
	
	public boolean isOrcamentoReferenciaDataBaseLicitacao() {
		return ORCAMENTO_REFERENCIA_DATABASE.equals(this.getReferencia()); 
	}
	
	public boolean isDataBaseDaLicitacaoIgualADataBaseDaAnalise() {
		return this.getDataBaseVrpl().equals(this.getDataBaseAnalise());
	}
	
	public String getTextoOrcamentoReferencia() {
		String retorno = "Referência não encontrada";
		
		if (this.isOrcamentoReferenciaAceitoNaAnalise()) {
			retorno = ORCAMENTO_REFERENCIA_ANALISE_TEXTO;
		} else if (this.isOrcamentoReferenciaDataBaseLicitacao()) {
			retorno = ORCAMENTO_REFERENCIA_DATABASE_TEXTO;
		}
		
		return retorno;
	}

}