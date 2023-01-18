CREATE OR REPLACE FUNCTION siconv.vrpl_servico_trigger()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_servico_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       cd_servico , 
       eventolog , 
       entity_id , 
       id_servico_analise , 
       in_fonte , 
       macroservicolog , 
       nr_servico , 
       pc_bdi_analise , 
       pc_bdi_licitado , 
       qt_total_itens_analise , 
       sg_unidade , 
       tx_descricao , 
       tx_observacao , 
       vl_custo_unitario_analise , 
       vl_custo_unitario_ref_analise , 
       vl_preco_total_analise , 
       vl_preco_unitario_analise , 
       vl_preco_unitario_licitado , 
       vl_custo_unitario_database 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  LOCALTIMESTAMP ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.cd_servico , 
      NEW.evento_fk , 
      NEW.id , 
      NEW.id_servico_analise , 
      NEW.in_fonte , 
      NEW.macro_servico_fk , 
      NEW.nr_servico , 
      NEW.pc_bdi_analise , 
      NEW.pc_bdi_licitado , 
      NEW.qt_total_itens_analise , 
      NEW.sg_unidade , 
      NEW.tx_descricao , 
      NEW.tx_observacao , 
      NEW.vl_custo_unitario_analise , 
      NEW.vl_custo_unitario_ref_analise , 
      NEW.vl_preco_total_analise , 
      NEW.vl_preco_unitario_analise , 
      NEW.vl_preco_unitario_licitado , 
      NEW.vl_custo_unitario_database 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_servico_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_servico_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       cd_servico , 
       eventolog , 
       entity_id , 
       id_servico_analise , 
       in_fonte , 
       macroservicolog , 
       nr_servico , 
       pc_bdi_analise , 
       pc_bdi_licitado , 
       qt_total_itens_analise , 
       sg_unidade , 
       tx_descricao , 
       tx_observacao , 
       vl_custo_unitario_analise , 
       vl_custo_unitario_ref_analise , 
       vl_preco_total_analise , 
       vl_preco_unitario_analise , 
       vl_preco_unitario_licitado , 
       vl_custo_unitario_database 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  LOCALTIMESTAMP ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.cd_servico , 
      OLD.evento_fk , 
      OLD.id , 
      OLD.id_servico_analise , 
      OLD.in_fonte , 
      OLD.macro_servico_fk , 
      OLD.nr_servico , 
      OLD.pc_bdi_analise , 
      OLD.pc_bdi_licitado , 
      OLD.qt_total_itens_analise , 
      OLD.sg_unidade , 
      OLD.tx_descricao , 
      OLD.tx_observacao , 
      OLD.vl_custo_unitario_analise , 
      OLD.vl_custo_unitario_ref_analise , 
      OLD.vl_preco_total_analise , 
      OLD.vl_preco_unitario_analise , 
      OLD.vl_preco_unitario_licitado , 
      OLD.vl_custo_unitario_database 
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $function$
;