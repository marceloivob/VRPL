package br.gov.planejamento.siconv.mandatarias.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

public class LocaleTest {

	@Test
	public void defeito1897544() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

		System.setProperty("user.timezone", "America/Sao_Paulo");
		System.setProperty("user.language", "pt");
		System.setProperty("user.country", "BR");
		System.setProperty("file.encoding", "UTF-8");

		Locale.setDefault(new Locale("pt", "BR"));
		Locale currentLocale = Locale.getDefault();

		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

		assertEquals("R$", currencyFormatter.getCurrency().getSymbol());
		assertEquals("BRL", currencyFormatter.getCurrency().getCurrencyCode());
		assertEquals("português", currentLocale.getDisplayLanguage());
		assertEquals("Brasil", currentLocale.getDisplayCountry());
		assertEquals("pt", currentLocale.getLanguage());
		assertEquals("BR", currentLocale.getCountry());

		assertEquals("pt", System.getProperty("user.language"));
		assertEquals("BR", System.getProperty("user.country"));
		assertEquals("America/Sao_Paulo", System.getProperty("user.timezone"));
		assertEquals("UTF-8", System.getProperty("file.encoding"));

	}

}
