package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.dto;

import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import lombok.Data;

@Data
public class TemplateLaudoDTO {

	private Long id;

	private Long versaoNr;

	private String versaoNmEvento;

	private String tipo;

	private String versaoId;

	private Long versao;

	private List<GrupoPerguntaDTO> grupos;

}
