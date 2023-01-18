package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.business.exception.EventoFrenteObraNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraDetalheBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.TotalizadorMesBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.CFFComEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.EventoCFFcomEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.EventoFrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.FrenteDeObraCFFcomEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class EventoFrenteObraBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private UserCanEditVerifier checkPermission;
	
	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<EventoFrenteObraBD> constraintBeanValidation;


	@RefreshRowPolice
	public void inserir(EventoFrenteObraDTO eventoFrenteObra, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.EVENTO, eventoFrenteObra.getEventoFk());

		// converterDTO
		EventoFrenteObraBD eventoFrenteObraConvertido = this.converterParaBD(eventoFrenteObra);
		
		constraintBeanValidation.validate(eventoFrenteObraConvertido);
		
		transaction.attach(EventoFrenteObraDAO.class).inserirEventoFrenteObra(eventoFrenteObraConvertido);

	}

	public void alterar(EventoFrenteObraDTO eventoFrenteObra, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.EVENTOS_FRENTES_DE_OBRA, eventoFrenteObra.getEventoFk());

		// valida se o EventoFrenteObra nao exista
		recuperarEventoFrenteObraPorId(eventoFrenteObra.getEventoFk(), eventoFrenteObra.getFrenteObraFk());

		// converterDTO
		EventoFrenteObraBD eventoFrenteObraConvertido = this.converterParaBD(eventoFrenteObra);
		
		constraintBeanValidation.validate(eventoFrenteObraConvertido);

		transaction.attach(EventoFrenteObraDAO.class).alterarMesConclusaoEventoFrenteObra(eventoFrenteObraConvertido);

	}

	public EventoFrenteObraDTO recuperarEventoFrenteObraPorId(Long eventoFk, Long frenteObraFk) {
		EventoFrenteObraBD eventoFrenteObraBD = dao.get(EventoFrenteObraDAO.class)
				.recuperarEventoFrenteObraPorId(eventoFk, frenteObraFk);
		if (eventoFrenteObraBD == null) {
			throw new EventoFrenteObraNaoEncontradoException();
		}

		EventoFrenteObraDTO eventoFrenteObraDTO = eventoFrenteObraBD.converterParaDTO();
		return eventoFrenteObraDTO;
	}

	public CFFComEventosDTO recuperarCFFPorIdPO(Long idPO) {
		CFFComEventosDTO cff = new CFFComEventosDTO();
		// recuperar Dados PO
		PoBD poBD = dao.get(PoDAO.class).recuperarPoVRPL(idPO);
		if (poBD != null) {
			cff.setIdPO(poBD.getId());
			cff.setQtdMeses(poBD.getQtMesesDuracaoObra());
			cff.setTotalPO(dao.get(PoDAO.class).recuperarValorTotalVRPL(poBD.getId()));

			List<TotalizadorMesBD> totaisMes = dao.get(EventoFrenteObraDAO.class).recuperarTotalizadorPorMes(idPO);
			Map<Integer, BigDecimal> mapaTotaisPorMes = converterTotaisParaMap(totaisMes);
			cff.setMapaMesValorParcela(mapaTotaisPorMes);
			// recuperar EventosFrentesObra (dados dos eventos e das frentes de Obra)
			List<EventoFrenteObraDetalheBD> listaEventosFrenteObra = dao.get(EventoFrenteObraDAO.class)
					.recuperarEventosFrenteObraComDetalhe(idPO);
			List<EventoCFFcomEventosDTO> listaEventosCFF = converterListaEventoFrenteObraDetalheBD(
					listaEventosFrenteObra);
			cff.setListaDeEventoCFF(listaEventosCFF);
		}

		return cff;
	}

	private List<EventoCFFcomEventosDTO> converterListaEventoFrenteObraDetalheBD(
			List<EventoFrenteObraDetalheBD> listaEventosFrenteObra) {
		List<EventoCFFcomEventosDTO> listaDTOs = new ArrayList<>();
		if (listaEventosFrenteObra != null) {
			int nrEvento = -1;
			EventoCFFcomEventosDTO evento = new EventoCFFcomEventosDTO();
			FrenteDeObraCFFcomEventosDTO frenteObra;
			for (EventoFrenteObraDetalheBD objBD : listaEventosFrenteObra) {
				if (objBD.getNumeroEvento() != nrEvento) {
					nrEvento = objBD.getNumeroEvento();
					evento = new EventoCFFcomEventosDTO();
					evento.setIdEvento(objBD.getIdEvento());
					evento.setNumeroEvento(objBD.getNumeroEvento());
					evento.setTituloEvento(objBD.getNomeEvento());
					evento.setListaFrenteObras(new ArrayList<FrenteDeObraCFFcomEventosDTO>());
					evento.setVersao(objBD.getVersao());
					listaDTOs.add(evento);
				}
				frenteObra = new FrenteDeObraCFFcomEventosDTO();
				frenteObra.setIdFrenteObra(objBD.getIdFrenteObra());
				frenteObra.setNomeFrenteObra(objBD.getNomefrenteObra());
				frenteObra.setNumeroFrenteObra(objBD.getNumerofrenteObra());
				frenteObra.setMesConclusao(objBD.getNumeroMesConclusao());
				//frenteObra.setVersao(objBD.getVersaoFrenteObra());
				frenteObra.setVersao(objBD.getVersao());
				evento.getListaFrenteObras().add(frenteObra);
			}
		}
		return listaDTOs;
	}

	private Map<Integer, BigDecimal> converterTotaisParaMap(List<TotalizadorMesBD> totaisMes) {
		Map<Integer, BigDecimal> mapaMensal = new HashMap<>();
		if (totaisMes != null) {
			for (TotalizadorMesBD totalMensal : totaisMes) {
				mapaMensal.put(totalMensal.getMes(), totalMensal.getTotal());
			}
		}
		return mapaMensal;
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterarEventosFrenteObra(EventoCFFcomEventosDTO eventoCFF) {

		List<EventoFrenteObraDTO> listEventoFrenteObra = converterEventoCFFcomEventosDTOEmListEventoFrenteObraDTO(
				eventoCFF);
		dao.getJdbi().useTransaction(transaction -> {
			for (EventoFrenteObraDTO eventoDTO : listEventoFrenteObra) {
				if (eventoDTO.getNrMesConclusao() > 0) {
					EventoFrenteObraDTO eventoFrenteObraConsultada = recuperarEventoFrenteObraPorId(
							eventoDTO.getEventoFk(), eventoDTO.getFrenteObraFk());
					if (eventoFrenteObraConsultada == null) {
						this.inserir(eventoDTO, transaction);
					} else {
						this.alterar(eventoDTO, transaction);
					}
				}

			}
		});
	}

	private List<EventoFrenteObraDTO> converterEventoCFFcomEventosDTOEmListEventoFrenteObraDTO(
			EventoCFFcomEventosDTO eventoCFF) {
		List<EventoFrenteObraDTO> lista = new ArrayList<>();
		if (eventoCFF != null) {
			for (FrenteDeObraCFFcomEventosDTO foCFFDTO : eventoCFF.getListaFrenteObras()) {
				EventoFrenteObraDTO evfDTO = new EventoFrenteObraDTO();
				evfDTO.setEventoFk(eventoCFF.getIdEvento());
				evfDTO.setFrenteObraFk(foCFFDTO.getIdFrenteObra());
				evfDTO.setNrMesConclusao(foCFFDTO.getMesConclusao());
				evfDTO.setVersao(foCFFDTO.getVersao());
				lista.add(evfDTO);
			}
		}
		return lista;
	}

	public EventoCFFcomEventosDTO recuperarFrenteObrasEvento(Long idEvento) {
		List<EventoFrenteObraDetalheBD> detalhes = dao.get(EventoFrenteObraDAO.class)
				.recuperarTodasAsFrentesObraAssociadasAUmEvento(idEvento);
		List<EventoCFFcomEventosDTO> listaEventos = converterListaEventoFrenteObraDetalheBD(detalhes);
		// so deveria ter um elemento na lista pois so foi consultada as frentes de obra
		// relacionadas a um unico evento.
		return listaEventos.get(0);
	}

	private EventoFrenteObraBD converterParaBD(EventoFrenteObraDTO eventoFrenteObra) {
		EventoFrenteObraBD eventoBD = new EventoFrenteObraBD();
		eventoBD.setEventoFk(eventoFrenteObra.getEventoFk());
		eventoBD.setFrenteObraFk(eventoFrenteObra.getFrenteObraFk());
		eventoBD.setNrMesConclusao(eventoFrenteObra.getNrMesConclusao());
		eventoBD.setVersao(eventoFrenteObra.getVersao());
		return eventoBD;
	}
	
	public void excluirMesesExcedentesPo(PoDetalhadaVRPLDTO po, Handle transaction) {
		if (po.getAcompanhamentoEvento() && (po.getQtMesesDuracaoObra()
				.compareTo(po.getQtMesesDuracaoObraValorOriginal()) < 0)) {
			
			for (Long mes = po.getQtMesesDuracaoObra()+1; mes <= po.getQtMesesDuracaoObraValorOriginal(); mes++ ) {
				transaction.attach(EventoFrenteObraDAO.class).apagarNumeroMesConclusaoDoEventoFrenteObraDaPO(mes, po.getId());
			}
		}
	}

}
