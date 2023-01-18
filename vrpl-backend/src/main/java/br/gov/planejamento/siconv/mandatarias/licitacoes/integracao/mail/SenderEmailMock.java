package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import javax.mail.Address;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SenderEmailMock implements SenderEmail {

	@Override
	public void send(Set<Address> enderecos, String assunto, String textoMensagem) {

		String destinatarios = Arrays.toString(enderecos.toArray());

		Date dataEnvio = Calendar.getInstance().getTime();

		String resultado = String.format(">>>EMAIL FAKE - EXCLUSIVO PARA TESTE<<<%nDestinatários: %s%nAssunto: %s%nData de Envio: %s%nTexto da Mensagem: %s",
				destinatarios, assunto, dataEnvio, textoMensagem);

		log.info("Envio de Email Fake:\n {}", resultado);
	}

}
