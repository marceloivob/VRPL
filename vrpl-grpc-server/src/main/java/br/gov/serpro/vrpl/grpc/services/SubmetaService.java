package br.gov.serpro.vrpl.grpc.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import br.gov.serpro.vrpl.grpc.DESubmetaVrplPARASubmetaProjetoBasico;
import br.gov.serpro.vrpl.grpc.IdPropostaRequest;
import br.gov.serpro.vrpl.grpc.ListaSubmetaRequest;
import br.gov.serpro.vrpl.grpc.ListaSubmetaResponse;
import br.gov.serpro.vrpl.grpc.ListaSubmetaResponse.Builder;
import br.gov.serpro.vrpl.grpc.PropostaLote;
import br.gov.serpro.vrpl.grpc.PropostaLotesResponse;
import br.gov.serpro.vrpl.grpc.Servico;
import br.gov.serpro.vrpl.grpc.Submeta.FrenteObra;
import br.gov.serpro.vrpl.grpc.Submeta.FrenteObra.Evento;
import br.gov.serpro.vrpl.grpc.Submeta.FrenteObra.MacroServico;
import br.gov.serpro.vrpl.grpc.SubmetaRequest;
import br.gov.serpro.vrpl.grpc.SubmetaResponse;
import br.gov.serpro.vrpl.grpc.SubmetaServiceGrpc.SubmetaServiceImplBase;
import br.gov.serpro.vrpl.grpc.business.PropostaBC;
import br.gov.serpro.vrpl.grpc.database.bean.VrplAceitaDTO;
import br.gov.serpro.vrpl.grpc.submeta.Submeta;
import br.gov.serpro.vrpl.grpc.submeta.SubmetaRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubmetaService extends SubmetaServiceImplBase {

	private SubmetaRepository submetaRepository;
	private PropostaBC propostaBC = new PropostaBC();

	public SubmetaService(SubmetaRepository submetaRepository) {
		this.submetaRepository = submetaRepository;
	}

	@Override
	public void consultarListaSubmetas(ListaSubmetaRequest request,
			StreamObserver<ListaSubmetaResponse> responseObserver) {
		if (request == null) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador das Submetas' inválido: " + request)));
			return;
		}

		if (request.getIdsList().isEmpty()) {
			responseObserver.onError(new StatusRuntimeException(
					Status.INVALID_ARGUMENT.withDescription("Parâmetro 'Identificador das Submetas' inválido")));
			return;
		}

		List<Submeta> submetas = new ArrayList<>();
		
		List<Boolean> indicaListaAcompEvento  = submetaRepository.getListaIndicadorAcompEvento(request.getIdsList());
		
		if(indicaListaAcompEvento.isEmpty()) {
			responseObserver.onError(new StatusRuntimeException(
					Status.INVALID_ARGUMENT.withDescription("Submetas não encontradas: " + request.getIdsList())));
			return;
		}
		
		if(indicaListaAcompEvento.size() > 1 ) {
			responseObserver.onError(new StatusRuntimeException(
					Status.FAILED_PRECONDITION.withDescription("Parâmetro 'Lista de Submetas' inválido. Lista precisa conter apenas submetas com um tipo de acompanhamento "
							                                 + "('Acompanhado por Evento' ou 'Não Acompanhado por Evento'")));
		} else {
			if (indicaListaAcompEvento.get(0)) {
				submetas = submetaRepository.getListaSubmetas(request.getIdsList());				
			} else {
				submetas = submetaRepository.getServicoListaSubmetas(request.getIdsList());
			}
		}

		if ((submetas == null) || (submetas.isEmpty())) {
			responseObserver.onError(new StatusRuntimeException(
					Status.NOT_FOUND.withDescription("Submetas não encontradas: " + request.getIdsList())));
		} else {
			ListaSubmetaResponse response = this.converteListaSubmetas(submetas);
			responseObserver.onNext(response);
			responseObserver.onCompleted();
		}

	}

	private ListaSubmetaResponse converteListaSubmetas(List<Submeta> submetas) {
		Builder builder = ListaSubmetaResponse.newBuilder();

		for (Submeta submeta : submetas) {

			br.gov.serpro.vrpl.grpc.Submeta.Builder sub = br.gov.serpro.vrpl.grpc.Submeta.newBuilder();
			sub.setId(submeta.getId());
			sub.setDescricao(submeta.getDescricao());
			sub.setNumero(submeta.getNumero());
			sub.setValorLicitado(submeta.getValorLicitado().toString());

			submeta.getFrentesObras().stream().forEach(fo -> {
				FrenteObra.Builder frenteObra = FrenteObra.newBuilder();
				frenteObra.setId(fo.getId());
				frenteObra.setDescricao(fo.getDescricao());

				fo.getEventos().stream().forEach(ev -> {
					Evento.Builder evento = Evento.newBuilder();
					evento.setId(ev.getId());
					evento.setDescricao(ev.getDescricao());
					
					BigDecimal valorTotal = ev.getServicos().stream().map(
							serv -> serv.getPreco().multiply(serv.getQtdeItens()).setScale(2, RoundingMode.HALF_UP))
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					
					evento.setValorTotal(valorTotal.toString());
					
					frenteObra.addEventos(evento);
				});
				
				fo.getMacroServicos().stream().forEach(macro -> {
					MacroServico.Builder macroServico = MacroServico.newBuilder();
					macroServico.setId(macro.getId());
					macroServico.setNumero(macro.getNumero());
					macroServico.setDescricao(macro.getDescricao());
					
					macro.getServicos().stream().forEach(serv->{
						Servico.Builder servico = Servico.newBuilder();
						servico.setId(serv.getId());
						servico.setNumero(serv.getNumero());
						servico.setDescricao(serv.getDescricao());
						servico.setSgUnidade(serv.getSgUnidade());
						servico.setValorUnitario(serv.getPreco().toString());
						servico.setQtdeItens(serv.getQtdeItens().toString());	
						
						macroServico.addServicos(servico);
					});

					frenteObra.addMacroServico(macroServico);
				});				

				sub.addFrentesObras(frenteObra);
			});

			builder.addSubmeta(sub);
		}

		return builder.build();
	}

	@Override
	public void consultarSubmetaPorId(SubmetaRequest request, StreamObserver<SubmetaResponse> responseObserver) {

		if (request == null) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador da Submeta' inválido: " + request)));
			return;
		}

		if (request.getId() <= 0) {
			responseObserver.onError(new StatusRuntimeException(Status.INVALID_ARGUMENT
					.withDescription("Parâmetro 'Identificador da Submeta' inválido: " + request.getId())));
			return;
		}
		
		Boolean acompanhadoEvento = submetaRepository.isContratoAcompEvento(request.getId());
		
		if (acompanhadoEvento != null) {
			Submeta submeta;
			
			List<Long> argList = new ArrayList<>();
			argList.add(request.getId());
			if(acompanhadoEvento.booleanValue()) {
				submeta = submetaRepository.getListaSubmetas(argList).get(0);
			} else { 
				submeta = submetaRepository.getServicoListaSubmetas(argList).get(0);
			}
	
			SubmetaResponse response = this.converteSubmeta(submeta);
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			
		} else {
			responseObserver.onError(new StatusRuntimeException(
					Status.NOT_FOUND.withDescription("Submeta não encontrada: " + request.getId())));
		}
		
	}

	private SubmetaResponse converteSubmeta(Submeta submeta) {
		SubmetaResponse.Builder builder = SubmetaResponse.newBuilder();

		br.gov.serpro.vrpl.grpc.Submeta.Builder sub = br.gov.serpro.vrpl.grpc.Submeta.newBuilder();
		sub.setId(submeta.getId());
		sub.setDescricao(submeta.getDescricao());
		sub.setNumero(submeta.getNumero());
		sub.setValorLicitado(submeta.getValorLicitado().toString());

		submeta.getFrentesObras().stream().forEach(fo -> {
			FrenteObra.Builder frenteObra = FrenteObra.newBuilder();
			frenteObra.setId(fo.getId());
			frenteObra.setDescricao(fo.getDescricao());

			fo.getEventos().stream().forEach(ev -> {
				Evento.Builder evento = Evento.newBuilder();
				evento.setId(ev.getId());
				evento.setDescricao(ev.getDescricao());

				BigDecimal valor = ev.getServicos().stream()
						.map(serv -> serv.getPreco().multiply(serv.getQtdeItens()).setScale(2, RoundingMode.HALF_UP))
						.reduce(BigDecimal.ZERO, BigDecimal::add);

				evento.setValorTotal(valor.toString());

				ev.getServicos().stream().forEach(serv -> {
					Servico.Builder servico = Servico.newBuilder();
					servico.setId(serv.getId());
					servico.setNumero(serv.getNumero());
					servico.setDescricao(serv.getDescricao());
					servico.setQtdeItens(serv.getQtdeItens().toString());
					servico.setSgUnidade(serv.getSgUnidade());

					evento.addServicos(servico);
				});

				frenteObra.addEventos(evento);
			});
			
			fo.getMacroServicos().stream().forEach(macro -> {
				MacroServico.Builder macroServico = MacroServico.newBuilder();
				macroServico.setId(macro.getId());
				macroServico.setNumero(macro.getNumero());
				macroServico.setDescricao(macro.getDescricao());
				
				macro.getServicos().stream().forEach(serv -> {
					Servico.Builder servico = Servico.newBuilder();
					servico.setId(serv.getId());
					servico.setNumero(serv.getNumero());
					servico.setDescricao(serv.getDescricao());
					servico.setQtdeItens(serv.getQtdeItens().toString());
					servico.setSgUnidade(serv.getSgUnidade());
					servico.setValorUnitario(serv.getPreco().toString());

					macroServico.addServicos(servico);
				});

				frenteObra.addMacroServico(macroServico);
			});		
			sub.addFrentesObras(frenteObra);
		});

		builder.setSubmeta(sub);

		return builder.build();
	}

	@Override
	public void consultarListaLotesComSubmetas(IdPropostaRequest request,
			StreamObserver<PropostaLotesResponse> responseObserver) {

		if (request.getIdProposta() <= 0) {
			responseObserver.onError(Status.INVALID_ARGUMENT
					.withDescription(
							"Parâmetro 'Identificador da Proposta no Siconv' inválido: " + request.getIdProposta())
					.asRuntimeException());
			return;
		}

		Optional<VrplAceitaDTO> vrplAceitaOpt = propostaBC.existeVrplAceita(request.getIdProposta());

		if (!vrplAceitaOpt.isPresent() || !vrplAceitaOpt.get().getExisteVrplAceita()) {
			responseObserver.onError(Status.NOT_FOUND.withDescription(
					"Ainda não houve um aceite do processo licitatório no módulo VRPL para o identificador da proposta: "
							+ request.getIdProposta())
					.asRuntimeException());
			return;
		}

		PropostaLote propostaLote = submetaRepository.getListaLotesComSubmetas(request.getIdProposta());

		if (propostaLote == null) {
			responseObserver.onError(Status.NOT_FOUND.withDescription(
					"Lotes/Submetas não encontrados para o identificador da proposta: " + request.getIdProposta())
					.asRuntimeException());
		} else {
			responseObserver.onNext(PropostaLotesResponse.newBuilder().setPropostaLote(propostaLote).build());
			responseObserver.onCompleted();
		}
	}

	@Override
	public void recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(ListaSubmetaRequest listaDeSubmetasAPesquisar,
			StreamObserver<DESubmetaVrplPARASubmetaProjetoBasico> responseObserver) {

		SubmetasDEPARAService submetas = new SubmetasDEPARAService(submetaRepository, listaDeSubmetasAPesquisar);

		try {
			Map<Long, Long> dePara = submetas.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL();

			DESubmetaVrplPARASubmetaProjetoBasico resultado = DESubmetaVrplPARASubmetaProjetoBasico.newBuilder()
					.putAllSubmetas(dePara).build();

			responseObserver.onNext(resultado);
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			responseObserver.onError(e);
		} finally {
			submetas = null;
		}

	}

}
