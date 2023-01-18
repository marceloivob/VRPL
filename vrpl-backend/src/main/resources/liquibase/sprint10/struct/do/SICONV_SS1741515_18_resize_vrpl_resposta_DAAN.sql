ALTER TABLE siconv.vrpl_resposta ALTER COLUMN resposta TYPE varchar(1500) USING resposta::varchar;
ALTER TABLE siconv.vrpl_resposta_log_rec ALTER COLUMN resposta TYPE varchar(1500) USING resposta::varchar;
