/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 21-10-2021
 * AUTOR: Bruno Henrique Prybecz
 * OBJETIVO: Criar uma chave única no número da versão para evitar duplicação de propostas.
 * Demanda 2907252: [CORRETIVA] Licitação duplicada VRPL. Convênio: 908643
 *********************************************************************************************************/

ALTER TABLE siconv.vrpl_proposta ADD CONSTRAINT uk_vrpl_proposta_versao UNIQUE (id_siconv, versao_nr);
