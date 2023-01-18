package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "grupoId", scope = GrupoPerguntaDTO.class)
public class GrupoPerguntaDTO {

	private Long grupoId;

	private String titulo;

	private Long versaoNr;

	private String versaoNmEvento;

	private Long templateFk;

	private String versaoId;

	private Boolean inGrupoObrigatorio;

	private Long numero;

	private Long versao;

	private List<PerguntaDTO> perguntas;

}
