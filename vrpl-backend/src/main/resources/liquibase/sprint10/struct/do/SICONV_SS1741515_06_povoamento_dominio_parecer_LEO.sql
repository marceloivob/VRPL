SET LOCAL vrpl.cpf_usuario TO 'SISTEMA';

DELETE FROM siconv.vrpl_pergunta;
DELETE FROM siconv.vrpl_grupo_pergunta;

INSERT INTO siconv.vrpl_grupo_pergunta (numero,                      titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     1, 'Dados Gerais da Licitação',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                                titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     2, 'Quadro Resumo de Metas da Licitação',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                         titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     3, 'Aspectos Gerais da Licitação',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                        titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     4, 'Aspectos da Análise do Lote',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,              titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     5, 'Relação de Anexos',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,              titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     6, 'Anexos do Parecer',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,                             titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     7, 'Pendências e Prazos para Solução',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));
INSERT INTO siconv.vrpl_grupo_pergunta (numero,      titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     8, 'Conclusão',                 true,           1,         0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/05/2019', 'DD/MM/YYYY'));


INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
                          VALUES (     1, 'Foi apresentada declaração de que o edital da licitação contempla todos os elementos necessários abaixo dispostos?',       'LISTA', 'Sim;Não;Não se aplica',             '',        (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 3 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id),         0,      NULL,             NULL,      0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'),     'INSERT');

INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (1, 'Orçamento utilizado para comparação', 'TEXTO', '', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (2, 'O valor total do lote %s é menor ou igual ao orçamento utilizado para comparação?', 'LISTA', 'Sim;Não', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (3, 'O preço unitário de cada item do orçamento da empresa vencedora da licitação é menor ou igual ao orçamento utilizado para comparação?', 'LISTA', 'Sim;Não', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (4, 'Comentários', 'TEXTO', '', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (5, 'Previsão de término', 'TEXTO', '', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (6, 'O cronograma cumpre as exigências do Programa (prazo máximo de construção, percentual mínimo nas últimas parcelas, etc.)?', 'LISTA', 'Sim;Não;Não se aplica', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (7, 'Comentários', 'TEXTO', '', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 4 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');

INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (1, 'Considera-se a verificação do resultado do processo licitatório', 'LISTA', 'Viável;Inviável', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 8 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
INSERT INTO siconv.vrpl_pergunta (numero,                                                                                                               titulo, tipo_resposta,          valor_resposta, valor_esperado, grupo_fk, versao_nr, versao_id, versao_nm_evento, versao,     adt_login,                        adt_data_hora, adt_operacao)
								VALUES (2, 'Justificativa', 'TEXTO', '', '', (select grupo.id from siconv.vrpl_grupo_pergunta grupo, siconv.vrpl_template_laudo templ where grupo.numero = 8 and templ.tipo = 'VRPL' and grupo.template_fk = templ.id), 0, NULL, NULL, 0, 'SISTEMA', to_date ('17/05/2019', 'DD/MM/YYYY'), 'INSERT');
