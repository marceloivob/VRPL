package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Dados básicos de uma proposta")
public class DadosBasicosIntegracao {

    @ColumnName("PROP_SICONV_ID")
	private Long idProposta;

    @ColumnName("NUMERO_PROPOSTA")
    private Integer numeroProposta;

    @ColumnName("ANO_PROPOSTA")
    private Integer anoProposta;

    @ColumnName("VALOR_GLOBAL")
    private BigDecimal valorGlobal;

    @ColumnName("VALOR_REPASSE")
    private BigDecimal valorRepasse;

    @ColumnName("VALOR_CONTRA_PARTIDA")
    private BigDecimal valorContrapartida;

    @ColumnName("MODALIDADE")
    private Integer modalidade;

    @ColumnName("NOME_OBJETO")
    private String objeto;

    @ColumnName("NUMERO_CONVENIO")
    private Integer numeroConvenio;

    @ColumnName("ANO_CONVENIO")
    private Integer anoConvenio;

    @ColumnName("DATA_ASSINATURA_CONVENIO")
    private LocalDate dataAssinaturaConvenio;

    @ColumnName("CODIGO_PROGRAMA")
    private String codigoPrograma;

    @ColumnName("NOME_PROGRAMA")
    private String nomePrograma;

    @ColumnName("IDENTIFICACAO_PROPONENTE")
    private String identificadorDoProponente;

    @ColumnName("NOME_PROPONENTE")
    private String nomeProponente;

    @ColumnName("UF")
    private String uf;

    @ColumnName("PC_MIN_CONTRA_PARTIDA")
    private BigDecimal percentualMinimoContrapartida;

    @ColumnName("NOME_MANDATARIA")
    private String nomeMandataria;

    @ColumnName("CATEGORIA")
    private String categoria;

    @ColumnName("NIVEL_CONTRATO")
    private String nivelContrato;

    @ColumnName("TX_APELIDO")
    private String apelidoDoEmpreendimento;

    @ColumnName("IN_SITUACAO")
    private String situacaoAcffo;
    
    @ColumnName("TERMO_COMPROMISSO_TEM_MANDATAR")
    private Boolean termoCompromissoTemMandataria;

    public String getDescricaoModalidade() {
        if (this.modalidade == null) {
            return "";
        }

        return ModalidadePropostaEnum.fromCodigo(this.modalidade, this.termoCompromissoTemMandataria).getDescricao();
    }

}