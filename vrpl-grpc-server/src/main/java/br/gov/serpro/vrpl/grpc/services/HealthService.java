package br.gov.serpro.vrpl.grpc.services;

import java.sql.SQLException;

import org.eclipse.jetty.server.Response;

import br.gov.serpro.siconv.med.grpc.HealthCheckRequest;
import br.gov.serpro.siconv.med.grpc.HealthCheckResponse;
import br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder;
import br.gov.serpro.siconv.med.grpc.HealthGrpc.HealthImplBase;
import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HealthService extends HealthImplBase {
	
	
	@Override
    public void check(br.gov.serpro.siconv.med.grpc.HealthCheckRequest request,
            io.grpc.stub.StreamObserver<br.gov.serpro.siconv.med.grpc.HealthCheckResponse> responseObserver) {
          
		log.info("Executando Liveness através de chamada gRPC");
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = this.generateReturn();
		
		responseObserver.onNext(healthCheckResponse.build());
		
		responseObserver.onCompleted();

    }
	
	@Override
	public void ready(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
		
		log.info("Executando Readiness através de chamada gRPC");
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = this.generateDatabaseReturn();
		
		responseObserver.onNext(healthCheckResponse.build());
		
		responseObserver.onCompleted();
		
	}
	

	public void httpCheck (spark.Response response) {

		log.info("Executando Liveness através de chamada http");
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = this.generateReturn();
		
		
		switch (healthCheckResponse.getStatus()) {
		case SERVING:
			response.body(generateBody(HealthCheckResponse.ServingStatus.SERVING));
			response.status(Response.SC_OK);
			break;

		default:
			break;
		}
		
	}
	
	
	public void httpReady (spark.Response response) {

		log.info("Executando Readiness através de chamada http");
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = this.generateDatabaseReturn();
		
		response.body(generateBody(healthCheckResponse.getStatus()));
		
		switch (healthCheckResponse.getStatus()) {
		case SERVING:
			response.status(Response.SC_OK);
			break;
		case NOT_SERVING:
			response.status(Response.SC_NOT_FOUND);
			break;
		default:
			break;
		}
		
	}
	
	
	private Builder generateDatabaseReturn() {
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = HealthCheckResponse.newBuilder();

		if (databaseIsOk()) {
			healthCheckResponse.setStatus(HealthCheckResponse.ServingStatus.SERVING);
		} else {
			healthCheckResponse.setStatus(HealthCheckResponse.ServingStatus.NOT_SERVING);
		}
		
		return healthCheckResponse;
	}

	private String generateBody(HealthCheckResponse.ServingStatus status) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("status: "+status.name());
		
		return sb.toString();
	}

	/**
	 * Executa a lógica do Liveness tanto para gRPC quanto para Http
	 * 
	 * @param healthCheckResponse
	 */
	private br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder generateReturn() {
		
		br.gov.serpro.siconv.med.grpc.HealthCheckResponse.Builder healthCheckResponse = HealthCheckResponse.newBuilder();

		healthCheckResponse.setStatus(HealthCheckResponse.ServingStatus.SERVING);
		
		return healthCheckResponse;
	}

	
	private boolean databaseIsOk() {
		try {
			return JDBIProducer.hasValidConnection();	
		} catch (SQLException sqlException) {
			log.error("Não foi possível obter uma conexão com o banco!", sqlException);

			return false;
		}
	}
}
