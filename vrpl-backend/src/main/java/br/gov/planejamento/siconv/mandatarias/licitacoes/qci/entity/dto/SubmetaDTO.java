package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum;
import lombok.Data;

@Data
public class SubmetaDTO implements Comparable<SubmetaDTO> {
    private Long id;
    private String descricao;
    private Long lote;
    private BigDecimal vlRepasse;
    private BigDecimal vlContrapartida;
    private BigDecimal vlOutros;
    private Long idMeta;
    private BigDecimal vlTotal;
    private Long numero;

	@JsonIgnore
    private SituacaoLicitacaoEnum situacaoDoProcessoLicitatorio;

	@JsonIgnore
    private String situacao;
    private String regimeExecucao;
    private Long itemPad;
    private String descricaoItemPad;
	private Long versao;

    public static SubmetaDTO from(SubmetaBD submetaBD) {
        SubmetaDTO submetaDto = new SubmetaDTO();
        submetaDto.setId(submetaBD.getId());
        submetaDto.setDescricao(submetaBD.getDescricao());
        submetaDto.setLote(submetaBD.getLote());
        submetaDto.setVlRepasse(submetaBD.getVlRepasse());
        submetaDto.setVlContrapartida(submetaBD.getVlContrapartida());
        submetaDto.setVlOutros(submetaBD.getVlOutros());
        submetaDto.setVlTotal(submetaBD.getVlTotalLicitado());
        submetaDto.setNumero(submetaBD.getNumero());
        submetaDto.setRegimeExecucao(submetaBD.getRegimeExecucaoAnalise());
        submetaDto.setItemPad(submetaBD.getItemPad());
		submetaDto.setVersao(submetaBD.getVersao());

		if (submetaBD.getSituacao() != null) {
			SituacaoLicitacaoEnum situacaoVRPL = SituacaoLicitacaoEnum.fromSigla(submetaBD.getSituacao());
			submetaDto.setSituacaoDoProcessoLicitatorio(situacaoVRPL);
		}

		if (submetaBD.getSituacaoAnalise() != null) {
			SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum situacaoAnalise = SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum
					.fromSigla(submetaBD.getSituacaoAnalise());
			submetaDto.setSituacao(situacaoAnalise.getDescricao());
		}

        return submetaDto;
    }

    public static SubmetaDTO from(SubmetaIntegracao submetaIntegracao) {
        SubmetaDTO submetaDto = new SubmetaDTO();
        submetaDto.setDescricao(submetaIntegracao.getDescricao());
        submetaDto.setLote(submetaIntegracao.getNumeroLote());
        submetaDto.setVlRepasse(submetaIntegracao.getValorRepasse());
        submetaDto.setVlContrapartida(submetaIntegracao.getValorContrapartida());
        submetaDto.setVlOutros(submetaIntegracao.getValorOutros());
        submetaDto.setVlTotal(submetaIntegracao.getValorAceitoNaAnalise());
        submetaDto.setNumero(submetaIntegracao.getNumero());
        SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum situacao = SituacaoDoDocumentoOrcamentarioNoProjetoBasicoEnum
                .fromSigla(submetaIntegracao.getSituacao());

        submetaDto.setSituacao(situacao.getDescricao());

        return submetaDto;
    }

    public String getDescricaoSituacaoVerificacaoLicitacao() {
		return situacaoDoProcessoLicitatorio == null ? null : situacaoDoProcessoLicitatorio.getDescricao();
    }

    public String getDescricaoSituacao() {
        return this.situacao;
    }

    public String getDescricaoRegimeExecucao() {
        RegimeExecucaoEnum execucaoEnum = RegimeExecucaoEnum.fromSigla(this.regimeExecucao);
        return execucaoEnum == null ? "" : execucaoEnum.getDescricao();
    }

    @Override
    public int compareTo(SubmetaDTO o) {
        return this.numero.compareTo(o.numero);
    }
}
