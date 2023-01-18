package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface SiconvPrincipal extends Principal {

    Long getId();

    @Override
    String getName();

    String getCpf();

    String getCodigoEnte();

	String getToken();

    Long getIdProposta();

    List<Profile> getProfiles();

    List<String> getRoles();

    boolean isPropostaDoUsuario(Long idProposta);

    default boolean isMandataria() {
        return getProfiles().contains(Profile.MANDATARIA);
    }

    default boolean isProponente() {
        return getProfiles().contains(Profile.PROPONENTE);
    }

    default boolean isConcedente() {
        return getProfiles().contains(Profile.CONCEDENTE);
    }

    default boolean isGuest() {
        return getProfiles().contains(Profile.GUEST) && getProfiles().size() == 1;
    }

    default boolean isAcessoLivre() {
    	final String cpfDeGuest = "guest";
        final String nomeDeGuest = "Guest";

        Boolean ehCPFGuest = getCpf().compareToIgnoreCase(cpfDeGuest) == 0;
        Boolean ehNomeGuest = getName().compareToIgnoreCase(nomeDeGuest) == 0;
        
        return ehCPFGuest && ehNomeGuest;
    }

    default boolean hasProfile(Profile... profiles) {
        return Arrays.stream(profiles).anyMatch(perfil -> getProfiles().contains(perfil));
    }

    default boolean hasRole(Role... roles) {
        return Arrays.stream(roles).anyMatch(role -> getRoles().contains(role.name()));
    }

    default Profile getProfile() {
        // Como todo usuário tem o perfil de Guest, ignora esse perfil,
        Optional<Profile> perfil = getProfiles().stream()
                .filter( profile -> !profile.equals(Profile.GUEST) )
                .findAny();

        if (perfil.isPresent()) {
            return perfil.get();
        } else {
            throw new IllegalStateException(
                    "O usuário possui APENAS perfil de GUEST. É esperado que também possua algum dos seguintes perfis: Mandatárias, Proponente ou Concedente");
        }
    }

}
