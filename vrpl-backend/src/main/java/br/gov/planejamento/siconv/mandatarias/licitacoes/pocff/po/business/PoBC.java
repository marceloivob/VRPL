package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao.SiconvDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.business.EventoFrenteObraBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business.MacroServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.DataInicioObraInvalidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.ExisteDataBaseDiferenteNoMesmoLoteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.NaoExisteCargaSinapiParaDataBaseException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.PoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.exceptions.SemCargaSinapiEExisteDataBaseDiferenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PoBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<PoBD> constraintBeanValidation;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private MacroServicoBC macroServicoBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private EventoFrenteObraBC eventoFrenteObraBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private UserCanEditVerifier checkPermission;

	@Log
	public List<PoVRPLDTO> recuperarNovoPosPorLicitacao(LicitacaoBD licitacao) {
		List<PoVRPLDTO> dtos = dao.get(PoDAO.class).recuperarPosPorLicitacao(licitacao);

		if (!dtos.isEmpty()) {
			List<Long> listaIdPos = dtos.stream().map (PoVRPLDTO::getId).collect(Collectors.toList());

			Map<PoBD,PoMacroServicoServicosDTO> mapa = macroServicoBC.recuperarMacroServicoServicosPorPo(listaIdPos);

			if (mapa.isEmpty()) {
				return Collections.emptyList();
			}

			Collection<PoMacroServicoServicosDTO> listaPoMacroServicoServicosDTO = mapa.values();

			for (PoVRPLDTO poVRPLDTO : dtos) {
				PoMacroServicoServicosDTO poMacroServicoServicos = obterPoMacroServicsDTO(poVRPLDTO,listaPoMacroServicoServicosDTO);

				if (poMacroServicoServicos != null) {
					poVRPLDTO.setPrecoTotalAnalise( poMacroServicoServicos.getTotalGeralAceitoNaAnalise() );
					poVRPLDTO.setPrecoTotalLicitacao( poMacroServicoServicos.getTotalGeralLicitado() );
					poVRPLDTO.setPoMacroServicoServicos (poMacroServicoServicos);

				} else {
					log.warn("PO sem Macro Serviço: " + poVRPLDTO.getId());
				}
			}
		}

		return dtos;
	}


	private PoMacroServicoServicosDTO obterPoMacroServicsDTO(PoVRPLDTO poVRPLDTO,
			Collection<PoMacroServicoServicosDTO> listaPoMacroServicoServicosDTO) {

		Optional<PoMacroServicoServicosDTO> firstPoMacroServicoServicos = listaPoMacroServicoServicosDTO.stream().
				filter(poMacroServico -> poMacroServico.getPoId().equals(poVRPLDTO.getId())).
				findFirst();

		if (firstPoMacroServicoServicos.isPresent()) {
			return firstPoMacroServicoServicos.get();
		} else {
			return null;
		}
	}


	@Log
	public PoDetalhadaVRPLDTO findPoDetalhada(Long idPo) {
		PoBD poBD = dao.get(PoDAO.class).recuperarPoPorId(idPo);

		if (poBD == null) {
			return null;
		}

		PoDetalhadaVRPLDTO poDetalhada = new PoDetalhadaVRPLDTO();
		poDetalhada.from(poBD);

		return poDetalhada;

	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	@Transaction
	public void alterar(PoDetalhadaVRPLDTO po) {
		checkPermission.check(TabelasDoVRPLEnum.PO, po.getId());

		this.verificaSePOExiste(po);

		PoBD poBD = new PoBD();

		poBD.setId(po.getId());
		poBD.setDtPrevisaoInicioObraAnalise(po.getPrevisaoAnalise());
		poBD.setDtPrevisaoInicioObra(po.getPrevisaoInicioVRPL());
		poBD.setQtMesesDuracaoObra(po.getQtMesesDuracaoObra());
		poBD.setDtBaseVrpl(po.getDataBaseVrpl());
		poBD.setVersao(po.getVersao());

		this.validaPrevisaoDeInicioDaObra(poBD);

		dao.getJdbi().useTransaction(transaction -> {

			transaction.attach(PoDAO.class).alterarDadosPo(poBD);
			PoDetalhadaVRPLDTO poComVersaoAtualizada = this.findPoDetalhada(po.getId());

			if (Boolean.FALSE.equals(po.getReaproveitaCFFSemEventos())) {

				transaction.attach(MacroServicoParcelaDAO.class).excluirParcelasDoMacroServico(poComVersaoAtualizada);
				this.getMacroServicoBC().inserirNovasParcelasDeMacroServicoParaAPO(po, 1, transaction);

			} else {

				this.ajustarParcelas(po, poComVersaoAtualizada, transaction);
			}
		});
	}

	private void ajustarParcelas(PoDetalhadaVRPLDTO poOriginal, PoDetalhadaVRPLDTO poComVersaoAtualizada, Handle transaction) {

		int resultadoComparacaoDuracao = poOriginal.getQtMesesDuracaoObra()
				.compareTo(poOriginal.getQtMesesDuracaoObraValorOriginal());

		boolean duracaoDiminuiu = resultadoComparacaoDuracao < 0;
		boolean duracaoAumentou = resultadoComparacaoDuracao > 0;

		if (duracaoDiminuiu) {

			for (Long mes = poOriginal.getQtMesesDuracaoObra()+1; mes <= poOriginal.getQtMesesDuracaoObraValorOriginal(); mes++ ) {
				transaction.attach(MacroServicoParcelaDAO.class).excluirParcelasDaPoPorMes(poComVersaoAtualizada, mes.intValue());
			}

			eventoFrenteObraBC.excluirMesesExcedentesPo(poOriginal, transaction);

		} else if (duracaoAumentou) {
			this.getMacroServicoBC().inserirNovasParcelasDeMacroServicoParaAPO(
					poOriginal, (poOriginal.getQtMesesDuracaoObraValorOriginal().intValue()+1), transaction);
		}
	}

	public void validarDataBasePo(PoDetalhadaVRPLDTO po) {

		boolean semCargaSinapi = false;
		boolean existeDataBaseDiferente = false;

		LocalDate dataBase = LocalDate.of(
				po.getDataBaseVrpl().getYear(), po.getDataBaseVrpl().getMonth(), 1);

		semCargaSinapi = !dao.get(SiconvDAO.class).existeCargaSinapiParaDataBase(dataBase);

		LoteBD lote = dao.get(LoteLicitacaoDAO.class).recuperarLotePorPo(po.getId());
		List<PoBD> posDoMesmoLote = dao.get(PoDAO.class).recuperarPosPorLicitacaoELote(lote.getIdentificadorDaLicitacao(), lote.getId());

		if(posDoMesmoLote.size() > 1) {
			existeDataBaseDiferente = posDoMesmoLote.stream().anyMatch(
				p -> !po.getDataBaseVrpl().equals(p.getDtBaseVrpl())
						&& !po.getId().equals(p.getId())
     		);
		}

		if (semCargaSinapi && existeDataBaseDiferente) {
			throw new SemCargaSinapiEExisteDataBaseDiferenteException(dataBase, lote.getNumeroDoLote().toString());
		}
		if (semCargaSinapi) {
			throw new NaoExisteCargaSinapiParaDataBaseException(dataBase);
		}
		if (existeDataBaseDiferente) {
			throw new ExisteDataBaseDiferenteNoMesmoLoteException(lote.getNumeroDoLote().toString());
		}
	}

	protected void verificaSePOExiste(@NotNull PoDetalhadaVRPLDTO poDTO) {
		PoBD poBD = this.getPoDAO().recuperarPoPorId(poDTO.getId());

		if ((poBD == null) || (poBD.getVersao() != poDTO.getVersao())) {
			throw new PoNaoEncontradaException(poDTO);
		}
	}

	/**
	 * RN: 510937 -
	 * SICONV-DocumentosOrcamentarios-ManterPO-RN-ValidacaoPODadosGerais
	 * <p>
	 * A data informada não pode ser menor que a data inicialmente prevista (<exibir
	 * data da fase de análise>) para início de obra na fase de análise do projeto
	 * básico
	 * <p>
	 * Relato 1829723
	 */
	private void validaPrevisaoDeInicioDaObra(PoBD poBD) {
		YearMonth previsaoInicioDaObraAnalise = YearMonth.from(poBD.getDtPrevisaoInicioObraAnalise());
		YearMonth previsaoInicioDaObra = YearMonth.from(poBD.getDtPrevisaoInicioObra());

		if (previsaoInicioDaObra.isBefore(previsaoInicioDaObraAnalise)) {
			throw new DataInicioObraInvalidaException(previsaoInicioDaObraAnalise, previsaoInicioDaObra);
		}
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterarReferenciaPo(Long poId, String referencia, Long versao) {
		checkPermission.check(TabelasDoVRPLEnum.PO, poId);

		PoBD poBD = new PoBD();

		poBD.setId(poId);
		poBD.setVersao(versao);
		poBD.setReferencia(referencia);

		getPoDAO().alterarReferenciaPo(poBD);
	}

	protected PoDAO getPoDAO() {
		return this.dao.get(PoDAO.class);
	}

	protected MacroServicoParcelaDAO getMacroServicoParcelaDAO() {
		return this.dao.get(MacroServicoParcelaDAO.class);
	}

}
