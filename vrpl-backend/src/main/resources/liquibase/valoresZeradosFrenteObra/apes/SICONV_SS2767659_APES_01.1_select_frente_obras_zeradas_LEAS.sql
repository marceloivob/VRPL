/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 18-08-2021
 * AUTOR: Leonardo de Assis
 * OBJETIVO: Apagar registros das tabelas SERVICO_FRENTE_OBRA e EVENTO_FRENTE_OBRA com frentes de obra
 *    com quantidade de itens zeradas no VRPL
 * Demanda 2767659 - [EVOLUTIVA] Não permitir que o valor aceito na análise fique zerado e nos demais
 *    campos cujo valor vem do projeto básico e não deve ser alterado e que o CFF da VRPL fiquei igual
 *    ao CFF do Projeto Básico
 *********************************************************************************************************/

SET LOCAL vrpl.cpf_usuario TO 'APES2767659';

select efo.*
from siconv.vrpl_evento_frente_obra efo
where 0 = (
	select count(sfo.frente_obra_fk)
	from siconv.vrpl_servico_frente_obra sfo
		inner join siconv.vrpl_servico s on s.id = sfo.servico_fk 
	where sfo.frente_obra_fk = efo.frente_obra_fk
		and s.evento_fk = efo.evento_fk 
);



