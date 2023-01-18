package br.gov.serpro.vrpl.grpc.services;

import java.util.List;
import java.util.Map;

import br.gov.serpro.vrpl.grpc.ListaSubmetaRequest;
import br.gov.serpro.vrpl.grpc.submeta.SubmetaRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

/**
 * Demanda: <a href=
 * "https://alm.serpro/ccm/web/projects/Gest%C3%A3o%20de%20Demandas%20%28SIGED%29#action=com.ibm.team.workitem.viewWorkItem&id=2224341">[EVOLUTIVA]
 * - 4,5%- Disponibilizar Serviços de integração da VRPL e Projeto Básico para o
 * módulo de Acompanhamento de Obras</a>
 * 
 */
public class SubmetasDEPARAService {

	private SubmetaRepository submetaRepository;

	private List<Long> submetasId;

	public SubmetasDEPARAService(SubmetaRepository submetaRepository, ListaSubmetaRequest request) {
		validarPreCondicoes(submetaRepository);
		validarPreCondicoes(request);

		this.submetaRepository = submetaRepository;
		this.submetasId = request.getIdsList();
	}

	private void validarPreCondicoes(SubmetaRepository submetaRepository) {
		if (submetaRepository == null) {
			throw new IllegalArgumentException(
					"Não foi fornecido um Repositório para este serviço: " + submetaRepository);
		}
	}

	private void validarPreCondicoes(ListaSubmetaRequest listaDeSubmetas) {
		if (listaDeSubmetas == null) {
			throw new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Não foi fornecido uma lista válida de Submetas: " + listaDeSubmetas));
		}

		if (listaDeSubmetas.getIdsList().isEmpty()) {
			throw new StatusRuntimeException(
					Status.INVALID_ARGUMENT.withDescription("A lista de Submetas está vazia: " + listaDeSubmetas));
		}
	}

	public Map<Long, Long> recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL() {
		Map<Long, Long> submetasDoProjetoBasico = submetaRepository
				.getRecuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(this.submetasId);

		if (submetasDoProjetoBasico.isEmpty()) {
			throw new StatusRuntimeException(Status.NOT_FOUND
					.withDescription("Não foi encontrada nenhuma Submeta com os IDs: " + this.submetasId));
		}

		return submetasDoProjetoBasico;

	}

}
