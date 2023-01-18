package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum.EM_ANALISE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum.EM_COMPLEMENTACAO;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum.EM_PREENCHIMENTO;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum.ENVIADA_PARA_ANALISE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum.SOLICITADA_COMPLEMENTACAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.dao.AnexoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.CtefIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.FornecedorIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.ExibeParecerBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.LaudoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.dao.RespostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.rules.SalvarLicitacaoRules;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.AssociacaoLicitacaoLoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.CtefDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.FornecedorDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDetalhadaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.SubmetaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.integracao.LicitacaoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.HistoricoLicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao.HistoricoLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import br.gov.serpro.siconv.grpc.Fornecedor;
import br.gov.serpro.siconv.grpc.LicitacaoEmApostilamento;
import br.gov.serpro.siconv.grpc.LicitacoesResponse;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse.ProcessoExecucao;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LicitacaoBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PUBLIC)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ClientLicitacoesInterface clientLicitacoes;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SiconvBC siconvBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SiconvPrincipal usuarioLogado;


	@Inject
	private PoBC poBC;

	@Inject
	private ExibeParecerBC exibeParecer;

	@Inject
	private LaudoBC laudoBC;

	@Inject
	private HistoricoLicitacaoBC historicoBC;

	@Inject
	private LicitacaoBCAux aux;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SalvarLicitacaoRules salvarLicitacaoRules;


	public List<LicitacaoDTO> licitacoesDaProposta(PropostaBD proposta, boolean licitacoesAtivas) {
		List<LicitacaoBD> licitacoes = dao.get(LicitacaoDAO.class)
				.recuperarLicitacoesAssociadasDaProposta(proposta, licitacoesAtivas);

		List<LicitacaoDTO> licitacoesDto = licitacoes.stream().map(LicitacaoDTO::from).collect(Collectors.toList());

		Collections.sort(licitacoesDto, Comparator.comparing(LicitacaoDTO::getNumeroAno));

		this.calcularValoresPorPo(licitacoesDto);

		return licitacoesDto;
	}

	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void removeLotesLicitacao(Long idLicitacao) {

		dao.getJdbi().useTransaction(transaction -> {
			//consultar anexoLicitacao
			List<AnexoBD> anexos = transaction.attach(AnexoDAO.class).findTodosAnexosByIdLicitacao(idLicitacao);

			for (AnexoBD anexo: anexos) {
				transaction.attach(AnexoDAO.class).deleteAnexo(anexo);
			}

			historicoBC.excluirHistoricoDaLicitacao(idLicitacao, transaction);

			laudoBC.deletarLaudosDaLicitacao(idLicitacao, transaction);

			LicitacaoBD lic = transaction.attach(LicitacaoDAO.class).findLicitacaoByIdLicitacao(idLicitacao);
			lic.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());
			transaction.attach(LicitacaoDAO.class).updateSituacaoDaLicitacao(lic);

			transaction.attach(LicitacaoDAO.class).updateSituacaoSubmetasAssociadasDaLicitacaoPorLicitacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla(), idLicitacao);

			transaction.attach(LoteLicitacaoDAO.class).removeTodosLoteLicitacao(idLicitacao);

		});
	}

	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void salvarLotesAssociados(AssociacaoLicitacaoLoteDTO dadosAssociar) {

		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(dadosAssociar.getIdLicitacao());
		PropostaBD proposta = dao.get(PropostaDAO.class).loadById(licitacao.getIdentificadorDaProposta());

		salvarLicitacaoRules.verificaSePodeSalvarLicitacao(proposta, licitacao, usuarioLogado);

		this.verificaSeHaApostilamentoParaLicitacaoSelecionada(proposta, licitacao);
		this.verificarFornecedorObsoletoAssociado(dadosAssociar);

		if (dadosAssociar.getLotes() != null && dadosAssociar.getLotes().isEmpty()) {

			this.removeLotesLicitacao(dadosAssociar.getIdLicitacao());

		} else {

			dao.getJdbi().useTransaction(transaction -> {

				// Lotes a serem criados
				// /////////////////////////////////////////////////////////
				List<Long> numerosNovosLotes = dadosAssociar.getSubmetasAlteradas().stream()
						.filter(submeta -> submeta.getIdNovoLote() == null).map(submeta -> submeta.getNumeroNovoLote())
						.collect(Collectors.toList());

				List<LoteBD> novosLotes = new ArrayList<>();

				if (!numerosNovosLotes.isEmpty()) {
					novosLotes.addAll(this.criarLotes(numerosNovosLotes, transaction));
				}
				//////////////////////////////////////////////////////////////////////////////////

				// Associa lotes com submetas
				// ////////////////////////////////////////////////////
				transaction.attach(QciDAO.class)
						.atualizaLoteDaSubmeta(dadosAssociar.getSubmetasAlteradas().stream().map(submetasDTO -> {
							SubmetaBD submetaBD = new SubmetaBD();
							submetaBD.setId(submetasDTO.getIdSubmeta());

							if (submetasDTO.getIdNovoLote() != null) { // Lotes já existentes
								submetaBD.setVrplLoteLicitacaoFk(submetasDTO.getIdNovoLote());
							} else { // Se id do lote é nulo ele está na lista dos novos lotes criados
								Long idLote = novosLotes.stream()
										.filter(novoLote -> novoLote.getNumeroDoLote()
												.equals(submetasDTO.getNumeroNovoLote()))
										.map(LoteBD::getId).findFirst().orElseThrow(
												() -> new IllegalStateException("Deve haver novo lote para id null!"));
								submetaBD.setVrplLoteLicitacaoFk(idLote);
							}

							submetaBD.setSituacao(SituacaoLicitacaoEnum.EM_PREENCHIMENTO.getSigla());
							submetaBD.setVersao(submetasDTO.getVersaoSubmeta());

							return submetaBD;
						}).collect(Collectors.toList()));
				//////////////////////////////////////////////////////////////////////////////////

				// Associa lotes com licitação
				// ///////////////////////////////////////////////////
				List<LoteBD> lotes = dadosAssociar.getLotes().stream().map(lotesAssociar -> {
					LoteBD lote = new LoteBD();
					Long id = lotesAssociar.getId();

					if (id == null) {
						lote = novosLotes.stream()
								.filter(loteBD -> loteBD.getNumeroDoLote().equals(lotesAssociar.getNumero()))
								.findFirst().orElseThrow(() -> new IllegalStateException(
										"Deve existir lote de numero: " + lotesAssociar.getNumero()));
					} else {
						lote.setId(lotesAssociar.getId());
						lote.setNumeroDoLote(lotesAssociar.getNumero());
						lote.setVersao(lotesAssociar.getVersao());
					}

					return lote;
				}).collect(Collectors.toList());

				this.associarLoteLicitacao(dadosAssociar.getIdLicitacao(), dadosAssociar.getIdFornecedor(), lotes,
						transaction);
				//////////////////////////////////////////////////////////////////////////////////

				// Exclui lotes sem submetas
				// /////////////////////////////////////////////////////
				transaction.attach(RespostaDAO.class).excluirRespostasSemLote();
				transaction.attach(LoteLicitacaoDAO.class).deleteLoteSemSubmeta();
				//////////////////////////////////////////////////////////////////////////////////

			});
		}
	}

	private void verificaSeHaApostilamentoParaLicitacaoSelecionada(PropostaBD proposta, LicitacaoBD licitacaoDB) {
		try {
			log.info("Verificando se a Licitação '{}' ainda pode ser associada para a Proposta '{}'", licitacaoDB.getIdLicitacaoFk(), proposta.getIdSiconv());

			ListaProcessoExecucaoResponse licitacoesAptaASeremSelecionadasESemApostilamento = getClientLicitacoesGRPC().listarProcessosExecucao(proposta.getIdSiconv());

			Optional<ProcessoExecucao> licitacaoSelecionada = licitacoesAptaASeremSelecionadasESemApostilamento //
				.getListaProcessoExecucaoList() //
				.stream() //
				.filter(processoExecucao -> licitacaoDB.getIdLicitacaoFk().equals(processoExecucao.getId())) //
				.findFirst();

			if (licitacaoSelecionada.isEmpty()) {
				throw new BusinessException(ErrorInfo.LICITACAO_COM_APOSTILAMENTO);
			}

		} catch (StatusRuntimeException statusRuntimeException) {

			if (statusRuntimeException.getStatus() == Status.NOT_FOUND) {
				log.debug("A licitação {} não está mais apta a ser selecionada para a proposta '{}'. ", licitacaoDB.getIdLicitacaoFk(), proposta.getIdSiconv());

				throw new BusinessException(ErrorInfo.LICITACAO_COM_APOSTILAMENTO);
			} else {
				log.error("Erro de comunicação:", statusRuntimeException);

				throw statusRuntimeException;
			}
		}
	}

	private void verificarFornecedorObsoletoAssociado(AssociacaoLicitacaoLoteDTO dadosAssociar) {
		FornecedorBD fornecedor = dao.get(LicitacaoDAO.class).findFornecedorPorId(dadosAssociar.getIdFornecedor());

		if(fornecedor.getObsoleto()) {
			throw new BusinessException(ErrorInfo.FORNECEDOR_OBSOLETO_SELECIONADO);
		}
	}

	private void associarLoteLicitacao(Long idLicitacao, Long idFornecedor, List<LoteBD> lotes, Handle transaction) {
		if (lotes.isEmpty()) {
			transaction.attach(LoteLicitacaoDAO.class).removeTodosLoteLicitacaoFornecedor(idLicitacao, idFornecedor);
		} else {

			List<Long> idsLotes = lotes.stream().map(LoteBD::getId).collect(Collectors.toList());

			List<Long> idsLotesAValidar = lotes.stream().map(LoteBD::getId).collect(Collectors.toList());
			List<LoteBD> lotesOutrosFornecedores = this.recuperarLotesDosOutrosFornecedores(idLicitacao, idFornecedor);
			idsLotesAValidar.addAll(lotesOutrosFornecedores.stream().map(LoteBD::getId).collect(Collectors.toList()));

			this.verificaSubmetasMesmoTipo(idsLotesAValidar, transaction);
			this.verificaAcompanhamentoPoMesmoTipoPorLote(idsLotesAValidar, transaction);
			this.verificaSubmetaMesmoRegimePorLote(idsLotesAValidar, transaction);

			transaction.attach(LoteLicitacaoDAO.class).removeLoteNaoSelecionadoLicitacao(idLicitacao, idFornecedor,
					idsLotes);

			transaction.attach(LoteLicitacaoDAO.class).updateLoteLicitacao(idLicitacao, idFornecedor, lotes);

			this.registrarHistoricoAssociacao(idLicitacao, transaction);

		}
	}

	private void registrarHistoricoAssociacao(Long idLicitacao, Handle transaction) {

		// o sistema está registrando para todas as vezes que uma associação é salva;
		// para registrar apenas a primeira associação, é preciso fazer um if com a condição abaixo:
		//		boolean aindaNaoRegistrouAssociacao = this.historicoBC.findHistoricoByIdLicitacaoEvento(idLicitacao,
		//				EventoQuadroResumoEnum.ASSOCIAR_LOTE_LICITACAO.getSigla()).isEmpty();

		LicitacaoBD licitacao = transaction.attach(LicitacaoDAO.class).findLicitacaoByIdLicitacao(idLicitacao);

	    HistoricoLicitacaoBD historico = new HistoricoLicitacaoBD();
        historico.setIdentificadorDaLicitacao(licitacao.getId());
        historico.setSituacaoDaLicitacao(licitacao.getSituacaoDaLicitacao());
        historico.setEventoGerador(EventoQuadroResumoEnum.ASSOCIAR_LOTE_LICITACAO.getSigla());
        historico.setCpfDoResponsavel(this.usuarioLogado.getCpf());
        historico.setNomeDoResponsavel(this.usuarioLogado.getName());
        historico.setIdentificadorDaLicitacao(licitacao.getId());

        transaction.attach(HistoricoLicitacaoDAO.class).insertHistoricoLicitacao(historico);
	}

	private List<LoteBD> recuperarLotesDosOutrosFornecedores(Long idLicitacao, Long idFornecedor){

		List<LoteBD> lotesOutrosFornecedores = dao.get(LoteLicitacaoDAO.class).findLotesByIdLicitacao(idLicitacao);

		lotesOutrosFornecedores = lotesOutrosFornecedores.stream().filter(
				loteBD -> (loteBD != null && !idFornecedor.equals(loteBD.getIdFornecedor())))
				.collect(Collectors.toList());

		return lotesOutrosFornecedores;
	}

	private List<LoteBD> criarLotes(List<Long> numerosLotes, Handle transaction) {
		List<LoteBD> lotes = numerosLotes.stream().distinct().map(numero -> {
			LoteBD lote = new LoteBD();
			lote.setNumeroDoLote(numero);
			lote.setVersao(0L);

			return lote;

		}).collect(Collectors.toList());

		return transaction.attach(LoteLicitacaoDAO.class).insertLotesLicitacao(lotes);
	}

	private void verificaSubmetasMesmoTipo(List<Long> idsLotes, Handle transaction) {
		if (!transaction.attach(QciDAO.class).verificaSubmetasMesmoTipoPorLote(idsLotes)) {
			throw new BusinessException(ErrorInfo.SUBMETAS_TIPO_DIFERENTE_LICITACAO);
		}
	}

	private void verificaAcompanhamentoPoMesmoTipoPorLote(List<Long> idsLotes, Handle transaction) {
		if (!transaction.attach(QciDAO.class).verificaAcompanhamentoPoMesmoTipoPorLote(idsLotes)) {
			throw new BusinessException(ErrorInfo.PO_ACOMPANHAMENTO_DIFERENTE_LICITACAO);
		}
	}

	private void verificaSubmetaMesmoRegimePorLote(List<Long> idsLotes, Handle transaction) {
		if (!transaction.attach(QciDAO.class).verificaSubmetaMesmoRegimePorLote(idsLotes)) {
			throw new BusinessException(ErrorInfo.SUBMETAS_REGIME_DIFERENTE_LICITACAO);
		}
	}

	@Log
	public LicitacaoDetalhadaDTO recuperarLicitacao(Long identificadorDaLicitacao) {
		LicitacaoBD licitacaoBD = dao.get(LicitacaoDAO.class).findLicitacaoByIdLicitacao(identificadorDaLicitacao);

		ProcessoExecucaoResponse detalheDaLicitacao = clientLicitacoes
				.detalharProcessosExecucao(licitacaoBD.getIdLicitacaoFk());

		LicitacaoDetalhadaDTO licitacaoDetalhada = new LicitacaoDetalhadaDTO();

		CtefIntegracao ctef = siconvBC.findCtefByIdLicitacaoFK(licitacaoBD.getIdLicitacaoFk());
		if (ctef != null) {
			licitacaoDetalhada.setCtef(new CtefDTO().from(ctef));
		}

		licitacaoDetalhada.updateFrom(licitacaoBD);
		licitacaoDetalhada.from(detalheDaLicitacao);


		for(LoteDTO lote : licitacaoDetalhada.getLotes()) {
			if(lote.getFornecedor() == null) {
				lote.setFornecedor( FornecedorDTO.from(dao.get(LicitacaoDAO.class).findFornecedorPorId(lote.getIdFornecedor())) );
			}
		}

		aux.preencherInformacoesDosLotes(licitacaoDetalhada);

		if (licitacaoDetalhada.getLotes() != null && !licitacaoDetalhada.getLotes().isEmpty()) {
			Boolean indicadorSocial = dao.get(LicitacaoDAO.class)
					.recuperarIndicadorSocialLicitacao(identificadorDaLicitacao);
			licitacaoDetalhada.setInSocial(indicadorSocial);
		}


		Boolean existeParecer = exibeParecer.dadaALicitacao(licitacaoBD).eOUsuario(usuarioLogado).devoExibirOParecer();
		licitacaoDetalhada.setExisteParecer(existeParecer);

		Boolean existeParecerEmitido = exibeParecer.dadaALicitacao(licitacaoBD).existeParecerEmitido();
		licitacaoDetalhada.setExisteParecerEmitido(existeParecerEmitido);

		return licitacaoDetalhada;
	}

	public void atualiza(Handle transaction, List<LicitacaoBD> licitacoesASeremAtualizadas) {
		if ((licitacoesASeremAtualizadas == null) || (licitacoesASeremAtualizadas.isEmpty())) {
			return;
		}

		transaction.attach(LicitacaoDAO.class).atualizaLicitacao(licitacoesASeremAtualizadas);
	}

	public List<LicitacaoBD> insere(Handle transaction, LicitacaoBD novaLicitacao) {
		List<LicitacaoBD> novasLicitacoes = new ArrayList<>();
		novasLicitacoes.add(novaLicitacao);

		return this.insere(transaction, novasLicitacoes);
	}

	public List<LicitacaoBD> insere(Handle transaction, List<LicitacaoBD> novasLicitacoes) {
		if (novasLicitacoes.isEmpty()) {
			return new ArrayList<>();
		}

		List<LicitacaoBD> licitacoesInseridas = transaction.attach(LicitacaoDAO.class).insertLicitacao(novasLicitacoes);

		return licitacoesInseridas;
	}

	public List<FornecedorBD> recuperaFornecedoresDasLicitacoesDoBancoLocal(Handle transaction, List<LicitacaoBD> licitacoes) {

		if (licitacoes.isEmpty()) {
			return new ArrayList<>();
		}

		List<Long> idsDasLicitacoes = licitacoes.stream().map(LicitacaoBD::getId).collect(Collectors.toList());

		List<FornecedorBD> fornecedores = transaction.attach(LicitacaoDAO.class)
				.recuperaFornecedoresDasLicitacoes(idsDasLicitacoes);

		return fornecedores;
	}

	public void insertFornecedores(Handle transaction, List<FornecedorBD> fornecedores) {

		if (fornecedores.isEmpty()) {
			return;
		}

		transaction.attach(LicitacaoDAO.class).insertFornecedores(fornecedores);
	}

	public void deleteFornecedores(Handle transaction, List<Long> idsFornecedores) {

		if (idsFornecedores.isEmpty()) {
			return;
		}

		Iterator<Long> it = idsFornecedores.iterator();
		while(it.hasNext()) {
			Long idFornecedor = it.next();
			boolean isFornecedorAssociado = transaction.attach(LoteLicitacaoDAO.class)
					.existeLoteAssociadoAoFornecedor(idFornecedor);

			if(isFornecedorAssociado) {
				it.remove();
				transaction.attach(LicitacaoDAO.class).fornecedorObsoleto(idFornecedor);
			}
		}

		// somente os que sobraram na lista após verificação de associação
		if (!idsFornecedores.isEmpty()) {
			transaction.attach(LicitacaoDAO.class).deleteFornecedores(idsFornecedores);
		}
	}


	public List<FornecedorBD> atualizaFornecedores(Handle transaction, List<FornecedorBD> fornecedores) {
		if (fornecedores.isEmpty()) {
			return new ArrayList<>();
		}

		List<FornecedorBD> fornecedoresAtualizados = transaction.attach(LicitacaoDAO.class)
				.atualizaFornecedores(fornecedores);

		return fornecedoresAtualizados;
	}

	public LicitacaoBD recuperaLicitacaoPorId(Long id) {
		return dao.get(LicitacaoDAO.class).findLicitacaoById(id);
	}

	public boolean recuperaIndicadorSocialLicitacao(Long id) {
		return dao.get(LicitacaoDAO.class).recuperarIndicadorSocialLicitacao(id);
	}

	public void apagaLicitacoes(List<LicitacaoBD> licitacoes) {
		for (LicitacaoBD licitacaoBD : licitacoes) {
			try {
				dao.get(LicitacaoDAO.class).delete(licitacaoBD.getId());
			} catch(Exception e) {
				log.error(e.getMessage());
				throw new IllegalStateException("Não foi possível apagar a Licitação " +
						licitacaoBD.getObjeto() + " com id " + licitacaoBD.getId());
			}
		}
	}

	public void apagaFornecedoresPorLicitacao(List<LicitacaoBD> licitacoes) {
		for (LicitacaoBD licitacaoBD : licitacoes) {
			try {
				dao.get(LicitacaoDAO.class).deleteFornecedorPorIdLicitacao(licitacaoBD.getId());
			} catch(Exception e) {
				
				this.verificaSeHouveApostilamentoParaAlgumaLicitacaoLocal(licitacoes);
				
				log.error(e.getMessage());
				throw new IllegalStateException("Não foi possível apagar o Fornecedor da Licitação " +
						licitacaoBD.getObjeto() + " com id " + licitacaoBD.getId());
			}
		}
	}

	private void calcularValoresPorPo(List<LicitacaoDTO> licitacoesDto) {

		for (LicitacaoDTO licitacaoDTO : licitacoesDto) {
			LicitacaoBD licbd = new LicitacaoBD();
			licbd.setId(licitacaoDTO.getId());

			List<PoVRPLDTO> pos = poBC.recuperarNovoPosPorLicitacao(licbd);

			for (LoteDTO loteDTO : licitacaoDTO.getLotes()) {
				for (SubmetaDTO submetaDTO : loteDTO.getSubmetas()) {

					PoVRPLDTO poDTO = getPoPorSubmeta(submetaDTO, pos);

					if(poDTO != null) {
						submetaDTO.setValorAceitoAnalise( poDTO.getPrecoTotalAnalise() );
						submetaDTO.setValorLicitado( poDTO.getPrecoTotalLicitacao() );
					}
				}
			}
		}
	}

	private PoVRPLDTO getPoPorSubmeta(SubmetaDTO subDTO, List<PoVRPLDTO> pos) {
		for (PoVRPLDTO poVRPLDTO : pos) {

			if(subDTO.getNumero().equals( poVRPLDTO.getNumeroSubmeta() )
					&& subDTO.getNumeroMeta().equals( poVRPLDTO.getNumeroMeta() )) {
				return poVRPLDTO;
			}

		}

		return null;
	}

	public void atualizarLicitacoesDaProposta(Handle transaction, PropostaBD proposta) {
		List<LicitacaoBD> licitacoesDoBancoLocal = consultaLicitacoesDoBancoLocal(usuarioLogado, transaction);

		//this.verificaSeHouveApostilamentoParaAlgumaLicitacaoLocal(licitacoesDoBancoLocal);

		List<LicitacaoIntegracao> licitacoesDoServico = consultaLicitacoesDoServico(usuarioLogado);

		List<LicitacaoBD> novasLicitacoes = meDeAsNovasLicitacoesDaProposta(proposta, licitacoesDoBancoLocal,
				licitacoesDoServico);

		List<LicitacaoBD> licitacoesASeremAtualizadas = meDeAsLicitacoesASeremAtualizadas(licitacoesDoBancoLocal,
				licitacoesDoServico);

		List<LicitacaoBD> licitacoesASeremApagadas = meDeAsLicitacoesApagadasDaProposta(proposta, licitacoesDoBancoLocal, licitacoesDoServico);
		this.apagaFornecedoresPorLicitacao(licitacoesASeremApagadas);
		this.apagaLicitacoes(licitacoesASeremApagadas);

		this.atualiza(transaction, licitacoesASeremAtualizadas);

		List<LicitacaoBD> novasLicitacoesInseridas = this.insere(transaction, novasLicitacoes);

		List<FornecedorBD> fornecedoresAtualizados = this.atualizaFornecedoresDasLicitacoes(transaction,
				licitacoesASeremAtualizadas);

		List<FornecedorBD> novosFornecedoresDasNovasLicitacoes = recuperaFornecedoresDasLicitacoesAPartirDoServico(
				novasLicitacoesInseridas);

		List<FornecedorBD> novosFornecedoresDasLicitacoesAtualizadas = recuperaNovosFornecedoresDasLicitacoesAtualizadas(
				licitacoesASeremAtualizadas, fornecedoresAtualizados);

		this.importarFornecedores(transaction, novosFornecedoresDasNovasLicitacoes);
		this.importarFornecedores(transaction, novosFornecedoresDasLicitacoesAtualizadas);
	}

	/**
	 * RN: 540583:
	 * SICONV-DocumentosOrcamentarios-ECU-ENTRADA-VerificacaoResultadoProcessoLicitatorio
	 * <p>
	 * E10.1.1. Verifica que foi criado apostilamento para Licitação selecionada do
	 * tipo "Processo de execução sem VRPL".
	 * <p>
	 * E10.1.2. Exibe a seguinte mensagem de alerta: 777320:
	 * SICONV-DocumentosOrcamentarios-ManterAssociacaoDeLotesALicitacao-MSG-Alerta-LicitacaoComApostilamento
	 */
	private void verificaSeHouveApostilamentoParaAlgumaLicitacaoLocal(List<LicitacaoBD> licitacoesDoBancoLocal) {
		Set<Long> conjuntoDeIDsDasLicitacoesDoBancoLocal = extraiOsIDsDasLicitacoesDoBancoLocal(licitacoesDoBancoLocal);

		if (conjuntoDeIDsDasLicitacoesDoBancoLocal.isEmpty()) {
			return;
		}

		LicitacoesResponse licitacoesComApostilamento = getClientLicitacoesGRPC().consultaLicitacoesEmApostilamento(new ArrayList<>(conjuntoDeIDsDasLicitacoesDoBancoLocal));

		for (LicitacaoEmApostilamento licitacaComApostilamento : licitacoesComApostilamento.getRespostaList()) {
			if (conjuntoDeIDsDasLicitacoesDoBancoLocal.contains(licitacaComApostilamento.getIdLicitacao())) {

				Optional<LicitacaoBD> licitacaoBD = licitacoesDoBancoLocal.stream().filter(licitacao -> licitacao.getIdLicitacaoFk().equals(licitacaComApostilamento.getIdLicitacao())).findFirst();

				throw new BusinessException(ErrorInfo.INCONSISTENCIA_VRPL_LICITACAO_COM_APOSTILAMENTO, licitacaoBD.get().getNumeroAno(), licitacaComApostilamento.getNumeroApostilamento());
			}
		}

	}

	protected List<LicitacaoBD> consultaLicitacoesDoBancoLocal(SiconvPrincipal usuarioLogado, Handle transaction) {
		List<LicitacaoBD> licitacoes = transaction.attach(LicitacaoDAO.class)
				.recuperarLicitacoesAtuaisDaProposta(usuarioLogado);

		return licitacoes;
	}

	@Log
	protected List<LicitacaoBD> meDeAsLicitacoesASeremAtualizadas(List<LicitacaoBD> licitacoesDoBancoLocal,
			List<LicitacaoIntegracao> licitacaoesDoServico) {

		Set<Long> conjuntoDeIDsDasLicitacoesDoServico = extraiOsIDsDasLicitacoesDoServico(licitacaoesDoServico);

		List<String> situacoesDasLicitacoesQueDevemSerAtualizadas = Arrays.asList(EM_ANALISE.getSigla(),
				EM_PREENCHIMENTO.getSigla(), ENVIADA_PARA_ANALISE.getSigla(), SOLICITADA_COMPLEMENTACAO.getSigla(),
				EM_COMPLEMENTACAO.getSigla());

		Predicate<LicitacaoBD> removeLicitacoesComSituacoesDeExclusao = licitacaoBanco -> situacoesDasLicitacoesQueDevemSerAtualizadas
				.contains(licitacaoBanco.getSituacaoDaLicitacao());

		Predicate<LicitacaoBD> idDaLicitacaoDoBancoLocalEhIgualAoIdDaLicitacaoDoServico = licitacaoBanco -> conjuntoDeIDsDasLicitacoesDoServico
				.contains(licitacaoBanco.getIdLicitacaoFk());

		List<LicitacaoBD> licitacoes = licitacoesDoBancoLocal.stream().filter(
				removeLicitacoesComSituacoesDeExclusao.and(idDaLicitacaoDoBancoLocalEhIgualAoIdDaLicitacaoDoServico))
				.collect(Collectors.toList());

		List<LicitacaoBD> licitacoesParaAtualizar = new ArrayList<>();

		for (LicitacaoBD licitacaoASerAtualizada : licitacoes) {
			ProcessoExecucaoResponse licitacaoAtualizadaAPartirDoServico = getClientLicitacoesGRPC()
					.detalharProcessosExecucao(licitacaoASerAtualizada.getIdLicitacaoFk());

			licitacaoASerAtualizada.atualizaComInformacoesDoServico(licitacaoAtualizadaAPartirDoServico);

			licitacoesParaAtualizar.add(licitacaoASerAtualizada);
		}

		return licitacoesParaAtualizar;
	}

	@Log
	protected List<LicitacaoBD> meDeAsNovasLicitacoesDaProposta(PropostaBD proposta,
			List<LicitacaoBD> licitacoesDoBancoLocal, List<LicitacaoIntegracao> licitacoesDoServico) {

		Set<Long> conjuntoDeIDsDasLicitacoesDoBancoLocal = extraiOsIDsDasLicitacoesDoBancoLocal(licitacoesDoBancoLocal);

		List<LicitacaoIntegracao> licitacoes = licitacoesDoServico.stream().filter(
				licitacaoIntegracao -> !conjuntoDeIDsDasLicitacoesDoBancoLocal.contains(licitacaoIntegracao.getId()))
				.collect(Collectors.toList());

		List<LicitacaoBD> novasLicitacoes = new ArrayList<>();

		for (LicitacaoIntegracao licitacaoIntegracao : licitacoes) {

			ProcessoExecucaoResponse licitacaoServico = getClientLicitacoesGRPC()
					.detalharProcessosExecucao(licitacaoIntegracao.getId());

			LicitacaoBD novaLicitacao = new LicitacaoBD(proposta, licitacaoServico);

			novasLicitacoes.add(novaLicitacao);
		}

		return novasLicitacoes;
	}

	@Log
	protected List<LicitacaoBD> meDeAsLicitacoesApagadasDaProposta(PropostaBD proposta,
			List<LicitacaoBD> licitacoesDoBancoLocal, List<LicitacaoIntegracao> licitacoesDoServico) {

		Set<Long> conjuntoDeIDsDasLicitacoesDoServico = extraiOsIDsDasLicitacoesDoServico(licitacoesDoServico);

		List<LicitacaoBD> licitacoesApagadas = licitacoesDoBancoLocal.stream().filter(
				licitacaoBD -> !conjuntoDeIDsDasLicitacoesDoServico.contains(licitacaoBD.getIdLicitacaoFk()))
				.collect(Collectors.toList());

		return licitacoesApagadas;
	}

	@Log
	private List<FornecedorBD> atualizaFornecedoresDasLicitacoes(Handle transaction, List<LicitacaoBD> licitacoes) {

		List<FornecedorBD> fornecedoresDoBancoLocal = this
				.recuperaFornecedoresDasLicitacoesDoBancoLocal(transaction, licitacoes);

		List<FornecedorBD> fornecedoresASeremAtualizados = new ArrayList<>();

		List<Long> idsFornecedoresASeremExcluidos = new ArrayList<>();

		Set<FornecedorBD> todosOsFornecedoresDeTodasAsLicitacoes = new HashSet<>();

		for (LicitacaoBD licitacao : licitacoes) {
			ProcessoExecucaoResponse dadosDaLicitacaoObtidaPeloServico = getClientLicitacoesGRPC()
					.detalharProcessosExecucao(licitacao.getIdLicitacaoFk());

			List<Fornecedor> fornecedoresServico = dadosDaLicitacaoObtidaPeloServico
					.getListaFornecedoresVencedoresList();

			for (Fornecedor fornecedor : fornecedoresServico) {
				FornecedorBD fornecedorConvertido = new FornecedorBD();
				fornecedorConvertido.updateFrom(fornecedor);
				fornecedorConvertido.setLicitacaoFk(licitacao.getId());

				todosOsFornecedoresDeTodasAsLicitacoes.add(fornecedorConvertido);
			}
		}

		for (FornecedorBD fornecedorDoBanco : fornecedoresDoBancoLocal) {
			for (FornecedorBD fornecedorDoServico : todosOsFornecedoresDeTodasAsLicitacoes) {
				if (fornecedorDoBanco.equals(fornecedorDoServico)) {
					fornecedorDoBanco.updateFrom(fornecedorDoServico);
					fornecedoresASeremAtualizados.add(fornecedorDoBanco);
				}
			}

			if ( ! todosOsFornecedoresDeTodasAsLicitacoes.contains(fornecedorDoBanco) ) {
				idsFornecedoresASeremExcluidos.add(fornecedorDoBanco.getId());
			} // TODO else -> verificar se o update acima nao poderia estar nesse else aqui (otimizar codigo)
		}

		List<FornecedorBD> fornecedoresAtualizados = atualizaFornecedores(transaction,
				fornecedoresASeremAtualizados);

		try {
			this.deleteFornecedores(transaction, idsFornecedoresASeremExcluidos);
		} catch(Exception pe) {
			this.verificaSeHouveApostilamentoParaAlgumaLicitacaoLocal(licitacoes);
		}
		
		return fornecedoresAtualizados;
	}

	private void importarFornecedores(Handle transaction, List<FornecedorBD> novoFornecedores) {
		insertFornecedores(transaction, novoFornecedores);
	}

	protected Set<Long> extraiOsIDsDasLicitacoesDoServico(List<LicitacaoIntegracao> licitacaoesDoServico) {
		Set<Long> licitacoes = licitacaoesDoServico.stream().map(LicitacaoIntegracao::getId)
				.collect(Collectors.toSet());

		return licitacoes;
	}

	protected Set<Long> extraiOsIDsDasLicitacoesDoBancoLocal(List<LicitacaoBD> licitacoesDoBancoLocal) {
		Set<Long> licitacoes = licitacoesDoBancoLocal.stream().map(LicitacaoBD::getIdLicitacaoFk)
				.collect(Collectors.toSet());

		return licitacoes;
	}

	protected Set<String> extraiOsIDsDosFornecedoresDoServico(Set<Fornecedor> fornecedoresDoServico) {
		Set<String> idsFornecedores = fornecedoresDoServico.stream().map(Fornecedor::getIdentificacao)
				.collect(Collectors.toSet());

		return idsFornecedores;
	}



	@Log
	protected List<LicitacaoIntegracao> consultaLicitacoesDoServico(SiconvPrincipal usuarioLogado) {
		List<LicitacaoIntegracao> licitacoes = getClientLicitacoesGRPC()
				.listarProcessosExecucao(usuarioLogado.getIdProposta()).getListaProcessoExecucaoList().stream()
				.map(LicitacaoIntegracao::new).collect(Collectors.toList());

		return licitacoes;
	}

	protected List<FornecedorBD> recuperaFornecedoresDasLicitacoesAPartirDoServico(List<LicitacaoBD> licitacoes) {

		if (licitacoes.isEmpty()) {
			return new ArrayList<>();
		}

		Long idPropostaSiconv = usuarioLogado.getIdProposta();

		List<FornecedorIntegracao> fornecedoresRecuperadosPelaIntegracao = getSiconvBC()
				.recuperarFornecedoresProposta(idPropostaSiconv);

		List<FornecedorBD> fornecedores = new ArrayList<>();

		licitacoes.forEach(licitacaoBD -> fornecedoresRecuperadosPelaIntegracao.forEach(fornecedorIntegracao -> {
			if (licitacaoBD.getIdLicitacaoFk().compareTo(fornecedorIntegracao.getIdLicitacao()) == 0) {
				FornecedorBD fornecedorBD = FornecedorBD.from(fornecedorIntegracao);

				fornecedorBD.setLicitacaoFk(licitacaoBD.getId());
				fornecedores.add(fornecedorBD);
			}
		}));

		return fornecedores;
	}

	protected List<FornecedorBD> recuperaNovosFornecedoresDasLicitacoesAtualizadas(List<LicitacaoBD> licitacoes,
			List<FornecedorBD> fornecedoresAtualizados) {

		if (licitacoes.isEmpty()) {
			return new ArrayList<>();
		}

		Long idPropostaSiconv = usuarioLogado.getIdProposta();

		List<FornecedorIntegracao> fornecedoresRecuperadosPelaIntegracao = getSiconvBC()
				.recuperarFornecedoresProposta(idPropostaSiconv);

		List<FornecedorBD> fornecedores = new ArrayList<>();

		for (LicitacaoBD licitacaoBD : licitacoes) {
			for (FornecedorIntegracao fornecedorIntegracao : fornecedoresRecuperadosPelaIntegracao) {

				if( licitacaoBD.getIdLicitacaoFk().equals( fornecedorIntegracao.getIdLicitacao() ) ) {
					FornecedorBD fornecedorBD = FornecedorBD.from(fornecedorIntegracao);
					fornecedorBD.setLicitacaoFk(licitacaoBD.getId());

					if (!fornecedoresAtualizados.contains(fornecedorBD)) {
						fornecedores.add(fornecedorBD);
					}
				}
			}
		}

		return fornecedores;
	}

	protected ClientLicitacoesInterface getClientLicitacoesGRPC() {
		return clientLicitacoes;
	}

	private void sincronizarLicitacoesComSICONV() {
		dao.getJdbi().useTransaction(transaction -> {
			PropostaDAO propostaDao = transaction.attach(PropostaDAO.class);
			PropostaBD proposta = propostaDao.recuperaUltimaVersaoDaProposta(usuarioLogado);

			this.atualizarLicitacoesDaProposta(transaction, proposta);
		});
	}

	public List<LicitacaoDTO> recuperarLicitacoesSincronizadasComSICONV() {

		this.sincronizarLicitacoesComSICONV();

		PropostaBD proposta = dao.get(PropostaDAO.class).recuperaUltimaVersaoDaProposta(usuarioLogado);
		List<LicitacaoDTO> licitacoes = licitacoesDaProposta(proposta, true);

		return licitacoes;
	}

	@Log
	private List<LicitacaoBD> importarLicitacao(PropostaBD proposta, Handle transaction) {

		List<LicitacaoBD> licitacoes = new ArrayList<>();

		List<LicitacaoIntegracao> licitacaoesIntegracao = consultaLicitacoesDoServico(usuarioLogado);

		for (LicitacaoIntegracao licitacaoIntegracao : licitacaoesIntegracao) {
			ProcessoExecucaoResponse licitacaoServico = getClientLicitacoesGRPC()
					.detalharProcessosExecucao(licitacaoIntegracao.getId());

			LicitacaoBD novaLicitacao = new LicitacaoBD(proposta, licitacaoServico);

			List<LicitacaoBD> licitacaoInserida = insere(transaction, novaLicitacao);

			licitacoes.addAll(licitacaoInserida);
		}

		return licitacoes;

	}

	public void importarLicitacoesEFornecedores(Handle transaction, PropostaBD proposta) {
		List<LicitacaoBD> licitacoes = this.importarLicitacao(proposta, transaction);

		List<FornecedorBD> fornecedores = recuperaFornecedoresDasLicitacoesAPartirDoServico(licitacoes);

		this.importarFornecedores(transaction, fornecedores);
	}

}












