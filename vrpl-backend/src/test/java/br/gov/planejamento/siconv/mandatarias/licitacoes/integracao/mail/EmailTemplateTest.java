package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

public class EmailTemplateTest {

	private EmailTemplate emailTemplate = new EmailTemplate();

	@Test
	public void getSolicitacaoComplementacaoParaProponente() {

		String corpoDoEmail = emailTemplate.getSolicitacaoComplementacaoParaProponente();

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil uma \"Solicitação de Complementação\" de Documentos da Verificação do Resultado do Processo Licitatório. Inicie a complementação, selecionando a aba Quadro Resumo, botão 'Iniciar Complementação'.</p><br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n"
				+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);

	}

	@Test
	public void getCancelarAnaliseDaComplementacaoProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String corpoDoEmail = emailTemplate.getCancelarAnaliseDaComplementacaoProponente(licitacao);

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil o 'retorno' dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº 11/2019.</p>\n"
				+ "<p>Para consulta, acesse a Proposta no SICONV, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p><br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n"
				+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);

	}

	@Test
	public void getAceitarDocumentacaoParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String corpoDoEmail = emailTemplate.getAceitarDocumentacaoParaProponente(licitacao);

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil o Aceite dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº 11/2019.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n"
				+ "<br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);

	}

	@Test
	public void getRejeitarParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String corpoDoEmail = emailTemplate.getRejeitarParaProponente(licitacao);

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil a \"Rejeição\" dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº 11/2019.</p>\n"
				+ "<p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n"
				+ "<br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n"
				+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);
	}

	@Test
	public void getCancelarAceiteParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String corpoDoEmail = emailTemplate.getCancelarAceiteParaProponente(licitacao);

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil o Cancelamento do Aceite dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº 11/2019.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n"
				+ "<br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n"
				+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);
	}

	@Test
	public void getCancelarRejeicaoParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String corpoDoEmail = emailTemplate.getCancelarRejeicaoParaProponente(licitacao);

		String resultadoEsperado = "<p>Prezado (a),</p><html><head>	<meta charset='UTF-8'>	<title>Plataforma +Brasil</title></head><body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'><table width='100%' cellpadding='0' cellspacing='0'>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>			Sistema de Convênios		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>			Plataforma Mais Brasil		</th>	</tr>	<tr>		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>			MINISTÉRIO DA ECONOMIA		</th>	</tr>	<tr>		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>	</tr>	<tr>		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>	</tr>	<tr>		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br /><p>Consta na Plataforma +Brasil o <b>Cancelamento da Rejeição</b> dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº 11/2019.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n"
				+ "<br /></td>	</tr>	<tr>		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>	</tr></table></body></html><p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n"
				+ "\n"
				+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

		assertNotNull(corpoDoEmail);
		assertEquals(corpoDoEmail, resultadoEsperado);
	}

	@Test
	public void getAssuntoSolicitacaoComplementacaoParaProponenteAsMandataria() {

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);

		String assuntoDoEmail = emailTemplate.getAssuntoSolicitacaoComplementacaoParaProponente(usuarioLogado,
				licitacao);

		String resultadoEsperado = "Documentos da Verificação do Resultado do Processo Licitatório – Solicitado Complementação – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);

	}

	@Test
	public void getAssuntoSolicitacaoComplementacaoParaProponenteAsConcedente() {

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.CONCEDENTE);

		String assuntoDoEmail = emailTemplate.getAssuntoSolicitacaoComplementacaoParaProponente(usuarioLogado,
				licitacao);

		String resultadoEsperado = "Documentos da Verificação do Resultado do Processo Licitatório – Solicitado Complementação para o Concedente – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);

	}

	@Test
	public void getAssuntoCancelarAnaliseDaComplementacaoProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String assuntoDoEmail = emailTemplate.getAssuntoCancelarAnaliseDaComplementacaoProponente(licitacao);

		String resultadoEsperado = "Documentos da Verificação do Resultado do Processo Licitatório Retornados para Análise – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);

	}

	@Test
	public void getAssuntoAceitarDocumentacaoParaProponente() {

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String assuntoDoEmail = emailTemplate.getAssuntoAceitarDocumentacaoParaProponente(licitacao);

		String resultadoEsperado = "Aceite dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);
	}

	@Test
	public void getAssuntoRejeitarParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String assuntoDoEmail = emailTemplate.getAssuntoRejeitarParaProponente(licitacao);

		String resultadoEsperado = "Rejeite dos Documentos da Verificação do Resultado do Processo Licitatório – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);
	}

	@Test
	public void getAssuntoCancelarAceiteParaProponente() {
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setNumeroAno("11/2019");

		String assuntoDoEmail = emailTemplate.getAssuntoCancelarAceiteParaProponente(licitacao);

		String resultadoEsperado = "Cancelamento do Aceite dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº 11/2019";

		assertNotNull(assuntoDoEmail);
		assertEquals(assuntoDoEmail, resultadoEsperado);
	}

//	@Test
//	public void getAssuntoCancelarRejeicaoParaProponente() {
//		LicitacaoBD licitacao = new LicitacaoBD();
//		licitacao.setNumeroAno("11/2019");
//
//		String assuntoDoEmail = emailTemplate.getAssuntoCancelarRejeicaoParaProponente(licitacao);
//
//		String resultadoEsperado = "Cancelamento do Rejeite dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº 11/2019";
//
//		assertNotNull(assuntoDoEmail);
//		assertEquals(assuntoDoEmail, resultadoEsperado);
//	}

}
