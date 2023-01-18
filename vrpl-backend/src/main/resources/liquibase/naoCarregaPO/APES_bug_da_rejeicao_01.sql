/*********************************************************************************************************
 * SERVIÇO FEDERAL DE PROCESSAMENTO DE DADOS - SERPRO
 * Superintendência de Desenvolvimento - SUPDE
 * Polo de Desenvolvimento em Recife
 *
 * ========================================= DADOS DO SCRIPT =============================================
 * DATA DE CRIAÇÃO: 01-10-2021
 * AUTOR: Leo Assis
 * OBJETIVO: Preencher a tabela siconv.vrpl_servico_frente_obra_analise e siconv.vrpl_servico_frente_obra
 * para as propostas com o bug da rejeição.
 *
 * Demanda 2891174 - [CORRETIVA] Verificação de problema no preenchimento da PO na VRPL. Convênio: 897198
 *********************************************************************************************************/

do $$
declare
   rec record;
   counter integer := 1;
   servicos record;
   n_inseridos integer := 1;
   id_fo bigint;
begin
	
set local vrpl.cpf_usuario TO 'APES2891174';

select count(*) into counter
from siconv.vrpl_servico_frente_obra;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA %', counter;

select count(*) into counter
from siconv.vrpl_servico_frente_obra_analise;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA_ANALISE %', counter;

-- Usar id siconv das Propostas que devem ser impactadas pela APES
for servicos in 
	select distinct vss.*
	from siconv.vrpl_proposta vp 
		inner join siconv.vrpl_submeta vs on vs.proposta_fk = vp.id
		inner join siconv.vrpl_po vpp on vpp.submeta_fk = vs.id
		inner join siconv.vrpl_macro_servico vm on vm.po_fk = vpp.id
		inner join siconv.vrpl_servico vss on vss.macro_servico_fk = vm.id
	where vp.versao_in_atual = true and
		vp.id_siconv in (
			1539289
		)
		 
loop
	--raise info 'Servico impactado pela APES: % % % %', servicos.id, servicos.nr_servico, servicos.cd_servico, servicos.tx_descricao;

	for rec in
		select distinct
		    serv.id,  	
		    srv_frente.frente_obra_fk,
		    srv_frente.qt_itens,
		    frente_obra.nr_frente_obra,
		    frente_obra.nm_frente_obra,
		    serv.versao_nr,
	        serv.versao_id,
	        serv.versao_nm_evento				   
		from
		    siconv.acffo_servico_frente_obra srv_frente,
		    siconv.acffo_frente_obra frente_obra,
		    siconv.vrpl_servico serv	
		where  		                                                                                   
		    srv_frente.frente_obra_fk = frente_obra.id   and
		    srv_frente.servico_fk = serv.id_servico_analise	and
		    serv.id = servicos.id
	loop
	
		select count(*) into n_inseridos
		from siconv.vrpl_servico_frente_obra_analise vsfoa
		where vsfoa.nm_frente_obra = rec.nm_frente_obra
			and vsfoa.nr_frente_obra = rec.nr_frente_obra
			and vsfoa.qt_itens = rec.qt_itens
			and vsfoa.servico_fk = rec.id;
		
		if n_inseridos = 0 then
			raise info 'servico_frente_obra_analise a ser adicionado: %, %, %, %, %, %, %, %, %, %, %', rec.nm_frente_obra,  rec.nr_frente_obra,  rec.qt_itens,  rec.id,  rec.versao_id,  rec.versao_nm_evento,  rec.versao_nr, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT';
		
			--insere registros coletados da consulta acima 
			INSERT INTO siconv.vrpl_servico_frente_obra_analise 
			(nm_frente_obra, nr_frente_obra, qt_itens, servico_fk, versao_id, versao_nm_evento, versao_nr, versao, adt_login, adt_data_hora, adt_operacao) 
			VALUES 
			(  rec.nm_frente_obra,  rec.nr_frente_obra,  rec.qt_itens,  rec.id,  rec.versao_id,  rec.versao_nm_evento,  rec.versao_nr, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT');		
			
		end if;
	
		select count(*) into n_inseridos
		from siconv.vrpl_servico_frente_obra vsfo
			inner join siconv.vrpl_frente_obra vfo on vfo.id = vsfo.frente_obra_fk 
		where vsfo.servico_fk = rec.id
			and vfo.nr_frente_obra = rec.nr_frente_obra
			and vfo.nm_frente_obra = rec.nm_frente_obra
			and vsfo.qt_itens = rec.qt_itens;
		
		if n_inseridos = 0 then

			select distinct vfo.id into id_fo
			from siconv.vrpl_frente_obra vfo
				inner join siconv.vrpl_po vp on vp.id = vfo.po_fk 
				inner join siconv.vrpl_macro_servico vms on vms.po_fk = vp.id
				inner join siconv.vrpl_servico vs on vs.macro_servico_fk = vms.id
			where vs.id = rec.id
				and vfo.nr_frente_obra = rec.nr_frente_obra
				and vfo.nm_frente_obra = rec.nm_frente_obra;

			if id_fo is null then

				raise info 'Buscando frente de obra da PO';

				select distinct vfo.id into id_fo
				from siconv.vrpl_frente_obra vfo
					inner join siconv.vrpl_po vp on vp.id = vfo.po_fk 
					inner join siconv.vrpl_macro_servico vms on vms.po_fk = vp.id
					inner join siconv.vrpl_servico vs on vs.macro_servico_fk = vms.id
				where vs.id = rec.id;
			end if;

			select count(*) into n_inseridos
			from siconv.vrpl_servico_frente_obra vsfo
			where frente_obra_fk = id_fo and servico_fk = rec.id;

			if n_inseridos = 0 then
			
				raise info 'vrpl.servico_frente_obra: nm_frente_obra=%, nr_frente_obra=%, qt_itens=%, serv=%, fo=%', rec.nm_frente_obra,  rec.nr_frente_obra,  rec.qt_itens,  rec.id, id_fo;
			
				INSERT INTO siconv.vrpl_servico_frente_obra
				(servico_fk, frente_obra_fk, qt_itens, versao, adt_login, adt_data_hora, adt_operacao, versao_nr, versao_id, versao_nm_evento)
				VALUES(rec.id, id_fo, rec.qt_itens, 1, current_setting('vrpl.cpf_usuario'), LOCALTIMESTAMP, 'INSERT', rec.versao_nr, rec.versao_id, rec.versao_nm_evento);
				
			end if;
		end if;
			
	
	end loop;
end loop;

select count(*) into counter
from siconv.vrpl_servico_frente_obra;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA %', counter;

select count(*) into counter
from siconv.vrpl_servico_frente_obra_analise;	

raise info 'Contando os registros da tabela SICONV.VRPL_SERVICO_FRENTE_OBRA_ANALISE %', counter;

end; $$

