package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao;

import java.util.List;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.stringtemplate4.UseStringTemplateSqlLocator;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect.DefineUserInSession;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

@RegisterBeanMapper(Clone.class)
@RegisterBeanMapper(PropostaBD.class)
@RegisterBeanMapper(SubitemInvestimentoBD.class)
@RegisterBeanMapper(LicitacaoBD.class)
@RegisterBeanMapper(FornecedorBD.class)
@RegisterBeanMapper(MetaBD.class)
@RegisterBeanMapper(SubmetaBD.class)
@RegisterBeanMapper(LoteBD.class)
@RegisterBeanMapper(PoBD.class)
@RegisterBeanMapper(EventoBD.class)
@RegisterBeanMapper(FrenteObraBD.class)
@RegisterBeanMapper(MacroServicoBD.class)
@RegisterBeanMapper(MacroServicoParcelaBD.class)
@RegisterBeanMapper(AnexoBD.class)
@RegisterBeanMapper(HistoricoLicitacaoBD.class)
@RegisterBeanMapper(LaudoBD.class)
@RegisterBeanMapper(PendenciaBD.class)
@RegisterBeanMapper(RespostaBD.class)
@RegisterBeanMapper(EventoFrenteObraBD.class)
@RegisterBeanMapper(ServicoBD.class)
@RegisterBeanMapper(ServicoFrenteObraBD.class)
@RegisterBeanMapper(ServicoFrenteObraAnaliseBD.class)

@RegisterFieldMapper(Clone.class)
@RegisterFieldMapper(PropostaBD.class)
@RegisterFieldMapper(SubitemInvestimentoBD.class)
@RegisterFieldMapper(LicitacaoBD.class)
@RegisterFieldMapper(FornecedorBD.class)
@RegisterFieldMapper(MetaBD.class)
@RegisterFieldMapper(SubmetaBD.class)
@RegisterFieldMapper(LoteBD.class)
@RegisterFieldMapper(PoBD.class)
@RegisterFieldMapper(EventoBD.class)
@RegisterFieldMapper(FrenteObraBD.class)
@RegisterFieldMapper(MacroServicoBD.class)
@RegisterFieldMapper(MacroServicoParcelaBD.class)
@RegisterFieldMapper(AnexoBD.class)
@RegisterFieldMapper(HistoricoLicitacaoBD.class)
@RegisterFieldMapper(LaudoBD.class)
@RegisterFieldMapper(PendenciaBD.class)
@RegisterFieldMapper(RespostaBD.class)
@RegisterFieldMapper(EventoFrenteObraBD.class)
@RegisterFieldMapper(ServicoBD.class)
@RegisterFieldMapper(ServicoFrenteObraBD.class)
@RegisterFieldMapper(ServicoFrenteObraAnaliseBD.class)
@UseStringTemplateSqlLocator
public interface VersionamentoDAO {

	@SqlUpdate
	@DefineUserInSession
	@GetGeneratedKeys
	PropostaBD updateProposta(@BindBean("propostaOriginal") PropostaBD propostaOriginal);

	@SqlUpdate
	@DefineUserInSession
	@GetGeneratedKeys
	PropostaBD cloneProposta(@BindBean("propostaOriginal") PropostaBD propostaOriginal,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<SubitemInvestimentoBD> selectSubItemDeInvestimentoParaClonar(
			@BindBean("propostaOriginal") PropostaBD propostaOriginal);

	@SqlQuery
	@GetGeneratedKeys
	@DefineUserInSession
	SubitemInvestimentoBD cloneSubItemDeInvestimento(
			@BindBean("subItemDeInvestimento") SubitemInvestimentoBD subItemDeInvestimento,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<MetaBD> selectMetaParaClonar(@BindBean("propostaOriginal") PropostaBD propostaOriginal);

	@SqlQuery
	@GetGeneratedKeys
	@DefineUserInSession
	MetaBD cloneMeta(@BindBean("meta") MetaBD meta,
			@BindBean("subitemInvestimentoClonado") SubitemInvestimentoBD subitemInvestimentoClonado,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<LicitacaoBD> selectLicitacaoParaClonarPorProposta(@BindBean("propostaOriginal") PropostaBD objetoOriginal);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	LicitacaoBD cloneLicitacaoPorProposta(@BindBean("licitacaoOriginal") LicitacaoBD licitacaoOriginal,
			@BindBean("propostaClonada") PropostaBD propostaClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<FornecedorBD> selectFornecedorParaClonar(@BindList("idsLicitacoesOriginais") List<Long> idsLicitacoesOriginais);

	@SqlQuery
	@GetGeneratedKeys
	@DefineUserInSession
	FornecedorBD cloneFornecedor(@BindBean("fornecedorOriginal") FornecedorBD fornecedorOriginal,
			@BindBean("licitacaoClonada") LicitacaoBD licitacaoClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<LoteBD> selectLoteParaClonar(@BindBean("propostaOriginal") PropostaBD propostaOriginal);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	LoteBD cloneLicitacaoLote(@BindBean("loteOriginal") LoteBD loteOriginal,
			@BindBean("fornecedorClonado") FornecedorBD fornecedorClonado,
			@BindBean("licitacaoClonada") LicitacaoBD licitacaoClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<SubmetaBD> selectSubmetaParaClonar(@BindBean("propostaOriginal") PropostaBD objetoOriginal);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	SubmetaBD cloneSubmeta(@BindBean("submetaOriginal") SubmetaBD submetaOriginal,
			@BindBean("metaClonada") MetaBD metaClonada, @BindBean("propostaClonada") PropostaBD propostaClonada,
			@BindBean("loteClonado") LoteBD loteClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<PoBD> selectPOParaClonar(@BindList("idsSubmetasOriginais") List<Long> idsSubmetasOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	PoBD clonePO(@BindBean("poOriginal") PoBD poOriginal, @BindBean("submetaClonada") SubmetaBD submetaClonada,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<EventoBD> selectEventoParaClonar(@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	EventoBD cloneEvento(@BindBean("eventoOriginal") EventoBD eventoOriginal, @BindBean("poClonado") PoBD poClonado,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<FrenteObraBD> selectFrenteDeObraParaClonar(@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	FrenteObraBD cloneFrenteDeObra(@BindBean("frenteDeObraOriginal") FrenteObraBD frenteDeObraOriginal,
			@BindBean("poClonado") PoBD poClonado, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<MacroServicoBD> selectMacroServicoParaClonar(@BindList("idsPosOriginais") List<Long> idsPosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	MacroServicoBD cloneMacroServico(@BindBean("macroServicoOriginal") MacroServicoBD macroServicoOriginal,
			@BindBean("poClonado") PoBD poClonado, @Bind("siglaEvento") String siglaEvento);

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

	@SqlQuery
	List<AnexoBD> selectAnexoParaClonar(@BindList("idsLicitacoesOriginais") List<Long> idsLicitacoesOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	AnexoBD cloneAnexo(@BindBean("anexoOriginal") AnexoBD anexoOriginal,
			@BindBean("licitacaoClonada") LicitacaoBD licitacaoClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<HistoricoLicitacaoBD> selectHistoricoParaClonar(
			@BindList("idsLicitacoesOriginais") List<Long> idsLicitacoesOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	HistoricoLicitacaoBD cloneHistorico(@BindBean("historicoOriginal") HistoricoLicitacaoBD historicoOriginal,
			@BindBean("licitacaoClonada") LicitacaoBD licitacaoClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<LaudoBD> selectLaudoParaClonar(@BindList("idsLicitacoesOriginais") List<Long> idsLicitacoesOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	LaudoBD cloneLaudo(@BindBean("laudoOriginal") LaudoBD laudoOriginal,
			@BindBean("licitacaoClonada") LicitacaoBD licitacaoClonada, @Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<PendenciaBD> selectPendenciaParaClonar(@BindList("idsLaudosOriginais") List<Long> idsLaudosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	PendenciaBD clonePendencia(@BindBean("pendenciaOriginal") PendenciaBD pendenciaOriginal,
			@BindBean("laudoClonado") LaudoBD laudoClonado, @BindBean("submetaClonada") SubmetaBD submetaClonada,
			@Bind("siglaEvento") String siglaEvento);

	@SqlQuery
	List<RespostaBD> selectRespostaParaClonar(@BindList("idsLaudosOriginais") List<Long> idsLaudosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	RespostaBD cloneResposta(@BindBean("respostaOriginal") RespostaBD respostaOriginal,
			@BindBean("laudoClonado") LaudoBD laudoClonado, @BindBean("loteClonado") LoteBD loteClonado,
			@Bind("siglaEvento") String siglaEvento);

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

	@SqlQuery
	List<ServicoBD> selectServicoParaClonar(
			@BindList("idsMacroServicosOriginais") List<Long> idsMacroServicosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoBD cloneServico(@BindBean("servicoOriginal") ServicoBD servicoOriginal,
			@BindBean("macroServicoClonado") MacroServicoBD macroServicoClonado,
			@BindBean("eventoClonado") EventoBD eventoClonado, @Bind("siglaEvento") String siglaEvento);
	
	
	@SqlBatch
	@DefineUserInSession
	void cloneServicoBatch(@BindBean("servicoClonado") List<ServicoBD> servicoclonado);
	
	
	@SqlBatch
	@DefineUserInSession
	void cloneServicoFrenteObraBatch(@BindBean("servicoFrenteObraClonado") List<ServicoFrenteObraBD> servicoFrenteObraclonado);
	
	@SqlBatch
	@DefineUserInSession
	void cloneServicoFrenteObraAnaliseBatch(@BindBean("servicoFrenteObraAnaliseClonado") List<ServicoFrenteObraAnaliseBD> servicoFrenteObraAnaliseClonado);
	
	@SqlBatch
	@DefineUserInSession
	void cloneMacroServicoParcelaBatch(@BindBean("macroServicoParcelaClonada") List<MacroServicoParcelaBD> macroServicoParcelaClonada);
	
	@SqlBatch
	@DefineUserInSession
	void cloneHistoricoBatch(@BindBean("historicoClonado") List<HistoricoLicitacaoBD> historicoClonado);
	
	@SqlBatch
	@DefineUserInSession
	void cloneMacroServicoBatch (@BindBean("macroServicoClone")  List<MacroServicoBD> macroServicoClone);
	
	@SqlBatch
	@DefineUserInSession
	void cloneEventoBatch (@BindBean("eventoClone")  List<EventoBD> eventoClone);
	
	@SqlBatch
	@DefineUserInSession
	void clonePOBatch (@BindBean("poClone")  List<PoBD> poClone);
	
	@SqlBatch
	@DefineUserInSession
	void cloneFrenteDeObraBatch (@BindBean("frenteObraClone")  List<FrenteObraBD> frenteDeObraClone);
	
	@SqlBatch
	@DefineUserInSession
	void cloneEventoFrenteObraBatch (@BindBean("eventoFrenteObraClone")  List<EventoFrenteObraBD> eventoFrenteObraClone);

	@SqlQuery
	List<ServicoFrenteObraBD> selectServicoFrenteDeObraParaClonar(
			@BindList("idsServicosOriginais") List<Long> idsServicosOriginais);
	
	@SqlQuery
	List<ServicoFrenteObraAnaliseBD> selectServicoFrenteDeObraAnaliseParaClonar(
			@BindList("idsServicosOriginais") List<Long> idsServicosOriginais);

	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoFrenteObraBD cloneServicoFrenteObra(
			@BindBean("servicoFrenteObraOriginal") ServicoFrenteObraBD servicoFrenteObraOriginal,
			@BindBean("frenteObraClonado") FrenteObraBD frenteObraClonado,
			@BindBean("servicoClonado") ServicoBD servicoClonado, @Bind("siglaEvento") String siglaEvento);
	
	
	@SqlQuery
	@DefineUserInSession
	@GetGeneratedKeys
	ServicoFrenteObraAnaliseBD cloneServicoFrenteObraAnalise(
		@BindBean("servicoFrenteObraAnaliseOriginal") ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseOriginal,
		@BindBean("servicoClonado") ServicoBD servicoClonado,
		@Bind("siglaEvento") String siglaEvento
	);

}
