package br.gov.serpro.vrpl.grpc;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.stop;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import br.gov.serpro.vrpl.grpc.application.EnvironmentProperties;
import br.gov.serpro.vrpl.grpc.services.HealthService;
import br.gov.serpro.vrpl.grpc.services.PropostaService;
import br.gov.serpro.vrpl.grpc.services.SubmetaServiceFactory;
import br.gov.serpro.vrpl.grpc.services.VrplService;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcServer {

	private Server server; 

	private HealthService hc;
	
	private EnvironmentProperties environmentProperties;

	public GrpcServer(EnvironmentProperties environmentProperties) {
		this.environmentProperties = environmentProperties;
	}

	private void startGRPCServer() throws IOException {

		log.info("Servidor gRPC iniciado na porta {}", environmentProperties.getGrpcPort());

		hc = new HealthService();

		ServerBuilder sb = ServerBuilder.forPort(environmentProperties.getGrpcPort());
		sb.addService(new PropostaService());
		sb.addService(SubmetaServiceFactory.createSubmetaService());
		sb.addService(hc); 
		sb.addService(new VrplService());
		
		server = sb.build().start();

		printServicesOutput();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				log.info("*** Parando servidor gRPC");

				GrpcServer.this.stopGrpcServer();

				log.info("*** Servidor gRPC parado");
			}
		});

	}

	/**
	 * Exibe no console a lista de Serviços e Métodos levantadas pelo servidor
	 */
	private void printServicesOutput() {

		List<ServerServiceDefinition> servicos = server.getServices();

		for (ServerServiceDefinition servico : servicos) {
			log.info("*** Serviços ****");
			log.info(servico.getServiceDescriptor().getName());
			log.info("*** Métodos ****");

			Collection<MethodDescriptor<?, ?>> metodos = servico.getServiceDescriptor().getMethods();

			for (MethodDescriptor<?, ?> metodo : metodos) {
				log.info(metodo.getFullMethodName());
			}
		}
	}

	private void startHTTPServer() {
		port(environmentProperties.getHttpPort());
		registerReadiness(); 
		registerLiveness();
	}

	private void registerReadiness() {
		get("/health/ready", (request, response) -> {
			log.info("Serviço Readiness OK");
			
			hc.httpReady(response);
			
			return response.body();			
			
		});
	}

	private void registerLiveness() {
		get("/health/live", (request, response) -> {
			log.info("Serviço Liveness OK");
			
			hc.httpCheck(response);
			
			return response.body();			
			
		});
	}


	private void registerEndpointToShuttdown() {
		get("/shutdown", (request, response) -> {
			log.info("Desligando o servidor HTTP");

			this.stopGrpcServer();
			stop();
			return "";
		});
	}

	public void stopGrpcServer() {
		if (server != null) {
			server.shutdown();
		}
	}

	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public Boolean isShutdown() {
		if (server != null) {
			return server.isShutdown();
		}

		return null;
	}

	public List<ServerServiceDefinition> getImmutableServices() {
		if (server != null) {
			return server.getImmutableServices();
		}

		return null;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		final GrpcServer services = new GrpcServer(new EnvironmentProperties());

		services.startGRPCServer();
		services.startHTTPServer();
		services.blockUntilShutdown();
	}

}
