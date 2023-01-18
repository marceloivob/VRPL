package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import lombok.Data;

@Data
public class LaudoBD {

	@ColumnName("id")
	private Long id;

	@NotNull
	@ColumnName("licitacao_fk")
	private Long licitacaoFk;

	@NotNull
	@ColumnName("versao_nr")
	private Long versaoNr;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;
	
	@NotNull
	@ColumnName("template_fk")
	private Long templateFk;

	@ColumnName("in_resultado")
	private String inResultado;

	@ColumnName("versao_id")
	private String versaoId;

	@NotNull
	@ColumnName("in_status")
	private Integer inStatus;
	
	@NotNull
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("adt_login")
	private String adtLogin;

	public LaudoDTO converterParaDTO() {
		LaudoDTO dto = new LaudoDTO();
		dto.setId(id);
		dto.setInResultado(inResultado);
		dto.setInStatus(inStatus);
		dto.setLicitacaoFk(licitacaoFk);
		dto.setTemplateFk(templateFk);
		dto.setVersao(versao);
		dto.setVersaoId(versaoId);
		dto.setVersaoNmEvento(versaoNmEvento);
		dto.setVersaoNr(versaoNr);
		dto.setAdtLogin(adtLogin);
		
		return dto;
	}
}
