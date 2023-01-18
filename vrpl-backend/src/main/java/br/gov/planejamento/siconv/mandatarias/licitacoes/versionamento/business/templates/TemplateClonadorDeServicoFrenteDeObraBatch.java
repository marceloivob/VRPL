package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;

public abstract class TemplateClonadorDeServicoFrenteDeObraBatch {

	protected String siglaEvento;

	protected abstract List<ServicoFrenteObraBD> consultarEntidadesOriginaisQueSeraoClonadasDAO(List<Long> idsServicosOriginais);
	protected abstract ServicoFrenteObraBD criarCloneParaInclusao(ServicoFrenteObraBD servicoFrenteObraOriginal,
			FrenteObraBD frenteObraClonada, ServicoBD servicoClonado);
	protected abstract void inserirClonesBatchDAO(List<ServicoFrenteObraBD> listaParaInclusao);
	protected abstract List<ServicoFrenteObraBD> consultarClonesEntidadesDAO(List<Long> idsServicosClonados);

	public List<Clone<ServicoFrenteObraBD>> clone(List<Clone<ServicoBD>> listaCloneDeServico,
			List<Clone<FrenteObraBD>> listaCloneDeFrenteDeObra) {

		Map<Long, ServicoBD> mapaServicosClonados = criarMapaServicosClonados(listaCloneDeServico);

		Map<Long, FrenteObraBD> mapaFrentesObraClonadas = criarMapaFrentesObraClonadas(listaCloneDeFrenteDeObra);

		List<ServicoFrenteObraBD> servicosFrenteObraOriginais = new ArrayList<>();

		if (mapaServicosClonados.keySet() != null && !mapaServicosClonados.keySet().isEmpty() ) {
			servicosFrenteObraOriginais = consultarEntidadesOriginaisQueSeraoClonadasDAO(new ArrayList<>(mapaServicosClonados.keySet()));
		}

		Map<String, ServicoFrenteObraBD> mapaServicosFrenteObraOriginal = new TreeMap<>();
		List<ServicoFrenteObraBD> listaParaInclusao = new ArrayList<>();

		for (ServicoFrenteObraBD servicoFrenteObraOriginal : servicosFrenteObraOriginais) {

			mapaServicosFrenteObraOriginal.put(servicoFrenteObraOriginal.getServicoFk()  +  "_" +  servicoFrenteObraOriginal.getFrenteObraFk(), servicoFrenteObraOriginal);

			ServicoBD servicoClonado = mapaServicosClonados.get(servicoFrenteObraOriginal.getServicoFk());

			FrenteObraBD frenteObraClonada = mapaFrentesObraClonadas.get(servicoFrenteObraOriginal.getFrenteObraFk());

			if (servicoClonado != null && frenteObraClonada != null) {

				ServicoFrenteObraBD clone  = criarCloneParaInclusao(servicoFrenteObraOriginal, frenteObraClonada,
						servicoClonado);

				listaParaInclusao.add(clone);

			} else {
				throw new IllegalStateException(
						"É esperado que exista um ServicoBD clonado e uma FrenteObraBD clonada para a ServicoFrenteObraBD "
								+ servicoFrenteObraOriginal);
			}
		}

		//incluir
		if (!listaParaInclusao.isEmpty()) {
			inserirClonesBatchDAO(listaParaInclusao);
		}

		//consultar
		List<Long> idsServicosClonados = extrairIdsServicosClonados(listaCloneDeServico);

		List<ServicoFrenteObraBD> servicosFrenteDeObraClonados = idsServicosClonados.isEmpty() ? new ArrayList<>()
				: consultarClonesEntidadesDAO(idsServicosClonados);

		//retornar lista
		List<Clone<ServicoFrenteObraBD>> clonesDeServicoFrenteDeObra = new ArrayList<>();

		for (ServicoFrenteObraBD clone : servicosFrenteDeObraClonados) {
			ServicoFrenteObraBD servicoFrenteOriginal = mapaServicosFrenteObraOriginal.get(clone.getVersaoId());
			clonesDeServicoFrenteDeObra.add(new Clone<>(servicoFrenteOriginal, clone));
		}

		return clonesDeServicoFrenteDeObra;
	}

	private List<Long> extrairIdsServicosClonados(List<Clone<ServicoBD>> listaCloneDeServico) {
		return listaCloneDeServico.stream().map(Clone::getObjetoClonado).map(ServicoBD::getId)
				.collect(Collectors.toList());
	}

	private Map<Long, FrenteObraBD> criarMapaFrentesObraClonadas(List<Clone<FrenteObraBD>> listaCloneDeFrenteDeObra) {
		Map<Long, FrenteObraBD> mapaFrentesObraClonadas = new TreeMap<>();

		for (Clone<FrenteObraBD> clone : listaCloneDeFrenteDeObra) {
			mapaFrentesObraClonadas.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaFrentesObraClonadas;
	}

	private Map<Long, ServicoBD> criarMapaServicosClonados(List<Clone<ServicoBD>> listaCloneDeServico) {
		Map<Long, ServicoBD> mapaServicosClonados = new TreeMap<>();

		for (Clone<ServicoBD> clone: listaCloneDeServico) {
			mapaServicosClonados.put(clone.getObjetoOriginal().getId(), clone.getObjetoClonado());
		}

		return mapaServicosClonados;
	}
}
