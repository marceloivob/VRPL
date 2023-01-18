package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.inspect;

import java.lang.reflect.Method;
import java.util.Objects;

import javax.enterprise.inject.spi.CDI;

import org.jdbi.v3.sqlobject.Handler;
import org.jdbi.v3.sqlobject.HandlerDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.dao.CacheDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;

public class DefineUserInSessionDecorator implements HandlerDecorator {

	private static final Logger logger = LoggerFactory.getLogger(DefineUserInSessionDecorator.class);

	@Override
	public Handler decorateHandler(Handler base, Class<?> sqlObjectType, Method method) {

		SiconvPrincipal usuarioLogado = CDI.current().select(SiconvPrincipal.class).get();

		Objects.requireNonNull(usuarioLogado);
		Objects.requireNonNull(usuarioLogado.getCpf());

		return (target, args, handleSupplier) -> handleSupplier.getHandle().inTransaction(transaction -> {

			logger.debug("Executando operação '{}.{}' com o usuario: '{}'", sqlObjectType.getSimpleName(),
					method.getName(), usuarioLogado.getCpf());

			transaction.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado.getCpf());

			return base.invoke(target, args, handleSupplier);
		});

	}

}
