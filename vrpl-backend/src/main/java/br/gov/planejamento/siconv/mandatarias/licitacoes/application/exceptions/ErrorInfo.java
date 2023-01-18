package br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.Severity.ERROR;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.Severity.WARN;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import javax.ws.rs.core.Response.Status;

public enum ErrorInfo {
	ERRO_GENERICO_NEGOCIO(000000, "Erro Genérico de Negócio.", ERROR),
	ERRO_PROPOSTA_NAO_ENCONTRADA(513493, "Não foi possível encontrar a proposta indicada.", ERROR, NOT_FOUND),
	ERRO_PROCESSO_EXECUCAO_NAO_ENCONTRADO(513493, "Processo de Execução não encontrado.", ERROR, NOT_FOUND),
	ERRO_LICITACAO_NAO_ENCONTRADA(543243, "Nenhuma licitação encontrada para a proposta.", WARN),
	ERRO_PROPOSTA_SEM_SPA_HOMOLOGADO(540930,
			"Os Documentos Orçamentários não possuem SPA homologado, não sendo permitido o acesso às funcionalidades de verificação do processo licitatório.",
			WARN, NOT_FOUND),
	ERRO_VRPL_SEM_ACESSO_PROPOSTA(707016, "Não foi possível carregar a proposta, pois ela possui análise de licitação iniciada no fluxo preexistente.",
			WARN, NOT_FOUND),

	PROPOSTA_SEM_DADOS_IMPORTADOS(721422, "Não é possível acessar as informações e funcionalidades deste módulo VRPL (Verificação do Resultado Processo Licitatório), pois ainda não existem dados cadastrados.",
			WARN, NOT_FOUND),

	PROPOSTA_ESTA_SENDO_IMPORTADA(721423, "Não é possível acessar as informações e funcionalidades deste módulo VRPL (Verificação do Resultado Processo Licitatório), pois ainda estão sendo importados os dados da proposta, por favor tente novamente mais tarde.",
			WARN, NOT_FOUND),

	ERRO_ACIONAMENTO_SERVICO_SICONV_LICITACOES(543245,
			"Erro ao acionar serviço para obtenção da lista de licitações associadas à proposta.", ERROR),
	// nao ha rng para isso
	ERRO_ACIONAMENTO_SERVICO_SICONV_CTEF(1,
			"Erro ao acionar serviço para obtenção do contrato de fornecimento relativo à licitação.", ERROR),

	ERRO_ACESSO_RECURSO_NAO_PERMITIDO(2, "Usuário está tentando acessar recurso que não tem permissão.", ERROR,
			FORBIDDEN),
	ERRO_ACESSO_PERFIL_NAO_AUTORIZADO(2, "Usuário não possui perfil necessário para realizar a ação.", ERROR,
			FORBIDDEN),
	ERRO_ACESSO_PERFIL_NAO_INFORMADO(3, "Usuário não possui nenhum perfil associado.", ERROR, FORBIDDEN),

	// mensagens da regras de negocio 520637
	ERRO_CAMPO_OBRIGATORIO(5206371, "Campo obrigatório não informado.", ERROR),
	ERRO_DATA_EMISSAO_POSTERIOR(5206373, "A data de emissão não pode ser maior que a data corrente.", ERROR),

	// anexo - sem rng para essas situacoes
	ERRO_LICITACAO_DO_ANEXO_NAO_ENCONTRADA(4, "Licitação do anexo não encontrada.", ERROR),
	ERRO_PARAMETROS_DO_ANEXO_INVALIDOS(5, "Existe ao menos um parâmetro inválido.", ERROR),
	ERRO_ANEXO_SEM_EXTENSAO(6, "O anexo <b>informado</b> não possui formato definido.", ERROR),
	ANEXO_NAO_ENCONTRADO(7, "O anexo informado não foi encontrado.", ERROR),

	// mensagens da regras de negocio 511551
	ERRO_EXISTE_ANEXO_COM_MESMA_DESCRICAO(5115511, "Já existe um anexo com esta descrição.", ERROR),
	ERRO_EXISTE_ANEXO_COM_MESMO_NOME(5115512, "Já existe um anexo com este arquivo.", ERROR),
	ERRO_FORMATO_ANEXO_NAO_PERMITIDO(5115513,
			"Formatos permitidos: PDF, DOC, DOCX, XLS, XLSX, JPG, JPEG, PNG, ODT, ODS e DWG.", ERROR),
	ERRO_TAMANHO_MAXIMO_ANEXO_EXCEDIDO(5115514, "O tamanho máximo do arquivo é 10MB.", ERROR),
	ERRO_CPF_REPETIDO(5115517, "Já existe CPF cadastrado.", ERROR),
	ATIVIDADE_INVALIDA(5115519, "Atividade ou Título Inválido.", ERROR), CPF_INVALIDO(5115520, "CPF Inválido.", ERROR),
	SUBMETA_NAO_ENCONTRADA(5115521, "Submeta Nao Encontrada.", ERROR),
	EVENTO_NUMERO_REPETIDO(511419, "Número do Evento já cadastrado. Favor informar um novo valor.", ERROR),
	EVENTO_NAO_ENCONTRADO(5115523, "Evento Não encontrado.", ERROR, NOT_FOUND),
	PO_NAO_ENCONTRADA(5115524, "Planilha Orçamentária Não Encontrada.", ERROR, NOT_FOUND),
	NAO_EXISTE_PO_CADASTRADA(5115524, "É necessário ter pelo menos uma Planilha Orçamentária cadastrada.", ERROR, NOT_FOUND),
	DATA_INICIO_OBRA_INVALIDA(511112,
			"A data informada não pode ser menor que a data inicialmente prevista ({0}) para início de obra na fase de análise do Projeto Básico",
			ERROR),
	FRENTE_DE_OBRA_NUMERO_REPETIDO(511450, "Número da Frente de Obra já cadastrado. Favor informar um novo valor.",
			ERROR),
	FRENTE_DE_OBRA_NAO_ENCONTRADO(511561, "Frente de Obra nao Encontrada", ERROR),
	MACROSERVICO_NAO_ENCONTRADO(511562, "Macrosserviço não encontrado!", ERROR, NOT_FOUND),
	SERVICO_NAO_ENCONTRADO(511563, "Serviço não encontrado!", ERROR, NOT_FOUND),
	MACROSERVICOPARCELA_NAO_ENCONTRADO(511565, "Parcela não encontrada!", ERROR, NOT_FOUND),
	MACROSERVICOPARCELA_NEGATIVA(511566, "Parcela não pode ser negativa!", ERROR),
	EVENTOFRENTEOBRA_NAO_ENCONTRADO(511567, "Evento frente Obra não encontrado!", ERROR, NOT_FOUND),
	SUBMETAS_TIPO_DIFERENTE_LICITACAO(511568,
			"Não é possível associar na mesma licitação lotes com submetas relacionados a Engenharia e lotes com submetas de Trabalho Social!",
			ERROR),
	PO_ACOMPANHAMENTO_DIFERENTE_LICITACAO(511569,
			"Não é possível associar lotes contendo submetas de Planilhas Orçamentárias acompanhadas por evento e sem acompanhamento por evento em uma mesma licitação!",
			ERROR),
	SUBMETAS_REGIME_DIFERENTE_LICITACAO(511570,
			"Não é possível associar lotes com submetas de regimes de execução diferentes!", ERROR),

	// mensagens quadro resumo
	PROCESSO_LICITATORIO_NAO_CONCLUIDO(570991,
			"A licitação ({0}) não está com a situação concluída no sistema de licitações.", ERROR),
	EXISTE_PO_SEM_EVENTOS_NA_LICITACAO(570992, "Não existe Evento cadastrado para a PO ({0}).", ERROR),
	EXISTE_PO_SEM_FRENTE_DE_OBRA_NA_LICITACAO(570993, "Não existe Frente de Obra cadastrada para a PO ({0})", ERROR),
	VALOR_LICITADO_EXCEDE_VALOR_APROVADO(570994,
			"O valor do campo 'Total Licitado' de todas as PO(s) (R$ {0}) não pode exceder o valor total do {2} (R$ {1}).",
			ERROR),
	SERVICO_SEM_EVENTO_EXCEPTION(570995, "O serviço {0} da submeta {1} não possui evento associado.", ERROR),
	SERVICO_SEM_FRENTE_DE_OBRA_EXCEPTION(570996, "Não existe frente de obra associada ao serviço {0}", ERROR),

	EVENTO_SEM_SERVICO_ASSOCIADO(586175, "A(s) submeta(s) abaixo possui/possuem evento(s) sem serviços associados:<br> {0}",
			ERROR),

	FRENTE_DE_OBRA_SEM_QUANTIDADE_INFORMADA(586178,
			"A(s) Frente(s) de Obra da PO {0} listada(s) abaixo não possuem quantidades informadas:</br></br> <ul>{1}</ul>", ERROR),

	FRENTE_DE_OBRA_SERVICO_QUANTIDADE_ZERADA(730617,
			"A(s) Frente(s) de Obra da PO {0} listadas abaixo possuem quantidade de itens igual a zero:</br></br> {1}", ERROR),

	SERVICO_SEM_QUANTIDADES_INFORMADAS_EXCEPTION(570997, "O serviço {0} não possui quantidades informadas.", ERROR),

	QUANTIDADES_TOTAL_SERVICO_DIFERENTE_TOTAL_ACEITO_ANALISE_EXCEPTION(586181,
			"A quantidade total do serviço {0} da submeta {1} difere do total aceito na análise.", ERROR),

	POS_DE_UM_LOTE_COM_DATABASE_DIFERENTE(586542,
			"Não é possível ter valores diferentes para a data base em planilhas orçamentárias em um mesmo lote (Lote {0}):",
			"</br><h2>Lista das submetas e os valores da data base, conforme abaixo:</h2> </br></br> <ul>{1}</ul>",
			ERROR),

	ITENS_PO_DE_UM_LOTE_COM_PRECO_DIFERENTE(586543,
			"Não é possível ter preços unitários diferentes para itens de PO de um mesmo lote (Lote {0}), considerando a mesma Fonte e o mesmo Código",
			"</br><h2>Lista dos itens com mesma fonte e mesmo código, mas com preços unitários diferentes, conforme abaixo:</h2></br></br>"
					+ "<ul><li>Fonte: {1} - Código: {2}</li></ul>"
					+ "Lista dos itens com preços diferentes, conforme abaixo:</br></br><ul>{3}</ul>",
			ERROR),

	EXISTE_SUBMETA_CONTRAPARTIDA_NEGATIVA(619892, "Existe item da licitação com contrapartida negativa. Favor averiguar as submetas na aba Itens.", ERROR),

	VALIDACAO_DATABASE_ANALISE(612354, "Dado que a data base da licitação informada diverge da data base aceita na aprovação do Projeto Básico, "
			+"o orçamento para comparação informado na(s) Planilha(s) Orçamentária(s) licitada(s) não pode ser o Aceito na Análise. "
			+ "Favor averiguar os dados informados para a data base da licitação e orçamento para comparação na(s) Planilha(s) Orçamentária(s).", ERROR),

	VALIDACAO_DATABASE_LICITACAO(612310, "Dado que a data base da licitação informada é igual a data base aceita na aprovação do Projeto Básico, "
			+ "o orçamento para comparação informado na(s) Planilha(s) Orçamentária(s) licitada(s) não pode ser o Atualizado na Data Base da Licitação. "
			+ "Favor averiguar os dados informados para a data base da licitação e orçamento para comparação na(s) Planilha(s) Orçamentária(s).", ERROR),

	EXISTE_LOTE_COM_ORCAMENTOS_DE_REFERENCIA_DIFERENTE(620568,
			"Não é possível informar orçamentos de referência diferentes para um mesmo lote de licitação.",
			"</br>Abaixo, estão listadas as submetas cujas Planilhas Orçamentárias (POs) possuem orçamentos de referência que foram informados com valores diferentes."+
			"{0}",
			ERROR),

	CONSIDERACOES_OBRIGATORIO_COMPLEMENTACAO(5676671,
			"O campo \"Considerações\" é obrigatório ao Solicitar Complementação ao Convenente.", ERROR),
	PROPOSTA_COM_PARECER_EMITIDO(597714,
			"Atenção! A complementação ou ajuste dos documentos orçamentários referente à verificação do resultado do processo desta licitação só pode ser solicitada ao Convenente, se ainda não houver um parecer técnico emitido no sistema. Caso seja necessário a complementação desta documentação, será necessário cancelar a emissão do parecer, entrando na Aba do respectivo parecer e clicar no botão Cancelar Emissão.",
			ERROR),
	CONSIDERACOES_OBRIGATORIO_ACEITAR_INVIAVEL(5676672,
			"O campo \"Considerações\" é obrigatório ao Aceitar a Documentação Orçamentária e existe ao menos um Laudo inviável.",
			ERROR),
	CONSIDERACOES_OBRIGATORIO_REJEITAR_VIAVEL(5676673,
			"O campo \"Considerações\" é obrigatório ao Rejeitar a Documentação Orçamentária e existe ao menos um Laudo viável.",
			ERROR),
	CFF_COM_EVENTO_FALTANDO_ASSOCIACAO(570998,
            "Existem inconsistências no CFF da submeta abaixo:\n" +
			"<ul><li>Submeta {0}: na Tabela \"Visão Frentes de Obra por Evento\" não foi computado o período correspondente ao evento {1}, favor cadastrar o evento informando o período correspondente no CFF.</ul></li>",
			ERROR),
	PERIODO_CONCLUSAO_NAO_PREENCHIDO(517066,
			"Existe número do período de conclusão para frente de obra não preenchido no CFF por Evento. Evento: {0}.",
			ERROR),
	MACROSERVICO_SEM_PERCENTUAL_PARCELA(516489,
            "Existem inconsistências no CFF da submeta abaixo:\n" +
			"<ul><li>Submeta {0}: na Tabela \"Visão das Parcelas por Macrosserviço\" não foi computado o percentual e parcela correspondentes ao macrosserviço {1}, favor cadastrar o macrosserviço correspondente no CFF.</ul></li>",
			ERROR),

	TODOS_PERCENTUAIS_PARCELA_ZERO(570999,
			"Todos os valores percentuais das parcelas estão zerados no CFF sem Evento da submeta {0}.", ERROR),
	SOMATORIO_LICITACAO_EXCEDE_VALOR_APROVADO(571000,
			" O valor total {0} do QCI (R$ {1}) excede o valor {2} do {3} (R$ {4}). ", ERROR),
	TOTAIS_SUBMETA_EXCEDE_TOTAL_LICITADO(567909,
			"O valor do campo \"Total Licitado\" (R$ {0}) da PO \"{1}\" deve ser igual ao somatório (R$ {2}) dos valores de \"Repasse Licitado\", \"Contrapartida Licitada\" e \"Outros Licitado\" do QCI da respectiva submeta.",
			ERROR),
	SOMATORIO_POS_DIFERE_VALOR_CTEF(571003,
			"O somatório dos valores totais da(s) PO(s) licitada(s) deve ser igual ao valor da licitação cadastrado na funcionalidade de licitação do SICONV.",
			ERROR),
	MUDANCA_ESTADO_DOCUMENTACAO_NAO_PERMITIDA(5676620,
			"A mudança de estado solicitada da documentação não é permitida neste contexto. ", ERROR),

	// mensagens da regra de negócio 595818
	CANCELAMENTO_REJEICAO_LOTE_NAO_EXISTE_MAIS(5958181,
			"Não é possível realizar o cancelamento da rejeição, pois o(s) lote(s) {0} não existe(m) mais.", ERROR),
	CANCELAMENTO_REJEICAO_LOTE_JA_ASSOCIADO_OUTRA_LICITACAO(5958182,
			"Não é possível realizar o cancelamento da rejeição, pois, após a rejeição desta licitação, pelo menos um lote anteriormente utilizado foi associado a outra licitação, conforme a seguir:", "{0}", ERROR),
	CANCELAMENTO_REJEICAO_LOTE_NAO_MAIS_ASSOCIADO_MESMAS_SUBMETAS(5958183,
			"Não é possível realizar o cancelamento da rejeição, pois o(s) lote(s) de número {0} não está(estão) mais associado(s) ao mesmo conjunto de submetas do momento da rejeição desta licitação.",
			"{1}", ERROR),
	CANCELAMENTO_REJEICAO_LOTE_FOI_ASSOCIADO_A_LICITACAO_REJEITADA_POSTERIORMENTE(5958184,
			"Não é possível realizar o cancelamento da rejeição, pois pelo menos um lote está vinculado a uma outra licitação com rejeição posterior à esta licitação que se deseja cancelar a rejeição, conforme a seguir:", "{0}", ERROR),

    // mensagens cancelar aceite de documentos
	CANCELAMENTO_ACEITE_EXISTE_INSTRUMENTO_CONTRATUAL(698035,
			"Não é possível cancelar o aceite da licitação, pois já existe(m) o(s) instrumento(s) contratual(is) de nº {0} cadastrado(s) para esta licitação.", ERROR),
	CANCELAMENTO_ACEITE_EXISTE_DOCUMENTO_LIQUIDACAO(6980391,
			"Não é possível realizar o Cancelamento do Aceite da Licitação, pois há Documento(s) de Liquidação: {0} associado(s) à licitação.", ERROR),

	// mensagens laudos
	TEMPLATELAUDO_NAO_ENCONTRADO(1722393, "Template de Laudo não encontrado!", ERROR, NOT_FOUND),
	GRUPOPERGUNTA_NAO_ENCONTRADO(1722393, "Grupo de Pergunta não encontrado!", ERROR, NOT_FOUND),
	PERGUNTA_NAO_ENCONTRADA(1722393, "Pergunta não encontrada!", ERROR, NOT_FOUND),
	RESPOSTA_NAO_ENCONTRADA(1722393, "Resposta não encontrada!", ERROR, NOT_FOUND),
	PENDENCIA_NAO_ENCONTRADA(1722393, "Pendência não encontrada!", ERROR, NOT_FOUND),
	LAUDO_NAO_ENCONTRADO(1722393, "Laudo não encontrado!", ERROR, NOT_FOUND),
	LAUDOGRUPOPERGUNTA_NAO_ENCONTRADO(1722393, "Laudo Grupo Pergunta não encontrado!", ERROR, NOT_FOUND),
	USUARIO_NAO_PODE_ASSINAR_PARECER(1722393, "Usuário não pode assinar este Parecer!", ERROR),
	USUARIO_JA_ASSINOU_PARECER(1722393, "Usuário já assinou este Parecer!", ERROR),
	USUARIO_NAO_PODE_ALTERAR_PARECER(1722394, "Usuário Não pode Alterar Este Parecer!", ERROR),

	// Validacao data base
	SEM_CARGA_SINAPI_PARA_DATA_BASE(585574, "Não há dados SINAPI carregados para a data base {0}.", WARN),
	DATA_BASE_DIFERENTE_NO_MESMO_LOTE(581044,
			"Foi informada uma data base diferente em outra Planilha Orçamentária associada a este mesmo lote (Lote {0}).",
			WARN),
	SEM_CARGA_SINAPI_PARA_DATA_BASE_DIFERENTE_MESMO_LOTE(5855744,
			"Não há dados SINAPI carregados para a data base {0}. Foi informada uma data base diferente em outra Planilha Orçamentária associada a este mesmo lote (Lote {1}).",
			WARN),

	ERRO_NAO_EXISTE_CPS_VIGENTE_PARA_PROPOSTA(554737,
			"Prezado(a), para esta proposta, não existe um Contrato de Prestação de Serviço (CPS) vigente. \n"
					+ "Como não existe um CPS vigente para esta proposta será necessário que o\n"
					+ "Gestor de Convênio da Instituição Mandatária ou Concedente verifique e regularize a situação\n"
					+ "em que se encontra o cadastro do CPS no sistema, acessando o menu\n"
					+ "Cadastros e em seguida o item de menu Manter CPS.",
			ERROR),

	PROPOSTA_SEM_NIVEL_CPS_CORRESPONDENTE(562918,
			"Prezado(a), não existe um nível de proposta corresponde para o valor de repasse.\n"
			+ "Note que o não enquadramento do nível desta proposta de contrato de repasse\n"
			+ "na tabela referencial gera implicações na cobrança dos serviços do\n"
			+ "Contrato de Prestação de Serviços (CPS) e, consequentemente, nos eventos geradores de tarifa.\n"
			+ "Favor verifique esta proposta e adeque se necessário,\n"
			+ "para que o ateste do serviço correspondente seja gerado.",

			ERROR),

	CPS_PROPOSTA_DIFERENTE(554738,
			"Esse contrato de repasse (CR) está vinculado a um Contrato de Prestação de Serviço (CPS) diferente do(s) CPS vigente(s) para esse órgão, logo não será possível gerar a cobrança correspondente a essa ação. Para que seja possível gerar a cobrança corretamente, é necessário que o concedente realize a transferência do CR para o CPS ativo através do menu CPS, opção \"Transferir Contrato de Repasse\".",
			ERROR),

	CPS_EM_ADITIVACAO(554739, "Caro usuário, de acordo com a legislação vigente, essa ação só poderá ser executada após o aditamento do Contrato de Prestação de Serviço - CPS."
			+ " Portanto, para que seja possível gerar a cobrança corretamente, é necessário que o concedente registre a aditivação do CPS na Plataforma +Brasil. Favor contatar"
			+ " o concedente e informá-lo da necessidade do aditamento do CPS.",
			ERROR),

	NAO_EXISTE_CPS_VIGENTE(554737,
            "Não existe um Contrato de Prestação de Serviço (CPS) vigente nesse órgão, logo não será possível gerar a cobrança correspondente a essa ação. Para que seja possível gerar a cobrança corretamente, é necessário que o Gestor de Convênio da Instituição Mandatária ou Concedente verifique e regularize a situação em que se encontra o CPS desse órgão na Plataforma +Brasil.",
            ERROR),

	CPS_NAO_ATIVO_VIGENTE(721749,
			"Não existe um Contrato de Prestação de Serviço (CPS) ativo nesse órgão, logo não será possível realizar a vinculação do Contrato de Repasse a um CPS e, consequentemente, gerar a cobrança correspondente a essa ação. Para prosseguir, é necessário que o Gestor de Convênio da Instituição Mandatária ou Concedente regularize a situação em que se encontra o CPS desse órgão na Plataforma +Brasil.",
			ERROR),

	CPS_ERRO_INTEGRACAO_SICONV(554740, "Erro de integração do CPS com o Serviço Siconv.", ERROR),

	// Mensagens email
	ERRO_ENVIO_EMAIL(515884, "Não foi possível enviar o e-mail para os destinatários.", WARN),
	EVENTO_FRENTE_OBRA_SEM_MES(515884, "A PO \"{0}\" possui evento de frente de obra sem mês de conclusão.", ERROR),
	ERRO_CAMPO_OBRIGATORIO_LAUDO(515885, "O campo \"{0}\" deve ser preenchido!", ERROR),
	ERRO_CAMPO_OBRIGATORIO_LAUDO_COM_SESSAO(515885, "O campo \"{0}\" {1} deve ser preenchido!", ERROR),
	ERRO_TAMANHO_INVALIDO_LAUDO(515885,
			"A quantidade de caracteres informada para o campo \"{0}\" da seção \"{1}\" excede o limite definido de 1500 caracteres!",
			ERROR),
	ERRO_TAMANHO_INVALIDO_JUSTIFICATIVA_LAUDO(515885,
			"A quantidade de caracteres informada para o campo \"{0}\" excede o limite definido de 1500 caracteres!",
			ERROR),
	ERRO_JUSTIFICAR_CAUSA_INVIABILIDADE_LAUDO(515885, "Justificar a causa de inviabilidade.", ERROR),
	ERRO_COMENTARIO_OBRIGATORIO_PARA_SECAO(566904_1,
			"O comentário é obrigatório na seção \"{0}\", pela existência de resposta(s) que não está(estão) de acordo com o esperado.",
			ERROR),
	ERRO_JUSTIFICATIVA_OBRIGATORIA_COM_RESPOSTA_NAO_SE_APLICA(607837,
			"A justificativa é obrigatória devido à existência da opção \"Não se aplica\" marcada na(s) seção(ões) \"{0}\".",
			ERROR),

	ERRO_AO_ACIONAR_SERVICO_NO_MODULO_DE_LICITACAO(548401, "Erro ao acionar serviço no módulo de Licitação.", ERROR),
	ERRO_AO_ACIONAR_SERVICO_NO_MODULO_SICONV(548402, "Erro ao acionar serviço no módulo Siconv.", ERROR),

	ERRO_AO_ACIONAR_SERVICO_GPRC_CPS(548401,
			"Erro ao acionar serviço no módulo de CPS (Contrato de Prestação de Serviço).",
			ERROR),

	ERRO_AO_ACIONAR_SERVICO_VERIFICACAO_DOC_LIQUIDACAO(698040,
			"Erro ao acionar serviço de verificação da existência de Documento de Liquidação.",
			ERROR),

	NAO_EH_POSSIVEL_CANCELAR_ACEITE(603909,
            "Não é possível realizar o cancelamento da rejeição, pois há {0} à licitação.", ERROR),
	EVENTO_NAO_ENCONTRADO_SERVICO(610000,"Este Serviço deve estar associado a um Evento Obrigatoriamente!", ERROR),
	NAO_EH_POSSIVEL_SALVAR_ASSOCIACAO(99998, "Não é possível salvar a associação em uma licitação na situação {0} por um usuário com perfil {1} e proposta versão atual {2}.", ERROR),
	ATRIBUICAO_RESPONSAVEL_NAO_INFORMADA(
			99999,
			"É necessário informar a Atribuição do Responsável para Aceitar ou Rejeitar a licitação.", ERROR),

	FORNECEDORES_OBSOLETOS(753047,
			"Foi detectada uma inconsistência e os Fornecedores abaixo não estão mais vinculados a uma ou mais Licitações na funcionalidade Processo de Execução conforme abaixo:</br> {0}. </br></br>",
			ERROR),

	FORNECEDORES_OBSOLETOS_PROPONENTE_EPE_COM(753048,
			"Antes do envio para análise, será necessário efetuar os ajustes dos fornecedores na Aba \"Associação a Lote\" da(s) respectiva(s) VRPL(s).",
			ERROR),

	FORNECEDORES_OBSOLETOS_MANDA_CONCE(753049,
			"Antes de aceitar ou rejeitar esta VRPL, será necessário que o Convenente efetue os ajustes dos fornecedores na Aba \"Associação a Lote\".",
			ERROR),

	FORNECEDORES_OBSOLETOS_OUTROS(753050,
			"Será necessário, que o Convenente, faça ajustes dos fornecedores na VRPL desta licitação.",
			ERROR),

	FORNECEDORES_OBSOLETOS_PROPONENTE_EAN(753051,
			"Será necessário efetuar os ajustes dos fornecedores na Aba \"Associação a Lote\". Para tanto, retorne a VRPL para ajuste por meio do botão Cancelar envio para análise na Aba Quadro Resumo.",
			ERROR),

	FORNECEDORES_OBSOLETOS_PROPONENTE_ANL(753052,
			"Antes que o Concedente/Mandatária faça o aceite ou rejeição desta VRPL, será necessário ajustes, por parte do Convenente, dos fornecedores na Aba \"Associação a Lote\".",
			ERROR),

	FORNECEDORES_OBSOLETOS_MANDA_CONCE_ANL(753053,
			"Antes de aceitar ou rejeitar esta VRPL, será necessário solicitar complementação para que o Convenente efetue os ajustes dos fornecedores na Aba \"Associação a Lote\".",
			ERROR),

	FORNECEDORES_OBSOLETOS_PROPONENTE_SCP(753054,
			"Será necessário efetuar os ajustes dos fornecedores na Aba \"Associação a Lote\". Para tanto, inicie a complementação desta VRPL para ajuste do fornecedor por meio do botão Iniciar Complementação na Aba Quadro Resumo.",
			ERROR),

	NAO_EXISTE_FORNECEDOR_VENCEDOR(753055,
			"Não existe um fornecedor vencedor cadastrado no módulo SICONV para a Licitação {0}",
			ERROR),

	FORNECEDORES_OBSOLETOS_REJEITAR(753056,
			"Antes de rejeitar esta VRPL, será necessário solicitar complementação para que o Convenente efetue os ajustes dos fornecedores na aba \"Associação a Lote\".",
			ERROR),

	FORNECEDORES_OBSOLETOS_ENVIO_ANALISE_ACEITE(753057,
		"Portanto, antes de efetuar esta ação, será necessário que o Convenente faça os devidos ajustes dos fornecedores na aba \"Associação a Lote\".",
		ERROR),

	FORNECEDOR_OBSOLETO_SELECIONADO(753058,
		"O fornecedor selecionado não está mais vinculado a esta licitação na funcionalidade Processo de execução, por favor selecione um fornecedor válido.",
		ERROR),

	LICITACAO_COM_APOSTILAMENTO(777324,
			"Foi cadastrado um apostilamento do tipo \"Processo de execução sem VRPL\" para a Licitação selecionada. Para que esta licitação seja vinculada a VRPL, será necessário cancelar ou excluir o apostilamento cadastrado ou então, selecione, se for o caso, uma outra Licitação."
			, ERROR),

	INCONSISTENCIA_VRPL_LICITACAO_COM_APOSTILAMENTO(777320,
			"Foi gerada uma inconsistência na VRPL, pelo fato de ter sido cadastrado um apostilamento do tipo \"Processo de execução sem VRPL\" para a Licitação nº {0}. Para resolver esta inconsistência na VRPL, será necessário cancelar ou excluir o apostilamento nº {1} do tipo \"Processo de execução sem VRPL\"."
			, ERROR)
	;

	private final Integer codigoRN;
	private final String mensagem;
	private String detail;
	private final Severity severity;

	// * Opcional */
	private Status codigoHttp;

	ErrorInfo(final Integer codigoRN, final String mensagem, final Severity severity) {
		this.codigoRN = codigoRN;
		this.mensagem = mensagem;
		this.severity = severity;
	}

	ErrorInfo(final Integer codigoRN, final String mensagem, final String detail, final Severity severity) {
		this.codigoRN = codigoRN;
		this.mensagem = mensagem;
		this.severity = severity;
		this.detail = detail;
	}

	ErrorInfo(final Integer codigoRN, final String mensagem, final Severity severity, final Status codigoHttp) {
		this(codigoRN, mensagem, severity);
		this.codigoHttp = codigoHttp;
	}

	public Integer getCodigo() {
		return codigoRN;
	}

	public String getMensagem() {
		return mensagem;
	}

	public Severity getSeverity() {
		return severity;
	}

	public Status getCodigoHttp() {
		return codigoHttp;
	}

	public String getDetail() {
		return detail;
	}

}
