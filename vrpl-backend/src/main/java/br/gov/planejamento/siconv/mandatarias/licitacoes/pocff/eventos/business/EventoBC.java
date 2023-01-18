package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception.EventoNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.exception.NumeroEventoRepetidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.dto.EventoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business.ServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class EventoBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<EventoBD> constraintBeanValidator;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ServicoBC servicoBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private UserCanEditVerifier checkPermission;

	public List<EventoDTO> consultarListaEventos(Long idPO) {

		List<EventoBD> eventosBD = dao.get(EventoDAO.class).recuperarListaEventosVRPLIdPo(idPO);

		List<EventoDTO> dtos = eventosBD.stream().map(eventoBD -> {
			EventoDTO dto = new EventoDTO();
			dto.setId(eventoBD.getId());
			dto.setIdPO(eventoBD.getIdPo());
			dto.setNumeroEvento(eventoBD.getNumeroEvento());
			dto.setTituloEvento(eventoBD.getNomeEvento());
			dto.setVersao(eventoBD.getVersao());

			return dto;
		}).collect(Collectors.toList());

		return dtos;

	}

	private EventoDTO converterEventoBDemEventoDTO(EventoBD eventoBD) {
		EventoDTO eventoDTO = null;
		if (eventoBD != null) {
			eventoDTO = new EventoDTO();
			eventoDTO.setId(eventoBD.getId());
			eventoDTO.setNumeroEvento(eventoBD.getNumeroEvento());
			eventoDTO.setTituloEvento(eventoBD.getNomeEvento());
			eventoDTO.setIdPO(eventoBD.getIdPo());
			eventoDTO.setVersao(eventoBD.getVersao());
		}
		return eventoDTO;
	}

	public EventoBD converterEventoDTOemEventoBD(EventoDTO eventoDTO) {
		EventoBD eventoBD = null;
		if (eventoDTO != null) {
			eventoBD = new EventoBD();
			eventoBD.setId(eventoDTO.getId());
			eventoBD.setIdPo(eventoDTO.getIdPO());
			eventoBD.setNomeEvento(eventoDTO.getTituloEvento());
			eventoBD.setVersao(eventoDTO.getVersao());

			if (eventoDTO.getNumeroEvento() != null) {
				eventoBD.setNumeroEvento(eventoDTO.getNumeroEvento());
			}

		}
		return eventoBD;
	}

	@RefreshRowPolice
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public EventoBD inserir(EventoDTO evento) {
		checkPermission.check(TabelasDoVRPLEnum.PO, evento.getIdPO());

		EventoBD eventoConvertido = converterEventoDTOemEventoBD(evento);
		constraintBeanValidator.validate(eventoConvertido);

		List<EventoBD> eventosBD = dao.get(EventoDAO.class).recuperarListaEventosVRPLIdPo(eventoConvertido.getIdPo());
		if (eventosBD != null) {
			for (EventoBD eventoBD : eventosBD) {
				if (eventoBD.getNumeroEvento().equals(eventoConvertido.getNumeroEvento())) {
					throw new NumeroEventoRepetidoException();
				}
			}
		}

		EventoBD eventoInserido = dao.get(EventoDAO.class).inserirEvento(eventoConvertido);

		return eventoInserido;

	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterar(EventoDTO evento) {
		checkPermission.check(TabelasDoVRPLEnum.EVENTO, evento.getId());

		this.verificarSeEventoExiste(evento);
		
		EventoBD eventoConvertido = converterEventoDTOemEventoBD(evento);
		constraintBeanValidator.validate(eventoConvertido);

		List<EventoBD> eventosBD = dao.get(EventoDAO.class).recuperarListaEventosVRPLIdPo(eventoConvertido.getIdPo());
		if (eventosBD != null) {
			for (EventoBD eventoBD : eventosBD) {
				if (eventoBD.getNumeroEvento().equals(eventoConvertido.getNumeroEvento())
						&& !eventoBD.getId().equals(eventoConvertido.getId())) {
					throw new NumeroEventoRepetidoException();
				}
			}
		}

		dao.get(EventoDAO.class).alterarEvento(eventoConvertido);

	}
	
	protected void verificarSeEventoExiste(EventoDTO evento) {
		EventoDTO eventoRecuperado = recuperarEventoPorId(evento.getId());
		
		if (eventoRecuperado == null || !evento.getVersao().equals(eventoRecuperado.getVersao())){
			throw new EventoNaoEncontradoException();
		}
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void excluir(Long idEvento, Long versao) {
		checkPermission.check(TabelasDoVRPLEnum.EVENTO, idEvento);

		// levanta excecao caso evento nao exista
		recuperarEventoPorId(idEvento);

		dao.getJdbi().useTransaction(transaction -> {
			List<ServicoDTO> servicosDoEvento = servicoBC.recuperarServicosPorEvento(idEvento);

			for (ServicoDTO servicoDTO : servicosDoEvento) {
				servicoBC.removeAssociacaoComEvento(servicoDTO, transaction);
			}

			transaction.attach(EventoFrenteObraDAO.class).excluirEventoFrenteObraPorEvento(idEvento, versao);
			transaction.attach(EventoDAO.class).excluirEvento(idEvento, versao);
		});
	}

	public EventoDTO recuperarEventoPorId(Long idEvento) {
		EventoBD eventoBD = dao.get(EventoDAO.class).recuperarEventoPorId(idEvento);

		if (eventoBD == null) {
			throw new EventoNaoEncontradoException();
		}

		EventoDTO eventoDTO = converterEventoBDemEventoDTO(eventoBD);
		return eventoDTO;
	}

	/**
	 * Implementa a RN: 511405 -
	 * SICONV-DocumentosOrcamentarios-ManterEvento-RN-FormularioInclusaoAlteracao
	 * <p>
	 * Valor associado à ordem de exibição de um evento.
	 * </p>
	 * <p>
	 * O número do evento é sequencial, editável e virá preenchido com o próximo
	 * valor a partir do último evento já cadastrado (último número + 1). Caso seja
	 * o primeiro evento a ser incluído, receberá o valor 1 como sugestão do
	 * sistema.
	 * </p>
	 * <p>
	 * Não é permitida a repetição de números. Possibilidade do usuário informar um
	 * valor que denote uma ordem mais conveniente.
	 * </p>
	 *
	 * @param idPo Identificador da Planilha Orçamentária
	 */
	public Long recuperarValorSequencialEvento(Long idPo) {
		Long sequencial = dao.get(EventoDAO.class).recuperarSequencialEventoPorPo(idPo);
		if (sequencial == null) {
			sequencial = Long.valueOf(1);
		} else {
			sequencial += 1;
		}
		return sequencial;
	}

}
