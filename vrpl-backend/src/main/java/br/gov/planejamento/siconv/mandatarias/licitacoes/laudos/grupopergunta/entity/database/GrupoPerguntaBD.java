package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import lombok.Data;

@Data
public class GrupoPerguntaBD {

	@ColumnName("id")
	private Long id;

	@NotNull
	@ColumnName("titulo")
	private String titulo;

	@NotNull
	@ColumnName("versao_nr")
	private Long versaoNr;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;
	
	@NotNull
	@ColumnName("template_fk")
	private Long templateFk;
	
	@ColumnName("versao_id")
	private String versaoId;

	@NotNull
	@ColumnName("in_grupo_obrigatorio")
	private Boolean inGrupoObrigatorio;

	@NotNull
	@ColumnName("numero")
	private Long numero;
	
	@NotNull
	@ColumnName("versao")
	private Long versao;

	public GrupoPerguntaDTO converterParaDTO() {
		GrupoPerguntaDTO dto = new GrupoPerguntaDTO();

		dto.setGrupoId(id);
		dto.setInGrupoObrigatorio(inGrupoObrigatorio);
		dto.setNumero(numero);
		dto.setTemplateFk(templateFk);
		dto.setTitulo(titulo);
		dto.setVersao(versao);

		return dto;
	}
}
