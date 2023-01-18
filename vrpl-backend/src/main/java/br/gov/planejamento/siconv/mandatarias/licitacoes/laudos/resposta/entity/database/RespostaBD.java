package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import lombok.Data;

@Data
public class RespostaBD {

    @ColumnName("id")
    public Long id;

    @ColumnName("resposta")
    public String resposta;

    @NotNull
    @ColumnName("laudo_fk")
    public Long laudoFk;

    @NotNull
    @ColumnName("pergunta_fk")
    public Long perguntaFk;

	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;
	
	@ColumnName("lote_fk")
	private Long loteFk;

    public RespostaDTO converterParaDTO(){
    	RespostaDTO dto = new RespostaDTO();
		dto.setRespostaId(id);
    	dto.setResposta(resposta);
    	dto.setVersao(versao);
    	return dto;
    }
}
