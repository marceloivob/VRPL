package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.util.Objects;

import javax.inject.Inject;

public class URLSiconvRestGenerator {

	@Inject
	private SiconvRestConfig siconvRestConfig;

	/**
	 * Construtor Padrão para o CDI
	 */
	public URLSiconvRestGenerator() {
		// noop
	}

	/**
	 * Construtor usado para os Testes Unitários
	 */
	public URLSiconvRestGenerator(SiconvRestConfig siconvRestConfig) {
		Objects.requireNonNull(siconvRestConfig);

		this.siconvRestConfig = siconvRestConfig;
	}

	public String getEndpointConsultarContrato(Long licitacao) {
		Objects.requireNonNull(licitacao);

		final String urlBase = this.siconvRestConfig.getEndpoint();
        final String queryParam = "/api/licitacao/contratodocliquidacao?licitacao=";
		final String urlTarget = urlBase.concat(queryParam).concat(licitacao.toString());

		return urlTarget;
	}

	public String getEndpointEnviarAceite() {
		final String urlBase = this.siconvRestConfig.getEndpoint();
        final String queryParam = "/api/licitacao/enviaraceite";
		final String urlTarget = urlBase.concat(queryParam);

		return urlTarget;
	}

	public String getEndpointAceitarRejeitar() {
		final String urlBase = this.siconvRestConfig.getEndpoint();
        final String queryParam = "/api/licitacao/aceitarrejeitar";
		final String urlTarget = urlBase.concat(queryParam);

		return urlTarget;
	}

	public String getEndpointEstornar() {
		final String urlBase = this.siconvRestConfig.getEndpoint();
        final String queryParam = "/api/licitacao/estornar";
		final String urlTarget = urlBase.concat(queryParam);

		return urlTarget;
	}
	
	public String getEndpointEstornarEnvioParaAceite() {
		final String urlBase = this.siconvRestConfig.getEndpoint();
        final String queryParam = "/api/licitacao/estornarEnvioParaAceite";
		final String urlTarget = urlBase.concat(queryParam);

		return urlTarget;
	}


}
