package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SinapiIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import lombok.Data;

@Data
public class ServicoDTO {

	private Long id;
	private Long idSubmeta;
	private Long idPo;

	private Long macroServicoFk;
	private Long eventoFk;

	private BigDecimal pcBdi;
	private BigDecimal pcBdiLicitado;

	private Long nrVersao;
	private String txObservacao;
	private String inFonte;

	private BigDecimal vlCustoUnitarioRef;
	private BigDecimal vlCustoUnitario;
	private BigDecimal custoUnitarioDataBase;
	private BigDecimal vlCustoUnitarioDataBase;
	private Boolean sinapiPossuiOcorrenciaNaDataBaseDeReferencia;

	private BigDecimal qtTotalItensAnalise;

	private String cdServico;
	private String txDescricao;
	private String sgUnidade;
	private Integer nrServico;
	private List<ServicoFrenteObraDTO> frentesObra = new ArrayList<>();
	private List<ServicoFrenteObraAnaliseDTO> frentesObraAnalise = new ArrayList<>();

	private Long versao;

	@JsonIgnore
	private SinapiIntegracao sinapi;

	@JsonIgnore
	private PoBD po;

	/********************************************************
	 * Valores da Licitação
	 *********************************************************/

	// Preço Unitário da Licitação
	private BigDecimal vlPrecoUnitarioDaLicitacao;

	// Preço Unitário Aceito na Análise
	private BigDecimal vlPrecoUnitarioAceitoNaAnalise;

	// Preço Total Aceito na Análise
	private BigDecimal vlPrecoTotalAceitoNaAnalise;

	public BigDecimal getQuantidade() {
		BigDecimal quantidade = BigDecimal.ZERO;
		
		for (ServicoFrenteObraDTO sfo : frentesObra) {
			quantidade = quantidade.add(sfo.getQtItens());
		}
		
		return quantidade;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Preço Total da Licitação
	 * <p>
	 * Preço unitário vezes a quantidade na fase de VRPL
	 */
	public BigDecimal getVlPrecoTotalDaLicitacao() {

		BigDecimal valorPorFrenteObra = BigDecimal.ZERO;
		BigDecimal vlPrecoTotalDaLicitacao = BigDecimal.ZERO;

		for (ServicoFrenteObraDTO sfo : frentesObra) {
			if (sfo.getQtItens() != null) {
				valorPorFrenteObra = this.getVlPrecoUnitarioDaLicitacao().multiply(sfo.getQtItens()).setScale(2, RoundingMode.HALF_UP);
				vlPrecoTotalDaLicitacao = vlPrecoTotalDaLicitacao.add(valorPorFrenteObra);
			}
		}

		return vlPrecoTotalDaLicitacao;
	}

	/***
	 * Preço Unitário na Data Base de Licitação
	 * <p>
	 * O valor exibido varia de acordo com a fonte associada ao serviço:
	 * <p>
	 * Para os serviços com fonte SINAPI: Aplicar a fórmula:
	 * <p>
	 * (Custo unitário do item no SINAPI na data base informada) x (BDI aceito na
	 * análise)
	 * <p>
	 * Outras fontes ou para serviços com fonte SINAPI não possuírem ocorrência na
	 * base de dados para a data base de referência:
	 * <p>
	 * Considerar o preço unitário aprovado na análise para o serviço.
	 */
	public BigDecimal getVlPrecoUnitarioNaDataBaseDaLicitacao() {
		if (ehServicoComFonteSinapi() && sinapiPossuiOcorrenciaNaDataBaseDeReferencia()) {
			BigDecimal custoUnitarioNaDataBaseInformada = getCustoUnitarioNaDataBase();
			BigDecimal bdiAceitoNaAnalise = getBDIAceitoNaAnalise();
			return custoUnitarioNaDataBaseInformada.multiply(bdiAceitoNaAnalise).setScale(2, RoundingMode.HALF_UP);
		} else {
			BigDecimal custoUnitarioEditado = this.getVlCustoUnitarioDataBase();
			BigDecimal bdiAceitoNaAnalise = getBDIAceitoNaAnalise();
			if (custoUnitarioEditado.compareTo(BigDecimal.ZERO) == 0) {
				return getVlPrecoUnitarioAceitoNaAnalise();
			} else {
				return custoUnitarioEditado.multiply(bdiAceitoNaAnalise).setScale(2, RoundingMode.HALF_UP);
			}
		}
	}

	/**
	 * Preço Total na Data Base da Licitação
	 * <p>
	 * O valor exibido varia de acordo com a fonte associada ao serviço:
	 * <p>
	 * Para os serviços com fonte SINAPI: Aplicar a fórmula:
	 * <p>
	 * (Quantidade) x (Custo unitário do item no SINAPI na data base informada) x
	 * (BDI aceito na análise)
	 * <p>
	 * Outras fontes: Aplicar a fórmula:
	 * <p>
	 * (Quantidade) x (Preço unitário aprovado na análise para o serviço)
	 *
	 */
	public BigDecimal getVlPrecoTotalDataBaseDaLicitacao() {

		BigDecimal valorPorFrenteObra = BigDecimal.ZERO;
		BigDecimal vlPrecoTotalDataBaseDaLicitacao = BigDecimal.ZERO;
		BigDecimal precoUnitario = getVlPrecoUnitarioNaDataBaseDaLicitacao();

		for (ServicoFrenteObraAnaliseDTO sfo : frentesObraAnalise) {
			if (sfo.getQtItens() != null) {
				valorPorFrenteObra = precoUnitario.multiply(sfo.getQtItens()).setScale(2, RoundingMode.HALF_UP);
				vlPrecoTotalDataBaseDaLicitacao = vlPrecoTotalDataBaseDaLicitacao.add(valorPorFrenteObra);
			}
		}

		return vlPrecoTotalDataBaseDaLicitacao;
	}

	public String getCdItem() {
		return cdServico != null ? cdServico.split("/")[0] : null;
	}

	public void defineSinapi(SinapiIntegracao sinapi) {
		this.sinapi = sinapi;

		this.sinapiPossuiOcorrenciaNaDataBaseDeReferencia = sinapi != null;
	}

	public void definePo(PoBD po) {
		this.po = po;
	}

	public ServicoBD converterParaBD() {
		ServicoBD servicoBD = new ServicoBD();

		servicoBD.setId(this.getId());
		servicoBD.setVlPrecoUnitarioLicitado(this.getVlPrecoUnitarioDaLicitacao());
		servicoBD.setPcBdiLicitado(this.getPcBdiLicitado());
		servicoBD.setVersao(this.versao);
		servicoBD.setEventoFk(this.eventoFk);
		servicoBD.setMacroServicoFk(this.macroServicoFk);
		servicoBD.setTxObservacao(this.txObservacao);
		servicoBD.setVlCustoUnitario(this.getVlCustoUnitario());
		servicoBD.setVlCustoUnitarioDatabase(this.getVlCustoUnitarioDataBase());

		return servicoBD;
	}

	public List<ServicoFrenteObraBD> converteFrenteObrasParaBD() {
		return frentesObra.stream().map(servicoFrenteObraDTO -> {
			ServicoFrenteObraBD servicoFrenteObraBD = servicoFrenteObraDTO.converterParaBD();
			servicoFrenteObraBD.setServicoFk(this.id);

			return servicoFrenteObraBD;
		}).collect(Collectors.toList());
	}

	private boolean sinapiPossuiOcorrenciaNaDataBaseDeReferencia() {
		return this.sinapiPossuiOcorrenciaNaDataBaseDeReferencia;
	}

	private boolean ehServicoComFonteSinapi() {
		final String SINAPI = "SINAPI";

		return SINAPI.equalsIgnoreCase(getInFonte());
	}

	protected BigDecimal getCustoUnitarioNaDataBase() {
		return po.getInDesonerado() ? sinapi.getVlDesonerado() : sinapi.getVlNaoDesonerado();
	}

	protected BigDecimal getBDIAceitoNaAnalise() {
		return getPcBdi().divide(BigDecimal.valueOf(100)).add(BigDecimal.ONE);
	}

}
