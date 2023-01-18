ALTER TABLE siconv.vrpl_servico_frente_obra 
DROP COLUMN IF EXISTS qt_itens_analise;

ALTER TABLE siconv.vrpl_servico_frente_obra_log_rec 
DROP COLUMN IF EXISTS qt_itens_analise;

-- DROP TRIGGER tg_vrpl_servico_frente_obra ON siconv.vrpl_servico_frente_obra;
 -- DROP FUNCTION siconv.vrpl_servico_frente_obra_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_servico_frente_obra_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_servico_frente_obra_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       frenteobralog , 
       qt_itens , 
       servicolog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_servico_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  LOCALTIMESTAMP ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,	  
     	  NEW.frente_obra_fk , 
      	  NEW.qt_itens , 
          NEW.servico_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_servico_frente_obra_log_rec WHERE frenteobralog = OLD.frente_obra_fk AND servicolog = OLD.servico_fk;
       
       INSERT INTO siconv.vrpl_servico_frente_obra_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       frenteobralog , 
       qt_itens , 
       servicolog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_servico_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  LOCALTIMESTAMP ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.frente_obra_fk , 
      OLD.qt_itens , 
      OLD.servico_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
