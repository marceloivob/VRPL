package br.gov.serpro.vrpl.grpc.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse;
import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse.Licitacao;
import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse.Licitacao.SituacaoLicitacaoVRPLEnum;
import br.gov.serpro.vrpl.grpc.PropostaGrpc.PropostaImplBase;
import br.gov.serpro.vrpl.grpc.PropostaRequest;
import br.gov.serpro.vrpl.grpc.IdentificadorPropostaRequest;
import br.gov.serpro.vrpl.grpc.VrplAceitaResponse;
import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.business.PropostaBC;
import br.gov.serpro.vrpl.grpc.database.LicitacaoDAO;
import br.gov.serpro.vrpl.grpc.database.bean.LicitacaoBD;
import br.gov.serpro.vrpl.grpc.database.bean.VerificarExclusaoVrplDTO;
import br.gov.serpro.vrpl.grpc.database.bean.VrplAceitaDTO;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class PropostaService extends PropostaImplBase {

	private Jdbi jdbi;
	private PropostaBC propostaBC;

	public PropostaService() {
		this.jdbi = JDBIProducer.getJdbi();
		propostaBC = new PropostaBC();
	}

	@Override
	public void consultarPermissoesDasLicitacoesDaProposta(PropostaRequest propostaRequest,
			StreamObserver<PermissoesDasLicitacoesResponse> responseObserver) {
		
		if (validaParametros(propostaRequest, responseObserver)) {

			try (Handle handle = jdbi.open()) {
	
				List<LicitacaoBD> licitacoes = handle.attach(LicitacaoDAO.class)
						.consultarPermissoesDasLicitacoesDaProposta(propostaRequest);
	
				if ((licitacoes == null) || (licitacoes.isEmpty())) {
					responseObserver.onError(new StatusRuntimeException(
							Status.NOT_FOUND.withDescription(
									"Processo de Execução não encontrado: " + propostaRequest.getIdProposta())));
				} else {
					PermissoesDasLicitacoesResponse response = this.converte(licitacoes);
	
					responseObserver.onNext(response);
					responseObserver.onCompleted();
				}
			}
		}
	}

	
	@Override
	public void existeVrplAceita(PropostaRequest propostaRequest, StreamObserver<VrplAceitaResponse> responseObserver) {
		
		
		if (validaParametros(propostaRequest, responseObserver)) {
			Optional<VrplAceitaDTO> vrplAceitaOpt = this.propostaBC.existeVrplAceita (propostaRequest.getIdProposta());
			
			if (vrplAceitaOpt.isPresent()) {
				br.gov.serpro.vrpl.grpc.VrplAceitaResponse.Builder response = VrplAceitaResponse.newBuilder();
				
				if (vrplAceitaOpt.get().getExisteVrplAceita()) {
					response.setVrplAceita(Boolean.TRUE);
				} else {
					response.setVrplAceita(Boolean.FALSE);
				}
				
				responseObserver.onNext(response.build());
				responseObserver.onCompleted();
			} else {
				responseObserver.onError(new StatusRuntimeException(
						Status.NOT_FOUND.withDescription(
								"Proposta não encontrada: " + propostaRequest.getIdProposta())));
			}
			
		}
		
	}

	private PermissoesDasLicitacoesResponse converte(List<LicitacaoBD> licitacoes) {

		br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse.Builder permissao = PermissoesDasLicitacoesResponse.newBuilder();

		List<SituacaoLicitacaoEnum> podeAlterar = Arrays.asList(SituacaoLicitacaoEnum.HOMOLOGADA_NO_PROJETO_BASICO,
				SituacaoLicitacaoEnum.EM_ELABORACAO, SituacaoLicitacaoEnum.EM_COMPLEMENTACAO);
		List<SituacaoLicitacaoEnum> podeExcluir = Arrays.asList(SituacaoLicitacaoEnum.HOMOLOGADA_NO_PROJETO_BASICO);

		for (LicitacaoBD licitacaoBD : licitacoes) {

			boolean alterar = podeAlterar.contains(licitacaoBD.getSituacao());
			boolean excluir = podeExcluir.contains(licitacaoBD.getSituacao());

			SituacaoLicitacaoEnumWrapper wrapper = new SituacaoLicitacaoEnumWrapper();
			SituacaoLicitacaoVRPLEnum estado = wrapper.convert(licitacaoBD.getSituacao());

			Licitacao licitacao = Licitacao.newBuilder().setIdDaLicitacao(licitacaoBD.getIdLicitacao().intValue())
					.setEstado(estado).setAlterar(alterar).setExcluir(excluir).build();

			permissao.addLicitacao(licitacao);
		}

		return permissao.build();
	}

	
	private Boolean validaParametros(PropostaRequest propostaRequest,
			StreamObserver<?> responseObserver) {
		
		if (propostaRequest == null) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador do Instrumento' inválido: " + propostaRequest)));
			return Boolean.FALSE;
		}

		if (propostaRequest.getIdProposta() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador do Instrumento' inválido: " + propostaRequest.getIdProposta())));
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
}
