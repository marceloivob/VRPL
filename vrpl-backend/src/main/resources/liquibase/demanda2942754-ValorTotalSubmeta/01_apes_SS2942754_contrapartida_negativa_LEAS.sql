/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 04-01-2022
 * AUTOR: Leo Assis
 * OBJETIVO: Atualizar a coluna vl_contrapartida da tabela siconv.vrpl_submeta das submetas com valor
 *    de contrapartida negativo.
 *
 * Demanda 2942754 - EVOLUTIVA - Na carga das informações do PB puxar os seguintes valores para a VRPL
 *********************************************************************************************************/

do $$
declare
   submetas record;
   contador integer := 0;
begin
	
set local vrpl.cpf_usuario TO 'APES2942754';

for submetas in 
	
	select vsub.id as id_submeta,
		vsub.vl_total_licitado as submeta_vl_total_licitado,
		vsub.vl_repasse as submeta_vl_repasse,
		vsub.vl_contrapartida as submeta_vl_contrapartida,
		vsub.vl_outros as submeta_vl_outros

	from siconv.vrpl_submeta vsub
		inner join siconv.vrpl_proposta vprop on vprop.id = vsub.proposta_fk 
	
	group by vsub.id, vsub.vl_total_licitado
	having vsub.vl_contrapartida < 0
	order by vsub.id
		 
loop

	raise info 'Submeta com id % e vl_total_licitado %, vl_repasse %, vl_contrapartida %, vl_outros %',
		submetas.id_submeta,
		submetas.submeta_vl_total_licitado,
		submetas.submeta_vl_repasse,
		submetas.submeta_vl_contrapartida,
		submetas.submeta_vl_outros;

	--update siconv.vrpl_submeta
	--set vl_repasse = submetas.submeta_vl_total_licitado - submetas.submeta_vl_outros,
	--	vl_contrapartida = 0,
	--	versao = versao+1
	--where id = submetas.id_submeta;
	
	raise info 'Submeta ATUALIZADA id % e vl_total_licitado %, vl_repasse %, vl_contrapartida %, vl_outros %',
		submetas.id_submeta,
		submetas.submeta_vl_total_licitado,
		(submetas.submeta_vl_total_licitado - submetas.submeta_vl_outros),
		0,
		submetas.submeta_vl_outros;

	contador := contador + 1;
	
end loop;

	raise info 'Numero de submetas impactadas pela APES %', contador;

end; $$

