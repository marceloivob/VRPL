CREATE OR REPLACE FUNCTION siconv.vrpl_fornecedor_trigger()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_fornecedor_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       identificacao , 
       licitacaolog , 
       razao_social , 
       tipo_identificacao ,
       obsoleto 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_fornecedor_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  LOCALTIMESTAMP ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.identificacao , 
      NEW.licitacao_fk , 
      NEW.razao_social , 
      NEW.tipo_identificacao ,
      NEW.obsoleto 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_fornecedor_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_fornecedor_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       identificacao , 
       licitacaolog , 
       razao_social , 
       tipo_identificacao ,
       obsoleto  
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_fornecedor_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  LOCALTIMESTAMP ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.identificacao , 
      OLD.licitacao_fk , 
      OLD.razao_social , 
      OLD.tipo_identificacao ,
      OLD.obsoleto 
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $function$
;

