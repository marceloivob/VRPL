package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.PermissaoDTO;

public interface PermissoesDaLicitacaoDAO {

	@SqlQuery
	@UseStringTemplateSqlLocator
	@RegisterBeanMapper(PermissaoDTO.class)
	List<PermissaoDTO> consultarPermissoesDasLicitacoesDaProposta(
			@Bind("identificadorDaProposta") Long identificadorDaProposta);

}
