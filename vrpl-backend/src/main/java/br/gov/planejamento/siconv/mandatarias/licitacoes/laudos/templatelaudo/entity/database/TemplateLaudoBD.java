package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.dto.TemplateLaudoDTO;
import lombok.Data;

@Data
public class TemplateLaudoBD {

    @ColumnName("id")
    public Long id;

    @NotNull
    @ColumnName("versao_nr")
    public Long versaoNr;

    @ColumnName("versao_nm_evento")
    public String versaoNmEvento;

    @ColumnName("tipo")
    public String tipo;

    @ColumnName("versao_id")
    public String versaoId;

	@ColumnName("versao")
	public Long versao;

    public TemplateLaudoDTO converterParaDTO(){
    	TemplateLaudoDTO resultado = new TemplateLaudoDTO();
    	resultado.setId(id);
    	resultado.setTipo(tipo);

        return resultado;
    }
}

















