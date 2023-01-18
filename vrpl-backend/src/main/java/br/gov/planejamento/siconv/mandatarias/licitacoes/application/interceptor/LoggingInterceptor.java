package br.gov.planejamento.siconv.mandatarias.licitacoes.application.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import lombok.extern.slf4j.Slf4j;

@Interceptor
@Log
@Slf4j
public class LoggingInterceptor implements Serializable {

	private static final long serialVersionUID = 8633807493749784650L;

	/**
	 * Constante.
	 */
	private static final String STRING_DESCONHECIDO = "DESCONHECIDO";

	/**
	 * Template msg log.
	 */
	private static final String MSG_LOG_ANTES_METODO = "[Start (%d):%s::%s]";

	/**
	 * Template msg log.
	 */
	private static final String MSG_LOG_APOS_METODO = "[End (%d):%s::%s::%d ms]";
	
	@Inject
	private TracerContext tc;
	
	
	@AroundInvoke
	public Object intercept(final InvocationContext ctx) throws Exception {

		final long t0 = System.currentTimeMillis();
		logarAntesExecucaoMetodo(ctx);
		try {
			return ctx.proceed();
		} finally {
			final long t1 = System.currentTimeMillis();
			logarAposExecucaoMetodo(ctx, t0, t1);
		}
	}

	private void logarAposExecucaoMetodo(final InvocationContext ctx, final long t0, final long t1) {
		
		var threadId = Thread.currentThread().getId();
		
		final String msgLogAposExecucaoMetodo = String.format(MSG_LOG_APOS_METODO, threadId , getNomeClasse(ctx), getNomeMetodo(ctx), t1 - t0);
		log.info(msgLogAposExecucaoMetodo);

		tc.finish();	
		
	}

	private void logarAntesExecucaoMetodo(final InvocationContext ctx) {
		
		var threadId = Thread.currentThread().getId();

		final String msgLogAntesExecucaoMetodo = String.format(MSG_LOG_ANTES_METODO, threadId, getNomeClasse(ctx),
				getNomeMetodo(ctx));
		log.info(msgLogAntesExecucaoMetodo);

		tc.start(getNomeMetodo(ctx));
		
	}


	private String getNomeMetodo(final InvocationContext ctx) {
		if (ctx == null) {
			return STRING_DESCONHECIDO;
		}

		final Method method = ctx.getMethod();
		if (method == null) {
			return STRING_DESCONHECIDO;
		}

		return method.getName();
	}

	private String getNomeClasse(final InvocationContext ctx) {
		if (ctx == null) {
			return STRING_DESCONHECIDO;
		}

		final Object target = ctx.getTarget();
		if (target == null) {
			return STRING_DESCONHECIDO;
		}

		String nomeClasse = target.getClass().getName();
		int indexProxy = nomeClasse.indexOf('$');

		if (indexProxy > 0) {
			return nomeClasse.substring(0, indexProxy);
		} else {
			return nomeClasse;
		}
	}

}
