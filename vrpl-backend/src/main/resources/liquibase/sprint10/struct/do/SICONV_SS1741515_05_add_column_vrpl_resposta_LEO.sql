ALTER TABLE siconv.vrpl_resposta
ADD COLUMN lote_fk bigint NULL;

ALTER TABLE siconv.vrpl_resposta
ADD CONSTRAINT fkc_vrpl_resposta_vrpl_lote_licitacao FOREIGN KEY (lote_fk) REFERENCES siconv.vrpl_lote_licitacao(id);

COMMENT ON COLUMN siconv.vrpl_resposta.lote_fk IS 'Chave estrangeira para a tabela vrpl_lote_licitacao.';



ALTER TABLE siconv.vrpl_resposta_log_rec
ADD COLUMN lotelog bigint NULL;



 CREATE OR REPLACE FUNCTION siconv.vrpl_resposta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_resposta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       laudolog , 
       perguntalog , 
       lotelog , 
       resposta 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_resposta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.laudo_fk , 
      NEW.pergunta_fk , 
      NEW.lote_fk , 
      NEW.resposta 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_resposta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_resposta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       laudolog , 
       perguntalog , 
       lotelog , 
       resposta 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_resposta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.laudo_fk , 
      OLD.pergunta_fk , 
      OLD.lote_fk , 
      OLD.resposta  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;