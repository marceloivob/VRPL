-- DROP TRIGGER tg_vrpl_pendencia ON siconv.vrpl_pendencia;
 -- DROP FUNCTION siconv.vrpl_pendencia_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_pendencia_trigger()
   RETURNS trigger AS 
 $BODY$
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
      OLD.prazo  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_pendencia ON SICONV.vrpl_pendencia;
 
 -- Trigger: siconv.tg_vrpl_pendencia on siconv.vrpl_pendencia
 CREATE TRIGGER tg_vrpl_pendencia
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_pendencia
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_pendencia_trigger(); 


-- DROP TRIGGER tg_vrpl_anexo ON siconv.vrpl_anexo;
 -- DROP FUNCTION siconv.vrpl_anexo_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_anexo_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_anexo_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       bucket , 
       caminho , 
       dt_upload , 
       entity_id , 
       id_cpf_enviadopor , 
       in_perfil , 
       in_tipoanexo , 
       nm_arquivo , 
       nome_enviadopor , 
       tx_descricao , 
       vrpllicitacaolog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_anexo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
      NEW.bucket , 
      NEW.caminho , 
      NEW.dt_upload , 
      NEW.id , 
      NEW.id_cpf_enviadopor , 
      NEW.in_perfil , 
      NEW.in_tipoanexo , 
      NEW.nm_arquivo , 
      NEW.nome_enviadopor , 
      NEW.tx_descricao , 
      NEW.vrpl_licitacao_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_anexo_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_anexo_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       bucket , 
       caminho , 
       dt_upload , 
       entity_id , 
       id_cpf_enviadopor , 
       in_perfil , 
       in_tipoanexo , 
       nm_arquivo , 
       nome_enviadopor , 
       tx_descricao , 
       vrpllicitacaolog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_anexo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.bucket , 
      OLD.caminho , 
      OLD.dt_upload , 
      OLD.id , 
      OLD.id_cpf_enviadopor , 
      OLD.in_perfil , 
      OLD.in_tipoanexo , 
      OLD.nm_arquivo , 
      OLD.nome_enviadopor , 
      OLD.tx_descricao , 
      OLD.vrpl_licitacao_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_anexo ON SICONV.vrpl_anexo;
 
 -- Trigger: siconv.tg_vrpl_anexo on siconv.vrpl_anexo
 CREATE TRIGGER tg_vrpl_anexo
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_anexo
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_anexo_trigger(); 

-- DROP TRIGGER tg_vrpl_evento_frente_obra ON siconv.vrpl_evento_frente_obra;
 -- DROP FUNCTION siconv.vrpl_evento_frente_obra_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_evento_frente_obra_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_evento_frente_obra_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       eventolog , 
       frenteobralog , 
       nr_mes_conclusao 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_evento_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.evento_fk , 
      NEW.frente_obra_fk , 
      NEW.nr_mes_conclusao 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_evento_frente_obra_log_rec WHERE frenteobralog = OLD.frente_obra_fk AND eventolog = OLD.evento_fk;
       
       INSERT INTO siconv.vrpl_evento_frente_obra_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       eventolog , 
       frenteobralog , 
       nr_mes_conclusao 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_evento_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.evento_fk , 
      OLD.frente_obra_fk , 
      OLD.nr_mes_conclusao  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_evento_frente_obra ON SICONV.vrpl_evento_frente_obra;
 
 -- Trigger: siconv.tg_vrpl_evento_frente_obra on siconv.vrpl_evento_frente_obra
 CREATE TRIGGER tg_vrpl_evento_frente_obra
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_evento_frente_obra
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_evento_frente_obra_trigger(); 

-- DROP TRIGGER tg_vrpl_proposta ON siconv.vrpl_proposta;
 -- DROP FUNCTION siconv.vrpl_proposta_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_proposta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_proposta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       ano_convenio , 
       ano_proposta , 
       categoria , 
       codigo_programa , 
       data_assinatura_convenio , 
       entity_id , 
       id_siconv , 
       identificacao_proponente , 
       modalidade , 
       nivel_contrato , 
       nome_mandataria , 
       nome_objeto , 
       nome_programa , 
       nome_proponente , 
       numero_convenio , 
       numero_proposta , 
       pc_min_contrapartida , 
       spa_homologado , 
       apelido_empreendimento ,
       uf , 
       valor_contrapartida , 
       valor_global , 
       valor_repasse , 
       versao_in_atual 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_proposta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.ano_convenio , 
      NEW.ano_proposta , 
      NEW.categoria , 
      NEW.codigo_programa , 
      NEW.data_assinatura_convenio , 
      NEW.id , 
      NEW.id_siconv , 
      NEW.identificacao_proponente , 
      NEW.modalidade , 
      NEW.nivel_contrato , 
      NEW.nome_mandataria , 
      NEW.nome_objeto , 
      NEW.nome_programa , 
      NEW.nome_proponente , 
      NEW.numero_convenio , 
      NEW.numero_proposta , 
      NEW.pc_min_contrapartida , 
      NEW.spa_homologado , 
      NEW.apelido_empreendimento ,
      NEW.uf , 
      NEW.valor_contrapartida , 
      NEW.valor_global , 
      NEW.valor_repasse , 
      NEW.versao_in_atual 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_proposta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_proposta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       ano_convenio , 
       ano_proposta , 
       categoria , 
       codigo_programa , 
       data_assinatura_convenio , 
       entity_id , 
       id_siconv , 
       identificacao_proponente , 
       modalidade , 
       nivel_contrato , 
       nome_mandataria , 
       nome_objeto , 
       nome_programa , 
       nome_proponente , 
       numero_convenio , 
       numero_proposta , 
       pc_min_contrapartida , 
       spa_homologado , 
       apelido_empreendimento ,
       uf , 
       valor_contrapartida , 
       valor_global , 
       valor_repasse , 
       versao_in_atual 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_proposta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.ano_convenio , 
      OLD.ano_proposta , 
      OLD.categoria , 
      OLD.codigo_programa , 
      OLD.data_assinatura_convenio , 
      OLD.id , 
      OLD.id_siconv , 
      OLD.identificacao_proponente , 
      OLD.modalidade , 
      OLD.nivel_contrato , 
      OLD.nome_mandataria , 
      OLD.nome_objeto , 
      OLD.nome_programa , 
      OLD.nome_proponente , 
      OLD.numero_convenio , 
      OLD.numero_proposta , 
      OLD.pc_min_contrapartida , 
      OLD.spa_homologado , 
      OLD.apelido_empreendimento ,
      OLD.uf , 
      OLD.valor_contrapartida , 
      OLD.valor_global , 
      OLD.valor_repasse , 
      OLD.versao_in_atual  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_proposta ON SICONV.vrpl_proposta;
 
 -- Trigger: siconv.tg_vrpl_proposta on siconv.vrpl_proposta
 CREATE TRIGGER tg_vrpl_proposta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_proposta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_proposta_trigger(); 


-- DROP TRIGGER tg_vrpl_template_laudo ON siconv.vrpl_template_laudo;
 -- DROP FUNCTION siconv.vrpl_template_laudo_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_template_laudo_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_template_laudo_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       tipo 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_template_laudo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.tipo 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_template_laudo_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_template_laudo_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       tipo 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_template_laudo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.tipo  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_template_laudo ON SICONV.vrpl_template_laudo;
 
 -- Trigger: siconv.tg_vrpl_template_laudo on siconv.vrpl_template_laudo
 CREATE TRIGGER tg_vrpl_template_laudo
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_template_laudo
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_template_laudo_trigger(); 


-- DROP TRIGGER tg_vrpl_subitem_investimento ON siconv.vrpl_subitem_investimento;
 -- DROP FUNCTION siconv.vrpl_subitem_investimento_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_subitem_investimento_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_subitem_investimento_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       codigo_und , 
       descricao , 
       descricao_und , 
       entity_id , 
       id_subitem_analise , 
       in_projeto_social 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_subitem_investimento_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.codigo_und , 
      NEW.descricao , 
      NEW.descricao_und , 
      NEW.id , 
      NEW.id_subitem_analise , 
      NEW.in_projeto_social 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_subitem_investimento_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_subitem_investimento_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       codigo_und , 
       descricao , 
       descricao_und , 
       entity_id , 
       id_subitem_analise , 
       in_projeto_social 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_subitem_investimento_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.codigo_und , 
      OLD.descricao , 
      OLD.descricao_und , 
      OLD.id , 
      OLD.id_subitem_analise , 
      OLD.in_projeto_social  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_subitem_investimento ON SICONV.vrpl_subitem_investimento;
 
 -- Trigger: siconv.tg_vrpl_subitem_investimento on siconv.vrpl_subitem_investimento
 CREATE TRIGGER tg_vrpl_subitem_investimento
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_subitem_investimento
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_subitem_investimento_trigger(); 


-- DROP TRIGGER tg_vrpl_licitacao ON siconv.vrpl_licitacao;
 -- DROP FUNCTION siconv.vrpl_licitacao_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_licitacao_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_licitacao_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       data_homologacao , 
       data_publicacao , 
       entity_id , 
       id_licitacao_fk , 
       in_situacao , 
       modalidade , 
       numero_ano , 
       objeto , 
       processo_de_execucao , 
       propostalog , 
       regime_contratacao , 
       status_processo , 
       valor_processo 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.data_homologacao , 
      NEW.data_publicacao , 
      NEW.id , 
      NEW.id_licitacao_fk , 
      NEW.in_situacao , 
      NEW.modalidade , 
      NEW.numero_ano , 
      NEW.objeto , 
      NEW.processo_de_execucao , 
      NEW.proposta_fk , 
      NEW.regime_contratacao , 
      NEW.status_processo , 
      NEW.valor_processo 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_licitacao_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_licitacao_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       data_homologacao , 
       data_publicacao , 
       entity_id , 
       id_licitacao_fk , 
       in_situacao , 
       modalidade , 
       numero_ano , 
       objeto , 
       processo_de_execucao , 
       propostalog , 
       regime_contratacao , 
       status_processo , 
       valor_processo 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.data_homologacao , 
      OLD.data_publicacao , 
      OLD.id , 
      OLD.id_licitacao_fk , 
      OLD.in_situacao , 
      OLD.modalidade , 
      OLD.numero_ano , 
      OLD.objeto , 
      OLD.processo_de_execucao , 
      OLD.proposta_fk , 
      OLD.regime_contratacao , 
      OLD.status_processo , 
      OLD.valor_processo  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_licitacao ON SICONV.vrpl_licitacao;
 
 -- Trigger: siconv.tg_vrpl_licitacao on siconv.vrpl_licitacao
 CREATE TRIGGER tg_vrpl_licitacao
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_licitacao
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_licitacao_trigger(); 


-- DROP TRIGGER tg_vrpl_servico ON siconv.vrpl_servico;
 -- DROP FUNCTION siconv.vrpl_servico_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_servico_trigger()
   RETURNS trigger AS 
 $BODY$
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
       vl_preco_unitario_licitado 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
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
      NEW.vl_preco_unitario_licitado 
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
       vl_preco_unitario_licitado 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
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
      OLD.vl_preco_unitario_licitado  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_servico ON SICONV.vrpl_servico;
 
 -- Trigger: siconv.tg_vrpl_servico on siconv.vrpl_servico
 CREATE TRIGGER tg_vrpl_servico
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_servico
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_servico_trigger(); 

-- DROP TRIGGER tg_vrpl_grupo_pergunta ON siconv.vrpl_grupo_pergunta;
 -- DROP FUNCTION siconv.vrpl_grupo_pergunta_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_grupo_pergunta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_grupo_pergunta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       in_grupo_obrigatorio , 
       numero , 
       templatelog , 
       titulo 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_grupo_pergunta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.in_grupo_obrigatorio , 
      NEW.numero , 
      NEW.template_fk , 
      NEW.titulo 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_grupo_pergunta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_grupo_pergunta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       in_grupo_obrigatorio , 
       numero , 
       templatelog , 
       titulo 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_grupo_pergunta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.in_grupo_obrigatorio , 
      OLD.numero , 
      OLD.template_fk , 
      OLD.titulo  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_grupo_pergunta ON SICONV.vrpl_grupo_pergunta;
 
 -- Trigger: siconv.tg_vrpl_grupo_pergunta on siconv.vrpl_grupo_pergunta
 CREATE TRIGGER tg_vrpl_grupo_pergunta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_grupo_pergunta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_grupo_pergunta_trigger(); 


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
       qt_itens_analise , 
       servicolog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_servico_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.frente_obra_fk , 
      NEW.qt_itens , 
      NEW.qt_itens_analise , 
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
       qt_itens_analise , 
       servicolog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_servico_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.frente_obra_fk , 
      OLD.qt_itens , 
      OLD.qt_itens_analise , 
      OLD.servico_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_servico_frente_obra ON SICONV.vrpl_servico_frente_obra;
 
 -- Trigger: siconv.tg_vrpl_servico_frente_obra on siconv.vrpl_servico_frente_obra
 CREATE TRIGGER tg_vrpl_servico_frente_obra
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_servico_frente_obra
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_servico_frente_obra_trigger(); 


-- DROP TRIGGER tg_vrpl_lote_licitacao ON siconv.vrpl_lote_licitacao;
 -- DROP FUNCTION siconv.vrpl_lote_licitacao_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_lote_licitacao_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_lote_licitacao_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       fornecedorlog , 
       entity_id , 
       licitacaolog , 
       numero_lote 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_lote_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.fornecedor_fk , 
      NEW.id , 
      NEW.licitacao_fk , 
      NEW.numero_lote 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_lote_licitacao_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_lote_licitacao_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       fornecedorlog , 
       entity_id , 
       licitacaolog , 
       numero_lote 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_lote_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.fornecedor_fk , 
      OLD.id , 
      OLD.licitacao_fk , 
      OLD.numero_lote  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_lote_licitacao ON SICONV.vrpl_lote_licitacao;
 
 -- Trigger: siconv.tg_vrpl_lote_licitacao on siconv.vrpl_lote_licitacao
 CREATE TRIGGER tg_vrpl_lote_licitacao
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_lote_licitacao
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_lote_licitacao_trigger(); 
   



-- DROP TRIGGER tg_vrpl_submeta ON siconv.vrpl_submeta;
 -- DROP FUNCTION siconv.vrpl_submeta_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_submeta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_submeta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       id_submeta_analise , 
       in_regime_execucao , 
       in_regime_execucao_analise , 
       in_situacao , 
       in_situacao_analise , 
       metalog , 
       natureza_despesa_sub_it_fk_analise , 
       nr_lote_analise , 
       nr_submeta_analise , 
       propostalog , 
       tx_descricao_analise , 
       vl_contrapartida , 
       vl_contrapartida_analise , 
       vl_outros , 
       vl_outros_analise , 
       vl_repasse , 
       vl_repasse_analise , 
       vl_total_analise , 
       vl_total_licitado , 
       vrpllotelicitacaolog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_submeta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.id_submeta_analise , 
      NEW.in_regime_execucao , 
      NEW.in_regime_execucao_analise , 
      NEW.in_situacao , 
      NEW.in_situacao_analise , 
      NEW.meta_fk , 
      NEW.natureza_despesa_sub_it_fk_analise , 
      NEW.nr_lote_analise , 
      NEW.nr_submeta_analise , 
      NEW.proposta_fk , 
      NEW.tx_descricao_analise , 
      NEW.vl_contrapartida , 
      NEW.vl_contrapartida_analise , 
      NEW.vl_outros , 
      NEW.vl_outros_analise , 
      NEW.vl_repasse , 
      NEW.vl_repasse_analise , 
      NEW.vl_total_analise , 
      NEW.vl_total_licitado , 
      NEW.vrpl_lote_licitacao_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_submeta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_submeta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       id_submeta_analise , 
       in_regime_execucao , 
       in_regime_execucao_analise , 
       in_situacao , 
       in_situacao_analise , 
       metalog , 
       natureza_despesa_sub_it_fk_analise , 
       nr_lote_analise , 
       nr_submeta_analise , 
       propostalog , 
       tx_descricao_analise , 
       vl_contrapartida , 
       vl_contrapartida_analise , 
       vl_outros , 
       vl_outros_analise , 
       vl_repasse , 
       vl_repasse_analise , 
       vl_total_analise , 
       vl_total_licitado , 
       vrpllotelicitacaolog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_submeta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.id_submeta_analise , 
      OLD.in_regime_execucao , 
      OLD.in_regime_execucao_analise , 
      OLD.in_situacao , 
      OLD.in_situacao_analise , 
      OLD.meta_fk , 
      OLD.natureza_despesa_sub_it_fk_analise , 
      OLD.nr_lote_analise , 
      OLD.nr_submeta_analise , 
      OLD.proposta_fk , 
      OLD.tx_descricao_analise , 
      OLD.vl_contrapartida , 
      OLD.vl_contrapartida_analise , 
      OLD.vl_outros , 
      OLD.vl_outros_analise , 
      OLD.vl_repasse , 
      OLD.vl_repasse_analise , 
      OLD.vl_total_analise , 
      OLD.vl_total_licitado , 
      OLD.vrpl_lote_licitacao_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_submeta ON SICONV.vrpl_submeta;
 
 -- Trigger: siconv.tg_vrpl_submeta on siconv.vrpl_submeta
 CREATE TRIGGER tg_vrpl_submeta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_submeta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_submeta_trigger(); 


-- DROP TRIGGER tg_vrpl_macro_servico_parcela ON siconv.vrpl_macro_servico_parcela;
 -- DROP FUNCTION siconv.vrpl_macro_servico_parcela_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_macro_servico_parcela_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_macro_servico_parcela_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       macroservicolog , 
       nr_parcela , 
       pc_parcela 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_macro_servico_parcela_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.macro_servico_fk , 
      NEW.nr_parcela , 
      NEW.pc_parcela 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_macro_servico_parcela_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_macro_servico_parcela_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       macroservicolog , 
       nr_parcela , 
       pc_parcela 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_macro_servico_parcela_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.macro_servico_fk , 
      OLD.nr_parcela , 
      OLD.pc_parcela  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_macro_servico_parcela ON SICONV.vrpl_macro_servico_parcela;
 
 -- Trigger: siconv.tg_vrpl_macro_servico_parcela on siconv.vrpl_macro_servico_parcela
 CREATE TRIGGER tg_vrpl_macro_servico_parcela
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_macro_servico_parcela
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_macro_servico_parcela_trigger(); 



-- DROP TRIGGER tg_vrpl_laudo ON siconv.vrpl_laudo;
 -- DROP FUNCTION siconv.vrpl_laudo_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_laudo_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_laudo_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       in_resultado , 
       in_status , 
       licitacaolog , 
       templatelog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_laudo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.in_resultado , 
      NEW.in_status , 
      NEW.licitacao_fk , 
      NEW.template_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_laudo_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_laudo_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       in_resultado , 
       in_status , 
       licitacaolog , 
       templatelog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_laudo_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.in_resultado , 
      OLD.in_status , 
      OLD.licitacao_fk , 
      OLD.template_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_laudo ON SICONV.vrpl_laudo;
 
 -- Trigger: siconv.tg_vrpl_laudo on siconv.vrpl_laudo
 CREATE TRIGGER tg_vrpl_laudo
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_laudo
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_laudo_trigger(); 


-- DROP TRIGGER tg_vrpl_macro_servico ON siconv.vrpl_macro_servico;
 -- DROP FUNCTION siconv.vrpl_macro_servico_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_macro_servico_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_macro_servico_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       id_macro_servico_analise , 
       nr_macro_servico , 
       polog , 
       tx_descricao 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_macro_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.id_macro_servico_analise , 
      NEW.nr_macro_servico , 
      NEW.po_fk , 
      NEW.tx_descricao 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_macro_servico_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_macro_servico_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       id_macro_servico_analise , 
       nr_macro_servico , 
       polog , 
       tx_descricao 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_macro_servico_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.id_macro_servico_analise , 
      OLD.nr_macro_servico , 
      OLD.po_fk , 
      OLD.tx_descricao  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_macro_servico ON SICONV.vrpl_macro_servico;
 
 -- Trigger: siconv.tg_vrpl_macro_servico on siconv.vrpl_macro_servico
 CREATE TRIGGER tg_vrpl_macro_servico
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_macro_servico
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_macro_servico_trigger(); 


-- DROP TRIGGER tg_vrpl_po ON siconv.vrpl_po;
 -- DROP FUNCTION siconv.vrpl_po_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_po_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_po_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       dt_base_analise , 
       dt_base_vrpl , 
       dt_previsao_inicio_obra , 
       entity_id , 
       id_po_analise , 
       in_acompanhamento_eventos , 
       in_desonerado , 
       qt_meses_duracao_obra , 
       referencia , 
       sg_localidade , 
       submetalog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_po_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.dt_base_analise , 
      NEW.dt_base_vrpl , 
      NEW.dt_previsao_inicio_obra , 
      NEW.id , 
      NEW.id_po_analise , 
      NEW.in_acompanhamento_eventos , 
      NEW.in_desonerado , 
      NEW.qt_meses_duracao_obra , 
      NEW.referencia , 
      NEW.sg_localidade , 
      NEW.submeta_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_po_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_po_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       dt_base_analise , 
       dt_base_vrpl , 
       dt_previsao_inicio_obra , 
       entity_id , 
       id_po_analise , 
       in_acompanhamento_eventos , 
       in_desonerado , 
       qt_meses_duracao_obra , 
       referencia , 
       sg_localidade , 
       submetalog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_po_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.dt_base_analise , 
      OLD.dt_base_vrpl , 
      OLD.dt_previsao_inicio_obra , 
      OLD.id , 
      OLD.id_po_analise , 
      OLD.in_acompanhamento_eventos , 
      OLD.in_desonerado , 
      OLD.qt_meses_duracao_obra , 
      OLD.referencia , 
      OLD.sg_localidade , 
      OLD.submeta_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_po ON SICONV.vrpl_po;
 
 -- Trigger: siconv.tg_vrpl_po on siconv.vrpl_po
 CREATE TRIGGER tg_vrpl_po
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_po
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_po_trigger(); 


-- DROP TRIGGER tg_vrpl_fornecedor ON siconv.vrpl_fornecedor;
 -- DROP FUNCTION siconv.vrpl_fornecedor_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_fornecedor_trigger()
   RETURNS trigger AS 
 $BODY$
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
       tipo_identificacao 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_fornecedor_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.identificacao , 
      NEW.licitacao_fk , 
      NEW.razao_social , 
      NEW.tipo_identificacao 
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
       tipo_identificacao 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_fornecedor_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.identificacao , 
      OLD.licitacao_fk , 
      OLD.razao_social , 
      OLD.tipo_identificacao  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_fornecedor ON SICONV.vrpl_fornecedor;
 
 -- Trigger: siconv.tg_vrpl_fornecedor on siconv.vrpl_fornecedor
 CREATE TRIGGER tg_vrpl_fornecedor
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_fornecedor
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_fornecedor_trigger(); 

   

-- DROP TRIGGER tg_vrpl_pergunta ON siconv.vrpl_pergunta;
 -- DROP FUNCTION siconv.vrpl_pergunta_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_pergunta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_pergunta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       grupolog , 
       entity_id , 
       numero , 
       tipo_resposta , 
       titulo , 
       valor_esperado , 
       valor_resposta 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_pergunta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.grupo_fk , 
      NEW.id , 
      NEW.numero , 
      NEW.tipo_resposta , 
      NEW.titulo , 
      NEW.valor_esperado , 
      NEW.valor_resposta 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_pergunta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_pergunta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       grupolog , 
       entity_id , 
       numero , 
       tipo_resposta , 
       titulo , 
       valor_esperado , 
       valor_resposta 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_pergunta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.grupo_fk , 
      OLD.id , 
      OLD.numero , 
      OLD.tipo_resposta , 
      OLD.titulo , 
      OLD.valor_esperado , 
      OLD.valor_resposta  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_pergunta ON SICONV.vrpl_pergunta;
 
 -- Trigger: siconv.tg_vrpl_pergunta on siconv.vrpl_pergunta
 CREATE TRIGGER tg_vrpl_pergunta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_pergunta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_pergunta_trigger(); 


-- DROP TRIGGER tg_vrpl_frente_obra ON siconv.vrpl_frente_obra;
 -- DROP FUNCTION siconv.vrpl_frente_obra_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_frente_obra_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_frente_obra_log_rec (
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
       polog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.nm_frente_obra , 
      NEW.nr_frente_obra , 
      NEW.po_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_frente_obra_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_frente_obra_log_rec (
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
       polog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_frente_obra_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.nm_frente_obra , 
      OLD.nr_frente_obra , 
      OLD.po_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_frente_obra ON SICONV.vrpl_frente_obra;
 
 -- Trigger: siconv.tg_vrpl_frente_obra on siconv.vrpl_frente_obra
 CREATE TRIGGER tg_vrpl_frente_obra
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_frente_obra
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_frente_obra_trigger(); 


-- DROP TRIGGER tg_vrpl_meta ON siconv.vrpl_meta;
 -- DROP FUNCTION siconv.vrpl_meta_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_meta_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_meta_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       id_meta_analise , 
       in_social , 
       nr_meta_analise , 
       qt_itens_analise , 
       subitemlog , 
       tx_descricao_analise 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_meta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.id_meta_analise , 
      NEW.in_social , 
      NEW.nr_meta_analise , 
      NEW.qt_itens_analise , 
      NEW.subitem_fk , 
      NEW.tx_descricao_analise 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_meta_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_meta_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       id_meta_analise , 
       in_social , 
       nr_meta_analise , 
       qt_itens_analise , 
       subitemlog , 
       tx_descricao_analise 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_meta_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.id_meta_analise , 
      OLD.in_social , 
      OLD.nr_meta_analise , 
      OLD.qt_itens_analise , 
      OLD.subitem_fk , 
      OLD.tx_descricao_analise  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_meta ON SICONV.vrpl_meta;
 
 -- Trigger: siconv.tg_vrpl_meta on siconv.vrpl_meta
 CREATE TRIGGER tg_vrpl_meta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_meta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_meta_trigger(); 


-- DROP TRIGGER tg_vrpl_resposta ON siconv.vrpl_resposta;
 -- DROP FUNCTION siconv.vrpl_resposta_trigger();
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
      OLD.resposta  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_resposta ON SICONV.vrpl_resposta;
 
 -- Trigger: siconv.tg_vrpl_resposta on siconv.vrpl_resposta
 CREATE TRIGGER tg_vrpl_resposta
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_resposta
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_resposta_trigger(); 



-- DROP TRIGGER tg_vrpl_historico_licitacao ON siconv.vrpl_historico_licitacao;
 -- DROP FUNCTION siconv.vrpl_historico_licitacao_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_historico_licitacao_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_historico_licitacao_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       dt_registro , 
       entity_id , 
       in_evento , 
       in_situacao , 
       licitacaolog , 
       nm_responsavel , 
       nr_cpf_responsavel , 
       tx_consideracoes 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_historico_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.dt_registro , 
      NEW.id , 
      NEW.in_evento , 
      NEW.in_situacao , 
      NEW.licitacao_fk , 
      NEW.nm_responsavel , 
      NEW.nr_cpf_responsavel , 
      NEW.tx_consideracoes 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_historico_licitacao_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_historico_licitacao_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       dt_registro , 
       entity_id , 
       in_evento , 
       in_situacao , 
       licitacaolog , 
       nm_responsavel , 
       nr_cpf_responsavel , 
       tx_consideracoes 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_historico_licitacao_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.dt_registro , 
      OLD.id , 
      OLD.in_evento , 
      OLD.in_situacao , 
      OLD.licitacao_fk , 
      OLD.nm_responsavel , 
      OLD.nr_cpf_responsavel , 
      OLD.tx_consideracoes  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_historico_licitacao ON SICONV.vrpl_historico_licitacao;
 
 -- Trigger: siconv.tg_vrpl_historico_licitacao on siconv.vrpl_historico_licitacao
 CREATE TRIGGER tg_vrpl_historico_licitacao
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_historico_licitacao
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_historico_licitacao_trigger(); 



-- DROP TRIGGER tg_vrpl_evento ON siconv.vrpl_evento;
 -- DROP FUNCTION siconv.vrpl_evento_trigger();
 CREATE OR REPLACE FUNCTION siconv.vrpl_evento_trigger()
   RETURNS trigger AS 
 $BODY$
   DECLARE wl_versao integer;
   DECLARE wl_id integer;
   BEGIN
   -- Aqui temos um bloco IF que confirmará o tipo de operação.
   IF (TG_OP = 'INSERT') THEN
       INSERT INTO siconv.vrpl_evento_log_rec (
       versao , 
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
	   
       entity_id , 
       nm_evento , 
       nr_evento , 
       polog 
      ) VALUES (
          1,
 	  nextval('siconv.vrpl_evento_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario') ,
	  NEW.adt_data_hora ,
	  NEW.adt_operacao ,
	  NEW.versao_nr ,
	  NEW.versao_id ,
	  NEW.versao_nm_evento ,
	  
	  
      NEW.id , 
      NEW.nm_evento , 
      NEW.nr_evento , 
      NEW.po_fk 
       );
       RETURN NEW;
   -- Aqui temos um bloco IF que confirmará o tipo de operação UPDATE.
   ELSIF (TG_OP = 'UPDATE') or (TG_OP = 'DELETE') THEN
       --Verifica qual vai ser a versão do registro na tabela de log
       SELECT max(versao) into wl_versao from siconv.vrpl_evento_log_rec WHERE entity_id = OLD.id;
       
       INSERT INTO siconv.vrpl_evento_log_rec (
 	   versao,
	   id ,
	   adt_login ,
	   adt_data_hora ,
	   adt_operacao ,
	   versao_nr ,
	   versao_id ,
	   versao_nm_evento ,
       entity_id , 
       nm_evento , 
       nr_evento , 
       polog 
	   
       ) VALUES (
 	  OLD.versao,
	  nextval('siconv.vrpl_evento_log_rec_seq'), 
	  current_setting('vrpl.cpf_usuario'),
	  OLD.adt_data_hora ,
	  TG_OP ,
	  OLD.versao_nr ,
	  OLD.versao_id ,
	  OLD.versao_nm_evento ,
      OLD.id , 
      OLD.nm_evento , 
      OLD.nr_evento , 
      OLD.po_fk  
       );
       RETURN OLD;
   END IF;
   RETURN NULL;
   END;
   $BODY$
   LANGUAGE plpgsql VOLATILE
   COST 100;
 
 DROP TRIGGER IF EXISTS tg_vrpl_evento ON SICONV.vrpl_evento;
 
 -- Trigger: siconv.tg_vrpl_evento on siconv.vrpl_evento
 CREATE TRIGGER tg_vrpl_evento
   AFTER INSERT OR UPDATE OR DELETE
   ON siconv.vrpl_evento
   FOR EACH ROW
   EXECUTE PROCEDURE siconv.vrpl_evento_trigger(); 
   