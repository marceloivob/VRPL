SET LOCAL vrpl.cpf_usuario TO 'SISTEMA';

INSERT INTO siconv.vrpl_template_laudo (   tipo, versao_id, versao_nm_evento, versao_nr, versao, adt_login,                        adt_data_hora, adt_operacao) 
                                VALUES ('VRPLS',         0,             NULL,         0,      0, 'SISTEMA', to_date ('30/08/2019', 'DD/MM/YYYY'),     'INSERT');

INSERT INTO siconv.vrpl_grupo_pergunta (numero,                      titulo, in_grupo_obrigatorio,                                                                  template_fk, versao_nr, versao_nm_evento, versao_id,adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     1, 'Dados Gerais da Licitação',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                                titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     2, 'Quadro Resumo de Metas da Licitação',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                         titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     3, 'Aspectos Gerais da Licitação',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                        titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     4, 'Aspectos Gerais do Lote',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,              titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     5, 'Relação de Anexos',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,              titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     6, 'Anexos do Parecer',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                             titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     7, 'Pendências e Prazos para Solução',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,      titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     8, 'Conclusão',                 true, (select laudo.id from siconv.vrpl_template_laudo laudo where tipo = 'VRPLS'),         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));

--LICITACAO
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                             titulo, tipo_resposta, valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     1, 'O processo licitatório, para Trabalho Social, foi específico e ocorreu em separado do das obras?',       'LISTA',      'Sim;Não',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 3 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');
						  
INSERT INTO siconv.vrpl_pergunta (numero,                                                  titulo, tipo_resposta,          valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     2, 'O objeto licitado é compatível com o objeto aprovado?',       'LISTA', 'Sim;Não;Não se aplica',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 3 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');
						  
-- LOTE
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                      titulo, tipo_resposta, valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     1, 'A equipe técnica responsável pelo Trabalho Social está compatível com o projeto aprovado?',       'LISTA',      'Sim;Não',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');

INSERT INTO siconv.vrpl_pergunta (numero,                                                                          titulo, tipo_resposta,          valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     2, 'O cronograma físico-financeiro é compatível com o aprovado no Projeto ou TR ?',       'LISTA', 'Sim;Não;Não se aplica',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');

INSERT INTO siconv.vrpl_pergunta (numero,                                                                             titulo, tipo_resposta,          valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     3, 'O percentual global das Despesas Indiretas atende aos parâmetros estabelecidos ?',       'LISTA', 'Sim;Não;Não se aplica',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');

INSERT INTO siconv.vrpl_pergunta (numero,                                                                           titulo, tipo_resposta,          valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     4, 'Em caso negativo, foi apresentado relatório técnico circunstanciado e aceito ?',       'LISTA', 'Sim;Não;Não se aplica',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');						  

INSERT INTO siconv.vrpl_pergunta (numero,                                                                           titulo, tipo_resposta, valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     5, 'O valor total do lote é menor ou igual ao orçamento utilizado para comparação?',       'LISTA',      'Sim;Não',          'Sim', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');						  

INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                                                  titulo, tipo_resposta, valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     6, 'O preço unitário de cada item do orçamento da empresa vencedora da licitação é menor ou igual ao orçamento utilizado para comparação?',       'LISTA',      'Sim;Não',          'Sim', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');						  

INSERT INTO siconv.vrpl_pergunta (numero,                                                       titulo, tipo_resposta,          valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     7, 'O cronograma está compatível com o orçamento apresentado ?',       'LISTA', 'Sim;Não;Não se aplica',          'Sim', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');						  

INSERT INTO siconv.vrpl_pergunta (numero,        titulo, tipo_resposta, valor_resposta, valor_esperado,                                                                                                                                                                  grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
						  VALUES (     8, 'Comentários',       'TEXTO',             '',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id),        0,      NULL,             NULL,      0,     'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');						  

INSERT INTO siconv.vrpl_pergunta (numero,                                                             titulo, tipo_resposta,    valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
 	  				      VALUES (     1, 'Considera-se a verificação do resultado do processo licitatório:',       'LISTA', 'Viável;Inviável',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 8 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,           titulo, tipo_resposta, valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
 						  VALUES (     2,  'Justificativa',       'TEXTO',             '',             '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 8 and templ.tipo = 'VRPLS' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');