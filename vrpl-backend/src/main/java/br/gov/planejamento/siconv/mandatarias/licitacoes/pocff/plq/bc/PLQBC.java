package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.bc;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.dao.PLQDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.FrenteObraComDetalhesBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.database.ServicoComEventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.FrenteObraPLQDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.MacroServicoPLQDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.PLQDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.ServicoPLQDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraAnaliseDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;

public class PLQBC {

	@Inject
	private DAOFactory dao;

	/**
	 * Recupera um PLQ dado o id da PO
	 * 
	 * @param idPO
	 * @return PLQDTO um objeto PLQDTO
	 */
	public PLQDTO recuperarPLQ(Long idPO) {
		Map<Long, MacroServicoPLQDTO> mapaMacroservicos = new TreeMap<>();
		Map<Long, ServicoPLQDTO> mapaServicos = new TreeMap<>();

		PLQDTO plqDTO = new PLQDTO();
		PoBD po = dao.get(PoDAO.class).recuperarPoPorId(idPO);
		if (po != null) {

			// macroServicos
			List<MacroServicoBD> macroServicos = dao.get(PLQDAO.class).recuperarMacroServicoPorIdPO(idPO);
			List<MacroServicoPLQDTO> listaMacroServicoDTO = converterMacroServicoBD(macroServicos, mapaMacroservicos);
			plqDTO.setMacroservicos(listaMacroServicoDTO);

			// servicos
			if (po.getInAcompanhamentoEventos()) {
				plqDTO.setPorEvento(true);
				List<ServicoComEventoBD> servicosComEvento = dao.get(PLQDAO.class)
						.recuperarServicosComEventoPorPo(idPO);
				this.converterServicoComEventoBD(servicosComEvento, mapaServicos, mapaMacroservicos);
			} else {
				plqDTO.setPorEvento(false);
				List<ServicoBD> servicos = dao.get(PLQDAO.class).recuperarServicosPorPo(idPO);
				this.converterServicoBD(servicos, mapaServicos, mapaMacroservicos);
			}
			// frentes de Obra
			List<FrenteObraComDetalhesBD> frentesObra = dao.get(PLQDAO.class)
					.recuperarFrentesDeObraComDetalhesPorPo(idPO);
			List<FrenteObraPLQDTO> converterFrenteObraBD = converterFrenteObraBD(frentesObra, mapaServicos);

		} else {
			// incluir mensagem que a po nao foi encontrada
		}
		return plqDTO;
	}

	private List<FrenteObraPLQDTO> converterFrenteObraBD(List<FrenteObraComDetalhesBD> frentesObra,
			Map<Long, ServicoPLQDTO> mapaServicos) {
		return frentesObra.stream().map(f -> {
			FrenteObraPLQDTO dto = new FrenteObraPLQDTO();
			dto.setDescricao(f.getNomeFrenteObra());
			dto.setNumero(f.getNumeroFrenteObra());
			dto.setId(f.getIdFrenteObra());
			dto.setServicoFK(f.getIdServico());
			dto.setQuantidade(f.getQuantidadeItens());

			ServicoPLQDTO servico = mapaServicos.get(f.getIdServico());
			if (servico != null) {
				dto.setValorLicitado(dto.getQuantidade().multiply(servico.getPrecoUnitarioServico()).setScale(2, RoundingMode.HALF_UP));
				servico.getFrentesObra().add(dto);
			}

			return dto;
		}).collect(Collectors.toList());

	}

	protected List<MacroServicoPLQDTO> converterMacroServicoBD(List<MacroServicoBD> macroservicos,
			Map<Long, MacroServicoPLQDTO> mapaMacroservicos) {

		return macroservicos.stream().map(m -> {
			MacroServicoPLQDTO dto = new MacroServicoPLQDTO();
			dto.setId(m.getId());
			dto.setDescricao(m.getTxDescricao());
			dto.setNumero(m.getNrMacroServico());
			dto.setServicos(new ArrayList<>());
			mapaMacroservicos.put(m.getId(), dto);

			return dto;
		}).collect(Collectors.toList());
	}

	protected List<ServicoPLQDTO> converterServicoBD(List<ServicoBD> servicos, Map<Long, ServicoPLQDTO> mapaServicos,
			Map<Long, MacroServicoPLQDTO> mapaMacroservicos) {

		return servicos.stream().map(s -> {
			
			ServicoDTO sDTO = s.converterParaDTO();
			
			List<ServicoFrenteObraBD> listaServicoFrenteObraBD = dao.get(ServicoDAO.class)
					.recuperaListaServicoFrenteObraPorIdServico(s.getId());
			List<ServicoFrenteObraDTO> listaSfoDto = listaServicoFrenteObraBD.stream().map(ServicoFrenteObraDTO::from).collect(Collectors.toList());
			sDTO.setFrentesObra(listaSfoDto);
			
			ServicoPLQDTO dto = new ServicoPLQDTO();
			dto.setId(s.getId());
			dto.setNumero(s.getNrServico());
			dto.setDescricao(s.getTxDescricao());
			dto.setPrecoTotalServico(s.getVlPrecoTotal());
			dto.setQuantidade(sDTO.getQuantidade());
			dto.setUnidade(s.getSgUnidade());
			dto.setFrentesObra(new ArrayList<>());
			dto.setPrecoUnitarioServico(s.getVlPrecoUnitarioLicitado());
			dto.setMacroServicoFk(s.getMacroServicoFk());
			dto.setPrecoTotalServico(dto.getQuantidade().multiply(dto.getPrecoUnitarioServico()).setScale(2, RoundingMode.HALF_UP));

			mapaServicos.put(s.getId(), dto);

			MacroServicoPLQDTO macroServicoPLQDTO = mapaMacroservicos.get(s.getMacroServicoFk());
			if (macroServicoPLQDTO != null) {
				macroServicoPLQDTO.getServicos().add(dto);
				macroServicoPLQDTO.setPrecoTotalLicitado(
						macroServicoPLQDTO.getPrecoTotalLicitado().add(dto.getPrecoTotalServico()));
			}

			return dto;
		}).collect(Collectors.toList());
	}

	protected List<ServicoPLQDTO> converterServicoComEventoBD(List<ServicoComEventoBD> servicos,
			Map<Long, ServicoPLQDTO> mapaServicos, Map<Long, MacroServicoPLQDTO> mapaMacroservicos) {

		return servicos.stream().map(s -> {
			ServicoPLQDTO dto = new ServicoPLQDTO();
			dto.setId(s.getId());
			dto.setNumero(s.getNrServico());
			dto.setDescricao(s.getTxDescricao());
			dto.setPrecoTotalServico(s.getVlPrecoTotal());
			dto.setQuantidade(s.getQtTotalItensAnalise());
			dto.setUnidade(s.getSgUnidade());
			dto.setDescricaoEvento(s.getEventoNome());
			dto.setNumeroEvento(s.getNumeroEvento());
			dto.setFrentesObra(new ArrayList<>());
			dto.setPrecoUnitarioServico(s.getVlPrecoUnitarioLicitado());
			dto.setMacroServicoFk(s.getMacroServicoFk());
			dto.setPrecoTotalServico(dto.getQuantidade().multiply(dto.getPrecoUnitarioServico()).setScale(2, RoundingMode.HALF_UP));

			mapaServicos.put(s.getId(), dto);

			MacroServicoPLQDTO macroServicoPLQDTO = mapaMacroservicos.get(s.getMacroServicoFk());
			if (macroServicoPLQDTO != null) {
				macroServicoPLQDTO.getServicos().add(dto);
				macroServicoPLQDTO.setPrecoTotalLicitado(
						macroServicoPLQDTO.getPrecoTotalLicitado().add(dto.getPrecoTotalServico()));
			}

			return dto;
		}).collect(Collectors.toList());
	}

	public void setDao(DAOFactory dao) {
		this.dao = dao;
	}

}
