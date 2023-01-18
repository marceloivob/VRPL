ALTER TABLE siconv.vrpl_pendencia
ADD COLUMN submeta_fk bigint NULL;

ALTER TABLE siconv.vrpl_pendencia
ADD CONSTRAINT fkc_vrpl_pendencia_vrpl_submeta FOREIGN KEY (submeta_fk) REFERENCES siconv.vrpl_submeta(id);

COMMENT ON COLUMN siconv.vrpl_pendencia.submeta_fk IS 'Chave estrangeira para a tabela vrpl_submeta.';



ALTER TABLE siconv.vrpl_pendencia_log_rec
ADD COLUMN submetalog bigint NULL;



 CREATE OR REPLACE FUNCTION siconv.vrpl_pendencia_trigger()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_pendencia_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       descricao , 
       entity_id , 
       in_resolvida , 
       laudolog , 
       submetalog , 
       prazo 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_pendencia_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.descricao , 
      NEW.id , 
      NEW.in_resolvida , 
      NEW.laudo_fk , 
      NEW.submeta_fk , 
      NEW.prazo 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_pendencia_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_pendencia_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       descricao , 
       entity_id , 
       in_resolvida , 
       laudolog , 
       submetalog , 
       prazo 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_pendencia_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.descricao , 
      OLD.id , 
      OLD.in_resolvida , 
      OLD.laudo_fk , 
      OLD.submeta_fk ,
      OLD.prazo  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $function$
;
