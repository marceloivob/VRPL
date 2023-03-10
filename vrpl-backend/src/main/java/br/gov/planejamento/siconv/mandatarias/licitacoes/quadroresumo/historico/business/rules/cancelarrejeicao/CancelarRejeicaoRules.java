package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelarrejeicao;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.SubmetaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteNaoExisteMaisException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.dao.HistoricoLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * RN: 595818 -
 * SICONV-DocumentosOrcamentarios-ManterQuadroResumo-VRPL-RN-ValidacaoCancelamentoRejeicao
 **/
public class CancelarRejeicaoRules implements QuadroResumoValidator {

	@Getter
	@Setter
	@Inject
	private SiconvPrincipal usuarioLogado;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PropostaBD proposta;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;
	
	@Inject
	private BusinessExceptionContext businessExceptionContext;
	
	@Override
	public void validate() {
		this.validarNumeroDeLote(proposta, licitacao, usuarioLogado);
	}

	// Regra 01 - N??mero do Lote
	protected void validarNumeroDeLote(PropostaBD propostaAtual, LicitacaoBD licitacaoEstadoAtual, SiconvPrincipal usuarioLogado) {	
		this.validarExistenciaTodosNumerosDeLotes(propostaAtual, licitacaoEstadoAtual, usuarioLogado);
		this.validarNaoAssociacaoDeLoteComOutraLicitacao(propostaAtual, licitacaoEstadoAtual, usuarioLogado);
		this.validarAssociacaoDeLoteComMesmasSubmetas(propostaAtual, licitacaoEstadoAtual, usuarioLogado);
		this.validarUltimaLicitacaoRejeitadaAssociadaALote(propostaAtual, licitacaoEstadoAtual, usuarioLogado);
	}

	/**
	 * Valida????o do N??mero de Lote
	 * Impedir o cancelamento da rejei????o de uma licita????o se:
	 * 1.1 - Pelo menos um n??mero de lote n??o existe mais.
	 * 
	 * @param propostaAtual
	 * @param licitacaoEstadoAtual
	 * @param usuarioLogado
	 * @return
	 */
	protected void validarExistenciaTodosNumerosDeLotes(PropostaBD propostaAtual, LicitacaoBD licitacaoEstadoAtual, SiconvPrincipal usuarioLogado) {
		List<Long> listaNumeroLotesLicitacaoAtual = getLoteLicitacaoDAO().findLotesByIdLicitacao(licitacaoEstadoAtual.getId())
				.stream().map(LoteBD::getNumeroDoLote).collect(Collectors.toList());
		
		List<Long> listaNumeroLotesAtivosProposta = getLoteLicitacaoDAO().findLotesAtivosByIdPropostaSiconv(usuarioLogado, propostaAtual.getVersaoNr().longValue())
				.stream().map(LoteBD::getNumeroDoLote).collect(Collectors.toList());
		
		// Verifica se a proposta ainda cont??m todos os n??meros de lotes da licita????o que se deseja cancelar a rejei????o
		if (!listaNumeroLotesAtivosProposta.containsAll(listaNumeroLotesLicitacaoAtual)) {
			listaNumeroLotesLicitacaoAtual.removeIf(nrLoteLic -> listaNumeroLotesAtivosProposta.contains(nrLoteLic));
			listaNumeroLotesLicitacaoAtual.sort(Comparator.naturalOrder());
			businessExceptionContext.add(new CancelamentoRejeicaoLoteNaoExisteMaisException(listaNumeroLotesLicitacaoAtual));
		}
	}
	
	/**
	 * Valida????o do N??mero de Lote
	 * Impedir o cancelamento da rejei????o de uma licita????o se:
	 * 1.2 - Existe pelo menos um lote j?? associado a uma outra licita????o.
	 * 
	 * @param propostaAtual
	 * @param licitacaoEstadoAtual
	 * @param usuarioLogado
	 * @return
	 */
	protected void validarNaoAssociacaoDeLoteComOutraLicitacao(PropostaBD propostaAtual, LicitacaoBD licitacaoEstadoAtual, SiconvPrincipal usuarioLogado) {
		List<LoteBD> listaLoteAssociado = new ArrayList<LoteBD>();
		Map<String, List<Long>> mapLicitacaoLotesAssociados = new HashMap<String, List<Long>>();
		
		List<LoteBD> listaLoteLicitacaoAtual = getLoteLicitacaoDAO().findLotesByIdLicitacao(licitacaoEstadoAtual.getId());
		List<LoteBD> listaLoteAtivoProposta = getLoteLicitacaoDAO().findLotesAtivosByIdPropostaSiconv(usuarioLogado, propostaAtual.getVersaoNr().longValue());
		
		// Verifica se os lotes da licita????o rejeitada j?? foram associados a outra licita????o
		listaLoteLicitacaoAtual.forEach(loteLicAtual -> {
			listaLoteAssociado.addAll(listaLoteAtivoProposta.stream()
				.filter(loteAtivoProposta -> 
					(loteAtivoProposta.getNumeroDoLote() == loteLicAtual.getNumeroDoLote() && loteAtivoProposta.getIdentificadorDaLicitacao() != null))
				.collect(Collectors.toList()));
		});
		
		listaLoteAssociado.forEach(lote -> {
			String numeroAnoLicitacao = this.getLicitacaoDAO().findLicitacaoById(lote.getIdentificadorDaLicitacao()).getNumeroAno();
			
			if (mapLicitacaoLotesAssociados.containsKey(numeroAnoLicitacao)) {
				mapLicitacaoLotesAssociados.get(numeroAnoLicitacao).add(lote.getNumeroDoLote());
			} else {
				mapLicitacaoLotesAssociados.put(numeroAnoLicitacao, new ArrayList<Long>(Arrays.asList(lote.getNumeroDoLote())));
			}
		});
		
		
		if (!mapLicitacaoLotesAssociados.isEmpty()) {
			Map<String, List<Long>> mapLicitacaoLotesAssociadosSorted = mapLicitacaoLotesAssociados.entrySet()
					.stream().sorted(Map.Entry.comparingByKey())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			
			mapLicitacaoLotesAssociadosSorted.forEach((licitacao, lote) -> mapLicitacaoLotesAssociadosSorted.get(licitacao).sort(Comparator.naturalOrder()));
			
			businessExceptionContext.add(new CancelamentoRejeicaoLoteJaAssociadoAOutraLicitacaoException(mapLicitacaoLotesAssociadosSorted));
		}
	}
	
	/**
	 * Valida????o do N??mero de Lote
	 * Impedir o cancelamento da rejei????o de uma licita????o se:
	 * 1.3 - Existe pelo menos um lote cujo conjunto de submetas n??o ?? o mesmo do momento da rejei????o.
	 * 
	 * @param propostaAtual
	 * @param licitacaoEstadoAtual
	 * @param usuarioLogado
	 * @return
	 */
	protected void validarAssociacaoDeLoteComMesmasSubmetas(PropostaBD propostaAtual, LicitacaoBD licitacaoEstadoAtual, SiconvPrincipal usuarioLogado) {
		List<Long> listaNrLoteSubmetasDiferentes = new ArrayList<Long>();
		Map<Long, List<String>> mapSubmetasDiferentesLoteLicitacaoAtual = new HashMap<Long, List<String>>();
		Map<Long, List<String>> mapSubmetasDiferentesLoteAtivoProposta = new HashMap<Long, List<String>>();

		List<LoteBD> listaLoteLicitacaoAtual = getLoteLicitacaoDAO().findLotesComAssociacoesByIdLicitacao(licitacaoEstadoAtual.getId());
		List<LoteBD> listaLoteAtivoProposta = getLoteLicitacaoDAO().findLotesAtivosByIdPropostaSiconv(usuarioLogado, propostaAtual.getVersaoNr().longValue());
		
		// Identifica os lotes que n??o possuem as mesmas submetas do momento da rejei????o
		listaLoteLicitacaoAtual.forEach(loteLicAtual -> {
			listaNrLoteSubmetasDiferentes.addAll(listaLoteAtivoProposta.stream()
				.filter(loteAtivoProposta -> 
					(loteAtivoProposta.getNumeroDoLote() == loteLicAtual.getNumeroDoLote() 
						&& this.lotesPossuemSubmetasDiferentes(loteLicAtual, loteAtivoProposta)))
				.map(LoteBD::getNumeroDoLote)
				.collect(Collectors.toList()));
		});
		
		
		if (!listaNrLoteSubmetasDiferentes.isEmpty()) {
			// Para cada lote recupera as submetas da licita????o atual e as submetas do lote ativo
			listaNrLoteSubmetasDiferentes.stream().forEach(nrLote -> {
				mapSubmetasDiferentesLoteLicitacaoAtual.put(nrLote,
						listaLoteLicitacaoAtual.stream()
							.filter(lote -> lote.getNumeroDoLote() == nrLote)
							.map(LoteBD::getSubmetas)
							.flatMap(Collection<SubmetaBD>::stream)
							.map(SubmetaDTO::from)
							.map(SubmetaDTO::getSubmeta)
							.sorted()
							.collect(Collectors.toList()));
				mapSubmetasDiferentesLoteAtivoProposta.put(nrLote,
						listaLoteAtivoProposta.stream()
							.filter(lote -> lote.getNumeroDoLote() == nrLote)
							.map(LoteBD::getSubmetas)
							.flatMap(Collection<SubmetaBD>::stream)
							.map(SubmetaDTO::from)
							.map(SubmetaDTO::getSubmeta)
							.sorted()
							.collect(Collectors.toList()));
			});
			
			listaNrLoteSubmetasDiferentes.sort(Comparator.naturalOrder());
			
			businessExceptionContext.add(new CancelamentoRejeicaoLoteNaoMaisAssociadoMesmasSubmetasException(listaNrLoteSubmetasDiferentes, mapSubmetasDiferentesLoteLicitacaoAtual, mapSubmetasDiferentesLoteAtivoProposta));
		}
	}
	
	private boolean lotesPossuemSubmetasDiferentes(LoteBD loteLicitacaoAtual, LoteBD loteAtivoProposta) {
		 List<Long> listaIdSumetaAnaliseLoteAtivoProposta = loteAtivoProposta.getSubmetas().stream()
		 	.map(SubmetaBD::getIdSubmetaAnalise).collect(Collectors.toList());
		 List<Long> listaIdSumetaAnaliseLoteLicitacaoAtual = loteLicitacaoAtual.getSubmetas().stream()
				 	.map(SubmetaBD::getIdSubmetaAnalise).collect(Collectors.toList());
		 
		 return !(listaIdSumetaAnaliseLoteAtivoProposta.containsAll(listaIdSumetaAnaliseLoteLicitacaoAtual) 
				 && listaIdSumetaAnaliseLoteLicitacaoAtual.containsAll(listaIdSumetaAnaliseLoteAtivoProposta));
	}
	
	
	/**
	 * Valida????o do N??mero de Lote
	 * Impedir o cancelamento da rejei????o de uma licita????o se:
	 * 1.4 - O lote est?? dispon??vel, mas j?? foi associado a uma outra licita????o que tamb??m foi rejeitada 
	 * - em um momento posterior ?? licita????o que se deseja cancelar a rejei????o.
	 * 
	 * Apenas a ??ltima licita????o rejeitada associada ao lote em quest??o ?? que pode ter a rejei????o cancelada
	 * 
	 * @param propostaAtual
	 * @param licitacaoEstadoAtual
	 * @param usuarioLogado
	 * @return
	 */
	protected void validarUltimaLicitacaoRejeitadaAssociadaALote(PropostaBD propostaAtual, LicitacaoBD licitacaoEstadoAtual, SiconvPrincipal usuarioLogado) {
		List<LoteBD> listaLoteLicitacaoAtual = getLoteLicitacaoDAO().findLotesByIdLicitacao(licitacaoEstadoAtual.getId());
		List<Long> listaNrLoteAtivoDisponivelProposta = getLoteLicitacaoDAO()
				.findLotesAtivosByIdPropostaSiconv(usuarioLogado, propostaAtual.getVersaoNr().longValue())
				.stream().filter(lote -> lote.getIdentificadorDaLicitacao() == null)
				.map(LoteBD::getNumeroDoLote).collect(Collectors.toList());
		
		// Apenas os lotes ativos dispon??veis na proposta e que estiveram associados a licita????o em quest??o
		listaNrLoteAtivoDisponivelProposta
				.retainAll(listaLoteLicitacaoAtual.stream().map(LoteBD::getNumeroDoLote).collect(Collectors.toList()));

		if (!listaNrLoteAtivoDisponivelProposta.isEmpty()) {
			List<LicitacaoBD> listaLicitacaoRejeitadaComMesmoNrLote = getLicitacaoDAO()
					.recuperarOutrasLicitacoesRejeitadasComMesmosNumerosLotes(
							licitacaoEstadoAtual, listaNrLoteAtivoDisponivelProposta);
			
			if (!listaLicitacaoRejeitadaComMesmoNrLote.isEmpty()) {
				Date dataRejeicaoLicitacaoAtual = recuperarDataUltimaRejeicaoHistoricoLicitacao(licitacaoEstadoAtual.getId());
		
				Map<String, List<Long>> mapLicitacaoLotesRejeitadaPosteriormente = new HashMap<String, List<Long>>();
				listaLicitacaoRejeitadaComMesmoNrLote.forEach(licitacao -> {
					Date dataRejeicaoLicitacaoComMesmoNrLote = recuperarDataUltimaRejeicaoHistoricoLicitacao(licitacao.getId());
					
					if (dataRejeicaoLicitacaoAtual.compareTo(dataRejeicaoLicitacaoComMesmoNrLote) < 0) {
						mapLicitacaoLotesRejeitadaPosteriormente.put(licitacao.getNumeroAno(), 
								licitacao.getLotesAssociados().stream().map(LoteBD::getNumeroDoLote).sorted().collect(Collectors.toList()));
					}
				});
				
				if (!mapLicitacaoLotesRejeitadaPosteriormente.isEmpty()) {
					Map<String, List<Long>> mapLicitacaoLotesRejeitadaPosteriormenteSorted = mapLicitacaoLotesRejeitadaPosteriormente.entrySet()
							.stream().sorted(Map.Entry.comparingByKey())
							.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
					
					businessExceptionContext.add(new CancelamentoRejeicaoLoteFoiAssociadoALicitacaoRejeitadaPosteriormenteException(mapLicitacaoLotesRejeitadaPosteriormenteSorted));
				}
			}
		}
	}
	
	/**
	 * Recupera no hist??rico a data da ??ltima rejei????o da licita????o 
	 * @param idLicitacao
	 * @return
	 */
	private Date recuperarDataUltimaRejeicaoHistoricoLicitacao(Long idLicitacao) {
		return getHistoricoLicitacaoDAO().findHistoricoLicitacaoByIdLicitacao(idLicitacao)
					.stream().map(HistoricoLicitacaoBD::converterParaDTO)
					.filter(historico -> historico.getSituacaoDaLicitacao().equals(SituacaoLicitacaoEnum.REJEITADA))
					.map(HistoricoLicitacaoDTO::getDataDeRegistro).max(Comparator.comparing(Date::getTime)).get();
	}
	
	protected LoteLicitacaoDAO getLoteLicitacaoDAO() {
		return dao.get(LoteLicitacaoDAO.class);
	}
	
	protected LicitacaoDAO getLicitacaoDAO() {
		return dao.get(LicitacaoDAO.class);
	}
	
	protected HistoricoLicitacaoDAO getHistoricoLicitacaoDAO() {
		return dao.get(HistoricoLicitacaoDAO.class);
	}
}
