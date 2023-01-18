package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SinapiIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business.MacroServicoParcelaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.ConsultaSinapiDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business.ServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;

public class MacroServicoBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private ServicoBC servicoBC;

	@Inject
	private MacroServicoParcelaBC macroServicoParcelaBC;


	public Map<PoBD,PoMacroServicoServicosDTO> recuperarMacroServicoServicosPorPo(List<Long> listaPoId) {
		Map<PoBD,PoMacroServicoServicosDTO> retorno = new HashMap<>();

		List<PoReducerDTO> listaPo = dao.get(PoDAO.class).findByListId(listaPoId);

		if (listaPo.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<ConsultaSinapiDTO, SinapiIntegracao> mapaSinapiIntengracao = servicoBC.obterMapaSinapi(listaPo);

		dao.getJdbi().useTransaction(transaction ->
			listaPo.forEach(po ->
				retorno.put(po.convert(), servicoBC.calculaTotaisDosServicos(transaction, po, mapaSinapiIntengracao))
			)
		);

		return retorno;
	}


	@Log
	public List<MacroServicoDTO> recuperarMacroServicoPorPo(Handle transaction, Long poId) {
		List<MacroServicoBD> lista = transaction.attach(MacroServicoDAO.class).recuperarMacroServicosPorIdPo(poId);
		List<MacroServicoDTO> retorno = new ArrayList<>();

		for (MacroServicoBD macroServicoBD : lista) {
			retorno.add(macroServicoBD.converterParaDTO());
		}

		return retorno;
	}


	public void inserirNovasParcelasDeMacroServicoParaAPO(PoDetalhadaVRPLDTO po, int nrPrimeiraParcelaNova, Handle transaction) {

		List<MacroServicoBD> macroServicos = dao.get(MacroServicoDAO.class).recuperarMacroServicosPorIdPo(po.getId());

		for (MacroServicoBD macroServico : macroServicos) {
			for (int i = nrPrimeiraParcelaNova; i <= po.getQtMesesDuracaoObra(); i++) {
				inserirParcelaMacroServico(macroServico, i, transaction);
			}
		}
	}

	private void inserirParcelaMacroServico(MacroServicoBD macroServico, int nrParcela, Handle transaction) {
		MacroServicoParcelaBD novaParcela = new MacroServicoParcelaBD();
		novaParcela.setNrParcela(nrParcela);
		novaParcela.setMacroServicoFk(macroServico.getId());
		novaParcela.setPcParcela(BigDecimal.ZERO);

		macroServicoParcelaBC.inserirParcela(novaParcela, transaction);
	}

	public SubmetaBD atualizarValoresSubmetaDeAcordoComSomaServicosPO(SubmetaBD submeta, BigDecimal somaValoresServicosPO) {
		return this.servicoBC.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);
	}
	
	// Metodos para testes

	public void setDao(DAOFactory dao) {
		this.dao = dao;
	}

	public void setServicoBC(ServicoBC servicoBC2) {
		this.servicoBC = servicoBC2;
	}

}
