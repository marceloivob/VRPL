package br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import br.gov.planejamento.siconv.grpc.CpsGRPCClient;
import br.gov.planejamento.siconv.grpc.DataDaPropostaResponse;
import br.gov.planejamento.siconv.grpc.PropostaBooleanResponse;
import br.gov.planejamento.siconv.grpc.ReferenciaPrecoResponse;
import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception.ErroIntegracaoCPS;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.EventoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NivelContratoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraDetalheIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubitemInvestimentoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.ErroIntegracaoSICONV;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.NaoExisteSPAHomologado;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.PropostaEstaSendoImportada;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.PropostaNotFoundException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.PropostaSemDadosImportados;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.VRPLSemPermissaoAcessoProposta;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.integracao.EventoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.integracao.FrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business.MacroServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraAnaliseDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.UpdatePropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Log
public class PropostaBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SiconvBC siconvBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private LicitacaoBC licitacaoBC;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private MacroServicoBC macroServicoBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SiconvPrincipal usuarioLogado;


	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<ServicoBD> beanValidator;

	@Inject
	private SiconvGRPCClient siconvGrpcClient;

	@Inject
	private CpsGRPCClient cpsGRPCClient;

	@Inject
	private BusinessExceptionContext businessExceptionContext;

	public List<LoteDTO> lotesAtivosDaProposta(SiconvPrincipal usuarioLogado, Long versaoDaProposta) {
		List<LoteBD> lotes = getLoteLicitacaoDAO().findLotesAtivosByIdPropostaSiconv(usuarioLogado, versaoDaProposta);

		return lotes.stream().map(LoteDTO::from).collect(Collectors.toList());
	}

	public List<LoteDTO> lotesRejeitadosDaProposta(SiconvPrincipal usuarioLogado, Long versaoDaProposta) {
		List<LoteBD> lotes = getLoteLicitacaoDAO().findLotesRejeitadosByIdPropostaSiconv(usuarioLogado,
				versaoDaProposta);

		return lotes.stream().map(LoteDTO::from).collect(Collectors.toList());
	}

	public Boolean verificaInicioVrpl() {

	    	if (Boolean.TRUE.equals(getPropostaDAO().existePropostaVrpl(usuarioLogado))) {
			    if (!usuarioLogado.isAcessoLivre()) {
			    	this.atualizaDadosImportados();
			    }
			} else {
		   		if (permiteAcessoVRPLProposta(usuarioLogado.getIdProposta())) {
		   			this.verificarImportacaoProposta();

		  		} else {
		     		throw new VRPLSemPermissaoAcessoProposta(usuarioLogado);
				}
			}

	    	return true;
	}

	private void verificarImportacaoProposta() {
		if (getSiconvBC().existeSPAHomologadoParaProposta(usuarioLogado)) {
			if (usuarioLogado.isAcessoLivre()) {
		    	throw new PropostaSemDadosImportados(usuarioLogado);

		    } else {
		    	this.importacaoSeguraDadosDaProposta();
		    }

		} else {
			throw new NaoExisteSPAHomologado(usuarioLogado);
		}
	}

	private void importacaoSeguraDadosDaProposta() {
		try {

    		this.importarTodosOsDadosDaProposta();

    	} catch (UnableToExecuteStatementException e) {
    		final String CODIGO_ERRO_CHAVE_DUPLICADA = "23505";

    		Throwable rootCause = e.getCause();

    		if (rootCause instanceof SQLException &&
    			CODIGO_ERRO_CHAVE_DUPLICADA.equals(((SQLException) rootCause).getSQLState())) {

    			// A proposta já está sendo importada e uma nova tentativa gerou o erro
    			// de duplicação da chave
    			throw new PropostaEstaSendoImportada(usuarioLogado);

    		} else {
    			// Outro erro
    			throw e;
    		}
    	}
	}

	@RefreshRowPolice
	@Log void atualizaDadosImportados() {
		dao.getJdbi().useTransaction(transaction -> {
			this.atualizaDadosImportados(transaction);
		});
	}

	protected void atualizaDadosImportados(Handle transaction) {

		PropostaDAO propostaDao = transaction.attach(PropostaDAO.class);
		PropostaBD proposta = propostaDao.recuperaUltimaVersaoDaProposta(usuarioLogado);

		getSiconvBC().atualizarDadosProposta(proposta, transaction, usuarioLogado);

		this.getLicitacaoBC().atualizarLicitacoesDaProposta(transaction, proposta);
	}

	@RefreshRowPolice
	@Log
	protected void importarTodosOsDadosDaProposta() {

		Jdbi jdbi = dao.getJdbi();
		jdbi.useTransaction(transaction -> {
			DadosBasicosIntegracao dadosBasicos = getSiconvBC().recuperarDadosBasicos(usuarioLogado);

			if (dadosBasicos == null) {
				throw new PropostaNotFoundException(usuarioLogado);
			}

			PropostaBD proposta = PropostaBD.from(dadosBasicos);

			getSiconvBC().atualizarDadosProposta(proposta, usuarioLogado);

			proposta = transaction.attach(UpdatePropostaDAO.class).inserirProposta(proposta);

			this.getLicitacaoBC().importarLicitacoesEFornecedores(transaction, proposta);

			Long idPropostaSiconv = usuarioLogado.getIdProposta();
			List<MetaBD> metas = this.importarMetas(idPropostaSiconv, transaction);

			// lotes e submetas
			List<SubmetaIntegracao> submetasIntegracao = getSiconvBC()
					.recuperarSubmetasDaProposta(proposta.getIdSiconv());
			List<LoteBD> lotes = this.criarLotes(submetasIntegracao, transaction);
			List<SubmetaBD> submetas = this.importarSubmetas(proposta, submetasIntegracao, metas, lotes, transaction);

			List<PoBD> pos = importarPos(submetas, transaction);
			List<FrenteObraBD> frentesObra = importarFrentesDeObra(idPropostaSiconv, pos, transaction);
			List<EventoBD> eventos = importarEventos(idPropostaSiconv, pos, transaction);
			importarRelacaoEventosFrentesObra(eventos, frentesObra, transaction);
			List<MacroServicoBD> macroServicos = importarMacroServicoEParcela(pos, transaction);
			List<ServicoBD> servicos = importarServicos(macroServicos, eventos, transaction);
			List<ServicoFrenteObraBD> servicosFrentesObra = importarRelacaoServicosFrentesDeObra(servicos, frentesObra, transaction);
			removerEventosOrfaos(servicosFrentesObra, transaction);
			atualizarValoresDasSubmetas(pos, transaction);
		});
	}



	private void removerEventosOrfaos(List<ServicoFrenteObraBD> servicosFrentesObra, Handle transaction) {

		List<ServicoFrenteObraBD> eventosFrenteObraRemover = new ArrayList<>();
		for (ServicoFrenteObraBD servicoFrenteObra : servicosFrentesObra) {
			ServicoBD s = transaction.attach(ServicoDAO.class).recuperarServicoPorId(servicoFrenteObra.getServicoFk());
			int qntsServicosFrenteObraMesmoEvento = transaction.attach(ServicoDAO.class)
					.qntsServicosFrenteObraMesmoEvento(s.getEventoFk(), servicoFrenteObra.getFrenteObraFk());

			if(qntsServicosFrenteObraMesmoEvento == 0) {
				eventosFrenteObraRemover.add(servicoFrenteObra);
			}
		}
		transaction.attach(ServicoDAO.class).removerEventosFrentesObraDosServicos(eventosFrenteObraRemover);

	}

	private List<MetaBD> importarMetas(Long idProposta, Handle transaction) {
		List<MetaIntegracao> metasIntegracao = getSiconvBC().recuperarMetasProposta(idProposta);

		List<SubitemInvestimentoBD> subitens = importarSubitemInvestimento(metasIntegracao, transaction);

		List<MetaBD> novasMetas = metasIntegracao.stream().map(metaIntegracao -> {
			MetaBD metaBD = MetaBD.from(metaIntegracao);

			SubitemInvestimentoBD subitem = subitens.stream().filter(
					subitemBD -> subitemBD.getIdSubitemAnalise().equals(metaIntegracao.getSubitemInvestimentoFk()))
					.findFirst().orElseThrow(() -> new IllegalStateException("Subitem da meta obrigatório"));

			metaBD.setSubItemFk(subitem.getId());
			return metaBD;
		}).collect(Collectors.toList());

		return transaction.attach(QciDAO.class).insertMetas(novasMetas);
	}

	protected List<SubitemInvestimentoBD> importarSubitemInvestimento(List<MetaIntegracao> metasIntegracao,
			Handle transaction) {
		List<Long> idsMetaAnalise = metasIntegracao.stream().map(MetaIntegracao::getId).collect(Collectors.toList());

		List<SubitemInvestimentoIntegracao> subItensAnalise = getSiconvBC()
				.recuperarSubitemInvestimentoPorMetas(idsMetaAnalise);

		List<Long> idsSubitemAnalise = subItensAnalise.stream().map(SubitemInvestimentoIntegracao::getId)
				.collect(Collectors.toList());

		List<SubitemInvestimentoBD> subItensCadastrados = transaction.attach(QciDAO.class)
				.recuperarSubitemPorFkAnalise(idsSubitemAnalise);

		List<Long> idsSubItemCadastrados = subItensCadastrados.stream().map(SubitemInvestimentoBD::getIdSubitemAnalise)
				.collect(Collectors.toList());

		List<SubitemInvestimentoBD> novoSubItens = subItensAnalise.stream()
				.filter(subitemIntegracao -> !idsSubItemCadastrados.contains(subitemIntegracao.getId()))
				.map(subitemIntegracao -> {
					SubitemInvestimentoBD subitemBD = SubitemInvestimentoBD.from(subitemIntegracao);

					return subitemBD;
				}).collect(Collectors.toList());

		novoSubItens = transaction.attach(QciDAO.class).insertSubitensInvestimento(novoSubItens);

		subItensCadastrados.addAll(novoSubItens);
		return subItensCadastrados;
	}

	protected List<SubmetaBD> importarSubmetas(PropostaBD proposta, List<SubmetaIntegracao> submetasIntegracao,
			List<MetaBD> metas, List<LoteBD> lotes, Handle transaction) {
		List<SubmetaBD> novaSubmetas = submetasIntegracao.stream().map(submetaIntegracao -> {
			SubmetaBD submetaBD = SubmetaBD.from(submetaIntegracao);

			MetaBD meta = metas.stream()
					.filter(metaBd -> metaBd.getIdMetaAnalise().equals(submetaIntegracao.getIdMeta())).findFirst()
					.orElseThrow(() -> new IllegalStateException("Meta obrigatório para submeta"));

			LoteBD lote = lotes.stream()
					.filter(loteBD -> loteBD.getNumeroDoLote().equals(submetaIntegracao.getNumeroLote())).findFirst()
					.orElseThrow(() -> new IllegalStateException("Lote obrigatório para submeta"));

			submetaBD.setIdProposta(proposta.getId());
			submetaBD.setIdMeta(meta.getId());
			submetaBD.setVrplLoteLicitacaoFk(lote.getId());

			return submetaBD;

		}).collect(Collectors.toList());

		return transaction.attach(QciDAO.class).insertSubmetas(novaSubmetas);
	}

	private List<LoteBD> criarLotes(List<SubmetaIntegracao> submetasIntegracao, Handle transaction) {
		List<LoteBD> lotes = submetasIntegracao.stream().map(submeta -> {
			LoteBD lote = new LoteBD();
			lote.setNumeroDoLote(submeta.getNumeroLote());

			return lote;

		}).distinct().collect(Collectors.toList());

		return transaction.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(lotes);
	}

	private List<PoBD> importarPos(List<SubmetaBD> novaSubmetas, Handle transaction) {
		List<Long> idsSubmetasAnalise = novaSubmetas.stream().map(SubmetaBD::getIdSubmetaAnalise)
				.collect(Collectors.toList());

		List<PoBD> novasPos = getSiconvBC().recuperarPosPorIdSubmeta(idsSubmetasAnalise).stream().map(poIntegracao -> {
			PoBD poBD = PoBD.from(poIntegracao);
			SubmetaBD submetaBD = novaSubmetas.stream()
					.filter(submeta -> submeta.getIdSubmetaAnalise().equals(poIntegracao.getIdSubmeta())).findFirst()
					.orElseThrow(() -> new IllegalStateException("Submeta Obrigatório para PO"));

			poBD.setSubmetaId(submetaBD.getId());

			return poBD;
		}).collect(Collectors.toList());

		return transaction.attach(PoDAO.class).insertPos(novasPos);
	}

	private List<FrenteObraBD> importarFrentesDeObra(Long idProposta, List<PoBD> novasPos, Handle transaction) {

		List<Long> idsPosAnalise = novasPos.stream().map(PoBD::getIdPoAnalise).collect(Collectors.toList());

		List<FrenteObraIntegracao> frenteObraIntegracoes = getSiconvBC()
				.recuperarListaFrentesObraAnalisePorIdPoIdProposta(idsPosAnalise, idProposta);

		List<FrenteObraBD> frentesObra = frenteObraIntegracoes.stream().map(frenteAnalise -> {
			FrenteObraBD frenteObraBD = new FrenteObraBD();
			frenteObraBD.setNmFrenteObra(frenteAnalise.getNmFrenteObra());
			frenteObraBD.setNrFrenteObra(frenteAnalise.getNrFrenteObra());
			Long idPoVRPL = novasPos.stream().filter(poBD -> poBD.getIdPoAnalise().equals(frenteAnalise.getPoFk()))
					.map(PoBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("PO obrigatório para Frente de Obra"));

			frenteObraBD.setPoFk(idPoVRPL);
			frenteObraBD.setIdAnalise(frenteAnalise.getId());

			return frenteObraBD;
		}).collect(Collectors.toList());

		Long[] ids = transaction.attach(FrenteObraDAO.class).insertFrentesDeObraBatch(frentesObra);
		for (int index = 0; index < ids.length; index++) {
			frentesObra.get(index).setId(ids[index]);
		}

		return frentesObra;

	}

	private List<EventoBD> importarEventos(Long idProposta, List<PoBD> novasPos, Handle transaction) {

		List<Long> idsPosAnalise = novasPos.stream().map(PoBD::getIdPoAnalise).collect(Collectors.toList());

		List<EventoIntegracao> eventosIntegracao = getSiconvBC()
				.recuperarListaEventosAnalisePorIdPoIdProposta(idsPosAnalise, idProposta);

		List<EventoBD> eventos = eventosIntegracao.stream().map(eventoAnalise -> {
			EventoBD eventoBD = new EventoBD();
			eventoBD.setNomeEvento(eventoAnalise.getNomeEvento());
			eventoBD.setNumeroEvento(eventoAnalise.getNumeroEvento());

			Long idPoVRPL = novasPos.stream().filter(poBD -> poBD.getIdPoAnalise().equals(eventoAnalise.idPo))
					.map(PoBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("PO obrigatório para Evento"));

			eventoBD.setIdPo(idPoVRPL);
			eventoBD.setIdAnalise(eventoAnalise.getId());

			return eventoBD;
		}).collect(Collectors.toList());

		Long[] ids = transaction.attach(EventoDAO.class).insertEventosBatch(eventos);
		for (int index = 0; index < ids.length; index++) {
			eventos.get(index).setId(ids[index]);
		}

		return eventos;
	}

	private List<EventoFrenteObraBD> importarRelacaoEventosFrentesObra(List<EventoBD> eventos, List<FrenteObraBD> frentesObra,
			Handle transaction) {

		if (eventos.isEmpty() || frentesObra.isEmpty())
			return new ArrayList<EventoFrenteObraBD>();

		List<Long> idsEvento = eventos.stream().map(EventoBD::getIdAnalise).collect(Collectors.toList());

		List<EventoFrenteObraIntegracao> eventosFrenteObraAnalise = getSiconvBC()
				.recuperarEventoFrenteObraPorIdEvento(idsEvento);

		List<EventoFrenteObraBD> eventosFrentesObraBD = eventosFrenteObraAnalise.stream().map(eventoFrenteAnalise -> {
			EventoFrenteObraBD eventoFrenteObraBD = new EventoFrenteObraBD();
			eventoFrenteObraBD.setNrMesConclusao(eventoFrenteAnalise.getNrMesConclusao());

			Long idEvento = eventos.stream()
					.filter(eventoBD -> eventoBD.getIdAnalise().equals(eventoFrenteAnalise.getEventoFk()))
					.map(EventoBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("Evento obrigatório para EventoFrenteObra"));

			Long idFrenteObra = frentesObra.stream()
					.filter(frenteObraBD -> frenteObraBD.getIdAnalise().equals(eventoFrenteAnalise.getFrenteObraFk()))
					.map(FrenteObraBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("Frente de Obra obrigatório para EventoFrenteObra"));

			eventoFrenteObraBD.setEventoFk(idEvento);
			eventoFrenteObraBD.setFrenteObraFk(idFrenteObra);

			return eventoFrenteObraBD;

		}).collect(Collectors.toList());

		transaction.attach(EventoDAO.class).insertEventosFrenteObra(eventosFrentesObraBD);

		return eventosFrentesObraBD;
	}

	private List<MacroServicoBD> importarMacroServicoEParcela(List<PoBD> novasPos, Handle transaction) {
		List<Long> idsPosAnalise = novasPos.stream().map(PoBD::getIdPoAnalise).collect(Collectors.toList());

		List<MacroServicoIntegracao> macraoServicosAnalise = getSiconvBC()
				.recuperarMacroServicosPorIdsPos(idsPosAnalise);

		List<MacroServicoBD> macroServicosBD = macraoServicosAnalise.stream().map(macroServicoAnalise -> {
			MacroServicoBD macroServicoBD = new MacroServicoBD();
			macroServicoBD.setTxDescricao(macroServicoAnalise.getTxDescricao());
			macroServicoBD.setNrMacroServico(macroServicoAnalise.getNrMacroServico());
			macroServicoBD.setIdMacroServicoAnalise(macroServicoAnalise.getId());

			Long idPo = novasPos.stream().filter(poBD -> poBD.getIdPoAnalise().equals(macroServicoAnalise.getPoFk()))
					.map(PoBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("PO obrigatório para Macrosserviço"));

			macroServicoBD.setPoFk(idPo);

			return macroServicoBD;

		}).collect(Collectors.toList());

		final List<MacroServicoBD> novosMacroServicosBD = transaction.attach(MacroServicoDAO.class)
				.inserirMacrosServicos(macroServicosBD);

		List<MacroServicoParcelaBD> parcelasBD = macraoServicosAnalise.stream()
				.flatMap(macroServicoIntegracao -> macroServicoIntegracao.getParcelas().stream())
				.map(parcelaIntegracao -> {
					MacroServicoParcelaBD parcelaBD = MacroServicoParcelaBD.from(parcelaIntegracao);
					MacroServicoBD macroServicoBD = novosMacroServicosBD.stream()
							.filter(macroServico -> macroServico.getIdMacroServicoAnalise()
									.equals(parcelaIntegracao.getMacroServicoFk()))
							.findFirst()
							.orElseThrow(() -> new IllegalStateException("Macrosserviço obrigatório para Parcela"));

					parcelaBD.setMacroServicoFk(macroServicoBD.getId());

					return parcelaBD;
				}).collect(Collectors.toList());

		transaction.attach(MacroServicoParcelaDAO.class).inserirMacroServicoParcelaBatch(parcelasBD);

		return novosMacroServicosBD;
	}

	private List<ServicoBD> importarServicos(List<MacroServicoBD> novosMacroservicos, List<EventoBD> novosEventos,
			Handle transaction) {

		if (novosMacroservicos.isEmpty())
			return new ArrayList<>();

		List<Long> idsMacroservicoAnalise = novosMacroservicos.stream().map(MacroServicoBD::getIdMacroServicoAnalise)
				.collect(Collectors.toList());

		List<ServicoIntegracao> servicosAnalise = getSiconvBC()
				.recuperarServicosPorIdsMacroServico(idsMacroservicoAnalise);

		List<ServicoBD> servicos = servicosAnalise.stream().map(servicoIntegracao -> {
			ServicoBD servicoBD = ServicoBD.from(servicoIntegracao);

			if (servicoIntegracao.getEventoFk() != null) {
				Long idEvento = novosEventos.stream()
						.filter(eventoBD -> eventoBD.getIdAnalise().equals(servicoIntegracao.getEventoFk()))
						.map(EventoBD::getId).findFirst()
						.orElseThrow(() -> new IllegalStateException("Evento obrigatório para Serviço com Evento"));

				servicoBD.setEventoFk(idEvento);
			}

			MacroServicoBD macroServico = novosMacroservicos.stream().filter(macroServicoBD -> macroServicoBD
					.getIdMacroServicoAnalise().equals(servicoIntegracao.getMacroServicoFk())).findFirst().get();

			servicoBD.setMacroServicoFk(macroServico.getId());

			beanValidator.validate(servicoBD);

			return servicoBD;
		}).collect(Collectors.toList());

		return transaction.attach(ServicoDAO.class).inserirServicos(servicos);
	}

	private List<ServicoFrenteObraBD> importarRelacaoServicosFrentesDeObra(List<ServicoBD> servicos, List<FrenteObraBD> frentesObra,
			Handle transaction) {

		if (servicos.isEmpty() || frentesObra.isEmpty())
			return new ArrayList<ServicoFrenteObraBD>();

		List<Long> idsServicoAnalise = servicos.stream().map(ServicoBD::getIdServicoAnalise)
				.collect(Collectors.toList());

		List<ServicoFrenteObraDetalheIntegracao> servicosFrentesObraAnalise = getSiconvBC()
				.recuperarServicoFrenteObraDetalhePorIdServico(idsServicoAnalise);

		List<ServicoFrenteObraBD> servicosFrenteObraBD = servicosFrentesObraAnalise.stream()
				.map(servicoFrenteObraAnalise -> {
					ServicoFrenteObraBD servicoFrenteObraBD = new ServicoFrenteObraBD();
					servicoFrenteObraBD.setQtItens(servicoFrenteObraAnalise.getQtItens());

					Long idServico = servicos.stream()
							.filter(servicoBD -> servicoBD.getIdServicoAnalise()
									.equals(servicoFrenteObraAnalise.getServicoFk()))
							.map(ServicoBD::getId).findFirst()
							.orElseThrow(() -> new IllegalStateException("Serviço obrigatório para ServicoFrenteObra"));

					Long idFrenteObra = frentesObra.stream()
							.filter(frenteObraBD -> frenteObraBD.getIdAnalise()
									.equals(servicoFrenteObraAnalise.getFrenteObraFk()))
							.map(FrenteObraBD::getId).findFirst().orElseThrow(
									() -> new IllegalStateException("Frente Obra obrigatório para ServicoFrenteObra"));

					servicoFrenteObraBD.setServicoFk(idServico);
					servicoFrenteObraBD.setFrenteObraFk(idFrenteObra);

					return servicoFrenteObraBD;

				}).collect(Collectors.toList());

		List<ServicoFrenteObraAnaliseBD> listaServicosFrenteObraAnaliseBD = new ArrayList<>();

		for (ServicoFrenteObraDetalheIntegracao detalhe : servicosFrentesObraAnalise) {

			ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseBD = new ServicoFrenteObraAnaliseBD();
			servicoFrenteObraAnaliseBD.setQtItens(detalhe.getQtItens());
			servicoFrenteObraAnaliseBD.setNmFrenteObra(detalhe.getNmFrenteObra());
			servicoFrenteObraAnaliseBD.setNrFrenteObra(detalhe.getNrFrenteObra());

			Long idServico = servicos.stream()
					.filter(servicoBD -> servicoBD.getIdServicoAnalise()
							.equals(detalhe.getServicoFk()))
					.map(ServicoBD::getId).findFirst()
					.orElseThrow(() -> new IllegalStateException("Serviço obrigatório para ServicoFrenteObra"));

			servicoFrenteObraAnaliseBD.setServicoFk(idServico);

			listaServicosFrenteObraAnaliseBD.add(servicoFrenteObraAnaliseBD);

		}

		transaction.attach(ServicoDAO.class).insertServicoFrenteObra(servicosFrenteObraBD);
		transaction.attach(ServicoFrenteObraAnaliseDAO.class).inserirServicoFrenteObraAnaliseBatch(listaServicosFrenteObraAnaliseBD);

		return servicosFrenteObraBD;
	}



	public PropostaBD recuperarProposta(SiconvPrincipal usuarioLogado, Long versaoDaProposta) {
		Objects.requireNonNull(usuarioLogado);

		PropostaBD propostaBD = null;

		if (versaoDaProposta == null) {
			propostaBD = this.recuperaUltimaVersaoDaProposta(usuarioLogado);
		} else {
			propostaBD = this.recuperaVersaoDaProposta(usuarioLogado, versaoDaProposta);
		}

		if (propostaBD == null) {
			throw new PropostaNotFoundException(usuarioLogado, versaoDaProposta);
		}

		if(modalidadeTemNivelDeContrato(propostaBD)) {
			getNivelProposta(propostaBD);
		}

		return propostaBD;
	}

	public PropostaBD recuperaUltimaVersaoDaProposta(SiconvPrincipal usuarioLogado) {
		PropostaBD propostaAtual = getPropostaDAO().recuperaUltimaVersaoDaProposta(usuarioLogado);

		return propostaAtual;
	}

	private PropostaBD recuperaVersaoDaProposta(SiconvPrincipal usuarioLogado, Long versao) {
		PropostaBD propostaAtual = getPropostaDAO().recuperaVersaoDaProposta(usuarioLogado, versao);

		return propostaAtual;
	}

	public List<Long> recuperaVersoesDaProposta(SiconvPrincipal usuarioLogado) {
		List<Long> versoes = getPropostaDAO().recuperaVersoesDaProposta(usuarioLogado);

		return versoes;
	}

	public PropostaBD loadById(Long identificadorDaProposta) {
		PropostaBD proposta = getPropostaDAO().loadById(identificadorDaProposta);

		return proposta;
	}


	protected LoteLicitacaoDAO getLoteLicitacaoDAO() {
		return dao.get(LoteLicitacaoDAO.class);
	}

	protected PropostaDAO getPropostaDAO() {
		return dao.get(PropostaDAO.class);
	}

	private void getNivelProposta(PropostaBD propostaBD) {
		DataDaPropostaResponse dpr = siconvGrpcClient.consultarDataDaProposta(propostaBD.getIdSiconv());
		Integer CATEGORIA = 1;

		try {
			ReferenciaPrecoResponse rpr = cpsGRPCClient.consultarNivelPorData(Double.valueOf(propostaBD.getValorRepasse().doubleValue()),
					CATEGORIA, dpr.getDataDaProposta());

			propostaBD.setNivelContrato( rpr.getDescricao() );

		} catch (StatusRuntimeException sre) {
			if (naoTemEnquadramentoEmNenhumNivelDeContrato(sre)) {
				propostaBD.setNivelContrato( NivelContratoEnum.NA.getDescricao() );
			} else {
				businessExceptionContext.add(new ErroIntegracaoCPS(sre));
				businessExceptionContext.throwException();
			}
		} catch (Exception e) {
			businessExceptionContext.add(new ErroIntegracaoCPS(e));
			businessExceptionContext.throwException();
		}
	}

	private boolean modalidadeTemNivelDeContrato(PropostaBD proposta) {
		return ModalidadePropostaEnum.TERMO_DE_COMPROMISSO_CONCEDENTE.getCodigo() != proposta.getModalidade().intValue()
				&& ModalidadePropostaEnum.TERMO_DE_COMPROMISSO_MANDATARIA.getCodigo() != proposta.getModalidade().intValue();
	}

	private boolean naoTemEnquadramentoEmNenhumNivelDeContrato(StatusRuntimeException sre) {
		// Serviço CPS-GRPC consultarNivelPorData retorna exceção se a proposta não se
		// enquadrar em nenhum nível
		return sre.getStatus().getCode() == Status.NOT_FOUND.getCode();
	}

	public Boolean verificaExisteVrpl(Long idProposta) {
		Boolean retorno = getPropostaDAO().existePropostaVrpl(usuarioLogado);

		return retorno;
	}

	public Boolean verificaVrplAceita(Long idProposta) {
		Boolean existeVrpl = this.verificaExisteVrpl(idProposta);

		if (existeVrpl) {
			Boolean existeVrplAceita = getPropostaDAO().existePropostaVrplAceita(idProposta);

			return existeVrplAceita;
		}else {
			throw new PropostaNotFoundException(usuarioLogado);
		}
	}

	private boolean permiteAcessoVRPLProposta(Long idProposta) {
		PropostaBooleanResponse acessoProposta = PropostaBooleanResponse.getDefaultInstance();
		try {
			acessoProposta = siconvGrpcClient.isVRPLResponsavelAceiteProcessoExecucao(idProposta);
		} catch (Exception e) {
			Status status = Status.fromThrowable(e);

			if (status.getCode() == Status.INVALID_ARGUMENT.getCode() ||
			   status.getCode() == Status.NOT_FOUND.getCode()) {
				throw new PropostaNotFoundException(usuarioLogado);
			}else {
				throw new ErroIntegracaoSICONV();
			}
		}

		return acessoProposta.getRetorno();
	}


	private void atualizarValoresDasSubmetas(List<PoBD> pos, Handle transaction) {
		
		List<Long> idsPos = pos.stream().map(PoBD::getId).collect(Collectors.toList());
		Map<PoBD,PoMacroServicoServicosDTO> mapaPos = macroServicoBC.recuperarMacroServicoServicosPorPo(idsPos);
		
		for(PoBD po : mapaPos.keySet()) {
			PoMacroServicoServicosDTO poMacroServicoServicosDTO = mapaPos.get(po);
			
			SubmetaBD submeta = transaction.attach(QciDAO.class).recuperarSubmetaVRPL( po.getSubmetaId() );
			BigDecimal vlTotalLicitado = poMacroServicoServicosDTO.getTotalGeralLicitado();
			
			if( vlTotalLicitado != null && !vlTotalLicitado.equals(submeta.getVlTotalLicitado()) ) {
				
				submeta = this.macroServicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, vlTotalLicitado);
				
				transaction.attach(QciDAO.class).atualizaValorTotalLicitadoDaSubmetaByID(submeta);
			}
		}
		
	}

	public Boolean verificaFornecedorObsoleto() {
		
		PropostaBD prop = getPropostaDAO().recuperaUltimaVersaoDaProposta(usuarioLogado);
		List<LicitacaoDTO> licitacoes = getLicitacaoBC().licitacoesDaProposta(prop, true);
		
		Map<LicitacaoDTO, Set<String>> obsoletos = new HashMap<>();
		for (LicitacaoDTO lic : licitacoes) {
			for(LoteDTO lote : lic.getLotes()) {
				if(lote.getFornecedor() != null && lote.getFornecedor().getObsoleto() ) {
					
					if(obsoletos.get(lic) == null) {
						obsoletos.put(lic, new HashSet<String>());
					}
					
					obsoletos.get(lic).add( lote.getFornecedor().getIdentificacaoNome() );
				}
			}
		}
		
		for (LicitacaoDTO licitacaoDTO : obsoletos.keySet()) {
			
			if(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla().equals(licitacaoDTO.getSituacaoDaLicitacao())
				|| SituacaoLicitacaoEnum.REJEITADA.getSigla().equals(licitacaoDTO.getSituacaoDaLicitacao()) ) {
				
				continue;
			}
			
			Set<String> fornecedoresIdentificacaoNome = obsoletos.get(licitacaoDTO);
			String msgErro = formatarStringErro(licitacaoDTO, fornecedoresIdentificacaoNome);
			
			businessExceptionContext.add(new BusinessException(msgErro));
		}
		
		if(!obsoletos.isEmpty()) {
			businessExceptionContext.throwException();
		}
		
		return true;
	}
	
	private String formatarStringErro(LicitacaoDTO licitacao, Set<String> fornecedoresIdentificacaoNome) {
		StringBuffer sb = new StringBuffer();
		
		
		sb.append("Licitação: ");
		sb.append(licitacao.getNumeroAno());
		sb.append(" [");
		
		for (String fornecedor : fornecedoresIdentificacaoNome) {
			sb.append("Fornecedor: ");
			sb.append(fornecedor);
			sb.append(", ");
		}
		
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		
		String msgErro = MessageFormat.format(ErrorInfo.FORNECEDORES_OBSOLETOS.getMensagem(), sb.toString());
		
		if( SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla().equals(licitacao.getSituacaoDaLicitacao()) 
				|| SituacaoLicitacaoEnum.EM_COMPLEMENTACAO.getSigla().equals(licitacao.getSituacaoDaLicitacao()) ) {
		
			if(usuarioLogado.isProponente()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_PROPONENTE_EPE_COM.getMensagem());
				return msgErro;
			}
			
			if(usuarioLogado.isConcedente() || usuarioLogado.isMandataria()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_MANDA_CONCE.getMensagem());
				return msgErro;
			}
			
			msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_OUTROS.getMensagem());
			return msgErro;
		}
		
		if( SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE.getSigla().equals(licitacao.getSituacaoDaLicitacao()) ) {
			
			if(usuarioLogado.isProponente()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_PROPONENTE_EAN.getMensagem());
				return msgErro;
			}
			
			if(usuarioLogado.isConcedente() || usuarioLogado.isMandataria()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_MANDA_CONCE.getMensagem());
				return msgErro;
			}
			
			msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_OUTROS.getMensagem());
			return msgErro;
			
		}
		
		if( SituacaoLicitacaoEnum.EM_ANALISE.getSigla().equals(licitacao.getSituacaoDaLicitacao()) ) {
		
			if(usuarioLogado.isProponente()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_PROPONENTE_ANL.getMensagem());
				return msgErro;
			}
			
			if(usuarioLogado.isConcedente() || usuarioLogado.isMandataria()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_MANDA_CONCE_ANL.getMensagem());
				return msgErro;
			}
			
			msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_OUTROS.getMensagem());
			return msgErro;
			
		}
		
		if( SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO.getSigla().equals(licitacao.getSituacaoDaLicitacao()) ) {
			
			if(usuarioLogado.isProponente()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_PROPONENTE_SCP.getMensagem());
				return msgErro;
			}
			
			if(usuarioLogado.isConcedente() || usuarioLogado.isMandataria()) {
				msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_MANDA_CONCE.getMensagem());
				return msgErro;
			}
			
			msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_OUTROS.getMensagem());
			return msgErro;
			
		}
		
		return msgErro;
	}
}
