package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.Resultado;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ControllerAccessForRowPolice {

	@Inject
	private DAOFactory dao;

	private LoadingCache<SiconvPrincipal, List<Resultado>> registrosQueUsuarioTemAcesso = CacheBuilder.newBuilder()
			.maximumSize(100).expireAfterAccess(10, TimeUnit.MINUTES)
			.build(new CacheLoader<SiconvPrincipal, List<Resultado>>() {
				@Override
				public List<Resultado> load(SiconvPrincipal usuarioLogado) {
					log.debug("Consultando registro do cache para o usuário: {}", usuarioLogado);

					return dao.get(CacheDAO.class).recuperarRegistrosPermitidos(usuarioLogado);
				}

			});

	@Log
	public LoadingCache<SiconvPrincipal, List<Resultado>> getRegistrosQueUsuarioTemAcesso() {
		return registrosQueUsuarioTemAcesso;
	}

	public void setDao(DAOFactory dao) {
		this.dao = dao;
	}

}
