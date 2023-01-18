alter table siconv.acffo_proposta 
add column TERMO_COMPROMISSO_TEM_MANDATAR BOOLEAN default false;

comment on column siconv.acffo_proposta.TERMO_COMPROMISSO_TEM_MANDATAR is 
'Indica se a modalidade tem instituicao mandatária (campo usado na modalidade termo de compromisso)';
