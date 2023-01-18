package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.AllowUnusedBindings;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

@RegisterBeanMapper(Clone.class)
@RegisterBeanMapper(SubitemInvestimentoBD.class)
@RegisterBeanMapper(LicitacaoBD.class)
@RegisterBeanMapper(MetaBD.class)
@RegisterBeanMapper(SubmetaBD.class)
@RegisterBeanMapper(LoteBD.class)
@RegisterBeanMapper(PoBD.class)
@RegisterBeanMapper(EventoBD.class)
@RegisterBeanMapper(FrenteObraBD.class)
@RegisterBeanMapper(MacroServicoBD.class)
@RegisterBeanMapper(MacroServicoParcelaBD.class)
@RegisterBeanMapper(EventoFrenteObraBD.class)
@RegisterBeanMapper(ServicoBD.class)
@RegisterBeanMapper(ServicoFrenteObraBD.class)
@RegisterBeanMapper(ServicoFrenteObraAnaliseBD.class)

@RegisterFieldMapper(Clone.class)
@RegisterFieldMapper(SubitemInvestimentoBD.class)
@RegisterFieldMapper(LicitacaoBD.class)
@RegisterFieldMapper(MetaBD.class)
@RegisterFieldMapper(SubmetaBD.class)
@RegisterFieldMapper(LoteBD.class)
@RegisterFieldMapper(PoBD.class)
@RegisterFieldMapper(EventoBD.class)
@RegisterFieldMapper(FrenteObraBD.class)
@RegisterFieldMapper(MacroServicoBD.class)
@RegisterFieldMapper(MacroServicoParcelaBD.class)
@RegisterFieldMapper(EventoFrenteObraBD.class)
@RegisterFieldMapper(ServicoBD.class)
@RegisterFieldMapper(ServicoFrenteObraBD.class)
@RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)

@UseStringTemplateSqlLocator
public interface LiberarLoteDAO {

	//LoteLicitacao
	@SqlQuery
	List<LoteBD> selectLoteParaClonarPorLicitacao(@BindBean("licitacaoOriginal") LicitacaoBD licitacaoOriginal);

	@SqlQuery
	List<LoteBD> selectLoteParaApagarPorProposta(@BindBean("propostaOriginal") PropostaBD propostaOriginal,
			final @BindBean("eventoCancelaRejeicao") EventoQuadroResumoEnum eventoCancelaRejeicao );

	@SqlQuery
	List<LoteBD> selectLoteLiberadosParaApagarDaLicitacaoRetornandoDaRejeicao(@BindBean("propostaOriginal") PropostaBD propostaOriginal,
			@BindBean("licitacao") LicitacaoBD licitacao);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	LoteBD cloneLicitacaoLote(@BindBean("loteOriginal") LoteBD loteOriginal, @Bind("siglaEvento") String siglaEvento);


	@SqlUpdate
	@DefineUserInSession
	void apagarLicitacaoLotes(@BindList("ids") List<Long> ids);


	//SubitemInvestimento
	@SqlQuery
	List<SubitemInvestimentoBD> selectSubItemDeInvestimentoParaClonarPorLicitacao(
			@BindBean("licitacaoOriginal") LicitacaoBD licitacaoOriginal);

	@SqlQuery
	List<SubitemInvestimentoBD> selectSubItemDeInvestimentoApagar(@BindList("fkSubitem") List<Long> fkSubitem);

	@SqlQuery
	List<SubitemInvestimentoBD> selectSubItemDeInvestimentoNAOApagar(@BindBean("propostaOriginal") PropostaBD propostaOriginal,
			final @BindBean("eventoCancelaRejeicao") EventoQuadroResumoEnum eventoCancelaRejeicao );

	@SqlQuery
	List<MetaBD> selectMetaNAOApagar(@BindBean("propostaOriginal") PropostaBD propostaOriginal,
			final @BindBean("eventoCancelaRejeicao") EventoQuadroResumoEnum eventoCancelaRejeicao );

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	SubitemInvestimentoBD cloneSubItemDeInvestimento(
			@BindBean("subItemDeInvestimento") SubitemInvestimentoBD subItemDeInvestimento,
			@Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarSubitemInvestimentos(@BindList("ids") List<Long> ids);


	//Meta
	@SqlQuery
	List<MetaBD> selectMetaParaClonarPorLicitacao(@BindBean("licitacaoOriginal") LicitacaoBD licitacaoOriginal);

	@SqlQuery
	List<MetaBD> selectMetasApagar(@BindList("idsSubmetas") List<Long> idsSubmetas);

	@SqlQuery
	@GetGeneratedKeys
	@DefineUserInSession
	MetaBD cloneMeta(@BindBean("meta") MetaBD meta,
			@BindBean("subitemInvestimentoClonado") SubitemInvestimentoBD subitemInvestimentoClonado,
			@Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarMetas(@BindList("ids") List<Long> ids);


	//Submeta
	@SqlQuery
	List<SubmetaBD> selectSubmetaParaClonarPorLicitacao(@BindBean("licitacaoOriginal") LicitacaoBD licitacaoOriginal);

	@SqlQuery
	List<SubmetaBD> selectSubmetasApagarPorLicitacao(@BindList("fkLotes") List<Long> fkLotes);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	SubmetaBD cloneSubmetaPorLicitacao(@BindBean("submetaOriginal") SubmetaBD submetaOriginal,
			@BindBean("propostaClonada") PropostaBD propostaClonada, @BindBean("metaClonada") MetaBD metaClonada,
			@BindBean("loteClonado") LoteBD loteClonado, @Bind("siglaEvento") String siglaEvento, @Bind("siglaSituacaoLicitacao") String siglaSituacaoLicitacao);

	@SqlUpdate
	@DefineUserInSession
	void apagarSubmetas(@BindList("ids") List<Long> ids);


	//PO
	@SqlQuery
	List<PoBD> selectPOParaClonar(
			@BindList("idsSubmetasOriginais") List<Long> idsSubmetasOriginais);

	@SqlQuery
	List<PoBD> selectPoParaApagar (@BindList("fkSubmetas") List<Long> fkSubmetas);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	PoBD clonePO(@BindBean("poOriginal") PoBD poOriginal, @BindBean("submetaClonada") SubmetaBD submetaClonada,
			@Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarPOs(@BindList("ids") List<Long> ids);


	//Evento
	@SqlQuery
	List<EventoBD> selectEventoParaClonar(
			@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	List<EventoBD> selectEventoApagar (@BindList("fkPos") List<Long> fkPos);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	EventoBD cloneEvento(@BindBean("eventoOriginal") EventoBD eventoOriginal, @BindBean("poClonado") PoBD poClonado,
			@Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarEventos(@BindList("ids") List<Long> ids);


	//FrenteObra
	@SqlQuery
	List<FrenteObraBD> selectFrenteDeObraParaClonar(
			@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	List<FrenteObraBD> selectFrenteDeObraParaApagar (@BindList("fkPos") List<Long> fkPos);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	FrenteObraBD cloneFrenteDeObra(@BindBean("frenteDeObraOriginal") FrenteObraBD frenteDeObraOriginal,
			@BindBean("poClonado") PoBD poClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarFrenteObras(@BindList("ids") List<Long> ids);



	//EventoFrenteObra
	@SqlQuery
	List<EventoFrenteObraBD> selectEventoFrenteDeObraParaClonar(
			@BindList("idsEventosOriginais") List<Long> idsEventosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	EventoFrenteObraBD cloneEventoFrenteObra(
			@BindBean("eventoFrenteObraOriginal") EventoFrenteObraBD eventoFrenteObraOriginal,
			@BindBean("frenteObraClonado") FrenteObraBD frenteObraClonado,
			@BindBean("eventoClonado") EventoBD eventoClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	@AllowUnusedBindings
	void apagarEventoFrenteObras(@BindList("fkEventos") List<Long> fkEventos, @BindList("fkFrenteObras") List<Long> fkFrenteObras);


	//MacroServico
	@SqlQuery
	List<MacroServicoBD> selectMacroServicoParaClonar(
			@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	List<MacroServicoBD> selectMacroServicoParaApagar(@BindList("fkPos") List<Long> fkPos);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	MacroServicoBD cloneMacroServico(@BindBean("macroServicoOriginal") MacroServicoBD macroServicoOriginal,
			@BindBean("poClonado") PoBD poClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarMacroServicos(@BindList("ids") List<Long> ids);



	///Macro Servico de Parcela
	@SqlQuery
	List<MacroServicoParcelaBD> selectMacroServicoParcelaParaClonar(
			@BindList("idsMacroServicosOriginais") List<Long> idsMacroServicosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	MacroServicoParcelaBD cloneMacroServicoParcela(
			@BindBean("macroServicoParcelaOriginal") MacroServicoParcelaBD macroServicoParcelaOriginal,
			@BindBean("macroServicoClonado") MacroServicoBD macroServicoClonado,
			@Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarMacroServicoParcelas (@BindList("fkMacroServico") List<Long> fkMacroServico);


	//Servico
	@SqlQuery
	List<ServicoBD> selectServicoParaClonar(
			@BindList("idsMacroServicosOriginais") List<Long> idsMacroServicosOriginais);

	@SqlQuery
	List<ServicoBD> selectServicoParaApagar(@BindList("fkMacroServico") List<Long> fkMacroServico);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoBD cloneServico(@BindBean("servicoOriginal") ServicoBD servicoOriginal,
			@BindBean("macroServicoClonado") MacroServicoBD macroServicoClonado,
			@BindBean("eventoClonado") EventoBD eventoClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlUpdate
	@DefineUserInSession
	void apagarServicos (@BindList("ids") List<Long> ids);


	//Servico Frente de Obra
	@SqlQuery
	List<ServicoFrenteObraBD> selectServicoFrenteDeObraParaClonar(
			@BindList("idsServicosOriginais") List<Long> idsServicosOriginais);


	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoFrenteObraBD cloneServicoFrenteObra(
			@BindBean("servicoFrenteObraOriginal") ServicoFrenteObraBD servicoFrenteObraOriginal,
			@BindBean("frenteObraClonado") FrenteObraBD frenteObraClonado,
			@BindBean("servicoClonado") ServicoBD servicoClonado, @Bind("siglaEvento") String siglaEvento);


	@SqlUpdate
	@DefineUserInSession
	void apagarServicoFrenteObras (@BindList("fkServicos") List <Long> fkServicos, @BindList("fkFrenteObras") List <Long> fkFrenteObras);

	//Servico Frente de Obra Analise
	@SqlQuery
	List<ServicoFrenteObraAnaliseBD> selectServicoFrenteDeObraAnaliseParaClonar(
			@BindList("idsServicosOriginais") List<Long> idsServicosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoFrenteObraAnaliseBD cloneServicoFrenteObraAnalise(
		@BindBean("servicoFrenteObraAnaliseOriginal") ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseOriginal,
		@BindBean("servicoClonado") ServicoBD servicoClonado,
		@Bind("siglaEvento") String siglaEvento
	);

	@SqlUpdate
	@DefineUserInSession
	void apagarServicosFrenteObraAnalise (@BindList("fkServicos") List <Long> fkServicos);

	@SqlBatch
	@DefineUserInSession
	void cloneFrenteDeObraBatch (@BindBean("frenteObraClone")  List<FrenteObraBD> frenteDeObraClone);

	@SqlBatch
	@DefineUserInSession
	void cloneServicoBatch(@BindBean("servicoClonado") List<ServicoBD> servicoclonado);

	@SqlBatch
	@DefineUserInSession
	void cloneServicoFrenteObraAnaliseBatch(@BindBean("servicoFrenteObraAnaliseClonado") List<ServicoFrenteObraAnaliseBD> servicoFrenteObraAnaliseClonado);

	@SqlBatch
	@DefineUserInSession
	void cloneServicoFrenteObraBatch(@BindBean("servicoFrenteObraClonado") List<ServicoFrenteObraBD> servicoFrenteObraclonado);

	@SqlBatch
	@DefineUserInSession
	void cloneMacroServicoParcelaBatch(@BindBean("macroServicoParcelaClonada") List<MacroServicoParcelaBD> macroServicoParcelaClonada);

	@SqlBatch
	@DefineUserInSession
	void cloneEventoFrenteObraBatch(@BindBean("eventoFrenteObraClone")  List<EventoFrenteObraBD> eventoFrenteObraClone);

	@SqlBatch
	@DefineUserInSession
	void cloneMacroServicoBatch(@BindBean("macroServicoClone")  List<MacroServicoBD> macroServicoClone);

	@SqlBatch
	@DefineUserInSession
	void cloneEventoBatch(@BindBean("eventoClone")  List<EventoBD> eventoClone);
}
