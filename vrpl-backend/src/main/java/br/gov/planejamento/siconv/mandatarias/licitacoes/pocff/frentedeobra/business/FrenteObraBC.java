package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.exception.FrenteObraNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.exception.NumeroFrenteDeObraRepetidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.dto.FrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class FrenteObraBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<FrenteObraBD> constraintBeanValidation;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private UserCanEditVerifier checkPermission;

	@RefreshRowPolice
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public FrenteObraBD inserir(FrenteObraDTO frenteObra) {
		checkPermission.check(TabelasDoVRPLEnum.PO, frenteObra.getIdPO());

		FrenteObraBD frenteObraConvertido = this.converterFrenteDTOemFrenteBD(frenteObra);
		constraintBeanValidation.validate(frenteObraConvertido);

		List<FrenteObraBD> frentesBD = dao.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraIdPo(frenteObraConvertido.getPoFk());
		if (frentesBD != null) {
			for (FrenteObraBD frenteBD : frentesBD) {
				if (frenteBD.getNrFrenteObra().equals(frenteObraConvertido.getNrFrenteObra())) {
					throw new NumeroFrenteDeObraRepetidoException();
				}
			}
		}

		return dao.get(FrenteObraDAO.class).inserirFrenteObra(frenteObraConvertido);
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public FrenteObraBD alterar(FrenteObraDTO frenteObra) {
		checkPermission.check(TabelasDoVRPLEnum.FRENTES_DE_OBRA, frenteObra.getId());

		FrenteObraBD frenteObraConvertido = frenteObra.converterParaBD();

		verificarSeFrenteObraExiste(frenteObraConvertido);

		constraintBeanValidation.validate(frenteObraConvertido);

		List<FrenteObraBD> frentesBD = dao.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraIdPo(frenteObraConvertido.getPoFk());
		if (frentesBD != null) {
			for (FrenteObraBD frenteBD : frentesBD) {
				if (frenteBD.getNrFrenteObra().equals(frenteObraConvertido.getNrFrenteObra())
						&& !frenteBD.getId().equals(frenteObraConvertido.getId())) {
					throw new NumeroFrenteDeObraRepetidoException();
				}
			}
		}

		return dao.get(FrenteObraDAO.class).alterarFrenteObra(frenteObraConvertido);

	}
	
	protected void verificarSeFrenteObraExiste(FrenteObraBD frenteObra) {
		FrenteObraDTO frenteObraRecuperada = recuperarFrenteObraPorId(frenteObra);
		
		if (frenteObraRecuperada == null || !frenteObraRecuperada.getVersao().equals(frenteObraRecuperada.getVersao())){
			throw new FrenteObraNaoEncontradoException(frenteObra);
		}
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void excluir(FrenteObraBD frenteObra) {
		
		checkPermission.check(TabelasDoVRPLEnum.FRENTES_DE_OBRA, frenteObra.getId());

		verificarSeFrenteObraExiste(frenteObra);

		dao.getJdbi().useTransaction(transaction -> {
			transaction.attach(EventoFrenteObraDAO.class).excluirEventoFrenteObraPorFrente(frenteObra.getId(), frenteObra.getVersao());
			transaction.attach(ServicoFrenteObraDAO.class).excluirServicoFrenteObraPorIdFrente(frenteObra.getId(), frenteObra.getVersao());
			transaction.attach(FrenteObraDAO.class).excluirFrenteObra(frenteObra);
		});
	}

	public FrenteObraDTO recuperarFrenteObraPorId(FrenteObraBD frenteObra) {
		FrenteObraBD frenteObraBD = dao.get(FrenteObraDAO.class).recuperarFrenteObraPorId(frenteObra);
		if (frenteObraBD == null) {
			throw new FrenteObraNaoEncontradoException(frenteObra);
		}

		return frenteObraBD.converterParaDTO();
	}

	public List<FrenteObraDTO> consultarListaFrentesDeObra(Long idPO) {
		List<FrenteObraBD> frentesObraBD = dao.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraPorPoIdProposta(idPO);

		List<FrenteObraDTO> frentesObraDTO = new ArrayList<>();

		for (FrenteObraBD bd : frentesObraBD) {
			frentesObraDTO.add(bd.converterParaDTO());
		}

		return frentesObraDTO;
	}

	/**
	 *
	 * @param idPo
	 * @return
	 */
	public long recuperarValorSequencialFrenteDeObra(Long idPo) {
		Long sequencial = dao.get(FrenteObraDAO.class).recuperarSequencialFrenteObraPorPo(idPo);
		if (sequencial == null) {
			sequencial = Long.valueOf(1);
		} else {
			sequencial += 1;
		}
		return sequencial;
	}

	public FrenteObraBD converterFrenteDTOemFrenteBD(FrenteObraDTO frenteObraDTO) {
		FrenteObraBD frenteObraBD = null;
		if (frenteObraDTO != null) {
			frenteObraBD = new FrenteObraBD();
			frenteObraBD.setPoFk(frenteObraDTO.getIdPO());
			frenteObraBD.setNmFrenteObra(frenteObraDTO.getNomeFrente());
			frenteObraBD.setVersao(frenteObraDTO.getVersao());
			frenteObraBD.setNrFrenteObra(frenteObraDTO.getNumeroFrente().intValue());
		}
		return frenteObraBD;
	}

}
