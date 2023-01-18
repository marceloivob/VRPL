package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.Data;

@Data
public class PropostaDTO {

	private Long idProposta;
	private Integer numeroProposta;
	private Integer anoProposta;
	private BigDecimal valorGlobal;
	private BigDecimal valorRepasse;
	private BigDecimal valorContrapartida;
	private Integer modalidade;
	private String objeto;
	private Integer numeroConvenio;
	private Integer anoConvenio;
	private LocalDate dataAssinaturaConvenio;
	private String codigoPrograma;
	private String nomePrograma;
	private String identificadorDoProponente;
	private String nomeProponente;
	private String uf;
	private BigDecimal percentualMinimoContrapartida;
	private String nomeMandataria;
	private String categoria;
	private String nivelContrato;
	private String apelidoDoEmpreendimento;
	private Boolean versaoAtual;
	private Integer versaoSelecionada;
	private Boolean termoCompromissoTemMandatar;
	private Integer propostaAtual; // TODO Atualizar o nome deste atributo para evitar confusao com o atributo
									// versaoAtual
	private Long versao;
	private List<Long> versoes;

	public PropostaDTO(PropostaBD propostaBD, List<Long> versoes) {
		this.idProposta = propostaBD.getIdSiconv();
		this.numeroProposta = propostaBD.getNumeroProposta();
		this.anoProposta = propostaBD.getAnoProposta();
		this.valorGlobal = propostaBD.getValorGlobal();
		this.valorRepasse = propostaBD.getValorRepasse();
		this.valorContrapartida = propostaBD.getValorContrapartida();
		this.modalidade = propostaBD.getModalidade();
		this.objeto = propostaBD.getNomeObjeto();
		this.numeroConvenio = propostaBD.getNumeroConvenio();
		this.anoConvenio = propostaBD.getAnoConvenio();
		this.dataAssinaturaConvenio = propostaBD.getDataAssinaturaConvenio();
		this.codigoPrograma = propostaBD.getCodigoPrograma();
		this.nomePrograma = propostaBD.getNomePrograma();
		this.identificadorDoProponente = propostaBD.getIdentificacaoProponente();
		this.nomeProponente = propostaBD.getNomeProponente();
		this.uf = propostaBD.getUf();
		this.percentualMinimoContrapartida = propostaBD.getPcMinContrapartida();
		this.nomeMandataria = propostaBD.getNomeMandataria();
		this.categoria = propostaBD.getCategoria();
		this.nivelContrato = propostaBD.getNivelContrato();
		this.versao = propostaBD.getVersao();
		this.versoes = versoes;
		this.propostaAtual = propostaBD.getVersaoNr();
		this.versaoAtual = propostaBD.getVersaoAtual();
		this.versaoSelecionada = propostaBD.getVersaoNr();
		this.apelidoDoEmpreendimento = propostaBD.getApelidoEmpreendimento();
		this.termoCompromissoTemMandatar = propostaBD.getTermoCompromissoTemMandatar();
	}

	public String getDescricaoModalidade() {
		if (this.modalidade == null) {
			return "";
		}

		return ModalidadePropostaEnum.fromCodigo(this.modalidade, this.termoCompromissoTemMandatar).getDescricao();
	}

}
