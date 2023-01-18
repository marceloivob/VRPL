package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.integracao;

import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import lombok.Data;

@Data
public class FrenteObraIntegracao {
     @ColumnName("id")
     public Long id;

     @NotNull
     @ColumnName("nm_frente_obra")
     public String nmFrenteObra;

     @NotNull
     @ColumnName("nr_frente_obra")
     public Integer nrFrenteObra;

     @NotNull
     @ColumnName("po_fk")
     public Long poFk;

}
