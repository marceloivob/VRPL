package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.grpc.SituacaoLicitacaoResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.SiconvGRPCException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScoped
public class SiconvGRPCConsumer {

	@Inject
	private SiconvGRPCClient siconvGrpcClient;

	public Boolean existeHistoricoAceiteLicitacaoPossuiSituacao(Long licitacaoId,
			SituacaoLicitacaoEnum situacaoLicitacao) {

		log.info(
				"Executando gRPC SICONV serviço: consultarHistoricoAceiteLicitacaoPossuiSituacao com os parâmetros: licitacaoId: '{}', situacaoLicitacao: '{}'",
				licitacaoId, situacaoLicitacao);
		try {
			SituacaoLicitacaoResponse response = siconvGrpcClient
					.consultarHistoricoAceiteLicitacaoPossuiSituacao(licitacaoId, situacaoLicitacao);

			Boolean situacaoJaOcorrida = response.getSituacaoJaOcorrida();

			log.info(
					"Resultado do gRPC SICONV serviço consultarHistoricoAceiteLicitacaoPossuiSituacao com os parâmetros: licitacaoId: '{}', situacaoLicitacao: '{}'. Resultado: '{}'",
					licitacaoId, situacaoLicitacao, situacaoJaOcorrida);

			return situacaoJaOcorrida;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SiconvGRPCException(e.getMessage());
		}

	}

}
