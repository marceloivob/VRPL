package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SiconvRestImpl implements SiconvRest {

	private static final int UNPROCESSABLE_ENTITY = 422;

	@Inject
	private BasicAuthenticatorFilter basicAuthenticatorFilter;

	@Inject
	private URLSiconvRestGenerator urlSiconvRestGenerator;

	@Inject
	private Client client;

	/**
	 * Construtor padrão para o CDI
	 */
	public SiconvRestImpl() {
		// noop
	}

	/**
	 * Construtor usado pelo Producer e Testes Unitários
	 */
	public SiconvRestImpl(Client client, BasicAuthenticatorFilter basicAuthenticatorFilter,
			URLSiconvRestGenerator urlSiconvRestGenerator) {
		this.client = client;
		this.basicAuthenticatorFilter = basicAuthenticatorFilter;
		this.urlSiconvRestGenerator = urlSiconvRestGenerator;
	}

	@Override
	public ContratoDocLiquidacaoResponse consultarContratoDocLiquidacaoVinculadoProcessoExecucao(Long licitacao,
			SiconvPrincipal usuarioLogado) {
		Objects.requireNonNull(licitacao);
		Objects.requireNonNull(usuarioLogado);
		Objects.requireNonNull(usuarioLogado.getCpf());

		final String url = urlSiconvRestGenerator.getEndpointConsultarContrato(licitacao);

		try (Response response = client //
				.register(basicAuthenticatorFilter)//
				.register(ContratoDocLiquidacaoResponseMessageBodyReaderProvider.class) //
				.target(url) //
				.request() //
				.accept(MediaType.APPLICATION_JSON) //
				.acceptLanguage("pt-BR")//
				.get()) {

			if (respostaComSucesso(response)) {
				ContratoDocLiquidacaoResponse contratoDocLiquidacaoResponse = response
						.readEntity(ContratoDocLiquidacaoResponse.class);

				return contratoDocLiquidacaoResponse;
			} else {
				Map<String, Object> parametros = new HashMap<>();
				parametros.put("licitacao", licitacao);
				parametros.put("usuario", usuarioLogado.getCpf());

				this.processarResposta(parametros, response, "CONTRATO_DOC_LIQUIDACAO");

				return null;
			}
		} catch(Exception e ) {

			throw new AcionarServicoVerificarExistenciaDocumentoLiquidacao(e);

		}
	}

	@Override
	public void enviarProcessoExecucaoParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {

		final String url = urlSiconvRestGenerator.getEndpointEnviarAceite();
		EnviarParaAnaliseIntegracao enviarAceiteIntegracao = new EnviarParaAnaliseIntegracao(licitacao, justificativa);

		try (Response response = client //
				.register(basicAuthenticatorFilter)//
				.register(ContratoDocLiquidacaoResponseMessageBodyReaderProvider.class) //
				.target(url) //
				.request() //
				.accept(MediaType.TEXT_PLAIN) //
				.acceptLanguage("pt-BR")//
				.post(Entity.json(enviarAceiteIntegracao))) {

			processarResposta(enviarAceiteIntegracao, response, "ENVIAR_ACEITE");

		}
	}

	@Override
	public void aceitarRejeitarProcessoExecucao(Long licitacao, EventoAceiteRejeicao evento,
			String atribuicaoResponsavel, Date dataAnalise, String justificativa, SiconvPrincipal usuarioLogado) {

		final String url = urlSiconvRestGenerator.getEndpointAceitarRejeitar();

		AceitarRejeitarIntegracao aceitarRejeitarIntegracao = new AceitarRejeitarIntegracao(licitacao, evento,
				atribuicaoResponsavel, dataAnalise, justificativa);

		log.info("Integração com SICONV REST AceitarRejeitarProcessoExecucao com os parâmetros: {}",
				Entity.json(aceitarRejeitarIntegracao));

		try (Response response = client //
				.register(basicAuthenticatorFilter)//
				.register(ContratoDocLiquidacaoResponseMessageBodyReaderProvider.class) //
				.target(url) //
				.request() //
				.accept(MediaType.TEXT_PLAIN) //
				.acceptLanguage("pt-BR")//
				.post(Entity.json(aceitarRejeitarIntegracao))) {

			processarResposta(aceitarRejeitarIntegracao, response, "ACEITAR");
		}
	}

	@Override
	public void estornarProcessoExecucao(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		final String url = urlSiconvRestGenerator.getEndpointEstornar();

		EstornarIntegracao estornarIntegracao = new EstornarIntegracao(licitacao, justificativa);

		try (Response response = client //
				.register(basicAuthenticatorFilter)//
				.register(ContratoDocLiquidacaoResponseMessageBodyReaderProvider.class) //
				.target(url) //
				.request() //
				.accept(MediaType.TEXT_PLAIN) //
				.acceptLanguage("pt-BR")//
				.post(Entity.json(estornarIntegracao))) {


			processarResposta(estornarIntegracao, response, "ESTORNAR");
		}
	}
	
	@Override
	public void estornarEnvioParaAceite(Long licitacao, String justificativa, SiconvPrincipal usuarioLogado) {
		final String url = urlSiconvRestGenerator.getEndpointEstornarEnvioParaAceite();

		EstornarEnvioParaAceite estornarEnvioParaAceite = new EstornarEnvioParaAceite(licitacao, justificativa);

		try (Response response = client //
				.register(basicAuthenticatorFilter)//
				.register(ContratoDocLiquidacaoResponseMessageBodyReaderProvider.class) //
				.target(url) //
				.request() //
				.accept(MediaType.TEXT_PLAIN) //
				.acceptLanguage("pt-BR")//
				.post(Entity.json(estornarEnvioParaAceite))) {


			processarResposta(estornarEnvioParaAceite, response, "ESTORNAR_ENVIO_ACEITE");
		}
	}

	////////////////////////////////////////////////////////////////////////
	// Métodos Auxiliares
	////////////////////////////////////////////////////////////////////////
	private void processarResposta(Object param, Response response, String metodoOrigem) {
		if (respostaComSucesso(response)) {
			registraRetornoDoServicoNoLog(metodoOrigem, param, response);
		} else {
			throw new AcionarServicoModuloSiconvException(metodoOrigem, param, response);
		}
	}

	protected boolean respostaComSucesso(Response response) {
		return response.getStatus() == Status.OK.getStatusCode();
	}

	private void registraRetornoDoServicoNoLog(String servico, Object parametros, Response resposta) {
		log.info("Serviço: {}, com os parâmetros {}, teve como resposta: {}", servico, parametros.toString(),
				resposta.getEntity());
	}

	public void setUrlSiconvRestGenerator(URLSiconvRestGenerator urlSiconvRestGenerator) {
		this.urlSiconvRestGenerator = urlSiconvRestGenerator;
	}

	public void setBasicAuthenticatorFilter(BasicAuthenticatorFilter basicAuthenticatorFilter) {
		this.basicAuthenticatorFilter = basicAuthenticatorFilter;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
