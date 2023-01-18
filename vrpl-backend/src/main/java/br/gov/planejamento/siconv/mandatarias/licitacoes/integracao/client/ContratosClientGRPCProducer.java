package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.net.ssl.SSLException;

import br.gov.serpro.siconv.contratos.grpc.services.ContratosGRPCClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class ContratosClientGRPCProducer implements Serializable  {/**
	 * 
	 */
	private static final long serialVersionUID = -5697296597377605L;
	
	private ContratosGRPCClient clientContratosGRPC;
	
	@Produces
	public ContratosGRPCClient create() throws SSLException {

		if (clientContratosGRPC != null) {
			return clientContratosGRPC;
		}

		final String HOST_GRPC_CONTRATOS = System.getProperty("integrations.PRIVATE.GRPC.CONTRATOS.endpoint");
		final Integer PORTA_GRPC_CONTRATOS = Integer.getInteger("integrations.PRIVATE.GRPC.CONTRATOS.port");
		final boolean USE_SSL = Boolean.getBoolean("integrations.PRIVATE.GRPC.CONTRATOS.useSSL");

        try {
		
			this.clientContratosGRPC = new ContratosGRPCClient(HOST_GRPC_CONTRATOS, PORTA_GRPC_CONTRATOS, USE_SSL);
	
			log.debug("Criando Client para Contratos gRPC '{}' com o Host {} e Porta {} e SSL {}", this.clientContratosGRPC,
					HOST_GRPC_CONTRATOS, PORTA_GRPC_CONTRATOS, USE_SSL);
	
			return clientContratosGRPC;
		
        } catch (Exception e) {
            throw new RuntimeException("Falha na integração com o serviço getQTDContratos do ContratosGRPCClient.", e);

        } 

	}

	public void close(@Disposes ContratosGRPCClient client) {
		try {
			log.debug("Fechando o Client para Contratos gRPC'{}'", client);

			client.shutdown();
		} catch (Exception e) {
			log.error("Erro ao fechar o Client de serviço Contratos gRPC", e);
		}
	}
}
