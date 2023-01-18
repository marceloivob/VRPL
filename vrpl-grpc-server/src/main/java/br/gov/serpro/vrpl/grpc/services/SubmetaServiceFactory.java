package br.gov.serpro.vrpl.grpc.services;

import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.submeta.SubmetaRepository;

public class SubmetaServiceFactory {

	public static SubmetaService createSubmetaService() {
		Jdbi jdbi = JDBIProducer.getJdbi();

		SubmetaRepository submetaRepository = new SubmetaRepository(jdbi);

		SubmetaService submetaService = new SubmetaService(submetaRepository);

		return submetaService;
	}
}
