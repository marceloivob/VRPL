package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper = new ObjectMapper();

	@Inject
	private SimpleModule simpleModule;

	@Inject
	private CPFSerializer cpfSerializer;

	@Inject
	private InscricaoGenericaSerializer inscricaoGenericaSerializer;

	public ObjectMapperContextResolver() {
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Produces
	public SimpleModule produtor() {
		SimpleModule simpleModule = new SimpleModule("AcessoLivre");

		return simpleModule;
	}

	private void registerGuestModule(ObjectMapper mapper) {
		simpleModule.addSerializer(CPF.class, getCpfSerializer());
		simpleModule.addSerializer(IG.class, getInscricaoGenericaSerializer());

		mapper.registerModule(simpleModule);
	}

	private void registerJavaTimeModule(ObjectMapper mapper) {
		mapper.registerModule(new JavaTimeModule());
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		registerJavaTimeModule(mapper);
		registerGuestModule(mapper);

		return mapper;
	}

	public SerializerProvider getSerializerProvider() {
		return mapper.getSerializerProvider();
	}

	public CPFSerializer getCpfSerializer() {
		return cpfSerializer;
	}

	public void setCpfSerializer(CPFSerializer cpfSerializer) {
		this.cpfSerializer = cpfSerializer;
	}

	public SimpleModule getSimpleModule() {
		return simpleModule;
	}

	public void setSimpleModule(SimpleModule simpleModule) {
		this.simpleModule = simpleModule;
	}

	public void setInscricaoGenericaSerializer(InscricaoGenericaSerializer inscricaoGenericaSerializer) {
		this.inscricaoGenericaSerializer = inscricaoGenericaSerializer;
	}

	public InscricaoGenericaSerializer getInscricaoGenericaSerializer() {
		return inscricaoGenericaSerializer;
	}

}