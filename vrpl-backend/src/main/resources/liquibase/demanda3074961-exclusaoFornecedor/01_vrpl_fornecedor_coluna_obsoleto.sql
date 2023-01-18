alter table siconv.vrpl_fornecedor 
add column obsoleto boolean not null default false;

alter table siconv.vrpl_fornecedor_log_rec 
add column obsoleto boolean;
