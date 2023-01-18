alter table siconv.vrpl_proposta 
add column TERMO_COMPROMISSO_TEM_MANDATAR BOOLEAN default false;

comment on column siconv.vrpl_proposta.TERMO_COMPROMISSO_TEM_MANDATAR is 
'Indica se a modalidade tem instituicao mandatária (campo usado na modalidade termo de compromisso)';

alter table siconv.vrpl_proposta_log_rec
add column TERMO_COMPROMISSO_TEM_MANDATAR BOOLEAN default false;

comment on column siconv.vrpl_proposta_log_rec.TERMO_COMPROMISSO_TEM_MANDATAR is 
'Indica se a modalidade tem instituicao mandatária (campo usado na modalidade termo de compromisso)';
