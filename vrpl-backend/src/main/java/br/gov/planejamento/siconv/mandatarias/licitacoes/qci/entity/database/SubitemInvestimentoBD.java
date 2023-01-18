package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubitemInvestimentoIntegracao;
import lombok.Data;

@Data
public class SubitemInvestimentoBD {
	
    private Long id;
    
    @NotNull
    private Long idSubitemAnalise;
    
    @NotNull
    private String descricao;
    
    private String inProjetoSocial;
    
    private String codigoUnd;
    
    private String descricaoUnd;
    
	@ColumnName("versao_nr")
	@NotNull
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    public static SubitemInvestimentoBD from(SubitemInvestimentoIntegracao subitemIntegracao) {
        SubitemInvestimentoBD subitemBD = new SubitemInvestimentoBD();
        subitemBD.setIdSubitemAnalise(subitemIntegracao.getId());
        subitemBD.setDescricao(subitemIntegracao.getDescricao());
        subitemBD.setInProjetoSocial(subitemIntegracao.getInProjetoSocial());
        subitemBD.setCodigoUnd(subitemIntegracao.getCodigoUnd());
        subitemBD.setDescricaoUnd(subitemIntegracao.getDescricaoUnd());

        return subitemBD;
    }
}
