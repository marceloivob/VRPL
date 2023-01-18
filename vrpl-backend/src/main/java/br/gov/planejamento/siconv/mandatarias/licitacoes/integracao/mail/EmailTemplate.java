package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.mail;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;

public class EmailTemplate {

	// Templates de Mensagens de Email
	private final String CABECALHO = "<p>Prezado (a),</p>";

	private final String RODAPE = "<p>Para mais informações, acesse o manual correspondente no Portal dos Convênios no endereço: http://portal.convenios.gov.br/ </p>\n\n"
			+ "<p>Este e-mail foi gerado de forma automática pela Plataforma +Brasil. Por favor, não o responda.</p>\n";

	private final String RETORNO_ANALISE_ASSUNTO = "Documentos da Verificação do Resultado do Processo Licitatório Retornados para Análise – Licitação nº {0}";
	private final String RETORNO_ANALISE_CONTEUDO = "<p>Consta na Plataforma +Brasil o 'retorno' dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº {0}.</p>\n<p>Para consulta, acesse a Proposta no SICONV, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>";

	private final String COMPLEMENTACAO_ASSUNTO = "Documentos da Verificação do Resultado do Processo Licitatório – Solicitado Complementação para {0} – Licitação nº {1}";
	private final String COMPLEMENTACAO_ASSUNTO_MANDATARIA = "Documentos da Verificação do Resultado do Processo Licitatório – Solicitado Complementação – Licitação nº {0}";
	private final String COMPLEMENTACAO_CONTEUDO = "<p>Consta na Plataforma +Brasil uma \"Solicitação de Complementação\" de Documentos da Verificação do Resultado do Processo Licitatório. Inicie a complementação, selecionando a aba Quadro Resumo, botão 'Iniciar Complementação'.</p>";

	private final String ACEITA_ASSUNTO = "Aceite dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº {0}";
	private final String ACEITA_CONTEUDO = "<p>Consta na Plataforma +Brasil o Aceite dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº {0}.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n";

	private final String REJEITA_ASSUNTO = "Rejeite dos Documentos da Verificação do Resultado do Processo Licitatório – Licitação nº {0}";
	private final String REJEITA_CONTEUDO = "<p>Consta na Plataforma +Brasil a \"Rejeição\" dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº {0}.</p>\n<p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n";

	private final String CANCELA_ACEITE_ASSUNTO = "Cancelamento do Aceite dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº {0}";
	private final String CANCELA_ACEITE_CONTEUDO = "<p>Consta na Plataforma +Brasil o Cancelamento do Aceite dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº {0}.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n";

	private final String CANCELA_REJEITE_ASSUNTO = "Cancelamento do Rejeição dos  Documentos da Verificação do Resultado do Processo Licitatório  – Licitação nº {0}";
	private final String CANCELA_REJEITE_CONTEUDO = "<p>Consta na Plataforma +Brasil o <b>Cancelamento da Rejeição</b> dos Documentos da Verificação do Resultado do Processo Licitatório, relacionados à Licitação nº {0}.</p> <p>Para consulta, acesse a Proposta na Plataforma +Brasil, selecione a aba Verificação do Resultado do Processo Licitatório. Selecione a aba Quadro Resumo e observe o Histórico de Comunicações.</p>\n";

	private String getCorpoEmail(String mensagem) {
		StringBuilder html = new StringBuilder();
		html.append("<html>");
		html.append("<head>");
		html.append("	<meta charset='UTF-8'>");
		html.append("	<title>Plataforma +Brasil</title>");
		html.append("</head>");
		html.append(
				"<body topmargin='0' leftmargin='50' style='margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; color: #444444;' link='#2C67CD' vlink='#205C90'>");
		html.append("<table width='100%' cellpadding='0' cellspacing='0'>");
		html.append("	<tr>");
		html.append(
				"		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-bottom: 0;'>");
		html.append("			Sistema de Convênios");
		html.append("		</th>");
		html.append("	</tr>");
		html.append("	<tr>");
		html.append(
				"		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 50px; color: #ffffff; padding: 15px; padding-top: 5px; padding-bottom: 5px;'>");
		html.append("			Plataforma Mais Brasil");
		html.append("		</th>");
		html.append("	</tr>");
		html.append("	<tr>");
		html.append(
				"		<th colspan='3' style='background: #1659BF; text-align: left; font-size: 14px; color: #ffffff; padding: 15px; padding-top: 0;'>");
		html.append("			MINISTÉRIO DA ECONOMIA");
		html.append("		</th>");
		html.append("	</tr>");
		html.append("	<tr>");
		html.append("		<td colspan='3' style='background: #dfdfdf; height: 8px;'></td>");
		html.append("	</tr>");

		///////////////////////
		html.append("	<tr>");
		html.append(
				"		<td colspan='3' style='padding: 14px; padding-bottom: 0;'><h2 style='margin-top: 14px; margin-bottom: 0;'>Alteração de Situação da Documentação da Verificação do Resultado do Processo Licitatório!</h2></td>");
		html.append("	</tr>");
		html.append("	<tr>");
		html.append(
				"		<td colspan='3' style='font-size: 14px; padding: 15px; padding-bottom: 30px;'><br /><br />");
		html.append(mensagem);
		html.append("<br /></td>");
		html.append("	</tr>");
		///////////////////////

		html.append("	<tr>");
		html.append("		<td colspan='3' style='background: #1659BF; color: #1659BF; height: 50px;'>&nbsp;</td>");
		html.append("	</tr>");
		html.append("</table>");
		html.append("</body>");
		html.append("</html>");

		return html.toString();
	}

	public String getSolicitacaoComplementacaoParaProponente() {
		String conteudo = COMPLEMENTACAO_CONTEUDO;

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);
		
		return conteudoMensagem.toString();
	}

	public String getAssuntoSolicitacaoComplementacaoParaProponente(SiconvPrincipal usuarioLogado,
			LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String destinatario = null;
		String assunto = "";
		if (usuarioLogado.getProfile() == Profile.MANDATARIA) {
			
			assunto = COMPLEMENTACAO_ASSUNTO_MANDATARIA.replace("{0}", numeroLicitacao);
		} else {
			destinatario = "o " + usuarioLogado.getProfile().getDescription();
			assunto = COMPLEMENTACAO_ASSUNTO.replace("{0}", destinatario).replace("{1}", numeroLicitacao);
		}

		return assunto;
	}

	public String getCancelarAnaliseDaComplementacaoProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String conteudo = RETORNO_ANALISE_CONTEUDO.replace("{0}", numeroLicitacao);

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);

		return conteudoMensagem.toString();
	}

	public String getAssuntoCancelarAnaliseDaComplementacaoProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String assunto = RETORNO_ANALISE_ASSUNTO.replace("{0}", numeroLicitacao);

		return assunto;
	}

	public String getAceitarDocumentacaoParaProponente(LicitacaoBD licitacaoEstadoAtual) {

		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String conteudo = ACEITA_CONTEUDO.replace("{0}", numeroLicitacao);

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);

		return conteudoMensagem.toString();

	}

	public String getAssuntoAceitarDocumentacaoParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String assunto = ACEITA_ASSUNTO.replace("{0}", numeroLicitacao);

		return assunto;
	}

	public String getRejeitarParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String conteudo = REJEITA_CONTEUDO.replace("{0}", numeroLicitacao);

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);

		return conteudoMensagem.toString();

	}

	public String getAssuntoRejeitarParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String assunto = REJEITA_ASSUNTO.replace("{0}", numeroLicitacao);

		return assunto;
	}

	public String getCancelarAceiteParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String conteudo = CANCELA_ACEITE_CONTEUDO.replace("{0}", numeroLicitacao);

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);

		return conteudoMensagem.toString();
	}

	public String getAssuntoCancelarAceiteParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String assunto = CANCELA_ACEITE_ASSUNTO.replace("{0}", numeroLicitacao);

		return assunto;
	}

	public String getCancelarRejeicaoParaProponente(LicitacaoBD licitacaoEstadoAtual) {
		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String conteudo = CANCELA_REJEITE_CONTEUDO.replace("{0}", numeroLicitacao);

		String corpoEmail = getCorpoEmail(conteudo);

		StringBuilder conteudoMensagem = new StringBuilder();
		conteudoMensagem.append(CABECALHO);
		conteudoMensagem.append(corpoEmail);
		conteudoMensagem.append(RODAPE);

		return conteudoMensagem.toString();
	}

	public String getAssuntoCancelarRejeicaoParaProponente(LicitacaoBD licitacaoEstadoAtual) {

		String numeroLicitacao = licitacaoEstadoAtual.getNumeroAno();

		String assunto = CANCELA_REJEITE_ASSUNTO.replace("{0}", numeroLicitacao);

		return assunto;

	}

}
