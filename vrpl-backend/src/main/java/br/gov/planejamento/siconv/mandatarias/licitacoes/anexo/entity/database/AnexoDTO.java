package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import lombok.Data;

@Data
public class AnexoDTO {

	// Usado para gerar o link para Download
	@JsonIgnore
	private String key;

    private String bucket;

    private String nomeDoArquivo;

    private Long identificadorDoAnexo;

    private String linkToDownload;

	private LocalDate dataDoUpload;

    private String descricaoDoAnexo;

	private TipoAnexoEnum tipoDoAnexo;

	private String tipoDoAnexoAsString;

    private String nomeDoUsuarioQueEnviou;

	private String perfilDoUsuarioQueEnviou;

	private Long versao;

    public AnexoDTO from(AnexoBD fromDatabase) {
        AnexoDTO anexoDTO = new AnexoDTO();
        anexoDTO.setIdentificadorDoAnexo(fromDatabase.getIdentificadorDoAnexo());
		anexoDTO.setNomeDoArquivo(fromDatabase.getNomeDoArquivo());
        anexoDTO.setDataDoUpload(fromDatabase.getDataDoUpload());
        anexoDTO.setDescricaoDoAnexo(fromDatabase.getDescricaoDoAnexo());
		anexoDTO.setTipoDoAnexo(fromDatabase.getTipoDoAnexo());
		anexoDTO.setTipoDoAnexoAsString(fromDatabase.getTipoDoAnexo().getDescription());
		anexoDTO.setPerfilDoUsuarioQueEnviou(
				Profile.valueOf(fromDatabase.getPerfilDoUsuarioQueEnviou()).getDescription());
		anexoDTO.setKey(fromDatabase.getCaminhoDoArquivo());
        anexoDTO.setNomeDoUsuarioQueEnviou(fromDatabase.getNomeDoUsuarioQueEnviou());
		anexoDTO.setVersao(fromDatabase.getVersao());
        anexoDTO.setBucket(fromDatabase.getBucket());

        return anexoDTO;
    }


}
