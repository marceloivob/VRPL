/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 04-10-2021
 * AUTOR: Daniel Augusto de Alcântara neto
 * OBJETIVO: Aplicar Grant de Delete na tabela vrpl_servico_frente_obra_analise
 * Demanda 2891174 - [CORRETIVA] Verificação de problema no preenchimento da PO na VRPL. Convênio: 897198
 *********************************************************************************************************/

GRANT DELETE ON TABLE siconv.vrpl_servico_frente_obra_analise TO usr_siconv_p;
