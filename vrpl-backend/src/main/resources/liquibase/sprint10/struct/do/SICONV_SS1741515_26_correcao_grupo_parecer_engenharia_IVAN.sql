SET LOCAL vrpl.cpf_usuario TO 'SISTEMA';

INSERT INTO siconv.vrpl_grupo_pergunta (numero,                                           titulo, in_grupo_obrigatorio, template_fk, versao_nr, versao_nm_evento, versao_id,    adt_login, adt_operacao, versao,                         adt_data_hora) 
                                VALUES (     9,    'Cronograma Físico-Financeiro lote engenharia',                true,         1,             0,            'INI',         1,'SISTEMA',     'INSERT',       0,  to_date ('17/08/2020', 'DD/MM/YYYY'));

UPDATE siconv.vrpl_pergunta
	SET grupo_fk = (select id from siconv.vrpl_grupo_pergunta where numero = 9 and template_fk=1) ,
	versao = versao + 1
	WHERE id in (select distinct p.id
					from siconv.vrpl_pergunta p, 
     				siconv.vrpl_grupo_pergunta g
					where 
   					g.template_fk = 1 and 
  					p.grupo_fk = g.id and 
   					(p.numero = 6 or p.numero = 7));
