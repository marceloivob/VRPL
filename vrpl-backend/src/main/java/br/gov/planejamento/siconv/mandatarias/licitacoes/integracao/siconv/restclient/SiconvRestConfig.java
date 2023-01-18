package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class SiconvRestConfig {

	@NotNull
	@Inject
	@Getter
	@ConfigProperty(name = "integrations.PRIVATE.REST.SICONV.endpoint")
	private String endpoint;

	@NotNull
	@Inject
	@ConfigProperty(name = "integrations.PRIVATE.REST.SICONV.secretKey")
	@Getter
	private String secretKey;

	@Inject
	@ConfigProperty(name = "SSL_TRUSTSTORE_PASS")
	@Getter
	private String trustStorePass;

	@Inject
	@ConfigProperty(name = "SSL_TRUSTSTORE_PATH")
	@Getter
	private String trustStorePath;

	@Inject
	@ConfigProperty(name = "SSL_KEYSTORE_PASS")
	@Getter
	private String keyStorePass;

	@Inject
	@ConfigProperty(name = "SSL_KEYSTORE_PATH")
	@Getter
	private String keyStorePath;

	@Inject
	private Validator beanValidator;

	public void isValid() {
		Set<ConstraintViolation<SiconvRestConfig>> violacoes = beanValidator.validate(this);

		if (violacoes.isEmpty()) {
			log.debug("Configurações da classe {} carregadas com sucesso.", this.getClass().getSimpleName());
            log.debug("Endpoint definido: '{}'", this.endpoint);

			return;
		}

		List<String> valoresInvalidos = new ArrayList<>();

		violacoes.forEach(violacao -> {
			String propriedade = violacao.getPropertyPath().toString();
			String mensagem = violacao.getMessage();

			valoresInvalidos.add(propriedade + " - " + mensagem);
		});

		throw new IllegalArgumentException(
				"Configurações da Integração com o SICONV Rest inválidas: " + valoresInvalidos.toString());
	}

}
