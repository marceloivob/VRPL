package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.slf4j.Logger;

@Liveness
//localhost:8080/health/live
public class LivenessHealthCheckResource implements HealthCheck {

	@Inject
	private Logger logger;

	@Override
	public HealthCheckResponse call() {
		// https://stackoverflow.com/questions/8809098/how-do-i-set-the-default-locale-for-my-jvm
		// https://stackoverflow.com/questions/55673886/what-is-the-difference-between-c-utf-8-and-en-us-utf-8-locales
		// https://github.com/flutter/flutter/issues/13574

		Locale currentLocale = Locale.getDefault();
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale);

		logger.trace("Liveness - VRPL");

		return HealthCheckResponse //
				.named("liveness") //
				.withData("data/hora", LocalDateTime.now().toString()) //
				.withData("TimeZone", TimeZone.getDefault().getID()) //
				.withData("DisplayLanguage", currentLocale.getDisplayLanguage()) //
				.withData("DisplayCountry", currentLocale.getDisplayCountry()) //
				.withData("Language", currentLocale.getLanguage()) //
				.withData("Country", currentLocale.getCountry()) //
				.withData("Currency Symbol", currencyFormatter.getCurrency().getSymbol()) //
				.withData("Currency Code", currencyFormatter.getCurrency().getCurrencyCode()) //
				.withData("java.class.version", System.getProperty("java.class.version")) //
				.withData("java.runtime.name", System.getProperty("java.runtime.name")) //
				.withData("java.runtime.version", System.getProperty("java.runtime.version")) //
				.withData("user.language", System.getProperty("user.language")) //
				.withData("user.country", System.getProperty("user.country")) //
				.withData("user.timezone", System.getProperty("user.timezone")) //
				.withData("file.encoding", System.getProperty("file.encoding")) //
				.up().build();
	}

}
