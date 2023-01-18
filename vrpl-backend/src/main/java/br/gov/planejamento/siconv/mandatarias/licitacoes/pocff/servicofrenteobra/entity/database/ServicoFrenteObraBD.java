package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.NotImplementedException;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import lombok.Data;

@Data
public class ServicoFrenteObraBD {

    @NotNull
    @ColumnName("frente_obra_fk")
	private Long frenteObraFk;

    @NotNull
    @ColumnName("qt_itens")
	private BigDecimal qtItens = BigDecimal.ZERO;

    @NotNull
    @ColumnName("servico_fk")
	private Long servicoFk;
    
	@ColumnName("versao_nr")
	private Long versaoNr;
	
	@ColumnName("versao")
	private Long versao;
	
	@ColumnName("versao_id")
	private String versaoId;
	
	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    public ServicoFrenteObraDTO converterParaDTO(){
		throw new NotImplementedException("Método não implementado!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ServicoFrenteObraBD that = (ServicoFrenteObraBD) o;
        return Objects.equals(frenteObraFk, that.frenteObraFk) && Objects.equals(servicoFk, that.servicoFk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(frenteObraFk, servicoFk);
    }
}
