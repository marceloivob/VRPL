package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import java.util.ArrayList;
import java.util.List;

import com.auth0.jwt.interfaces.DecodedJWT;

public class ConcreteSiconvPrincipal implements SiconvPrincipal {
    private Long id;
    private Long idProposta;
    private String name;
    private String cpf;
    private String codigoEnte;
    private List<Profile> perfis = new ArrayList<>();
    private List<String> papeis;
	private String token;

    public ConcreteSiconvPrincipal(DecodedJWT jwt) {
        validateIfTokenIsPresent(jwt);

        this.id = jwt.getClaim("id").asLong();
        this.idProposta = jwt.getClaim("idProposta").asLong();
        this.name = jwt.getClaim("nome").asString();
        this.cpf = jwt.getClaim("cpf").asString();
        this.codigoEnte = jwt.getClaim("ente").asString();
        this.papeis = jwt.getClaim("roles").asList(String.class);
		this.token = jwt.getToken();

        if (this.papeis == null) {
            this.papeis = new ArrayList<>();
        }

        this.perfis.add(Profile.GUEST);

        String tipoEnte = jwt.getClaim("tipoEnte").asString();
        if (tipoEnte != null && !isAdministrator(papeis)) {
            this.perfis.add(Profile.valueOf(tipoEnte));
        }
    }
    
    private boolean isAdministrator(List<String> papeis) {
    	boolean retorno = false;
    	if (papeis != null && papeis.size() == 1 
    			&& papeis.contains("ADMINISTRADOR_SISTEMA_ORGAO_EXTERNO")) {
    		retorno = true;
    	}
    	return retorno;
    }

    private void validateIfTokenIsPresent(DecodedJWT jwt) {
        if (jwt == null) {
            throw new IllegalArgumentException("Token não fornecido: " + jwt);
        }
    }

    @Override
    public List<Profile> getProfiles() {
        return this.perfis;
    }

    @Override
    public List<String> getRoles() {
        return this.papeis;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCpf() {
        return cpf;
    }

    @Override
	public String getToken() {
		return token;
	}

	@Override
    public String getCodigoEnte() {
        return codigoEnte;
    }

    @Override
    public Long getIdProposta() {
        return this.idProposta;
    }

    @Override
    public boolean isPropostaDoUsuario(Long idProposta) {
        return idProposta != null && this.getIdProposta().equals(idProposta);
    }

}
