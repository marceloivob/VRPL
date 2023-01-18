package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Stream;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.Resultado;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.SecurityAccessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserCanEditVerifier {

	@Getter
	@Setter
	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private ControllerAccessForRowPolice rowPolice;

	@Log
	public void check(TabelasDoVRPLEnum tabela, Long idRegistro) {

		if (usuarioLogado.isAcessoLivre()) {
			throw new SecurityAccessException(usuarioLogado, tabela, idRegistro);
		}

		Predicate<Resultado> propostaEstaNoControleDeAcesso = resultado -> resultado.getProposta()
				.equals(usuarioLogado.getIdProposta());
		Predicate<Resultado> tabelaEstaNaRelacao = resultado -> resultado.getTabela()
				.equalsIgnoreCase(tabela.getNomeDaTabela());
		Predicate<Resultado> registroPossuiMesmoId = resultado -> resultado.getId().equals(idRegistro);

		try {
			log.debug("Verificando se usuário ({}) tem acesso ao registro '{}' da tabela '{}'", usuarioLogado.getCpf(),
					idRegistro, tabela);

			List<Resultado> resultadoComoList = rowPolice.getRegistrosQueUsuarioTemAcesso().get(usuarioLogado);

			Stream<Resultado> resultadoComoStream = resultadoComoList.stream();
			Optional<Resultado> usuarioEstaEditandoRegistroAnteriormenteSelecionado = resultadoComoStream
					.filter(propostaEstaNoControleDeAcesso.and(tabelaEstaNaRelacao).and(registroPossuiMesmoId))
					.findAny();

			if (!usuarioEstaEditandoRegistroAnteriormenteSelecionado.isPresent()) {
				throw new SecurityAccessException(usuarioLogado, tabela, idRegistro);
			}

		} catch (ExecutionException e) {
			throw new MandatariasException(e);
		}
	}

}
