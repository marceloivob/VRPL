package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubItemInvestimento;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import lombok.Data;

@Data
public class MetaDTO implements Comparable<MetaDTO> {
    private Long id;
    private Long idMetaAnalise;
    private String descricao;
    private BigDecimal quantidade;
    private Long idSubitemInvestimento;
    private Long numero;

    private SubItemInvestimento subItemInvestimento = null;

    private List<SubmetaDTO> submetas = new ArrayList<>();

    public static MetaDTO from(MetaBD metaBD) {
        MetaDTO metaDto = new MetaDTO();
        metaDto.setId(metaBD.getId());
        metaDto.setIdMetaAnalise(metaBD.getIdMetaAnalise());
        metaDto.setNumero(metaBD.getNumero());
        metaDto.setDescricao(metaBD.getDescricao());
        metaDto.setQuantidade(metaBD.getQuantidade());
        metaDto.setIdSubitemInvestimento(metaBD.getSubItemFk());

        metaDto.subItemInvestimento = new SubItemInvestimento();
        metaDto.subItemInvestimento.setId(metaBD.getSubItemFk());
        metaDto.subItemInvestimento.setCodigoUnidade(metaBD.getSubitem().getCodigoUnd());
        metaDto.subItemInvestimento.setDescricao(metaBD.getSubitem().getDescricao());
        metaDto.subItemInvestimento.setTipoProjetoSocial(metaBD.getSubitem().getInProjetoSocial());

        metaDto.submetas = metaBD.getSubmetas().stream()
                .map( SubmetaDTO::from )
                .collect(Collectors.toList());

        return metaDto;
    }

    public static MetaDTO from(MetaIntegracao metaIntegracao) {
        MetaDTO metaDto = new MetaDTO();
        metaDto.setIdMetaAnalise(metaIntegracao.getId());
        metaDto.setNumero(metaIntegracao.getNumero());
        metaDto.setDescricao(metaIntegracao.getDescricao());
        metaDto.setQuantidade(metaIntegracao.getQtItens());
        metaDto.setIdSubitemInvestimento(metaIntegracao.getSubitemInvestimentoFk());

        metaDto.subItemInvestimento = new SubItemInvestimento();
        metaDto.subItemInvestimento.setId(metaIntegracao.getSubitemInvestimentoFk());
        metaDto.subItemInvestimento.setCodigoUnidade(metaIntegracao.getSubitemCodigoUnidade());
        metaDto.subItemInvestimento.setDescricao(metaIntegracao.getSubitemDescricao());
        metaDto.subItemInvestimento.setTipoProjetoSocial(metaIntegracao.getSubitemTipoProjetoSocial());

        metaDto.submetas = metaIntegracao.getSubmetas().stream()
                .map( SubmetaDTO::from )
				.sorted(Comparator.comparing(SubmetaDTO::getNumero))
                .collect(Collectors.toList());

        return metaDto;

    }

    public BigDecimal getVlRepasse() {
        return submetas.stream()
                .map( submeta -> submeta.getVlRepasse() != null ? submeta.getVlRepasse() : BigDecimal.ZERO )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getVlContrapartida() {
        return submetas.stream()
                .map( submeta -> submeta.getVlContrapartida() != null ? submeta.getVlContrapartida() : BigDecimal.ZERO )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getVlOutros() {
        return submetas.stream()
                .map( submeta -> submeta.getVlOutros() != null ? submeta.getVlOutros() : BigDecimal.ZERO )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getVlTotal() {
        return submetas.stream()
                .map( submeta -> submeta.getVlTotal() != null ? submeta.getVlTotal() : BigDecimal.ZERO )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public int compareTo(MetaDTO o) {
        return this.numero.compareTo(o.numero);
    }
}
