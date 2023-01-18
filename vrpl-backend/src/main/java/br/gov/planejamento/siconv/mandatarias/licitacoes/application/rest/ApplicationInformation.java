package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.IgnorePrincipal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

@Path("/app")
@IgnorePrincipal
public class ApplicationInformation {

    @GET
    @Path("/info")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<Map<String, Object>> recoveryInformationAboutBuildFromBackend() throws IOException {

        final Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));

        Map<String, Object> projectInfo = new HashMap<>();

        projectInfo.put("date", LocalDate.now().toString());
        projectInfo.put("time", LocalTime.now().toString());
        projectInfo.put("version", properties.getProperty("version"));
        projectInfo.put("build.date", properties.getProperty("build.date"));

        return success(projectInfo);

    }

    @GET
    @Path("/integrations")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<Map<String, Object>> getEndpointIDP() {
        String chaveToIDP = "integrations.PUBLIC.IDP.endpoint";
        String valorToIDP = System.getProperty(chaveToIDP);

        if (valorToIDP == null) {
            throw new IllegalArgumentException(
                    String.format("Configuração do IDP inválida: '%s' - '%s'", chaveToIDP, valorToIDP));
        }

        String chaveToSICONV = "integrations.PUBLIC.SICONV.endpoint";
        String valorToSICONV = System.getProperty(chaveToSICONV);

        if (valorToSICONV == null) {
            throw new IllegalArgumentException(
                    String.format("Configuração do SICONV inválida: '%s' - '%s'", chaveToSICONV, valorToSICONV));
        }

        Map<String, Object> mm = new HashMap<>();
        mm.put("IDP", valorToIDP);
        mm.put("SICONV", valorToSICONV);

        return success(mm);
    }

    @GET
    @Path("/integration/licitacao")
    @Produces(APPLICATION_JSON_UTF8)
    public DefaultResponse<Map<String, Object>> getEndpointGRPCLicitacao() {
        String chaveEndpoint = "integrations.PRIVATE.GRPC.LICITACAO.endpoint";
        String valorEndpoint = System.getProperty(chaveEndpoint);

        String chavePort = "integrations.PRIVATE.GRPC.LICITACAO.port";
        Integer valorPort = Integer.getInteger(chavePort);

        String chaveUseSSL = "integrations.PRIVATE.GRPC.LICITACAO.useSSL";
        Boolean valorUseSSL = Boolean.getBoolean(chaveUseSSL);

        Map<String, Object> mm = new HashMap<>();
        mm.put(chaveEndpoint, valorEndpoint);
        mm.put(chavePort, valorPort);
        mm.put(chaveUseSSL, valorUseSSL);

        if ((valorEndpoint == null) || (valorPort == null) || (valorUseSSL == null)) {
            throw new IllegalArgumentException(String.format("Configuração do GRPC_Licitacao inválida: '%s' ", mm));
        }

        return success(mm);
    }
}
