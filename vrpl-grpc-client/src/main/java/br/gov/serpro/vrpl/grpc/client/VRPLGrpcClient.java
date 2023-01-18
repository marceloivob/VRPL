package br.gov.serpro.vrpl.grpc.client;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.gov.serpro.siconv.med.grpc.HealthCheckRequest;
import br.gov.serpro.siconv.med.grpc.HealthCheckResponse;
import br.gov.serpro.siconv.med.grpc.HealthGrpc;
import br.gov.serpro.siconv.med.grpc.HealthGrpc.HealthBlockingStub;
import br.gov.serpro.vrpl.grpc.DESubmetaVrplPARASubmetaProjetoBasico;
import br.gov.serpro.vrpl.grpc.ExclusaoVrplResponse;
import br.gov.serpro.vrpl.grpc.IdPropostaRequest;
import br.gov.serpro.vrpl.grpc.IdentificadorExclusaoVRPLRequest;
import br.gov.serpro.vrpl.grpc.IdentificadorPropostaRequest;
import br.gov.serpro.vrpl.grpc.ListaSubmetaRequest;
import br.gov.serpro.vrpl.grpc.ListaSubmetaResponse;
import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse;
import br.gov.serpro.vrpl.grpc.PropostaGrpc;
import br.gov.serpro.vrpl.grpc.PropostaGrpc.PropostaBlockingStub;
import br.gov.serpro.vrpl.grpc.PropostaLotesResponse;
import br.gov.serpro.vrpl.grpc.PropostaRequest;
import br.gov.serpro.vrpl.grpc.SubmetaRequest;
import br.gov.serpro.vrpl.grpc.SubmetaResponse;
import br.gov.serpro.vrpl.grpc.SubmetaServiceGrpc;
import br.gov.serpro.vrpl.grpc.SubmetaServiceGrpc.SubmetaServiceBlockingStub;
import br.gov.serpro.vrpl.grpc.VrplGrpc.VrplBlockingStub;
import br.gov.serpro.vrpl.grpc.VrplAceitaResponse;
import br.gov.serpro.vrpl.grpc.VrplGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VRPLGrpcClient {

	
	
	private static final Integer MAX_MSG_SIZE = 20_971_520; // 20MB

	private static final Boolean useSSLGRPC = Boolean.valueOf(System.getProperty("useSSLGRPC", "false"));

	public static final String HOST = System.getProperty("host", "localhost");
		
	public static final int PORT = Integer.parseInt(System.getProperty("MEDICAO_GRPC_PORT", "8001"));
	
	private final ManagedChannel channel;
	private final PropostaBlockingStub blockingPropostaBlockingStub;
	private final SubmetaServiceBlockingStub blockingSubmetaBlockingStub;
	private final HealthBlockingStub blockingHealthCheckStub;
	private final VrplBlockingStub blockingVrplStub;

	public VRPLGrpcClient(String host, int port, boolean useSSL) {

		this(useSSL ? ManagedChannelBuilder.forAddress(host, port).useTransportSecurity().build()
				: ManagedChannelBuilder.forAddress(host, port).usePlaintext().maxInboundMessageSize(MAX_MSG_SIZE)
						.build());

		log.info("[VRPL GRPC Client]: **Channel criado com sucesso na porta {} e no host {}, com SSL {}", port, host,
				useSSL);

	}
	
	/**
	 * Como chamar o gRPC a partir do Estaleiro (kubernets)
	 * 
	 * https://cloud.google.com/blog/topics/developers-practitioners/health-checking-your-grpc-servers-gke
	 * 
	 * Liveness Health Check Service
	 * 
	 * @return
	 */
	public HealthCheckResponse liveness() {
		
		HealthCheckRequest request = HealthCheckRequest.newBuilder().build();
		
		HealthCheckResponse healthCheck = blockingHealthCheckStub.check(request);

		return healthCheck;		
		
	}	

	/**
	 * Readiness Health Check Service
	 * 
	 * @return
	 */
	public HealthCheckResponse readiness() {
		
		HealthCheckRequest request = HealthCheckRequest.newBuilder().build();
		
		HealthCheckResponse healthCheck = blockingHealthCheckStub.ready(request);

		return healthCheck;
		
	}	

	public VRPLGrpcClient(String host, int port) {

		this(useSSLGRPC ? ManagedChannelBuilder.forAddress(host, port).useTransportSecurity().build()
				: ManagedChannelBuilder.forAddress(host, port).maxInboundMessageSize(MAX_MSG_SIZE).build());

		log.info("[VRPL GRPC Client]: **Channel criado com sucesso na porta {} e no host {}, com SSL {}", port, host,
				useSSLGRPC);

	}

	VRPLGrpcClient(ManagedChannel channel) {
		this.channel = channel;
		this.blockingPropostaBlockingStub = PropostaGrpc.newBlockingStub(channel);
		this.blockingSubmetaBlockingStub = SubmetaServiceGrpc.newBlockingStub(channel);
		this.blockingHealthCheckStub = HealthGrpc.newBlockingStub(channel);
		this.blockingVrplStub = VrplGrpc.newBlockingStub(channel);
	}

	public PermissoesDasLicitacoesResponse consultarPermissoesDasLicitacoesDaProposta(Long idProposta) {
		if (idProposta == null) {
			throw new IllegalArgumentException("Parâmetro 'Identificador do Instrumento' inválido: " + idProposta);
		}

		PropostaRequest propostaRequest = PropostaRequest.newBuilder().setIdProposta(idProposta).build();

		return this.blockingPropostaBlockingStub.consultarPermissoesDasLicitacoesDaProposta(propostaRequest);

	}

	public ListaSubmetaResponse consultarListaSubmetas(List<Long> ids) {

		if (ids == null || ids.isEmpty()) {
			throw new IllegalArgumentException("Parâmetro 'Identificador das Submetas' inválido: " + ids);
		}

		ListaSubmetaRequest request = ListaSubmetaRequest.newBuilder().addAllIds(ids).build();
		return this.blockingSubmetaBlockingStub.consultarListaSubmetas(request);
	}

	public SubmetaResponse consultarSubmetaPorId(Long id) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("Parâmetro 'Identificador da Submeta' inválido: " + id);
		}

		SubmetaRequest request = SubmetaRequest.newBuilder().setId(id).build();
		return this.blockingSubmetaBlockingStub.consultarSubmetaPorId(request);
	}

	public PropostaLotesResponse consultarListaLotesComSubmetas(Long idPropostaSiconv) {
		if (idPropostaSiconv == null || idPropostaSiconv <= 0) {
			throw new IllegalArgumentException(
					"Parâmetro 'Identificador da Proposta no Siconv' inválido: " + idPropostaSiconv);
		}

		IdPropostaRequest request = IdPropostaRequest.newBuilder().setIdProposta(idPropostaSiconv).build();
		return this.blockingSubmetaBlockingStub.consultarListaLotesComSubmetas(request);
	}

	public Map<Long, Long> recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(List<Long> idSubmetaVrpl) {
		if (idSubmetaVrpl == null || idSubmetaVrpl.isEmpty()) {
			throw new IllegalArgumentException(
					"Parâmetro 'Lista de IDs das Submetas do VRPL' inválido: " + idSubmetaVrpl);
		}

		ListaSubmetaRequest request = ListaSubmetaRequest.newBuilder().addAllIds(idSubmetaVrpl).build();

		DESubmetaVrplPARASubmetaProjetoBasico d = this.blockingSubmetaBlockingStub
				.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(request);

		return d.getSubmetasMap();
	}

	
	public Boolean existeVRPLAceita(Long idProposta) {
		if (idProposta == null) {
			throw new IllegalArgumentException(
					"Parâmetro inválido: " + idProposta);
		}

		PropostaRequest request = PropostaRequest.newBuilder().setIdProposta(idProposta).build();

		VrplAceitaResponse response = this.blockingPropostaBlockingStub
				.existeVrplAceita(request);

		return response.getVrplAceita();
	}

	public ExclusaoVrplResponse verificarExclusaoVrpl(Long numeroProposta, Long anoProposta) {
		if (numeroProposta == null || numeroProposta <= 0) {
			throw new IllegalArgumentException(
					"Parâmetro 'Número da proposta' inválido: " + numeroProposta);
		}

		if (anoProposta == null || anoProposta <= 0) {
			throw new IllegalArgumentException(
					"Parâmetro 'Ano da proposta' inválido: " + anoProposta);
		}

		IdentificadorPropostaRequest request = IdentificadorPropostaRequest.newBuilder().setNumeroProposta(numeroProposta).setAnoProposta(anoProposta).build();
		return this.blockingVrplStub.verificarExclusaoVrpl(request);
	}

	public ExclusaoVrplResponse excluirVRPL(Long numeroProposta, Long anoProposta, String usuarioLogado) {
		IdentificadorExclusaoVRPLRequest request = IdentificadorExclusaoVRPLRequest.newBuilder().setNumeroProposta(numeroProposta).setAnoProposta(anoProposta).setUsuarioLogado(usuarioLogado).build();
		return this.blockingVrplStub.excluirVRPL(request);
	}
	
	
	/**
	 * Finaliza execução
	 * 
	 * @throws InterruptedException
	 * 
	 * @throws Exception
	 */
	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) throws Exception {
		String host = HOST;
		Integer porta = PORT;

		if (args.length > 0) {
			host = args[0];
			porta = Integer.parseInt(args[1]);
		}

		VRPLGrpcClient client = null;
		
		if (host != null && porta != null) {

			client = new VRPLGrpcClient(host, porta, false);
			
			//testarExisteVrplAceita(client);
			
			//testarHealthCheck(client);

//			testarConsultaListaLoteComSubmetas (client);
			
			
			
			SubmetaResponse resp = client.consultarSubmetaPorId(573L);
			
			System.out.println(resp.getSubmeta());
			
			
			
//			Optional<ContratoDTO> contrato = client.getContratoPorContratoFk(16);
//			
//			if (contrato.isPresent()) {
//				log.info("Configuracao iniciada: {} e Medicao iniciada: {} ", contrato.get().isConfiguracaoIniciada(),contrato.get().isMedicaoIniciada());
//			} else {
//				log.info("Contrato não encontrado.");
//			}

			//testarConsultaListaLoteComSubmetas (client);

			//testarVerificarExclusao(client);

			//testarExcluirVRPL(client);			
		}
	}

	private static void testarExcluirVRPL(VRPLGrpcClient client) {

		//ExclusaoVrplResponse response = client.excluirVRPL(null, null, null);
		ExclusaoVrplResponse response = client.excluirVRPL(1551L, 2019L, "");

		System.out.println(response.getSituacao()+" - "+
							response.getMensagem());

	}

	private static void testarVerificarExclusao(VRPLGrpcClient client) {

		ExclusaoVrplResponse response = client.verificarExclusaoVrpl(43115L, 2018L);

		System.out.println(response.getSituacao()+" - "+
							response.getMensagem());

	}

	private static void testarConsultaListaLoteComSubmetas(VRPLGrpcClient client) {
		
//		PropostaLotesResponse response = client.consultarListaLotesComSubmetas (1331812L);
		PropostaLotesResponse response = client.consultarListaLotesComSubmetas (1471235L);
		
		
		
		System.out.println("Nome do Objeto:" +response.getPropostaLote().getNomeObjeto());
		System.out.println("Modalidade:" +response.getPropostaLote().getModalidade());
		System.out.println("Possui Inst Mandataria:" +response.getPropostaLote().getPossuiInstituicaoMandataria());
		System.out.println("Nome Proponente:" +response.getPropostaLote().getNomeProponente());
		
		response.getPropostaLote().getLotesList().forEach(lote -> 
					{
						System.out.println("Lote: "+lote.getNumero());
						
						lote.getSubmetasList().
							forEach(sub -> System.out.println(
											" ID: "+ sub.getId() +
											" - Descrição:"+sub.getDescricao()+
											" - Valor: "+sub.getValorTotal()+
											" - Regime de Execução: "+sub.getRegimeExecucao()+
											" - Situação: "+sub.getSituacao()
										));
					}
		);
	}

	private static void testarHealthCheck(VRPLGrpcClient client) {
		System.out.println(client.liveness());

		System.out.println(client.readiness());
	}

	private static void testarExisteVrplAceita(VRPLGrpcClient client) {
		//existe
		System.out.println(client.existeVRPLAceita(1346156L));
		
		//existe aceita
		System.out.println(client.existeVRPLAceita(1332025L));
		
		//Não existe
		try {
			System.out.println(client.existeVRPLAceita(1332020L));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Arg Invalido
		try {
			System.out.println(client.existeVRPLAceita(0L));
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Arg Invalido
		try {
			System.out.println(client.existeVRPLAceita(-3L));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
