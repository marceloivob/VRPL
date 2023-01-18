package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperContextResolverTest {

	@DisplayName(value = "Teste serialização de Data no formato yyyy-MM-dd utilizado pelo Jackson")
	@Test
	public void testeLocalDate01() throws JsonProcessingException {

		String esperado = "2018-07-23";

		LocalDate data = LocalDate.parse(esperado, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		ObjectMapperContextResolver localMapper = new ObjectMapperContextResolver();
		localMapper.setSimpleModule(new SimpleModule("Teste"));
		localMapper.setCpfSerializer(new CPFSerializer());
		localMapper.setInscricaoGenericaSerializer(new InscricaoGenericaSerializer());

		String encontrado = localMapper.getContext(String.class).writeValueAsString(data);
		encontrado = encontrado.replaceAll("\"", "");

		assertEquals(encontrado, esperado);
	}

	@DisplayName(value = "Teste serialização de Data no formato yyyy-MM utilizado pelo Jackson")
	@Test
	public void testeYearMonth01() throws JsonProcessingException {
		String esperado = "2018-07";

		YearMonth data = YearMonth.parse(esperado, DateTimeFormatter.ofPattern("yyyy-MM"));

		ObjectMapperContextResolver localMapper = new ObjectMapperContextResolver();
		localMapper.setSimpleModule(new SimpleModule("Teste"));
		localMapper.setCpfSerializer(new CPFSerializer());
		localMapper.setInscricaoGenericaSerializer(new InscricaoGenericaSerializer());

		String encontrado = localMapper.getContext(String.class).writeValueAsString(data);
		encontrado = encontrado.replaceAll("\"", "");

		assertEquals(encontrado, esperado);
	}

}
