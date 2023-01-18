/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 21-06-2022
 * AUTOR: Jose Augusto
 * OBJETIVO: Regressão da situação Aguardando Aceite no siconv para as licitações Em Preenchimento e Em Complementação no VRPL
 *
 * Demanda 3207076 - [EVOLUTIVA] - VRPL - Integrar com Processo de Execução para que em determinadas situações envie um comando de estorno do envio para aceite no SICONV
 *********************************************************************************************************/
DO 
$$DECLARE 

registros_modificados	integer;
r_rec    		record; 
cur                     refcursor;  

begin
	registros_modificados        := 0;
   
	raise notice 'APES/TRE - Tabela siconv.licitacao - BANCO: % - %', current_database(), to_char(CURRENT_TIMESTAMP, 'dd/mm/yyyy hh24:mi:ss');
	raise notice ' ';
	raise notice '+---------------+----------------';
	raise notice '|  Proposta_fk  |  Id_Licitacao  ';
	raise notice '+---------------+----------------';



	open cur for execute 'select distinct l.id as id_l, l.prop_fk as prop_fk_l from siconv.licitacao l 
	inner join siconv.vrpl_proposta vp on l.prop_fk = vp.id_siconv 
	inner join siconv.vrpl_licitacao vl on vl.id_licitacao_fk = l.id and vl.proposta_fk = vp.id
	where vl.in_situacao in ('||chr(39)||'EPE'||chr(39)||','||chr(39)||'COM'||chr(39)||')
	and l.situacao_aceite_processo_execu = '||chr(39)||'AGUARDANDO_ACEITE'||chr(39)||'
	and vp.versao_in_atual
	order by l.prop_fk, l.id';
	
		loop
		fetch next from cur into r_rec;
		exit when r_rec is null;

			begin
			
				update siconv.licitacao set 
				situacao_aceite_processo_execu = null,
				adt_login = 'APES_3207076', 
				adt_data_hora = now(),
				adt_operacao = 'AE SS3207076'
				where id = r_rec.id_l and prop_fk = r_rec.prop_fk_l;
				
				insert into licitacao_log_rec ( 
					id, adt_data_hora, adt_login, 
					adt_operacao, ano, ataregistroprecoslog, 
					caput, cpf_homologador, cpf_homologador_cotacao, 
					cpf_responsavel_aceite, data_abertura_licitacao, data_analise_aceite, 
					data_encerramento_cotacao, data_encerramento_licitacao, data_homologacao, 
					data_hora_registro_analise_ace, data_inicio_cotacao, data_publicacao, 
					data_realizacao_licitacao, data_solicitacao_dispensa, entiy_id, 
					funcao_responsavel, funcao_responsavel_aceite, fundamento_legal, 
					hibernate_version, id_externo, inciso, 
					inciso_dispensa, incluida_por_executor, justificativa, 
					justificativa_aceite, modalidade, municipiolog, 
					nome_responsavel, numero, numero_cotacao, 
					numero_dispensa, numero_inexigbilidade, numero_licitacao, 
					numero_pesquisa_mercado, numero_processo, numero_processo_cotacao, 
					objeto, origem_recurso, processo_compra, 
					propostalog, recurso_financeiro, sistema_origem_fk, 
					situacao_aceite_processo_execu, situacao_licitacao_sc_fk, srp, 
					status, tipo_compra, tipo_licitacao, 
					tipo_processo_execucao, valor, valor_cotacao, 
					valor_dispensa
				) select
					(nextval('licitacao_log_rec_seq')), l.adt_data_hora, l.adt_login, 
					l.adt_operacao, l.ano, l.ata_registro_precos_fk, 
					l.caput, l.cpf_homologador, l.cpf_homologador_cotacao, 
					l.cpf_responsavel_aceite, l.data_abertura_licitacao, l.data_analise_aceite, 
					l.data_encerramento_cotacao, l.data_encerramento_licitacao, l.data_homologacao, 
					l.data_hora_registro_analise_ace, l.data_inicio_cotacao, l.data_publicacao, 
					l.data_realizacao_licitacao, l.data_solicitacao_dispensa, l.id, 
					l.funcao_responsavel, l.funcao_responsavel_aceite, l.fundamento_legal, 
					l.hibernate_version, l.id_externo, l.inciso, 
					l.inciso_dispensa, l.incluida_por_executor, l.justificativa, 
					l.justificativa_aceite, l.modalidade, l.municipio_fk, 
					l.nome_responsavel, l.numero, l.numero_cotacao, 
					l.numero_dispensa, l.numero_inexigbilidade, l.numero_licitacao, 
					l.numero_pesquisa_mercado, l.numero_processo, l.numero_processo_cotacao, 
					l.objeto, l.origem_recurso, l.processo_compra,
					l.prop_fk, l.recurso_financeiro, l.sistema_origem_fk,
					l.situacao_aceite_processo_execu, l.situacao_licitacao_sc_fk, l.srp,
					l.status, l.tipo_compra, l.tipo_licitacao, 
					l.tipo_processo_execucao, l.valor, l.valor_cotacao, 
					l.valor_dispensa
					from licitacao l where l.id = r_rec.id_l and l.prop_fk = r_rec.prop_fk_l;
				
				insert into siconv.hist_aceite_processo_execucao (
					id, 
					hibernate_version,	
					adt_login,
					adt_data_hora, 
					adt_operacao, 
					cpf_responsavel,
					data_hora_registro_analise,
					situacao_aceite_processo_execu, 
					justificativa,
					licitacao_fk
				) values (
					nextval('siconv.hist_aceite_processo_execucao_seq'), --id
					0, --hibernate_version,
					'APES_3207076', --adt_login
					now(), --adt_data_hora
					'AE SS3207076', --adt_operacao				
					'sistema', --cpf_responsavel
					now(), --data_hora_registro_analise
					null, --situacao_aceite_processo_execu 
					'AE 3207076 - [APES]   (ESTORNO) - Estorno do Aceite realizado', --justificativa
					r_rec.id_l -- licitacao_fk
				);
				
				insert into hist_aceite_processo_execucao_log_rec ( 
					id , 
					hibernate_version, 
					adt_login, 
					adt_data_hora,
					adt_operacao, 
					entiy_id, 
					cpf_responsavel, 
					data_hora_registro_analise, 
					situacao_aceite_processo_execu, 
					justificativa, 
					licitacaolog
				) select 
					(nextval('hist_aceite_processo_execucao_log_rec_seq')), --id
					0, --hibernate_version
					hape.adt_login,
					hape.adt_data_hora, 
					hape.adt_operacao, 
					hape.id,
					hape.cpf_responsavel, 
					hape.data_hora_registro_analise , 
					hape.situacao_aceite_processo_execu , 
					hape.justificativa , 
					hape.licitacao_fk 
					from hist_aceite_processo_execucao hape where hape.id = (currval('hist_aceite_processo_execucao_seq'));
				
				registros_modificados := registros_modificados + 1;
				raise notice '|   %    |   % ', to_char(r_rec.prop_fk_l,'9999999'), to_char(r_rec.id_l,'9999999')  ;
				
			end;
			
		end loop;
		
	close cur;
	raise notice '+---------------+----------------';
	raise notice '========================================================';
	raise notice ' *** Registros Modificados : %',registros_modificados;
	raise notice '========================================================';
	raise notice 'APES - BANCO: % - %', current_database(), to_char(CURRENT_TIMESTAMP, 'dd/mm/yyyy hh24:mi:ss');

end$$;

