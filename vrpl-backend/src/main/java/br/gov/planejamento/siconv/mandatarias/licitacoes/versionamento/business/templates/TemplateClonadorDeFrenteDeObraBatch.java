package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeFrenteDeObraBatch {
	protected String siglaEvento;

	protected abstract FrenteObraBD criarCloneParaInclusao(FrenteObraBD frenteDeObraOriginal, PoBD poPaiClonada);
	protected abstract List<FrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsPosOriginais);
	protected abstract void inserirClonesBatchDAO(List<FrenteObraBD> listaParaInclusao);
	protected abstract List<FrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsPosClonadas);

	public List<Clone<FrenteObraBD>> clone(List<Clone<PoBD>> listaClonesDePO) {

		// Vamos criar um mapa "Chave Po original -> Clone Po" para agilizar a localizacao do clone da PO
		Map<Long, PoBD> mapaChavePoOriginalClone = criarMapaChavePoOriginalClone(listaClonesDePO);

		List<FrenteObraBD> frenteDeObraOriginais = mapaChavePoOriginalClone.isEmpty() ? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaChavePoOriginalClone.keySet()));

		Map<Long, FrenteObraBD> mapaChaveFrenteDeObraOriginal = new TreeMap<>();

		List<FrenteObraBD> listaParaInclusao = new ArrayList<>();

		// Criar clones
		for (FrenteObraBD frenteDeObraOriginal : frenteDeObraOriginais) {

			mapaChaveFrenteDeObraOriginal.put(frenteDeObraOriginal.getId(), frenteDeObraOriginal);

			PoBD poPaiClonada = mapaChavePoOriginalClone.get(frenteDeObraOriginal.getPoFk());

			if (poPaiClonada != null) {
				FrenteObraBD clone = criarCloneParaInclusao(frenteDeObraOriginal, poPaiClonada);
				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista uma PoBD clonada para o FrenteDeObraBD " + frenteDeObraOriginal);
			}
		}

		// Inserir clones
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		// Consultar entidades que foram clonadas para montar a saida
		List<Long> idsPosClonadas = extrairIdsPosClonadas(listaClonesDePO);

		List<FrenteObraBD> frenteDeObraClonados = idsPosClonadas.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsPosClonadas);

		// Criar saida lista "Entidade Original - Entidade Clone"
		List<Clone<FrenteObraBD>> clonesDeFrenteDeObra = new ArrayList<>();

		for (FrenteObraBD clone : frenteDeObraClonados) {
			FrenteObraBD frenteDeObraOriginal = mapaChaveFrenteDeObraOriginal.get(Long.parseLong(clone.getVersaoId()));

			clonesDeFrenteDeObra.add(new Clone<>(frenteDeObraOriginal, clone));
		}

		return clonesDeFrenteDeObra;
	}

	private List<Long> extrairIdsPosClonadas(List<Clone<PoBD>> listaClonesDePO) {
		return listaClonesDePO.stream().map(Clone::getObjetoClonado).map(PoBD::getId).collect(Collectors.toList());
	}

	private Map<Long, PoBD> criarMapaChavePoOriginalClone(List<Clone<PoBD>> listaClonesDePO) {
		Map<Long, PoBD> mapaChavePoOriginalClone = new TreeMap<>();

		for (Clone<PoBD> clone : listaClonesDePO) {
			mapaChavePoOriginalClone.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaChavePoOriginalClone;
	}
}
