package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.dto.PendenciaDTO;
import lombok.Data;

@Data
public class PendenciaBD {

	@ColumnName("id")
	private Long id;

	@NotNull
	@ColumnName("in_resolvida")
	private Boolean inResolvida;

	@NotNull
	@ColumnName("prazo")
	private String prazo;

	@NotNull
	@ColumnName("laudo_fk")
	private Long laudoFk;

	@NotNull
	@ColumnName("descricao")
	private String descricao;

	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;
	
	@ColumnName("submeta_fk")
	private Long submetaFk;

	public PendenciaDTO converterParaDTO() {
		PendenciaDTO dto = new PendenciaDTO();
		dto.setDescricao(descricao);
		dto.setId(id);
		dto.setInResolvida(inResolvida);
		dto.setLaudoFk(laudoFk);
		dto.setPrazo(prazo);
		dto.setSubmetaFk(submetaFk);
		dto.setVersao(versao);
		return dto;
	}
}
