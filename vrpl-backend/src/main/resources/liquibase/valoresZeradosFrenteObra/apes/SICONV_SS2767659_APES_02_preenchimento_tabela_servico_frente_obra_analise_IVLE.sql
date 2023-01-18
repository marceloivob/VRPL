/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 23-08-2021
 * AUTOR: Ivan Leite
 * OBJETIVO: Preencher a tabela siconv.vrpl_servico_frente_obra_analise
 * Demanda 2767659 - [EVOLUTIVA] Não permitir que o valor aceito na análise fique zerado e nos demais
 *    campos cujo valor vem do projeto básico e não deve ser alterado e que o CFF da VRPL fiquei igual
 *    ao CFF do Projeto Básico
 *********************************************************************************************************/

do $$
declare
   rec record;
   counter integer := 1;
  
BEGIN
	
SET LOCAL vrpl.cpf_usuario TO 'APES2767659';

select count(*) into counter
from siconv.vrpl_servico_frente_obra_analise;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA_ANALISE %', counter;
-- consultar registros da tabela do projeto basico
FOR rec IN
	SELECT distinct 
	     
	    serv.id,  	
	    srv_frente.frente_obra_fk,
	    srv_frente.qt_itens,
	    frente_obra.nr_frente_obra,
	    frente_obra.nm_frente_obra,
	    serv.versao_nr,
            serv.versao_id,
            serv.versao_nm_evento				   
	FROM
	    siconv.acffo_servico_frente_obra srv_frente,
	    siconv.acffo_frente_obra frente_obra,
	    siconv.vrpl_servico serv	
	WHERE  		                                                                                   
	    srv_frente.frente_obra_fk = frente_obra.id   and
	    srv_frente.servico_fk = serv.id_servico_analise order by id, frente_obra_fk
LOOP
--insere registros coletados da consulta acima 
INSERT INTO siconv.vrpl_servico_frente_obra_analise 
(nm_frente_obra, nr_frente_obra, qt_itens, servico_fk, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) 
VALUES 
(  rec.nm_frente_obra,  rec.nr_frente_obra,  rec.qt_itens,  rec.id,  rec.versao_id,  rec.versao_nm_evento,  rec.versao_nr, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT');
	
END LOOP;

select count(*) into counter
from siconv.vrpl_servico_frente_obra_analise;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA_ANALISE %', counter;


end; $$












