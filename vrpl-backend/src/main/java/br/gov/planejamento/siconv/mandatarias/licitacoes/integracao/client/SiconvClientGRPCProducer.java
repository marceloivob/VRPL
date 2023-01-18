package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.net.ssl.SSLException;

import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class SiconvClientGRPCProducer implements Serializable {

	private static final long serialVersionUID = -7398370894876974073L;

	private SiconvGRPCClient clientSiconvGRPC;

	@Produces
	public SiconvGRPCClient create() throws SSLException {

		if (clientSiconvGRPC != null) {
			return clientSiconvGRPC;
		}

		final String HOST_GRPC_SICONV = System.getProperty("integrations.PRIVATE.GRPC.SICONV.endpoint");
		final Integer PORTA_GRPC_SICONV = Integer.getInteger("integrations.PRIVATE.GRPC.SICONV.port");
		final boolean USE_SSL = Boolean.getBoolean("integrations.PRIVATE.GRPC.SICONV.useSSL");

		this.clientSiconvGRPC = new SiconvGRPCClient(HOST_GRPC_SICONV, PORTA_GRPC_SICONV, USE_SSL);

		log.debug("Criando Client para o SICONV gRPC '{}' com o Host {} e Porta {} e SSL {}", this.clientSiconvGRPC,
				HOST_GRPC_SICONV, PORTA_GRPC_SICONV, USE_SSL);

		return clientSiconvGRPC;
	}

	public void close(@Disposes SiconvGRPCClient client) {
		try {
			log.debug("Fechando o Client para o SICONV gRPC'{}'", client);

			client.shutdown();
		} catch (Exception e) {
			log.error("Erro ao fechar o Client de serviço do SICONV gRPC", e);
		}
	}
}
