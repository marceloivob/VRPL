package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business.exception.MacroServicoParcelaNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.CFFSemEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.MacroServicoCFFDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.MacroServicoParcelaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.PrecoTotalMacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class MacroServicoParcelaBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private UserCanEditVerifier checkPermission;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<MacroServicoParcelaBD> constraintBeanValidation;

	@RefreshRowPolice
	public void inserirParcela(MacroServicoParcelaBD macroServicoParcela, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.MACRO_SERVICO, macroServicoParcela.getMacroServicoFk());

		constraintBeanValidation.validate(macroServicoParcela);

		transaction.attach(MacroServicoParcelaDAO.class).inserirMacroServicoParcela(macroServicoParcela);

	}

	@RefreshRowPolice
	public void inserir(MacroServicoParcelaDTO macroServicoParcela, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.MACRO_SERVICO, macroServicoParcela.getMacroServicoFk());
		// inserir regras de validacao
		// converterDTO
		MacroServicoParcelaBD macroServicoParcelaConvertido = macroServicoParcela.converterParaBD();

		constraintBeanValidation.validate(macroServicoParcelaConvertido);

		transaction.attach(MacroServicoParcelaDAO.class).inserirMacroServicoParcela(macroServicoParcelaConvertido);

	}

	public void alterar(MacroServicoParcelaDTO macroServicoParcela, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.MACRO_SERVICO_PARCELA, macroServicoParcela.getId());

		// valida se o MacroServicoParcela existe
		recuperarMacroServicoParcelaPorId(macroServicoParcela.getId());

		// converterDTO
		MacroServicoParcelaBD macroServicoParcelaConvertido = macroServicoParcela.converterParaBD();
		constraintBeanValidation.validate(macroServicoParcelaConvertido);
		transaction.attach(MacroServicoParcelaDAO.class).alterarMacroServicoParcela(macroServicoParcelaConvertido);

	}

	public void excluir(Long id) {
		throw new IllegalArgumentException("Está faltando o número da versão.");

//		checkPermission.check(TabelasDoVRPLEnum.MACRO_SERVICO_PARCELA, macroServicoParcela.getId());
//		// levanta excecao caso MacroServicoParcela nao exista
//
//		MacroServicoParcelaDTO macroServicoParcela = recuperarMacroServicoParcelaPorId(id);
//
//		if (macroServicoParcela != null) {
//			dao.get(MacroServicoParcelaDAO.class).excluirMacroServicoParcela(id);
//		}
	}

	public MacroServicoParcelaDTO recuperarMacroServicoParcelaPorId(Long id) {
		MacroServicoParcelaBD macroServicoParcelaBD = dao.get(MacroServicoParcelaDAO.class)
				.recuperarMacroServicoParcelaPorId(id);
		if (macroServicoParcelaBD == null) {
			throw new MacroServicoParcelaNaoEncontradoException();
		}

		MacroServicoParcelaDTO macroServicoParcelaDTO = macroServicoParcelaBD.converterParaDTO();
		return macroServicoParcelaDTO;
	}

	public CFFSemEventosDTO recuperarCFFPorIdPO(Long idPO) {

		// consultar po
		PoBD po = dao.get(PoDAO.class).recuperarPoPorId(idPO);

		// consultar macroservicos de uma po
		if (po == null) {
			return new CFFSemEventosDTO();
		}

		CFFSemEventosDTO cff = new CFFSemEventosDTO();
		cff.setIdPO(po.getId());
		cff.setDtPrevisaoInicioObra(po.getDtPrevisaoInicioObra());
		cff.setQtMesesDuracaoObra(po.getQtMesesDuracaoObra());

		List<MacroServicoBD> listaMacroServicos = dao.get(MacroServicoDAO.class)
				.recuperarMacroServicosPorIdPo(po.getId());
		// transformar BD em DTO
		List<MacroServicoCFFDTO> listaMacroservicosConvertida = converterMacroServicoBD(listaMacroServicos, po.getId());

		if (listaMacroservicosConvertida == null || listaMacroservicosConvertida.isEmpty()) {
			return cff;
		}

		cff.setListaMacroServicos(listaMacroservicosConvertida);

		// consultar parcelas de um macroservico
		List<Long> idsMacroservicos = listaMacroservicosConvertida.stream().map(macroServico -> macroServico.getId())
				.collect(Collectors.toList());

//		List<PrecoTotalMacroServicoBD> precosTotais = dao.get(MacroServicoDAO.class)
//				.recuperarPrecoTotalMacroServicos(idsMacroservicos);
		List<PrecoTotalMacroServicoBD> precosTotais = dao.get(MacroServicoDAO.class)
				.recuperarPrecoTotalMacroServicosPorFrenteObra(idsMacroservicos);
		Map<Long, BigDecimal> mapaPrecos = new HashMap<>();
		if (precosTotais != null && !precosTotais.isEmpty()) {
			for (PrecoTotalMacroServicoBD preco : precosTotais) {
				mapaPrecos.put(preco.getMacroservicoFK(), preco.getPrecoTotal());
			}
		}

		List<MacroServicoParcelaBD> listaParcelas = dao.get(MacroServicoParcelaDAO.class)
				.recuperarParcelasDoMacroServicoPorIdsMacroServico(idsMacroservicos);
		// mapeamento chave do macroservico com a sua respectiva lista de parcelas
		Map<Long, List<MacroServicoParcelaBD>> mapaChaveLista = new HashMap<>();
		for (MacroServicoParcelaBD parcela : listaParcelas) {
			if (mapaChaveLista.containsKey(parcela.getMacroServicoFk())) {
				mapaChaveLista.get(parcela.getMacroServicoFk()).add(parcela);
			} else {
				List<MacroServicoParcelaBD> lista = new ArrayList<>();
				lista.add(parcela);
				mapaChaveLista.put(parcela.getMacroServicoFk(), lista);
			}
		}
		if (!mapaChaveLista.isEmpty()) {
			for (MacroServicoCFFDTO macroServico : listaMacroservicosConvertida) {
				if (mapaChaveLista.containsKey(macroServico.getId())) {
					macroServico.setParcelas(converterMacroServicoBD(mapaChaveLista.get(macroServico.getId())));
				}
				if (mapaPrecos.containsKey(macroServico.getId())) {
					macroServico.setPrecoMacroservico(mapaPrecos.get(macroServico.getId()));
				}
			}
		}

		return cff;
	}

	private List<MacroServicoParcelaDTO> converterMacroServicoBD(List<MacroServicoParcelaBD> listBD) {
		List<MacroServicoParcelaDTO> listDTO = new ArrayList<>();
		if (listBD == null) {
			return listDTO;
		}

		for (MacroServicoParcelaBD parcelaBD : listBD) {
			MacroServicoParcelaDTO parcelaDTO = parcelaBD.converterParaDTO();
			listDTO.add(parcelaDTO);
		}
		return listDTO;
	}

	private List<MacroServicoCFFDTO> converterMacroServicoBD(List<MacroServicoBD> listaMacroServicos, Long idPo) {
		List<MacroServicoCFFDTO> listaSaida = new ArrayList<>();

		for (MacroServicoBD macroServicoBD : listaMacroServicos) {
			MacroServicoCFFDTO msDTO = new MacroServicoCFFDTO();
			msDTO.setIdPO(idPo);
			msDTO.setId(macroServicoBD.getId());
			msDTO.setNrMacroServico(macroServicoBD.getNrMacroServico());
			msDTO.setTxDescricao(macroServicoBD.getTxDescricao());
			listaSaida.add(msDTO);
		}

		return listaSaida;
	}

	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterarParcelas(MacroServicoCFFDTO macroServicoCffDTO) {

		Objects.requireNonNull(macroServicoCffDTO);
		Objects.requireNonNull(macroServicoCffDTO.getParcelas());

		dao.getJdbi().useTransaction(transaction -> {
			for (MacroServicoParcelaDTO parcela : macroServicoCffDTO.getParcelas()) {
				if (parcela.getPcParcela() == null || parcela.getPcParcela().compareTo(BigDecimal.ZERO) < 0) {
					throw new ParcelaNegativaException();
				}
			}
			
			transaction.attach(MacroServicoParcelaDAO.class).excluirParcelasPorMacroServicoFK(macroServicoCffDTO.getId());
			
			for (MacroServicoParcelaDTO parcela : macroServicoCffDTO.getParcelas()) {
				this.inserir(parcela, transaction);
			}
			
		});

	}

}
