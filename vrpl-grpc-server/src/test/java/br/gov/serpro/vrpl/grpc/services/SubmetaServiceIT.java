package br.gov.serpro.vrpl.grpc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import br.gov.serpro.vrpl.grpc.DESubmetaVrplPARASubmetaProjetoBasico;
import br.gov.serpro.vrpl.grpc.ListaSubmetaRequest;
import io.grpc.stub.StreamObserver;

@Disabled
@DisplayName(value = "Teste de Integração - SubmetaService")
public class SubmetaServiceIT {

	private SubmetaService submetaService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);

		this.submetaService = SubmetaServiceFactory.createSubmetaService();
	}

	@DisplayName(value = "Teste de Integração - SubmetaService")
	@Test
	public void teste() {
		//////////////////////////////////////////////////////////////
		// Preparação dos dados de teste
		//////////////////////////////////////////////////////////////

		final Long idSubmeta2370 = 2370L;
		final Long idSubmeta2371 = 2371L;
		final Long idSubmetaInexistente = -1L;

		ListaSubmetaRequest listaDeSubmetasAPesquisar = ListaSubmetaRequest //
				.newBuilder() //
				.addIds(idSubmeta2370) //
				.addIds(idSubmeta2371) //
				.addIds(idSubmetaInexistente) //
				.build();

		StreamObserver<DESubmetaVrplPARASubmetaProjetoBasico> submetasEncontradas = criarStreamDeResposta();

		//////////////////////////////////////////////////////////////
		// Método de Negócio
		//////////////////////////////////////////////////////////////
		submetaService.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(listaDeSubmetasAPesquisar,
				submetasEncontradas);
	}

	//////////////////////////////////////////////////////////////
	// Asserts
	//////////////////////////////////////////////////////////////
	private StreamObserver<DESubmetaVrplPARASubmetaProjetoBasico> criarStreamDeResposta() {
		return new StreamObserver<DESubmetaVrplPARASubmetaProjetoBasico>() {

			@Override
			public void onNext(DESubmetaVrplPARASubmetaProjetoBasico dePara) {
				assertNotNull(dePara);

				assertEquals(dePara.getSubmetasCount(), 2);
			}

			@Override
			public void onError(Throwable t) {
				assertNotNull(t);

				fail("Erro inesperado :" + t.getMessage());
			}

			@Override
			public void onCompleted() {

			}
		};
	}
}
