package br.gov.planejamento.siconv.mandatarias.licitacoes.application.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeradorDeTicket {

    /**
     * Pattern para formatação de data.
     */
	private static final String DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";

    public String generateNewTicket() {
        final StringBuilder ticket = new StringBuilder();
        final String currentTime = getCurrentTimeFormatted();
        ticket.append(currentTime);

        String ticketSessionId = gerarSessionIdAleatorio();

        ticket.append(ticketSessionId);

        return ticket.toString();
    }

    private String getCurrentTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

        return formatter.format(LocalDateTime.now());
    }

    private String gerarSessionIdAleatorio() {
		long codigoAleatorio = new SecureRandom().nextLong();
        codigoAleatorio = codigoAleatorio % 10000000000L;
        return Long.toString(codigoAleatorio);
    }

}
