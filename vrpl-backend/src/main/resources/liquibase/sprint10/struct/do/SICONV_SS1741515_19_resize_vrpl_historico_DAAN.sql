ALTER TABLE siconv.vrpl_historico_licitacao ALTER COLUMN tx_consideracoes TYPE varchar(1500) USING tx_consideracoes::varchar;
ALTER TABLE siconv.vrpl_historico_licitacao_log_rec ALTER COLUMN tx_consideracoes TYPE varchar(1500) USING tx_consideracoes::varchar;
