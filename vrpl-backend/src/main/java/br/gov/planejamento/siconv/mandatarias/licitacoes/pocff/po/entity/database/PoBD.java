package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.PoIntegracao;
import lombok.Data;

@Data
public class PoBD {

	@NotNull
	private Long id;

	private Long idLicitacao;

	@NotNull
	private LocalDate dtPrevisaoInicioObraAnalise;

	@NotNull
	private LocalDate dtPrevisaoInicioObra;

	@NotNull
	private LocalDate dtBaseAnalise;
	
	private LocalDate dtBaseVrpl;

	private Boolean inTrabalhoSocial;

	@NotNull
	private Long idPoAnalise;

	@NotNull
	private Long qtMesesDuracaoObra;
	private Boolean inDesonerado;

	@NotNull
	private Boolean inAcompanhamentoEventos;
	private String sgLocalidade;

	@ColumnName("submeta_fk")
	@NotNull
	private Long submetaId;
	
	private Long submetaAnaliseId;
	private Long submetaNumero;
	private String submetaDescricao;
	private BigDecimal submetaVlRepasse;
	private BigDecimal submetaVlContrapartida;
	private BigDecimal submetaVlOutros;
	private BigDecimal submetaVlTotalLicitado;

	private Long metaNumero;
	private String metaDescricao;
	
	private String referencia;

	@NotNull
	private Long versaoNr;
	
	private Long versao;
	
	private String versaoId;
	
	private String versaoNmEvento;

	private BigDecimal precoTotalAnalise = BigDecimal.ZERO;
	private BigDecimal precoTotalLicitacao = BigDecimal.ZERO;

	public static PoBD from(PoIntegracao poIntegracao) {
		PoBD po = new PoBD();
		po.setIdPoAnalise(poIntegracao.getId());
		po.setQtMesesDuracaoObra(poIntegracao.getQuantidadeMesesDuracaoObra());
		po.setDtPrevisaoInicioObraAnalise(poIntegracao.getDataPrevisaoInicioObra());
		po.setDtPrevisaoInicioObra(poIntegracao.getDataPrevisaoInicioObra());
		po.setInAcompanhamentoEventos(poIntegracao.getIndicadorAcompanhamentoPorEvento());
		po.setInDesonerado(poIntegracao.getIndicadorDesonerado());
		po.setSgLocalidade(poIntegracao.getSiglaLocalidade());
		po.setDtBaseAnalise(poIntegracao.getDataBase());
		po.setDtBaseVrpl(poIntegracao.getDataBase());

		return po;
	}

	public Boolean getInAcompanhamentoEventos() {
		// https://stackoverflow.com/questions/25824269/java-lang-nullpointerexception-with-boolean
		return inAcompanhamentoEventos != null && Boolean.TRUE.equals(inAcompanhamentoEventos);
	}

	public void setDtBaseVrpl(LocalDate dataBase) {
		if (dataBase == null) {
			this.dtBaseVrpl = null;
		} else {
			LocalDate dataBaseAjustada = LocalDate.of(dataBase.getYear(), dataBase.getMonth(), 1);

			this.dtBaseVrpl = dataBaseAjustada;
		}
	}

}
