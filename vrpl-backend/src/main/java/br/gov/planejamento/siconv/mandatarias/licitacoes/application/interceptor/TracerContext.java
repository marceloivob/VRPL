package br.gov.planejamento.siconv.mandatarias.licitacoes.application.interceptor;

import java.util.Deque;
import java.util.LinkedList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.opentracing.Span;
import io.opentracing.Tracer;

@ApplicationScoped
public class TracerContext {

	
	@Inject
	private Tracer tracer;

	private ThreadLocal<Deque<Span>> store = new ThreadLocal<>();
	
	
	public void start(String ref) {
		
		
		Deque<Span> stack = initializeStack();
		
		startSpan(stack, ref);
		
	}
	
	
	public Span finish() {
		
		Deque<Span> stack = initializeStack();
		
		if (!stack.isEmpty()) {
			Span span = stack.removeFirst();	
			span.finish();
			return span;
			
		} else {
			return null;
		}
	}
	

	private Deque<Span> initializeStack() {
		
		Deque<Span> stack = store.get();

		if (stack == null) {
			stack = new LinkedList<>();
		}
		
		store.set(stack);
		
		return stack;
	}

	private void startSpan (Deque<Span> stack, String ref) {
		
		Span span = null;
		if (stack.isEmpty()) {
			span = tracer
					.buildSpan(ref)
					.withTag("type", "sender")
					.start();

		} else {
			Span parent = stack.peekFirst();
			span = tracer
					.buildSpan(ref)
					.asChildOf(parent)
					.withTag("type", "sender")
					.start();
			
		}
		
		stack.addFirst(span);

	}
}
