package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ContratoDocLiquidacaoResponseMessageBodyReaderProvider
		implements MessageBodyReader<ContratoDocLiquidacaoResponse> {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type == ContratoDocLiquidacaoResponse.class;
	}

	@Override
	public ContratoDocLiquidacaoResponse readFrom(Class<ContratoDocLiquidacaoResponse> type, Type genericType,
			Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders,
			InputStream entityStream) throws IOException {

		ContratoDocLiquidacaoResponse response = mapper.readValue(entityStream, ContratoDocLiquidacaoResponse.class);

		return response;
	}

}
