package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;

public interface AnexoDAO {

	/***
	 * Conforme RN 511478-SICONV-DocumentosOrcamentarios-ManterAnexo-RN-Listagem:
	 * <p>
	 * A exibição deve ser feita na ordem dos itens incluídos por data de inclusão,
	 * apresentando do mais recente até o mais antigo.
	 * </p>
	 * https://alm.serpro/rm/resources/_rZ0BwcigEeeaG7ogmTcBIg?oslc_config.context=https%3A%2F%2Falm.serpro%2Frm%2Fcm%2Fstream%2F_Kc6U8AMOEeiiA79T4NHyOQ
	 * <p>
	 * Conforme RN
	 * 579357-SICONV-DocumentosOrcamentarios-ManterAnexo-RN-TipoAnexoManterAnexo-VRPL
	 * </p>
	 * Tipos de anexo fase de Verificação do Resultado de Processo Licitatório:
	 * <ul>
	 * <li>Ata de Homologação da Licitação</li>
	 * <li>Despacho de Adjudicação</li>
	 * <li>Publicação do Resumo do Edital</li>
	 * <li>Outros</li>
	 * </ul>
	 * https://alm.serpro/rm/resources/_i-CyEWgoEemSoekbIDX-aQ?oslc_config.context=https%3A%2F%2Falm.serpro%2Frm%2Fcm%2Fstream%2F_Kc6U8AMOEeiiA79T4NHyOQ
	 * 
	 * @param identificadorDaLicitacao Identificador Da Licitacao
	 */
	@SqlQuery
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	List<AnexoBD> findAnexosByIdLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);
	
	@SqlQuery
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	List<AnexoBD> findTodosAnexosByIdLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	Long insertAnexo(@BindBean AnexoBD anexo);

	@SqlUpdate
	@DefineUserInSession
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	Long deleteAnexo(@BindBean AnexoBD anexo);

	@SqlUpdate
	@DefineUserInSession
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	@GetGeneratedKeys
	AnexoBD updateAnexo(@BindBean AnexoBD anexo);

	@SqlQuery
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	AnexoBD recuperarAnexo(@Bind("idAnexo") Long idAnexo);

	@SqlQuery
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	List<AnexoBD> recuperarOutrosAnexosDaLicitacao(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao,
			@Bind("identificadorDoAnexo") Long identificadorDoAnexo);

	@SqlUpdate
	@DefineUserInSession
	@GetGeneratedKeys
	@UseStringTemplateSqlLocator
	Boolean deleteAnexoPorIdLicitacao(@BindBean AnexoBD anexo);

	@SqlQuery
	@RegisterFieldMapper(AnexoBD.class)
	@UseStringTemplateSqlLocator
	List<AnexoBD> findAnexosByTipo(@Bind("identificadorDaLicitacao") Long identificadorDaLicitacao,
			@Bind("tipoAnexo") TipoAnexoEnum tipoAnexo);

}
