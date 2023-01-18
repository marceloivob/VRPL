package br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import lombok.extern.slf4j.Slf4j;

@Interceptor
@Slf4j
public class RefreshRowPoliceInterceptor {

	@Inject
	private ControllerAccessForRowPolice rowPolice;

	@Inject
	private SiconvPrincipal usuarioLogado;

	@Log
	@AroundInvoke
	public Object manageTransaction(InvocationContext ctx) throws Exception {

		Object result = ctx.proceed();

		if (ctx.getMethod().isAnnotationPresent(RefreshRowPolice.class)) {
			log.debug("Atualizando os registros que o usuário '{}' tem acesso ", usuarioLogado);

			rowPolice.getRegistrosQueUsuarioTemAcesso().refresh(usuarioLogado);
		}

		return result;
	}

}
