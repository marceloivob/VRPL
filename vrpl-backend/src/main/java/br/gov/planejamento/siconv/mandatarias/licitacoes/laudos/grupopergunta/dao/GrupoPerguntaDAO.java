package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database.GrupoPerguntaBD;

public interface GrupoPerguntaDAO {

	@SqlQuery("SELECT * FROM siconv.vrpl_grupo_pergunta evento WHERE id = :id")
	@RegisterFieldMapper(GrupoPerguntaBD.class)
	GrupoPerguntaBD recuperarGrupoPerguntaPorId(@Bind("id") Long id);

	@SqlQuery("SELECT * FROM siconv.vrpl_grupo_pergunta evento WHERE template_fk = :idTemplateLaudo ORDER BY numero")
	@RegisterFieldMapper(GrupoPerguntaBD.class)
	List<GrupoPerguntaBD> recuperarGruposPerguntaPorTemplate(@Bind("idTemplateLaudo") Long idTemplateLaudo);

}
