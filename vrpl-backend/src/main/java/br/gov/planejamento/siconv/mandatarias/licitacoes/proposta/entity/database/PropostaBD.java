package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import lombok.Data;

@Data
public class PropostaBD {

    private Long id;
    private Long idSiconv;
    private Integer numeroProposta;
    private Integer anoProposta;
    private BigDecimal valorGlobal;
    private BigDecimal valorRepasse;
    private BigDecimal valorContrapartida;
    private Integer modalidade;
    private String nomeObjeto;
    private Integer numeroConvenio;
    private Integer anoConvenio;
    private LocalDate dataAssinaturaConvenio;
    private String codigoPrograma;
    private String nomePrograma;
    private String identificacaoProponente;
    private String nomeProponente;
    private String uf;
    private BigDecimal pcMinContrapartida;
    private String nomeMandataria;
    private String categoria;
    private String nivelContrato;
    private Boolean spaHomologado;
    private Long versao;
    private String versaoId;
    private String versaoNmEvento;
    private Integer versaoNr;
    private String apelidoEmpreendimento;
    private Boolean termoCompromissoTemMandatar;

	// TODO Renomear atributo versaoAtual para versaoInAtual e alterar nas consultas
	@ColumnName(value = "versao_in_atual")
	private Boolean versaoAtual;

	public Boolean getVersaoInAtual() {
		return this.getVersaoAtual();
	}

	public void setVersaoInAtual(Boolean versaoInAtual) {
		this.setVersaoAtual(versaoInAtual);
	}

	public Boolean ehVersaoAtual() {
		// https://stackoverflow.com/questions/25824269/java-lang-nullpointerexception-with-boolean
		return versaoAtual != null && Boolean.TRUE.equals(versaoAtual);
	}

	public static PropostaBD from(DadosBasicosIntegracao integracao) {
        PropostaBD proposta = new PropostaBD();

		proposta.idSiconv = integracao.getIdProposta();
        proposta.numeroProposta = integracao.getNumeroProposta();
        proposta.anoProposta = integracao.getAnoProposta();
        proposta.valorGlobal = integracao.getValorGlobal();
        proposta.valorRepasse = integracao.getValorRepasse() ;
        proposta.valorContrapartida = integracao.getValorContrapartida();
        proposta.modalidade = integracao.getModalidade();
        proposta.nomeObjeto = integracao.getObjeto();
        proposta.numeroConvenio = integracao.getNumeroConvenio();
        proposta.anoConvenio = integracao.getAnoConvenio();
        proposta.dataAssinaturaConvenio = integracao.getDataAssinaturaConvenio();
        proposta.codigoPrograma = integracao.getCodigoPrograma();
        proposta.nomePrograma = integracao.getNomePrograma();
        proposta.identificacaoProponente = integracao.getIdentificadorDoProponente();
        proposta.nomeProponente = integracao.getNomeProponente();
        proposta.uf = integracao.getUf();
        proposta.pcMinContrapartida = integracao.getPercentualMinimoContrapartida();
        proposta.nomeMandataria = integracao.getNomeMandataria();
        proposta.categoria = integracao.getCategoria();
        proposta.nivelContrato = integracao.getNivelContrato();
		proposta.spaHomologado = true;
		proposta.apelidoEmpreendimento = integracao.getApelidoDoEmpreendimento();
		proposta.termoCompromissoTemMandatar = integracao.getTermoCompromissoTemMandataria();
        return proposta;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdSiconv() {
		return idSiconv;
	}

	public void setIdSiconv(Long idSiconv) {
		this.idSiconv = idSiconv;
	}

	public Integer getNumeroProposta() {
		return numeroProposta;
	}

	public void setNumeroProposta(Integer numeroProposta) {
		this.numeroProposta = numeroProposta;
	}

	public Integer getAnoProposta() {
		return anoProposta;
	}

	public void setAnoProposta(Integer anoProposta) {
		this.anoProposta = anoProposta;
	}

	public BigDecimal getValorGlobal() {
		return valorGlobal;
	}

	public void setValorGlobal(BigDecimal valorGlobal) {
		this.valorGlobal = valorGlobal;
	}

	public BigDecimal getValorRepasse() {
		return valorRepasse;
	}

	public void setValorRepasse(BigDecimal valorRepasse) {
		this.valorRepasse = valorRepasse;
	}

	public BigDecimal getValorContrapartida() {
		return valorContrapartida;
	}

	public void setValorContrapartida(BigDecimal valorContrapartida) {
		this.valorContrapartida = valorContrapartida;
	}

	public Integer getModalidade() {
		return modalidade;
	}

	public void setModalidade(Integer modalidade) {
		this.modalidade = modalidade;
	}

	public String getNomeObjeto() {
		return nomeObjeto;
	}

	public void setNomeObjeto(String nomeObjeto) {
		this.nomeObjeto = nomeObjeto;
	}

	public Integer getNumeroConvenio() {
		return numeroConvenio;
	}

	public void setNumeroConvenio(Integer numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}

	public Integer getAnoConvenio() {
		return anoConvenio;
	}

	public void setAnoConvenio(Integer anoConvenio) {
		this.anoConvenio = anoConvenio;
	}

	public LocalDate getDataAssinaturaConvenio() {
		return dataAssinaturaConvenio;
	}

	public void setDataAssinaturaConvenio(LocalDate dataAssinaturaConvenio) {
		this.dataAssinaturaConvenio = dataAssinaturaConvenio;
	}

	public String getCodigoPrograma() {
		return codigoPrograma;
	}

	public void setCodigoPrograma(String codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	public String getNomePrograma() {
		return nomePrograma;
	}

	public void setNomePrograma(String nomePrograma) {
		this.nomePrograma = nomePrograma;
	}

	public String getIdentificacaoProponente() {
		return identificacaoProponente;
	}

	public void setIdentificacaoProponente(String identificacaoProponente) {
		this.identificacaoProponente = identificacaoProponente;
	}

	public String getNomeProponente() {
		return nomeProponente;
	}

	public void setNomeProponente(String nomeProponente) {
		this.nomeProponente = nomeProponente;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public BigDecimal getPcMinContrapartida() {
		return pcMinContrapartida;
	}

	public void setPcMinContrapartida(BigDecimal pcMinContrapartida) {
		this.pcMinContrapartida = pcMinContrapartida;
	}

	public String getNomeMandataria() {
		return nomeMandataria;
	}

	public void setNomeMandataria(String nomeMandataria) {
		this.nomeMandataria = nomeMandataria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getNivelContrato() {
		return nivelContrato;
	}

	public void setNivelContrato(String nivelContrato) {
		this.nivelContrato = nivelContrato;
	}

	public Boolean getSpaHomologado() {
		return spaHomologado;
	}

	public void setSpaHomologado(Boolean spaHomologado) {
		this.spaHomologado = spaHomologado;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public String getVersaoId() {
		return versaoId;
	}

	public void setVersaoId(String versaoId) {
		this.versaoId = versaoId;
	}

	public String getVersaoNmEvento() {
		return versaoNmEvento;
	}

	public void setVersaoNmEvento(String versaoNmEvento) {
		this.versaoNmEvento = versaoNmEvento;
	}

	public Integer getVersaoNr() {
		return versaoNr;
	}

	public void setVersaoNr(Integer versaoNr) {
		this.versaoNr = versaoNr;
	}

	public String getApelidoEmpreendimento() {
		return apelidoEmpreendimento;
	}

	public void setApelidoEmpreendimento(String apelidoEmpreendimento) {
		this.apelidoEmpreendimento = apelidoEmpreendimento;
	}

	public Boolean getVersaoAtual() {
		return versaoAtual;
	}

	public void setVersaoAtual(Boolean versaoAtual) {
		this.versaoAtual = versaoAtual;
	}

	public String getDescricaoModalidade() {
        if (this.modalidade == null) {
            return "";
        }

        return ModalidadePropostaEnum.fromCodigo(this.modalidade, this.termoCompromissoTemMandatar).getDescricao();
    }

}
