package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.dao;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.database.TemplateLaudoBD;

public interface TemplateLaudoDAO {

	@SqlQuery("SELECT * FROM siconv.vrpl_template_laudo WHERE id = :id")
	@RegisterFieldMapper(TemplateLaudoBD.class)
	TemplateLaudoBD recuperarTemplateLaudoPorId(@Bind("id") Long id);

	@SqlQuery("SELECT * FROM siconv.vrpl_template_laudo WHERE tipo = :tipoTemplateLaudo")
	@RegisterFieldMapper(TemplateLaudoBD.class)
	TemplateLaudoBD recuperarTemplateLaudoPorTipo(@Bind("tipoTemplateLaudo") String tipoTemplateLaudo);

}
