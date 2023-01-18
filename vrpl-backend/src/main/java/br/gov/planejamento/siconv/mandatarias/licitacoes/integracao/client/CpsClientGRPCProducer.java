package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.net.ssl.SSLException;

import br.gov.planejamento.siconv.grpc.CpsGRPCClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class CpsClientGRPCProducer implements Serializable {
	
	private static final long serialVersionUID = -136221483774243970L;

	private CpsGRPCClient clientCpsGRPC;
	
	@Produces
	public CpsGRPCClient create() throws SSLException {

		if (clientCpsGRPC != null) {
			return clientCpsGRPC;
		}

		final String HOST_GRPC_CPS = System.getProperty("integrations.PRIVATE.GRPC.CPS.endpoint");
		final Integer PORTA_GRPC_CPS = Integer.getInteger("integrations.PRIVATE.GRPC.CPS.port");
		final boolean USE_SSL = Boolean.getBoolean("integrations.PRIVATE.GRPC.CPS.useSSL");

		this.clientCpsGRPC = new CpsGRPCClient(HOST_GRPC_CPS, PORTA_GRPC_CPS, USE_SSL);

		log.debug("Criando Client para o CPS gRPC '{}' com o Host {} e Porta {} e SSL {}", this.clientCpsGRPC,
				HOST_GRPC_CPS, PORTA_GRPC_CPS, USE_SSL);

		return clientCpsGRPC;
	}

	public void close(@Disposes CpsGRPCClient client) {
		try {
			log.debug("Fechando o Client para o CPS gRPC'{}'", client);

			client.shutdown();
		} catch (Exception e) {
			log.error("Erro ao fechar o Client de serviço do CPS gRPC", e);
		}
	}
	
}











