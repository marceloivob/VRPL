package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.net.ssl.SSLException;

import br.gov.serpro.siconv.grpc.ClientLicitacoes;
import br.gov.serpro.siconv.grpc.ClientLicitacoesInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class LicitacaoClientProducer implements Serializable {

	private static final long serialVersionUID = 950475534367512616L;

	private ClientLicitacoesInterface clientLicitacoes;

	@Produces
	public ClientLicitacoesInterface create() throws SSLException {

		if (clientLicitacoes != null) {
			return clientLicitacoes;
		}

		final String HOST_GRPC_LICITACAO = System.getProperty("integrations.PRIVATE.GRPC.LICITACAO.endpoint");
		final Integer PORTA_GRPC_LICITACAO = Integer.getInteger("integrations.PRIVATE.GRPC.LICITACAO.port");
		final boolean USE_SSL = Boolean.getBoolean("integrations.PRIVATE.GRPC.LICITACAO.useSSL");

		this.clientLicitacoes = new ClientLicitacoes(HOST_GRPC_LICITACAO, PORTA_GRPC_LICITACAO, USE_SSL);

		log.debug("Criando Client para o Licitações gRPC '{}' com o Host {} e Porta {} e SSL {}",
				this.clientLicitacoes, HOST_GRPC_LICITACAO, PORTA_GRPC_LICITACAO, USE_SSL);

		return clientLicitacoes;
	}

	public void close(@Disposes ClientLicitacoesInterface client) {
		try {
			log.debug("Fechando o Client para o Licitações gRPC '{}'", client);

			client.shutdown();
		} catch (Exception e) {
			log.error("Erro ao fechar o Client de serviço do Licitações gRPC", e);
		}
	}

}
