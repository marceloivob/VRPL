package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeServicoFrenteDeObraAnaliseBatch {

	protected String siglaEvento;

	protected abstract ServicoFrenteObraAnaliseBD criarCloneParaInclusao(
			ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseOriginal, ServicoBD servicoClonado);
	protected abstract List<ServicoFrenteObraAnaliseBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(
			List<Long> idsServicosOriginais);
	protected abstract void inserirClonesBatchDAO(List<ServicoFrenteObraAnaliseBD> listaParaInclusao);
	protected abstract List<ServicoFrenteObraAnaliseBD> consultarClonesEntidadesDAO(List<Long> idsServicosClonados);

	public List<Clone<ServicoFrenteObraAnaliseBD>> clone(List<Clone<ServicoBD>> listaCloneDeServico) {

		Map<Long, ServicoBD> mapaServicosClonados = criarMapaServicoesClonados(listaCloneDeServico);

		List<ServicoFrenteObraAnaliseBD> servicosFrenteObraAnaliseOriginais = mapaServicosClonados.isEmpty()
				? new ArrayList<>()
				: consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaServicosClonados.keySet()));

		Map<Long, ServicoFrenteObraAnaliseBD> mapaServicosFrenteObraAnaliseOriginal = new TreeMap<>();

		List<ServicoFrenteObraAnaliseBD> listaParaInclusao = new ArrayList<>();

		for (ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseOriginal : servicosFrenteObraAnaliseOriginais) {

			mapaServicosFrenteObraAnaliseOriginal.put(servicoFrenteObraAnaliseOriginal.getId(), servicoFrenteObraAnaliseOriginal);

			ServicoBD servicoClonado = mapaServicosClonados.get(servicoFrenteObraAnaliseOriginal.getServicoFk());

			if (servicoClonado != null) {
				ServicoFrenteObraAnaliseBD clone = criarCloneParaInclusao(servicoFrenteObraAnaliseOriginal,
						servicoClonado);

				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista um ServicoBD clonado para a ServicoFrenteObraAnaliseBD "
								+ servicoFrenteObraAnaliseOriginal);
			}
		}

		//incluir
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consultar
		List<Long> idsServicosClonados = extrairIdsServicosClonados(listaCloneDeServico);

		List<ServicoFrenteObraAnaliseBD> listaClones = idsServicosClonados.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsServicosClonados);

		//retornar lista
		List<Clone<ServicoFrenteObraAnaliseBD>> clonesDeServicoFrenteDeObraAnalise = new ArrayList<>();

		for (ServicoFrenteObraAnaliseBD clone : listaClones) {
			ServicoFrenteObraAnaliseBD original = mapaServicosFrenteObraAnaliseOriginal.get(Long.parseLong(clone.getVersaoId()));

			clonesDeServicoFrenteDeObraAnalise.add(new Clone<>(original, clone));
		}

		return clonesDeServicoFrenteDeObraAnalise;
	}

	private List<Long> extrairIdsServicosClonados(List<Clone<ServicoBD>> listaCloneDeServico) {
		return listaCloneDeServico.stream().map(Clone::getObjetoClonado).map(ServicoBD::getId)
					.collect(Collectors.toList());
	}

	private Map<Long, ServicoBD> criarMapaServicoesClonados(List<Clone<ServicoBD>> listaCloneDeServico) {
		Map<Long, ServicoBD> mapaServicosClonados = new TreeMap<>();

		for (Clone<ServicoBD> clone: listaCloneDeServico) {
			mapaServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaServicosClonados;
	}
}
