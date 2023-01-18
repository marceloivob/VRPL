package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultReaderConfig;
import io.swagger.jaxrs.config.SwaggerContextService;

@ApplicationPath("/")
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        CustomBeanConfig conf = new CustomBeanConfig();
        conf.setTitle("Mandatárias VRPL API");
        conf.setDescription("Verificação do Processo de Licitação do Mandatárias");
		conf.setVersion("1.0.0");
        conf.setHost("localhost:8080");
        conf.setBasePath("/");
        conf.setSchemes(new String[] { "http" });
        conf.setResourcePackage("br.gov.planejamento.siconv.mandatarias.licitacoes");
        conf.setScanAllResources();
        conf.setScan(true);

    }

    private class CustomBeanConfig extends BeanConfig {

        @Override
        public void setScan(boolean shouldScan) {
            scanAndRead();
            new SwaggerContextService()
                    .withConfigId(getConfigId())
                    .withScannerId(getScannerId())
                    .withContextId(getContextId())
                    .withServletConfig(servletConfig)
                    .withSwaggerConfig(this)
                    .withScanner(this)
                    .withBasePath(getBasePath())
                    .withPathBasedConfig(isUsePathBasedConfig())
                    .initConfig(this.getSwagger())
                    .initScanner();
        }

        void setScanAllResources() {
            ((DefaultReaderConfig) reader.getConfig()).setScanAllResources(true);
        }

    }

}
