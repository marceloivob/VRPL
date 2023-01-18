package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;
import lombok.Data;

@Data
public class LicitacaoBD {
	@ColumnName("id")
	private Long id;

	@ColumnName("proposta_fk")
	@NotNull
	private Long identificadorDaProposta;

	@ColumnName("numero_ano")
	@NotNull
	@Size(max = 1024)
	private String numeroAno;

	@ColumnName("objeto")
	@NotNull
	@Size(max = 1024)
	private String objeto;

	@ColumnName("valor_processo")
	@NotNull
	private BigDecimal valorProcessoLicitatorio;

	@ColumnName("status_processo")
	@NotNull
	@Size(max = 20)
	private String situacaoDoProcessoLicitatorio;

	@ColumnName("in_situacao")
	@NotNull
	@Size(max = 3)
	private String situacaoDaLicitacao;

	@ColumnName("id_licitacao_fk")
	@NotNull
	private Long idLicitacaoFk;

	@ColumnName("versao_nr")
	@NotNull
	private Integer numeroVersao;

	@ColumnName("maior_versao")
	private Integer maiorVersao;

	@ColumnName("modalidade")
	private String modalidade;

	@ColumnName("regime_contratacao")
	private String regimeContratacao;

	@ColumnName("data_publicacao")
	private LocalDate dataPublicacao;

	@ColumnName("data_homologacao")
	private LocalDate dataHomologacao;

	@ColumnName("processo_de_execucao")
	private String processoDeExecucao;

	private Long versao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

	@ColumnName("versao_nr")
	private Integer versaoNr;

	private List<LoteBD> lotesAssociados = new ArrayList<>();

	private List<FornecedorBD> fornecedores = new ArrayList<>();

	public LoteBD addLote(LoteBD loteBD) {
		int pos = this.lotesAssociados.indexOf(loteBD);
		if (pos == -1) {
			this.lotesAssociados.add(loteBD);
			return loteBD;
		}

		return this.lotesAssociados.get(pos);
	}

	/**
	 * Construtor Padrão
	 */
	public LicitacaoBD() {
		this.versaoNr = 0;
	}

	public LicitacaoBD(PropostaBD proposta, ProcessoExecucaoResponse licitacaoServico) {
		this.identificadorDaProposta = proposta.getId();
		this.situacaoDaLicitacao = SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla();
		this.atualizaComInformacoesDoServico(licitacaoServico);
		this.versaoNr = proposta.getVersaoNr();
	}

	public void atualizaComInformacoesDoServico(ProcessoExecucaoResponse licitacaoServico) {
		this.numeroAno = licitacaoServico.getNumeroAno();
		this.situacaoDoProcessoLicitatorio = licitacaoServico.getStatus();

		/**
		 * Ver
		 * https://stackoverflow.com/questions/3829029/how-to-convert-from-float-to-bigdecimal-in-java
		 *
		 *
		 * Note: For values other float and double NaN and ±Infinity, this constructor
		 * is compatible with the values returned by Float.toString(float) and
		 * Double.toString(double). This is generally the preferred way to convert a
		 * float or double into a BigDecimal, as it doesn't suffer from the
		 * unpredictability of the BigDecimal(double) constructor.
		 */
		this.valorProcessoLicitatorio = new BigDecimal(Double.toString(licitacaoServico.getValor()));
		this.idLicitacaoFk = licitacaoServico.getId();
		this.objeto = licitacaoServico.getObjeto();

		if (licitacaoServico.getDataHomologacao() != null && !"".equals(licitacaoServico.getDataHomologacao())) {
			this.dataHomologacao = LocalDate.parse(licitacaoServico.getDataHomologacao());
		}

		if (licitacaoServico.getDataPublicacaoEdital() != null
				&& !"".equals(licitacaoServico.getDataPublicacaoEdital())) {
			this.dataPublicacao = LocalDate.parse(licitacaoServico.getDataPublicacaoEdital());
		}

		this.processoDeExecucao = licitacaoServico.getDescricaoProcesso();
		this.regimeContratacao = licitacaoServico.getRegimeContratacao();
		this.modalidade = licitacaoServico.getModalidade();
	}

}
