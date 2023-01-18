/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 27-09-2021
 * AUTOR: Leonardo de Assis
 * OBJETIVO: Apontar os anexos listados para os buckets corretos.
 * Demanda 2759275 - [CORRETIVA] Erro ao clicar em anexos do VRPL. Convênio: 888920.
 *********************************************************************************************************/

SET LOCAL vrpl.cpf_usuario TO 'APES2759275';

select 'Anexos que serão afetados pela APES ', *
from siconv.vrpl_anexo
where id in (13921,13756,13757,71745,71744,71743,71742,71741,71740,71739,71738,71737,71736,71735,71734,
    57020,62081,62080,62079,53665,53664,53663,53661,53660,53658,53650,53649,53648,53647,53641,53639,53635);

update siconv.vrpl_anexo
set bucket = 'mandatarias-06-2021', versao = versao+1
where id in (13921,13756,13757,71745,71744,71743,71742,71741,71740,71739,71738,71737,71736,71735,71734);

update siconv.vrpl_anexo
set bucket = 'mandatarias-08-2021', versao = versao+1
where id in (57020,62081,62080,62079,53665,53664,53663,53661,53660,53658,53650,53649,53648,53647,53641,53639,53635);

select 'Anexos que foram afetados pela APES ', *
from siconv.vrpl_anexo
where id in (13921,13756,13757,71745,71744,71743,71742,71741,71740,71739,71738,71737,71736,71735,71734,
    57020,62081,62080,62079,53665,53664,53663,53661,53660,53658,53650,53649,53648,53647,53641,53639,53635);
