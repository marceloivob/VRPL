SET LOCAL vrpl.cpf_usuario TO '00000000000';

update siconv.vrpl_licitacao 
set in_situacao = 'EPE',
versao = versao + 1
where in_situacao = 'ELA';

update siconv.vrpl_submeta
set in_situacao = 'EPE',
versao = versao + 1
where in_situacao = 'ELA';
