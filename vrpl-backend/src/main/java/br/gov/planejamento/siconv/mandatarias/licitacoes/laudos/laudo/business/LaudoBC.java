package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.dao.GrupoPerguntaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database.GrupoPerguntaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.LaudoNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.UsuarioJaAssinouParecerException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.UsuarioNaoPodeAlterarParecer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.UsuarioNaoPodeAssinarParecerException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.StatusParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.TipoDeParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.dao.PerguntaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.database.PerguntaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.dao.RespostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.dao.TemplateLaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.database.TemplateLaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.HistoricoLicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.business.PendenciaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.dao.PendenciaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.dto.PendenciaDTO;

public class LaudoBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private ParecerValidator parecerValidator;

	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private HistoricoLicitacaoBC historicoBC;
	
	@Inject
	private PendenciaBC pendenciaBC;

	public LaudoDTO recuperarLaudoPorId(Long id) {
		LaudoBD laudoBD = dao.get(LaudoDAO.class).recuperarLaudoPorId(id);
		if (laudoBD == null) {
			throw new LaudoNaoEncontradoException();
		}

		LaudoDTO laudoDTO = laudoBD.converterParaDTO();
		return laudoDTO;
	}

	public LaudoDTO recuperarLaudoPorLicitacao(Long idLicitacao, TipoDeParecerEnum tipoDeLaudo) {
		LaudoBD laudoBD = dao.get(LaudoDAO.class).recuperarLaudoPorLicitacao(idLicitacao, tipoDeLaudo.name());

		if (laudoBD == null) {
			TemplateLaudoBD templateBD = dao.get(TemplateLaudoDAO.class)
					.recuperarTemplateLaudoPorTipo(tipoDeLaudo.name());

			laudoBD = new LaudoBD();
			laudoBD.setLicitacaoFk(idLicitacao);
			laudoBD.setTemplateFk(templateBD.getId());
			laudoBD.setInStatus(StatusParecerEnum.RASCUNHO.getCodigo());
			laudoBD.setVersaoNr(0L);
			laudoBD = dao.get(LaudoDAO.class).inserirLaudo(laudoBD);
		}

		LaudoDTO dto = laudoBD.converterParaDTO();
		dto.setRespostas(new LinkedList<RespostaDTO>());

		List<RespostaBD> respostas = dao.get(RespostaDAO.class).recuperarRespostaPorLaudo(dto.getId());
		for (RespostaBD respostaBD : respostas) {
			RespostaDTO respostaDTO = respostaBD.converterParaDTO();
			respostaDTO.setLaudoFk(dto.getId());

			PerguntaBD perguntaBD = dao.get(PerguntaDAO.class).recuperarPerguntaPorId(respostaBD.getPerguntaFk());
			PerguntaDTO perguntaDTO = perguntaBD.converterParaDTO();

			GrupoPerguntaBD grupo = dao.get(GrupoPerguntaDAO.class)
					.recuperarGrupoPerguntaPorId(perguntaBD.getGrupoFk());
			perguntaDTO.setGrupo(grupo.converterParaDTO());

			respostaDTO.setPergunta(perguntaDTO);

			if (respostaBD.getLoteFk() != null) {
				respostaDTO.setLote(
						LoteDTO.from(dao.get(LoteLicitacaoDAO.class).recuperarLotePorId(respostaBD.getLoteFk())));
				perguntaDTO.setLoteId(respostaBD.getLoteFk());
			}

//			if (perguntaTemRespostaAutomatica(perguntaBD, tipoDeLaudo)) {
//				preencheRespostaAutomatica(perguntaBD, respostaDTO, idLicitacao);
//			}

			dto.getRespostas().add(respostaDTO);
		}

		return dto;
	}

	private void preencheRespostaAutomatica(PerguntaBD perguntaBD, RespostaDTO respostaDTO, Long idLicitacao) {

		if (perguntaQualOrcamentoUtilizado(perguntaBD)) {
			List<PoBD> relacaoDePOs = dao.get(PoDAO.class).recuperarPosPorLicitacaoELote(idLicitacao,
					respostaDTO.getLote().getId());

			List<String> referencias = relacaoDePOs.stream().map(po -> po.getReferencia()).distinct()
					.collect(Collectors.toList());

			if ((referencias.isEmpty() || (referencias.size() > 1))) {
				throw new IllegalArgumentException(
						"É esperado que exista um tipo de Orçamento de Referência para a PO. Foram encontrados os seguintes tipos: "
								+ referencias);
			} else {
				String referencia = referencias.get(0);

				respostaDTO.setResposta(referencia);
			}

		} else if (perguntaValorTotalDoLoteMenorOuIgualAoOrcamentoUtilizadoParaComparacao(perguntaBD)) {

		} else if (perguntaPrecoUnitario(perguntaBD)) {

		}

	}

	protected boolean perguntaPrecoUnitario(PerguntaBD perguntaBD) {
		return perguntaBD.getTitulo().equals(
				"O preço unitário de cada item do orçamento da empresa vencedora da licitação é menor ou igual ao orçamento utilizado para comparação");
	}

	protected boolean perguntaValorTotalDoLoteMenorOuIgualAoOrcamentoUtilizadoParaComparacao(PerguntaBD perguntaBD) {
		return perguntaBD.getTitulo()
				.equals("O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?");
	}

	protected boolean perguntaQualOrcamentoUtilizado(PerguntaBD perguntaBD) {
		return perguntaBD.getTitulo().equals("Orçamento utilizado para comparação");
	}

	private boolean perguntaTemRespostaAutomatica(PerguntaBD perguntaBD, TipoDeParecerEnum tipoDeLaudo) {
		if (TipoDeParecerEnum.VRPL == tipoDeLaudo) {
			return peguntasComRespostasAutomaticasDoParecerDeEngenharia(perguntaBD);
		} else if (TipoDeParecerEnum.VRPLS == tipoDeLaudo) {
			return peguntasComRespostasAutomaticasDoParecerDeSocial(perguntaBD);
		}

		return false;
	}

	private boolean peguntasComRespostasAutomaticasDoParecerDeSocial(PerguntaBD perguntaBD) {
		// Para o Parecer de Trabalho Social, as seguintes perguntas tem resposta
		// automática:

		List<String> perguntasComRespostaAutomatica = Arrays
				.asList("O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?");

		return perguntasComRespostaAutomatica.contains(perguntaBD.getTitulo());

	}

	private boolean peguntasComRespostasAutomaticasDoParecerDeEngenharia(PerguntaBD perguntaBD) {
		// Para o Parecer de Engenharia, as seguintes perguntas tem resposta automática:

		List<String> perguntasComRespostaAutomatica = Arrays.asList("Orçamento utilizado para comparação",
				"O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?",
				"O preço unitário de cada item do orçamento da empresa vencedora da licitação é menor ou igual ao orçamento utilizado para comparação");

		return perguntasComRespostaAutomatica.contains(perguntaBD.getTitulo());
	}

	public void salvarLaudo(LaudoDTO laudo) {
		if (podeEditarLaudo(laudo)) {
			if (laudo.estaAssinado()) {
				verificaSeUsuarioPodeAssinar(laudo);
			}
	
			dao.getJdbi().useTransaction(transaction -> {
				laudo.definirResultadoDoLaudo();
	
				if (laudo.estaAssinado()) {
					salvarHistorico(laudo);
				} else if (laudo.estaCancelado()) {
					salvarHistorico(laudo);
	
					laudo.setInStatus(StatusParecerEnum.RASCUNHO.getCodigo());
				} else if (laudo.estaEmitido()) {
					if (parecerValidator.isValid(laudo)) {
						salvarHistorico(laudo);
					}
				}
	
				if (laudo.getId() == null) {
					LaudoBD bd = transaction.attach(LaudoDAO.class).inserirLaudo(laudo.converterParaBD());
					laudo.setId(bd.getId());
				} else {
					transaction.attach(LaudoDAO.class).atualizarLaudo(laudo.converterParaBD());
				}
	
				transaction.attach(RespostaDAO.class).excluirRespostasPorLaudo(laudo.getId());
	
				for (RespostaDTO r : laudo.getRespostas()) {
					// verificar se respostas ja existem
					transaction.attach(RespostaDAO.class).inserirResposta(r.converterParaBD());
				}
			});
		} else {
			throw new UsuarioNaoPodeAlterarParecer();
		}
	}

	private boolean podeEditarLaudo(LaudoDTO laudo) {
		PropostaBD prop = dao.get(PropostaDAO.class).recuperaUltimaVersaoDaProposta(usuarioLogado);
		
		boolean ehTermoDeCompromissoMandataria = prop.getModalidade() == 11 && prop.getTermoCompromissoTemMandatar();
		boolean ehTermoDeCompromissoConcedente = prop.getModalidade() == 11 && !prop.getTermoCompromissoTemMandatar();
		boolean ehConvenio = prop.getModalidade() == 1 || prop.getModalidade() == 6;
		boolean ehContratoDeRepasse = prop.getModalidade() == 2;
		
		if(usuarioLogado.isMandataria()) {
			return ehContratoDeRepasse || ehTermoDeCompromissoMandataria;
		}
		
		if(usuarioLogado.isConcedente()) {
			return ehConvenio || ehTermoDeCompromissoConcedente;
		}
		
		return false;
	}

	private void verificaSeUsuarioPodeAssinar(LaudoDTO laudo) {
		EventoQuadroResumoEnum eventoEmitir = EventoQuadroResumoEnum.EMITIR_PARECER_ENGENHARIA;
		EventoQuadroResumoEnum eventoCancelar = EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_ENGENHARIA;
		EventoQuadroResumoEnum eventoAssinar =  EventoQuadroResumoEnum.ASSINAR_PARECER_ENGENHARIA;
		
		if (laudo.ehVRPLS()) {
			eventoEmitir = EventoQuadroResumoEnum.EMITIR_PARECER_SOCIAL;
			eventoCancelar = EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_SOCIAL;
			eventoAssinar = EventoQuadroResumoEnum.ASSINAR_PARECER_SOCIAL;
			
		} 
		List<HistoricoLicitacaoBD> hists = historicoBC.findHistoricoByIdLicitacaoEvento(laudo.getLicitacaoFk(),
				eventoEmitir.getSigla());
		//COMPARAR apenas com o primeiro nao com todos
		if  (!hists.isEmpty()) {
			//comparar apenas com o mais recente
			HistoricoLicitacaoBD historicoLicitacaoBD = hists.get(0);
			if (usuarioLogado.getCpf().equals(historicoLicitacaoBD.getCpfDoResponsavel())) {
				throw new UsuarioNaoPodeAssinarParecerException();
			}
		}
		
		//se houve cancelamentos verificar apenas as assinaturas apos o cancelamento
		if (historicoBC.consultarSeHouveCancelamentos(laudo.getLicitacaoFk(), eventoCancelar.getSigla())) {
			hists = historicoBC.findHistoricoByIdLicitacaoEventoConsiderandoCancelamento(laudo.getLicitacaoFk(),
					eventoCancelar.getSigla(), eventoAssinar.getSigla());
		} else {
			//se não houve cancelamentos considerar todas as assinaturas
			hists = historicoBC.findHistoricoByIdLicitacaoEvento(laudo.getLicitacaoFk(),
					eventoAssinar.getSigla());
		}

		for (HistoricoLicitacaoBD historicoLicitacaoBD : hists) {
			if (usuarioLogado.getCpf().equals(historicoLicitacaoBD.getCpfDoResponsavel())) {
				throw new UsuarioJaAssinouParecerException();
			}
		}
	}

	private void salvarHistorico(LaudoDTO laudo) {
		HistoricoLicitacaoDTO novoHistorico = new HistoricoLicitacaoDTO();
		novoHistorico.setIdentificadorDaLicitacao(laudo.getLicitacaoFk());

		if (laudo.estaEmitido()) {
			novoHistorico.setEventoGerador(laudo.getEventoEmissao());
			novoHistorico.setConsideracoes(laudo.getComentarioEmissao());
		} else if (laudo.estaAssinado()) {
			novoHistorico.setEventoGerador(laudo.getEventoAssinatura());
			novoHistorico.setConsideracoes(laudo.getComentarioAssinatura());
		} else if (laudo.estaCancelado()) {
			novoHistorico.setEventoGerador(laudo.getEventoCancelamento());
			novoHistorico.setConsideracoes(laudo.getComentarioCancelamento());
		}

		historicoBC.salvarHistorico(novoHistorico);
	}

	public Boolean existeParecerEmitidoViavelParaALicitacao(LicitacaoBD licitacao) {
		List<LaudoBD> laudos = dao.get(LaudoDAO.class).existeParecerEmitidoViavelParaALicitacao(licitacao);

		return !laudos.isEmpty();
	}

	public Boolean existeParecerEmitidoParaALicitacao(LicitacaoBD licitacao) {
		List<LaudoBD> laudos = dao.get(LaudoDAO.class).existeParecerEmitidoParaALicitacao(licitacao);

		return !laudos.isEmpty();
	}
	
	public String quemEmitiuLaudo(Long idLicitacao, String tipoParecer) {
		return historicoBC.quemEmitiuLaudo(idLicitacao, tipoParecer);
	}
	
	public void deletarLaudosDaLicitacao(Long idLicitacao, Handle transaction) {
		LaudoDTO laudoEngenharia = this.recuperarLaudoPorLicitacao(idLicitacao, TipoDeParecerEnum.VRPL);
		this.excluirLaudo(laudoEngenharia, transaction);
		
		LaudoDTO laudoSocial = this.recuperarLaudoPorLicitacao(idLicitacao, TipoDeParecerEnum.VRPLS);
		this.excluirLaudo(laudoSocial, transaction);
	}
	
	private void excluirLaudo(LaudoDTO laudo, Handle transaction) {
		List<PendenciaDTO> pendencias = pendenciaBC.recuperarPendenciaPorLaudo(laudo.getId());
		for (PendenciaDTO pendenciaDTO : pendencias) {
			transaction.attach(PendenciaDAO.class).excluirPendencia(pendenciaDTO.getId());
		}
		
		for (RespostaDTO resposta : laudo.getRespostas()) {
			transaction.attach(RespostaDAO.class).excluirResposta(resposta.getRespostaId());
		}
		
		transaction.attach(LaudoDAO.class).excluirLaudo(laudo.getId());
	}

}
