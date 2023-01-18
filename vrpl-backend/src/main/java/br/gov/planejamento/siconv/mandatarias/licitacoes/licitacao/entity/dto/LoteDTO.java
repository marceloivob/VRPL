package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import lombok.Data;

@Data
public class LoteDTO {

    private Long id;
    private Long numero;
    private Long idLicitacao;
    private Long idFornecedor;
    private FornecedorDTO fornecedor;
    private Long versao;
    private List<SubmetaDTO> submetas = new ArrayList<>();
    
    private String referencia;
    private Boolean vlTotalLicitadoMenorOuIgualRef;
    private Boolean precoUnitarioMenorOuIgualRef;
    private LocalDate previsaoTermino;

    public static LoteDTO from(LoteBD fromBD) {
        LoteDTO lote = new LoteDTO();
        lote.id = fromBD.getId();
        lote.numero = fromBD.getNumeroDoLote();
        lote.idLicitacao = fromBD.getIdentificadorDaLicitacao();
        lote.idFornecedor = fromBD.getIdFornecedor();
		lote.versao = fromBD.getVersao();

        fromBD.getSubmetas().forEach(
            submetaBD -> lote.submetas.add( SubmetaDTO.from(submetaBD) )
        );

        return lote;
    }

    public BigDecimal getValorAceitoAnalise() {
        return submetas.stream()
                .map(SubmetaDTO::getValorAceitoAnalise)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getValorVerificadoLicitado() {
        return submetas.stream()
                .map(SubmetaDTO::getValorLicitado)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<SubmetaDTO> getSubmetas() {
        return this.submetas.stream()
                .sorted(Comparator.comparing(SubmetaDTO::getSubmeta))
                .collect(Collectors.toList());
    }

	@Override
	public String toString() {
		return "LoteDTO [id=" + id + ", numero=" + numero + ", idLicitacao=" + idLicitacao + ", idFornecedor="
				+ idFornecedor + ", fornecedor=" + fornecedor + ", versao=" + versao + ", referencia=" + referencia
				+ ", vlTotalLicitadoMenorOuIgualRef=" + vlTotalLicitadoMenorOuIgualRef
				+ ", precoUnitarioMenorOuIgualRef=" + precoUnitarioMenorOuIgualRef + ", previsaoTermino="
				+ previsaoTermino + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoteDTO other = (LoteDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (versao == null) {
			if (other.versao != null)
				return false;
		} else if (!versao.equals(other.versao))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((versao == null) ? 0 : versao.hashCode());
		return result;
	}

    
}
