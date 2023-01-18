package br.gov.serpro.vrpl.grpc.services;

import br.gov.serpro.vrpl.grpc.ExclusaoVrplResponse;
import br.gov.serpro.vrpl.grpc.IdentificadorExclusaoVRPLRequest;
import br.gov.serpro.vrpl.grpc.IdentificadorPropostaRequest;
import br.gov.serpro.vrpl.grpc.VrplGrpc.VrplImplBase;
import br.gov.serpro.vrpl.grpc.business.VrplBC;
import br.gov.serpro.vrpl.grpc.database.bean.VerificarExclusaoVrplDTO;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class VrplService extends VrplImplBase {

	private VrplBC vrplBC;

	public VrplService() {
		vrplBC = new VrplBC();
	}

    /**
	 * Autor - Valdecio Ribeiro
	 * ALM - 733245: SICONV-DocumentosOrcamentarios-gRPC-Servico consultar situação da proposta na VRPL
	 * 
	 */
	@Override
	public void verificarExclusaoVrpl(IdentificadorPropostaRequest request, StreamObserver<ExclusaoVrplResponse> responseObserver) {

		//validar parâmetros
		if (request == null) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador da Proposta' inválido: " + request)));
		}

		if (request.getNumeroProposta() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador da Proposta' inválido: " + request.getNumeroProposta())));
		}

		if (request.getAnoProposta() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador do ano da Proposta' inválido: " + request.getAnoProposta())));
		}
		
		//verificar exclusão VRPL
		VerificarExclusaoVrplDTO situacaoVrpl = this.verificarExclusaoVRPL(request.getNumeroProposta(), request.getAnoProposta());

		//Preparar resposta
		br.gov.serpro.vrpl.grpc.ExclusaoVrplResponse.Builder response = ExclusaoVrplResponse.newBuilder();

		if(situacaoVrpl != null) {
			response.setSituacao(situacaoVrpl.getSituacao());
			response.setMensagem(situacaoVrpl.getMensagem());
		} else {
			responseObserver.onError(new StatusRuntimeException(
						Status.FAILED_PRECONDITION.withDescription(
								"Erro ao consultar proposta: " + request.getNumeroProposta() + "/" + request.getAnoProposta())));
		}
	
		responseObserver.onNext(response.build());
		responseObserver.onCompleted();

	}

	
	private VerificarExclusaoVrplDTO verificarExclusaoVRPL(long numeroProposta, long anoProposta) {		
		
		return this.vrplBC.verificarExclusaoVRPL(numeroProposta, anoProposta);
	}

	/**
	 * Autor - Valdecio Ribeiro
	 * ALM - 709387: SICONV-DocumentosOrcamentarios-gRPC-Servico excluir dados da proposta na VRPL
	 * 
	 */
	@Override
	public void excluirVRPL(IdentificadorExclusaoVRPLRequest request, StreamObserver<ExclusaoVrplResponse> responseObserver) {
		//validar parâmetros
		if (request == null) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador da Proposta' inválido: " + request)));
		}

		if (request.getNumeroProposta() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador da Proposta' inválido: " + request.getNumeroProposta())));
		}

		if (request.getAnoProposta() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador do ano da Proposta' inválido: " + request.getAnoProposta())));
		}

		if (request.getUsuarioLogado().isEmpty()) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(
					"Parâmetro 'Identificador do usuário logado' inválido: " + request.getUsuarioLogado())));

		}

		//verificar exclusão VRPL
		VerificarExclusaoVrplDTO situacaoVrpl = this.verificarExclusaoVRPL(request.getNumeroProposta(), request.getAnoProposta());

		//Preparar resposta
		br.gov.serpro.vrpl.grpc.ExclusaoVrplResponse.Builder response = ExclusaoVrplResponse.newBuilder();

		if (situacaoVrpl != null) { 
			if (situacaoVrpl.getSituacao().equals(VerificarExclusaoVrplEnum.PROPOSTA_NAO_EXISTE.getCodigo()))  {
				//proposta não existe
				response.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_NAO_EXISTE.getCodigo());
				response.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_NAO_EXISTE.getDescricao());
			
			} else if (situacaoVrpl.getSituacao().equals(VerificarExclusaoVrplEnum.PROPOSTA_LOTES_VINCULADOS.getCodigo())) {
				//proposta possui lotes vinculados e não pode ser excluída
				response.setSituacao(situacaoVrpl.getSituacao());
				response.setMensagem(situacaoVrpl.getMensagem());
			} else if ( (situacaoVrpl.getSituacao().equals(VerificarExclusaoVrplEnum.PROPOSTA_PODE_SER_EXCLUIDA.getCodigo()) ) ||
						(situacaoVrpl.getSituacao().equals(VerificarExclusaoVrplEnum.PROPOSTA_VERSIONADA.getCodigo()) ) ) {
				
						//proposta existe e será excluída
				this.excluirDadosVRPL(request.getNumeroProposta(), request.getAnoProposta(), request.getUsuarioLogado());
			
				response.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_EXCLUIDA.getCodigo());
				response.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_EXCLUIDA.getDescricao());

			}
		} else {
			responseObserver.onError(new StatusRuntimeException(
						Status.FAILED_PRECONDITION.withDescription(
								"Erro ao consultar proposta: " + request.getNumeroProposta() + "/" + request.getAnoProposta())));
		}
	
		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	private void excluirDadosVRPL(long numeroProposta, long anoProposta, String usuarioLogado) {		
		
		vrplBC.excluirDadosVRPL(numeroProposta, anoProposta, usuarioLogado);

	}

}
