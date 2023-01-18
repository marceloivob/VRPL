package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph;

import static com.amazonaws.services.s3.internal.Constants.MB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class CephConfig {

	@NotNull
	private String endpointUrl = System.getProperty("integrations.PRIVATE.CEPH.endpoint");

	@NotNull
	private Boolean ssl = Boolean.getBoolean("integrations.PRIVATE.CEPH.useSSL");

	@NotNull
	private String region = System.getProperty("integrations.PRIVATE.CEPH.region");

	@NotNull
	private String bucketName = this.getBucketName(System.getProperty("integrations.PRIVATE.CEPH.bucket"));

	@NotNull
	private String secretKey = System.getProperty("integrations.PRIVATE.CEPH.secretKey");

	@NotNull
	private String accessToken = System.getProperty("integrations.PRIVATE.CEPH.accessToken");

	@NotNull
	private String maxFileSizeInMB = System.getProperty("integrations.PRIVATE.CEPH.maxFileSizeInMB");

	@Inject
	private Validator beanValidator;

	public void isValid() {
		Set<ConstraintViolation<CephConfig>> violacoes = beanValidator.validate(this);

		if (violacoes.isEmpty()) {
			log.debug("Configurações da classe {} carregadas com sucesso.", this.getClass().getSimpleName());
			return;
		}

		List<String> valoresInvalidos = new ArrayList<>();

		violacoes.forEach(violacao -> {
			String propriedade = violacao.getPropertyPath().toString();
			String mensagem = violacao.getMessage();

			valoresInvalidos.add(propriedade + " - " + mensagem);
		});

		throw new IllegalArgumentException("Configurações do CEPH inválidas: " + valoresInvalidos.toString());

	}

	// em bytes
	public Integer getMaxFileSize() {
		Integer fileSize = Integer.valueOf(maxFileSizeInMB);

		return fileSize * MB;
	}

	public void parse(Map<String, String> properties) {
		this.endpointUrl = properties.get("integrations.PRIVATE.CEPH.endpoint");

		if (properties.get("integrations.PRIVATE.CEPH.useSSL") != null) {
			this.ssl = Boolean.parseBoolean(properties.get("integrations.PRIVATE.CEPH.useSSL"));
		}

		this.region = properties.get("integrations.PRIVATE.CEPH.region");
		this.bucketName = this.getBucketName(properties.get("integrations.PRIVATE.CEPH.bucket"));
		this.secretKey = properties.get("integrations.PRIVATE.CEPH.secretKey");
		this.accessToken = properties.get("integrations.PRIVATE.CEPH.accessToken");
		this.maxFileSizeInMB = properties.get("integrations.PRIVATE.CEPH.maxFileSizeInMB");

	}

	public String getBucketName(String seed) {

		int ano = LocalDate.now().getYear();
		int mes = LocalDate.now().getMonthValue();

		String bucket = String.format("%s-%02d-%d", seed, mes, ano);

		return bucket;

	}
}
