package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import java.util.Calendar;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.ErroEnvioEmailException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteSenderEmail implements SenderEmail {

	private static final String MAIL_TRANSPORT_PROTOCOL_PROPERTY = "mail.transport.protocol";
	private static final String MAIL_SMTP_HOST_PROPERTY = "mail.smtp.host";
	private static final String MAIL_SMTP_PORT_PROPERTY = "mail.smtp.port";
	private static final String MAIL_FROM_PROPERTY = "mail.from";
	private static final String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";

	@Override
	public void send(Set<Address> destinatarios, String assunto, String corpoEmail) {

		if ((destinatarios == null) || (destinatarios.isEmpty())) {
			throw new IllegalArgumentException("Não foi fornecido um destinatário para o envio de email.");
		}

		if ((assunto == null) || (assunto.isEmpty())) {
			throw new IllegalArgumentException("Não foi fornecido o Assunto para envio de email");
		}

		if ((corpoEmail == null) || (corpoEmail.isEmpty())) {
			throw new IllegalArgumentException("Não foi fornecido o Conteúdo para envio de email");
		}

		Properties properties = getMailConfigProperties();

		try {
			Session session = Session.getInstance(properties, null);

			Message msg = new MimeMessage(session);

			Address from = getInternetAddresses(properties.getProperty(MAIL_FROM_PROPERTY))[0];
			Address[] to = destinatarios.toArray(new Address[destinatarios.size()]);

			msg.setFrom(from);
			msg.setRecipients(Message.RecipientType.TO, to);
			msg.setSubject(assunto);
			msg.setSentDate(Calendar.getInstance().getTime());
			msg.setContent(corpoEmail, DEFAULT_CONTENT_TYPE);

			Transport.send(msg);

		} catch (MessagingException e) {
			throw new ErroEnvioEmailException(e);
		}
	}

	/**
	 * Retorna as configurações de email do SICONV.
	 *
	 * @return
	 */
	private Properties getMailConfigProperties() {
		Properties config = new Properties();
		config.put(MAIL_TRANSPORT_PROTOCOL_PROPERTY, "smtp");
		config.put(MAIL_SMTP_HOST_PROPERTY, System.getProperty("integrations.PRIVATE.MAIL.remote-host"));
		config.put(MAIL_SMTP_PORT_PROPERTY, System.getProperty("integrations.PRIVATE.MAIL.remote-port"));
		config.put(MAIL_FROM_PROPERTY, System.getProperty("integrations.PRIVATE.MAIL.from"));

		if (config.getProperty(MAIL_SMTP_HOST_PROPERTY) == null || config.getProperty(MAIL_FROM_PROPERTY) == null) {
			throw new RuntimeException("Configurações de email inválidas");
		}

		return config;
	}

	/**
	 *
	 * @param addresses
	 * @return
	 */
	private static InternetAddress[] getInternetAddresses(String addresses) throws AddressException {
		String[] addressesArray = addresses.split(";");
		InternetAddress[] internetAddresses = new InternetAddress[addressesArray.length];

		for (int i = 0; i < addressesArray.length; i++) {
			internetAddresses[i] = new InternetAddress(addressesArray[i]);
		}

		return internetAddresses;
	}
}
