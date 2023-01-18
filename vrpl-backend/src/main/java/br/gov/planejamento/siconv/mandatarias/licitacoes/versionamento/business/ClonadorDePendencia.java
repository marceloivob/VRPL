package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDePendencia {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDePendencia(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<PendenciaBD>> clone(List<Clone<LaudoBD>> cloneDeLaudo, List<Clone<SubmetaBD>> cloneDeSubmeta) {

		List<Clone<PendenciaBD>> clonesDePendencia = new ArrayList<>();

		List<Long> idsLaudosOriginais = cloneDeLaudo.stream().map(Clone::getObjetoOriginal)
				.map(LaudoBD::getId).collect(Collectors.toList());
		
		List<PendenciaBD> pendenciasOriginais = new ArrayList<>();
		if (idsLaudosOriginais != null && !idsLaudosOriginais.isEmpty()) {
			pendenciasOriginais = dao.selectPendenciaParaClonar(idsLaudosOriginais);
		}

		for (PendenciaBD pendenciaOriginal : pendenciasOriginais) {
			Optional<Clone<SubmetaBD>> submetasClonadas = cloneDeSubmeta.stream()
					.filter(submetaClonada -> submetaClonada.getObjetoOriginal().getId()
							.equals(pendenciaOriginal.getSubmetaFk()))
					.findFirst();

			Optional<Clone<LaudoBD>> laudosClonados = cloneDeLaudo.stream().filter(
					laudoClonado -> laudoClonado.getObjetoOriginal().getId().equals(pendenciaOriginal.getLaudoFk()))
					.findFirst();

			if (submetasClonadas.isPresent() && laudosClonados.isPresent()) {
				SubmetaBD submetaClonada = submetasClonadas.get().getObjetoClonado();
				LaudoBD laudoClonado = laudosClonados.get().getObjetoClonado();

				PendenciaBD clone = dao.clonePendencia(pendenciaOriginal, laudoClonado, submetaClonada,
						siglaEvento);

				clonesDePendencia.add(new Clone<PendenciaBD>(pendenciaOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista uma SubmetaBD clonada para a PendenciaBD " + pendenciaOriginal);
			}
		}
		
		return clonesDePendencia;
	}
}
