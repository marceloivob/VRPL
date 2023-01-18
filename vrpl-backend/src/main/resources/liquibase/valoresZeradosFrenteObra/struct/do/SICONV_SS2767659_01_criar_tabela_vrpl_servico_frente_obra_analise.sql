--vrpl_servico_frente_obra_analise
----------------------------------------------
--id 
--servico_fk 
--nm_frente_obra 
--nr_frente_obra 
--qt_itens 
--...<colunas versao, adt, etc> 
----------------------------------------------


CREATE SEQUENCE siconv.vrpl_servico_frente_obra_analise_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE siconv.vrpl_servico_frente_obra_analise (
    id bigint DEFAULT nextval('siconv.vrpl_servico_frente_obra_analise_seq'::regclass) PRIMARY KEY,
    servico_fk bigint NOT NULL,
    qt_itens numeric(17,2) NOT NULL,
    nm_frente_obra varchar(100) NOT NULL,
    nr_frente_obra smallint NOT NULL,
    adt_login varchar,
    adt_data_hora timestamp without time zone,
    adt_operacao varchar,
    versao bigint DEFAULT 0 NOT NULL,
    versao_nr integer DEFAULT 0 NOT NULL,
    versao_id varchar(20),
    versao_nm_evento varchar(3)
);


ALTER TABLE ONLY siconv.vrpl_servico_frente_obra_analise
    ADD CONSTRAINT vrpl_servico_frente_obra_analise_vrpl_servico_fk FOREIGN KEY (servico_fk) REFERENCES siconv.vrpl_servico(id);


COMMENT ON TABLE siconv.vrpl_servico_frente_obra_analise IS 'Tabela que guarda as informacoes das frentes de obra de analise';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.id  IS 'Identificador da tabela';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.servico_fk  IS 'chave para a tabela de servico';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.qt_itens  IS 'quantidade usada na relação servico frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.nm_frente_obra  IS 'nome da frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.nr_frente_obra  IS 'numero da frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';




-- Sequence: siconv.vrpl_servico_frente_obra_analise_log_rec_seq


-- DROP SEQUENCE siconv.vrpl_servico_frente_obra_analise_log_rec_seq;

CREATE SEQUENCE siconv.vrpl_servico_frente_obra_analise_log_rec_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;


-- Table: siconv.vrpl_servico_frente_obra_analise_log_rec

-- DROP TABLE siconv.vrpl_servico_frente_obra_analise_log_rec;

CREATE TABLE siconv.vrpl_servico_frente_obra_analise_log_rec
(
  id bigint NOT NULL DEFAULT nextval('siconv.vrpl_servico_frente_obra_analise_log_rec_seq'::regclass), -- Coluna de id.
  entity_id bigint NOT NULL, 
  nm_frente_obra character varying (100), 
  nr_frente_obra smallint, 
  qt_itens NUMERIC(17,2), 
  servicolog bigint, 
  versao bigint,
  versao_nr integer DEFAULT 0,
  versao_id character varying(20),
  versao_nm_evento character varying(3),
  adt_login character varying(60),
  adt_data_hora timestamp without time zone,
  adt_operacao character varying(6),
  CONSTRAINT vrpl_servico_frente_obra_analise_log_rec_pkey PRIMARY KEY (id),
  CONSTRAINT ck_vrpl_servico_frente_obra_analise_adt_operacao CHECK (adt_operacao::text = ANY (ARRAY['INSERT'::character varying::text, 'UPDATE'::character varying::text, 'DELETE'::character varying::text]))
)
WITH (
  OIDS=FALSE
);
COMMENT ON TABLE siconv.vrpl_servico_frente_obra_analise_log_rec
  IS 'Tabela de log rec da tabela servico_frente_obra_analise';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.id  IS 'Identificador da tabela';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.entity_id  IS 'Identificador da tabela de log rec';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.servicolog  IS 'chave para a tabela de servico';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.qt_itens  IS 'quantidade usada na relação servico frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.nm_frente_obra  IS 'nome da frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.nr_frente_obra  IS 'numero da frente de obra';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.adt_login IS 'Usuário que alterou o registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.adt_data_hora IS 'Data/Hora de modificação do registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.adt_operacao IS 'Operacão (INSERT/UPDATE/DELETE) da última ação no registro';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.versao IS 'Versão usada para controlar a concorrência';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.versao_id IS 'Identificador de agrupamento das versões';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.versao_nm_evento IS 'Nome do evento que deu origem a versão';
COMMENT ON COLUMN siconv.vrpl_servico_frente_obra_analise_log_rec.versao_nr IS 'Identificador do conjunto de dados referente ao versionamento';


-- DROP TRIGGER tg_vrpl_servico_frente_obra_analise ON siconv.vrpl_servico_frente_obra_analise;
 -- DROP FUNCTION siconv.vrpl_servico_frente_obra_analise_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_servico_frente_obra_analise_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_servico_frente_obra_analise_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       nm_frente_obra , 
       nr_frente_obra , 
       qt_itens , 
       servicolog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_servico_frente_obra_analise_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  LOCALTIMESTAMP ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.nm_frente_obra , 
      NEW.nr_frente_obra , 
      NEW.qt_itens , 
      NEW.servico_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_servico_frente_obra_analise_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_servico_frente_obra_analise_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       nm_frente_obra , 
       nr_frente_obra , 
       qt_itens , 
       servicolog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_servico_frente_obra_analise_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  LOCALTIMESTAMP ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.nm_frente_obra , 
      OLD.nr_frente_obra , 
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
 
 
 -- Trigger: siconv.tg_vrpl_servico_frente_obra_analise on siconv.vrpl_servico_frente_obra_analise
 CREATE TRIGGER tg_vrpl_servico_frente_obra_analise
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_servico_frente_obra_analise
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_servico_frente_obra_analise_trigger(); 



-- DROP TRIGGER tg_vrpl_servico_frente_obra_analise_concorrencia ON siconv.vrpl_servico_frente_obra_analise;
-- DROP FUNCTION siconv.vrpl_servico_frente_obra_analise_concorrencia_trigger();
 
 
 CREATE OR REPLACE FUNCTION siconv.vrpl_servico_frente_obra_analise_concorrencia_trigger()
  RETURNS trigger AS 
  $BODY$
  DECLARE wl_versao integer;
     BEGIN
	   SELECT versao into wl_versao from siconv.vrpl_servico_frente_obra_analise WHERE id = OLD.id;
	   
	   IF wl_versao is null then
       	   wl_versao := 0;
       END IF;
	   
       IF (TG_OP = 'UPDATE') THEN
	   		IF (wl_versao + 1) <> NEW.versao then 
		       RAISE EXCEPTION 'Versao invalida da versao --> % na tabela vrpl_servico_frente_obra_analise valor atual -> %', (wl_versao + 1), NEW.versao
		  	   USING ERRCODE = '23501';
	   		END IF;   
	   		RETURN NEW;
	   ELSIF (TG_OP = 'DELETE') THEN
	   	    IF (wl_versao) <> OLD.versao then 
		       RAISE EXCEPTION 'Versao invalida da versao --> % na tabela vrpl_servico_frente_obra_analise valor atual -> %', wl_versao, OLD.versao
		  	   USING ERRCODE = '23501';
	   		END IF;
	   		RETURN OLD;
	   END IF;
	   
	 RETURN NULL;
 	 END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
   
   CREATE TRIGGER tg_vrpl_servico_frente_obra_analise_concorrencia
   BEFORE UPDATE OR DELETE
   ON siconv.vrpl_servico_frente_obra_analise
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_servico_frente_obra_analise_concorrencia_trigger();



    
