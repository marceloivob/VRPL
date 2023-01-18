package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import lombok.Data;

@Data
public class PerguntaBD {

	@ColumnName("id")
	private Long id;
	
	@NotNull
	@ColumnName("numero")
	private Long numero;
	
	@NotNull
	@ColumnName("titulo")
	private String titulo;

	@NotNull
	@ColumnName("valor_esperado")
	private String valorEsperado;

	@NotNull
	@ColumnName("tipo_resposta")
	private String tipoResposta;

	@NotNull
	@ColumnName("grupo_fk")
	private Long grupoFk;

	@NotNull
	@ColumnName("valor_resposta")
	private String valorResposta;

	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

	public PerguntaDTO converterParaDTO() {
		PerguntaDTO dto = new PerguntaDTO();

		dto.setPerguntaId(id);
		dto.setNumero(numero);
		dto.setTipoResposta(tipoResposta);
		dto.setTitulo(titulo);
		dto.setValorEsperado(valorEsperado);
		dto.setValorResposta(valorResposta);

		return dto;
	}
}
