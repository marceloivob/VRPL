package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeServico {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeServico(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<ServicoBD>> clone (List<Clone<EventoBD>> cloneDeEvento,
			List<Clone<MacroServicoBD>> cloneDeMacroServico) {
		

		//criar mapa chave macro servico Original -> clone
		Map<Long,Clone<MacroServicoBD>> mapaMacroServicosClonados = new TreeMap<>();
		for (Clone<MacroServicoBD> clone : cloneDeMacroServico) {
			mapaMacroServicosClonados.put(clone.getObjetoOriginal().getId(), clone);

		}

		
		List<ServicoBD> servicosOriginais = mapaMacroServicosClonados.isEmpty() ? new ArrayList<>()
				: dao.selectServicoParaClonar(new ArrayList<Long>(mapaMacroServicosClonados.keySet()));

		List<Clone<ServicoBD>> clonesDeServico = new ArrayList<>();
		
		//criar mapa chave evento Original clone evento
		Map<Long,Clone<EventoBD>> mapaEventosClonados = new TreeMap<>();
		for (Clone<EventoBD> clone : cloneDeEvento) {
			mapaEventosClonados.put(clone.getObjetoOriginal().getId(), clone);
		}
		
		Map<Long,ServicoBD> mapaServicosOriginais = new TreeMap<>();
		List<ServicoBD> listaParaInclusao = new ArrayList<>();
		
		for (ServicoBD servicoOriginal : servicosOriginais) {
			
			mapaServicosOriginais.put(servicoOriginal.getId(), servicoOriginal);
			
			Clone<EventoBD> eventoClonadoMapa = null;
			if (servicoOriginal.getEventoFk() != null) {
				eventoClonadoMapa = mapaEventosClonados.get(servicoOriginal.getEventoFk());
			}
			
			Clone<MacroServicoBD> macroServicosClonadoMapa = mapaMacroServicosClonados.get(servicoOriginal.getMacroServicoFk());
			
			EventoBD eventoClonado = new EventoBD();

			if (eventoClonadoMapa != null) {
				eventoClonado = eventoClonadoMapa.getObjetoClonado();
			}

			if (macroServicosClonadoMapa != null) {
				MacroServicoBD macroServicoClonado = macroServicosClonadoMapa.getObjetoClonado();
				
				ServicoBD cloneServico = criarCloneServicoParaInclusao(servicoOriginal, macroServicoClonado, eventoClonado,
						siglaEvento);

				listaParaInclusao.add(cloneServico);
			} else {
				throw new IllegalStateException(
						"É esperado que exista um EventoBD clonado e um MacroServicoBD clonad para o ServicoBD "
								+ servicoOriginal);
			}
		}
		
		//inclusao em lote
		if (!listaParaInclusao.isEmpty()) {
			dao.cloneServicoBatch(listaParaInclusao);
		}
		
		//consulta por macroservicos clonados
		List<Long> idsMacroServicosClonados = cloneDeMacroServico.stream().map(Clone::getObjetoClonado)
				.map(MacroServicoBD::getId).collect(Collectors.toList());
		
		List<ServicoBD> servicosIncluidos = idsMacroServicosClonados.isEmpty() ? new ArrayList<>()
				: dao.selectServicoParaClonar(idsMacroServicosClonados);
		
		//criar relacoes
		for (ServicoBD clone: servicosIncluidos) {
			ServicoBD servicoOriginal = mapaServicosOriginais.get(Long.parseLong(clone.getVersaoId()));
			clonesDeServico.add(new Clone<ServicoBD>(servicoOriginal, clone));
		}
		return clonesDeServico;
	}
	
	protected ServicoBD criarCloneServicoParaInclusao(ServicoBD servicoOriginal, MacroServicoBD macroServicoClonado, EventoBD eventoClonado,
			String siglaEvento) {
		ServicoBD servicoClone = new ServicoBD();
		servicoClone.setMacroServicoFk(macroServicoClonado.getId());
		if (eventoClonado != null) {
			servicoClone.setEventoFk(eventoClonado.getId());
		}
		servicoClone.setTxObservacao(servicoOriginal.getTxObservacao());
		servicoClone.setNrServico(servicoOriginal.getNrServico());
		servicoClone.setCdServico(servicoOriginal.getCdServico());
		servicoClone.setTxDescricao(servicoOriginal.getTxDescricao());
		servicoClone.setSgUnidade(servicoOriginal.getSgUnidade());
		servicoClone.setVlCustoUnitarioRef(servicoOriginal.getVlCustoUnitarioRef());
		servicoClone.setPcBdi(servicoOriginal.getPcBdi());
		servicoClone.setQtTotalItensAnalise(servicoOriginal.getQtTotalItensAnalise());
		servicoClone.setVlCustoUnitario(servicoOriginal.getVlCustoUnitario());
		servicoClone.setVlCustoUnitarioDatabase(servicoOriginal.getVlCustoUnitarioDatabase());
		servicoClone.setVlPrecoUnitario(servicoOriginal.getVlPrecoUnitario());
		servicoClone.setVlPrecoTotal(servicoOriginal.getVlPrecoTotal());
		servicoClone.setPcBdiLicitado(servicoOriginal.getPcBdiLicitado());
		servicoClone.setVlPrecoUnitarioLicitado(servicoOriginal.getVlPrecoUnitarioLicitado());
		servicoClone.setInFonte(servicoOriginal.getInFonte());
		servicoClone.setIdServicoAnalise(servicoOriginal.getIdServicoAnalise());
		servicoClone.setVersao(servicoOriginal.getVersao());
		servicoClone.setVersaoId(servicoOriginal.getId() + "");
		servicoClone.setVersaoNmEvento(siglaEvento);
		servicoClone.setVersaoNr(servicoOriginal.getVersaoNr() + 1);
		
		return servicoClone;
	}

}
