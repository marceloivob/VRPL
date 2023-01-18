alter table siconv.vrpl_servico 
add column vl_custo_unitario_database numeric(17, 2) NOT NULL DEFAULT 0;

alter table siconv.vrpl_servico_log_rec 
add column vl_custo_unitario_database numeric(17, 2) NOT NULL DEFAULT 0;