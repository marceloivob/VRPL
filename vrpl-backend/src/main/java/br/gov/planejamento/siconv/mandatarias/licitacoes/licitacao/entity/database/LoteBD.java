package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import lombok.Data;

@Data
public class LoteBD {

    @ColumnName("id")
    private Long id;

    @ColumnName("numero_lote")
    @NotNull
    private Long numeroDoLote;

    @ColumnName("licitacao_fk")
    private Long identificadorDaLicitacao;

    @ColumnName("fornecedor_fk")
    private Long idFornecedor;

    @ColumnName("versao_nr")
    @NotNull
    private Integer numeroVersao;

	@ColumnName("versao")
    @NotNull
    private Long versao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;

    private List<SubmetaBD> submetas = new ArrayList<>();

    public SubmetaBD addSubmeta(SubmetaBD submetaBD) {
        int pos = this.submetas.indexOf(submetaBD);
        if (pos == -1) {
            this.submetas.add(submetaBD);
            return submetaBD;
        }

        return this.submetas.get(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoteBD loteBD = (LoteBD) o;
        return numeroDoLote.equals(loteBD.numeroDoLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroDoLote);
    }
}
