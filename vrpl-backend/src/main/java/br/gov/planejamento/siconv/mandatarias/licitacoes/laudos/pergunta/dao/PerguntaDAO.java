package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.database.PerguntaBD;

public interface PerguntaDAO {

    @SqlQuery("SELECT * FROM siconv.vrpl_pergunta WHERE id = :id")
    @RegisterFieldMapper(PerguntaBD.class)
    PerguntaBD recuperarPerguntaPorId(@Bind ("id") Long id);

    @SqlQuery("SELECT * FROM siconv.vrpl_pergunta WHERE grupo_fk = :idGrupo order by numero")
    @RegisterFieldMapper(PerguntaBD.class)
	List<PerguntaBD> recuperarPerguntaPorGrupo(@Bind ("idGrupo") Long idGrupo);

}
