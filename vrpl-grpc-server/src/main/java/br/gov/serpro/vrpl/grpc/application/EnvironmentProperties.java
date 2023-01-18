package br.gov.serpro.vrpl.grpc.application;

import java.util.Objects;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

public class EnvironmentProperties {

	private Config config;

	private final Integer maxPoolSizeDefaultValue = 2;
	private final Integer grpcPortDefaultValue = 8001;
	private final Integer httpPortDefaultValue = 8002;

	private String databaseUrl;
	private String databaseUser;
	private String databasePassword;
	private Integer databaseMaxPoolSize = maxPoolSizeDefaultValue;
	private Integer grpcPort;
	private Integer httpPort;

	public EnvironmentProperties() {
		final ConfigBuilder configBuilder = ConfigProviderResolver //
				.instance() //
				.getBuilder() //
				.addDefaultSources() //
				.addDiscoveredConverters() //
				.addDiscoveredSources();

		this.config = configBuilder.build();

		loadValues();
	}

	private void loadValues() {
		this.databaseUrl = config.getValue("DB_URL_CONNECTION_MANDATARIAS", String.class);
		this.databaseUser = config.getValue("DB_USER_MANDATARIAS", String.class);
		this.databasePassword = config.getValue("DB_PASSWORD_MANDATARIAS", String.class);
		this.databaseMaxPoolSize = config.getOptionalValue("DB_MAX_POOL_SIZE", Integer.class)
				.orElse(maxPoolSizeDefaultValue);
		this.grpcPort = config.getOptionalValue("VRPL_GRPC_PORT", Integer.class).orElse(grpcPortDefaultValue);
		this.httpPort = config.getOptionalValue("VRPL_HTTP_PORT", Integer.class).orElse(httpPortDefaultValue);
	}

	public EnvironmentProperties(final String databaseUrl, final String databaseUser, final String databasePassword,
			final Integer databaseMaxPoolSize, final Integer grpcPort, final Integer httpPort) {
		Objects.requireNonNull(databaseUrl);
		Objects.requireNonNull(databaseUser);
		Objects.requireNonNull(databasePassword);
		Objects.requireNonNull(databaseMaxPoolSize);
		Objects.requireNonNull(grpcPort);
		Objects.requireNonNull(httpPort);

		this.databaseUrl = databaseUrl;
		this.databaseUser = databaseUser;
		this.databasePassword = databasePassword;
		this.databaseMaxPoolSize = databaseMaxPoolSize;
		this.grpcPort = grpcPort;
		this.httpPort = httpPort;
	}

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public String getDatabaseUser() {
		return databaseUser;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public Integer getDatabaseMaxPoolSize() {
		return databaseMaxPoolSize;
	}

	public Integer getGrpcPort() {
		return grpcPort;
	}

	public Integer getHttpPort() {
		return httpPort;
	}

	@Override
	public String toString() {
		return "Configuration [url=" + databaseUrl + ", user=" + databaseUser + ", password="
				+ (databasePassword == null ? null : "*********") + ", maxPoolSize=" + databaseMaxPoolSize
				+ ", grpcPort = " + grpcPort + ", httpPort = " + httpPort + "]";
	}

}
