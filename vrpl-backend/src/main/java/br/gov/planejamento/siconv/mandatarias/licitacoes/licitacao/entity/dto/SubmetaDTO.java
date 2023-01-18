package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import lombok.Data;

@Data
public class SubmetaDTO {

	private Long id;
	private String meta;
	private String submeta;
	private Long numero;
	private Boolean social;
	private Boolean porEventos;
	private String regimeExecucao;
	private Long versao;
	private BigDecimal valorAceitoAnalise = BigDecimal.ZERO;
	private BigDecimal valorLicitado = BigDecimal.ZERO;
	
	private BigDecimal vlRepasse;
    private BigDecimal vlContrapartida;
    private BigDecimal vlOutros;
    
    @JsonIgnore
    private SituacaoLicitacaoEnum situacaoDoProcessoLicitatorio;

	public static SubmetaDTO from(SubmetaBD submetaBD) {
		SubmetaDTO submetaDto = new SubmetaDTO();
		submetaDto.meta = submetaBD.getMetaBD().getNumero() + " - " + submetaBD.getMetaBD().getDescricao();
		submetaDto.submeta = submetaBD.getMetaBD().getNumero() + "." + submetaBD.getNumero() + " - "
				+ submetaBD.getDescricao();

		submetaDto.numero = submetaBD.getNumero();
		submetaDto.id = submetaBD.getId();
		submetaDto.social = submetaBD.getMetaBD().getSocial();
		submetaDto.regimeExecucao = submetaBD.getRegimeExecucao() == null ? null
				: submetaBD.getRegimeExecucao().getDescricao();

		submetaDto.valorAceitoAnalise = submetaBD.getVlTotalAnalise();
		submetaDto.valorLicitado = submetaBD.getVlTotalLicitado();
		submetaDto.porEventos = submetaBD.getInAcompanhamentoEventos();

		submetaDto.versao = submetaBD.getVersao();
		
		submetaDto.setVlRepasse(submetaBD.getVlRepasse());
        submetaDto.setVlContrapartida(submetaBD.getVlContrapartida());
        submetaDto.setVlOutros(submetaBD.getVlOutros());
		
		if (submetaBD.getSituacao() != null) {
			SituacaoLicitacaoEnum situacaoVRPL = SituacaoLicitacaoEnum.fromSigla(submetaBD.getSituacao());
			submetaDto.setSituacaoDoProcessoLicitatorio(situacaoVRPL);
		}

		return submetaDto;
	}
	
	public String getDescricaoSituacaoVerificacaoLicitacao() {
		return situacaoDoProcessoLicitatorio == null ? null : situacaoDoProcessoLicitatorio.getDescricao();
    }
	
	public Long getNumeroMeta() {
		String[] metaSplit = meta.split(" ");
		return Long.parseLong(metaSplit[0]);
	}


}
