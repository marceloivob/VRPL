package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class AnexoBD {

	@ColumnName("id")
	private Long identificadorDoAnexo;

	@ColumnName("vrpl_licitacao_fk")
	@NotNull
	private Long identificadorDaLicitacao;

	@ColumnName("nm_arquivo")
	@NotNull
	@Size(max = 100)
	private String nomeDoArquivo;

	@ColumnName("caminho")
	@JsonIgnore
	@NotNull
	private String caminhoDoArquivo;

	@JsonIgnore
	@NotNull
	@Size(max = 25)
	private String bucket;

	@ColumnName("dt_upload")
	@NotNull
	private LocalDate dataDoUpload = LocalDate.now();

	@ColumnName("tx_descricao")
	@NotNull
	@Size(max = 30)
	private String descricaoDoAnexo;

	@ColumnName("in_tipoanexo")
	@NotNull
	private TipoAnexoEnum tipoDoAnexo;

	@ColumnName("id_cpf_enviadopor")
	@JsonIgnore
	@NotNull
	@Size(max = 11)
	private String cpfDoUsuarioQueEnviou;

	@ColumnName("nome_enviadopor")
	@NotNull
	@Size(max = 60)
	private String nomeDoUsuarioQueEnviou;

	@ColumnName("in_perfil")
	@NotNull
	@Size(max = 10)
	private String perfilDoUsuarioQueEnviou;

	@NotNull
	@ColumnName("versao_nr")
	private Long versaoNr = 0L;

	@ColumnName("versao_nm_evento")
	@JsonIgnore
	private String versaoNmEvento;

	@ColumnName("versao_id")
	@JsonIgnore
	private String versaoId;

	@ColumnName("versao")
	private Long versao;
}
