
ALTER TABLE siconv.vrpl_proposta ADD COLUMN apelido_empreendimento varchar(50) NOT NULL; -- Apelido do ACFFO.
ALTER TABLE siconv.vrpl_proposta_log_rec ADD COLUMN apelido_empreendimento varchar(50); -- Apelido do ACFFO.

COMMENT ON COLUMN siconv.vrpl_proposta.apelido_empreendimento IS 'Apelido do ACFFO / Empreendimento';



