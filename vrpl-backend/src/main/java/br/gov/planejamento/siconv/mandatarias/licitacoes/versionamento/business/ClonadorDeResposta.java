package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class ClonadorDeResposta {

	private VersionamentoDAO dao;
	private String siglaEvento;

	public ClonadorDeResposta(VersionamentoDAO dao, EventoQuadroResumoEnum evento) {
		this.dao = dao;
		this.siglaEvento = evento.getSigla();
	}

	public List<Clone<RespostaBD>> clone(List<Clone<LaudoBD>> cloneDeLaudo, List<Clone<LoteBD>> cloneDeLote) {
		
		List<Long> idsLaudosOriginais = cloneDeLaudo.stream().map(Clone::getObjetoOriginal)
				.map(LaudoBD::getId).collect(Collectors.toList());

		List<Clone<RespostaBD>> clonesDeResposta = new ArrayList<>();

		List<RespostaBD> respostasOriginais = new ArrayList<>();
		if(idsLaudosOriginais != null && !idsLaudosOriginais.isEmpty()) {
			respostasOriginais = dao.selectRespostaParaClonar(idsLaudosOriginais);
		}

		for (RespostaBD respostaOriginal : respostasOriginais) {
			Optional<Clone<LoteBD>> lotesClonados = cloneDeLote.stream().filter(loteClonado -> loteClonado
					.getObjetoOriginal().getId().equals(respostaOriginal.getLoteFk())).findFirst();

			Optional<Clone<LaudoBD>> laudosClonados = cloneDeLaudo.stream().filter(
					laudoClonado -> laudoClonado.getObjetoOriginal().getId().equals(respostaOriginal.getLaudoFk()))
					.findFirst();

			// Lote é opcional
			LoteBD loteClonado = new LoteBD();

			if (lotesClonados.isPresent()) {
				loteClonado = lotesClonados.get().getObjetoClonado();
			}

			if (laudosClonados.isPresent()) {
				LaudoBD laudoClonado = laudosClonados.get().getObjetoClonado();
				
				RespostaBD clone = dao.cloneResposta(respostaOriginal, laudoClonado, loteClonado, siglaEvento);

				clonesDeResposta.add(new Clone<RespostaBD>(respostaOriginal, clone));
			} else {
				throw new IllegalStateException(
						"É esperado que exista um LoteBD clonado para a RespostaBD "
								+ respostaOriginal);
			}
		}

		return clonesDeResposta;
	}

}
