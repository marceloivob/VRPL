package br.gov.planejamento.siconv.mandatarias.test.core;

import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;

public class MockSiconvPrincipal implements SiconvPrincipal {

	private List<Profile> perfis = new ArrayList<>();
	private List<String> papeis = new ArrayList<>();

	private String cpf;
	private Boolean isPropostaUsuario = false;
	private Long idProposta = 0L;
	private String nome = "guest";
	private String token;

	public MockSiconvPrincipal(Profile perfilDoUsuario) {
		if (perfilDoUsuario != Profile.GUEST) {
			this.perfis.add(Profile.GUEST);
		}

		this.perfis.add(perfilDoUsuario);
	}

	public MockSiconvPrincipal(Profile perfilDoUsuario, Long idProposta, String cpf) {
		this(perfilDoUsuario, idProposta);
		this.cpf = cpf;

	}

	public MockSiconvPrincipal(Profile perfilDoUsuario, Long idProposta) {
		this(perfilDoUsuario);
		this.idProposta = idProposta;
	}

	public MockSiconvPrincipal(Profile perfilDoUsuario, String cpf) {
		this(perfilDoUsuario);
		this.cpf = cpf;
	}

	public MockSiconvPrincipal(Profile perfilDoUsuario, String cpf, Boolean isPropostaUsuario) {
		this(perfilDoUsuario, cpf);
		this.isPropostaUsuario = isPropostaUsuario;
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
		return null;
	}

	@Override
	public String getName() {
		return nome;
	}

	@Override
	public String getCpf() {
		return cpf;
	}

	@Override
	public String getCodigoEnte() {
		return null;
	}

	@Override
	public Long getIdProposta() {
		return idProposta;
	}

	@Override
	public boolean isPropostaDoUsuario(Long idProposta) {
		return isPropostaUsuario;
	}

	public void setName(String nome) {
		this.nome = nome;
	}

	@Override
	public String getToken() {
		return token;
	}

}
