package br.gov.planejamento.siconv.mandatarias.test.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.Stage;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph.CephConfig;

public class ProjectConfiguration {

	private Boolean useDatabaseInMemory = false;

	private Liquibase liquibase = new Liquibase();

	private DatabaseConfig databaseConfig = new DatabaseConfig();

	private CephConfig cephConfig = new CephConfig();

	private Stage stage;

	private DataSource datasource;

	private String fileConfigPath;

	public ProjectConfiguration() {
		Map<String, String> properties = loadConfigurationFile();

		parseProperties(properties);
	}

	private void parseProperties(Map<String, String> properties) {
		this.stage = Stage.fromSystemStage(properties.getOrDefault("thorntail.project.stage", "INTEGRATION_TEST"));
		this.useDatabaseInMemory = Boolean.parseBoolean(properties.getOrDefault("useDatabaseInMemory", "false"));

		liquibase.parse(properties);
		databaseConfig.parse(properties);
		cephConfig.parse(properties);
	}

	private Map<String, String> loadConfigurationFile() {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

		String configFile = "project-defaults.yml";

		ClassLoader classLoader = getClass().getClassLoader();
		File projectDefaults = new File(classLoader.getResource(configFile).getFile());

		fileConfigPath = projectDefaults.getAbsolutePath();

		try {
			JsonNode json = mapper.readTree(projectDefaults);
			Map<String, String> map = new HashMap<String, String>();

			flatterYamlFile("", json, map);

			return map;
		} catch (IOException e) {
			throw new FileSystemNotFoundException(configFile);
		}

	}

	private void flatterYamlFile(String currentPath, JsonNode jsonNode, Map<String, String> map) {
		if (jsonNode.isObject()) {
			ObjectNode objectNode = (ObjectNode) jsonNode;
			Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
			String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

			while (iter.hasNext()) {
				Map.Entry<String, JsonNode> entry = iter.next();
				flatterYamlFile(pathPrefix + entry.getKey(), entry.getValue(), map);
			}
		} else if (jsonNode.isArray()) {
			ArrayNode arrayNode = (ArrayNode) jsonNode;
			for (int i = 0; i < arrayNode.size(); i++) {
				flatterYamlFile(currentPath + "[" + i + "]", arrayNode.get(i), map);
			}
		} else if (jsonNode.isValueNode()) {
			ValueNode valueNode = (ValueNode) jsonNode;
			map.put(currentPath, valueNode.asText());
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Boolean getUseDatabaseInMemory() {
		return useDatabaseInMemory;
	}

	public void setUseDatabaseInMemory(Boolean useDatabaseInMemory) {
		this.useDatabaseInMemory = useDatabaseInMemory;
	}

	public Boolean applyScriptsFromLiquibase() {
		return this.liquibase.getExecute();
	}

	public String getContext() {
		return this.liquibase.getContext();
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public String getFileConfigPath() {
		return fileConfigPath;
	}

	public void setFileConfigPath(String fileConfigPath) {
		this.fileConfigPath = fileConfigPath;
	}

	public CephConfig getCephConfig() {
		return cephConfig;
	}

	public void setCephConfig(CephConfig cephConfig) {
		this.cephConfig = cephConfig;
	}

	class Liquibase {

		private Boolean execute;
		private String context;

		public Boolean getExecute() {
			return execute;
		}

		public void parse(Map<String, String> properties) {
			this.execute = Boolean.parseBoolean(properties.getOrDefault("liquibase.shouldRun", "false"));
			this.context = properties.getOrDefault("liquibase.context", "");
		}

		public void setExecute(Boolean execute) {
			this.execute = execute;
		}

		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}
	}

	class DatabaseConfig {
		private String url;
		private String username;
		private String password;

		public void parse(Map<String, String> properties) {
			this.url = properties.get("thorntail.datasources.data-sources.vrpl.connection-url");
			this.username = properties.get("thorntail.datasources.data-sources.vrpl.user-name");
			this.password = properties.get("thorntail.datasources.data-sources.vrpl.password");

			PGSimpleDataSource postgreSqlDataSource = new PGSimpleDataSource();

			postgreSqlDataSource.setUrl(url);
			postgreSqlDataSource.setUser(username);
			postgreSqlDataSource.setPassword(password);

			setDatasource(postgreSqlDataSource);
		}
	}

}
