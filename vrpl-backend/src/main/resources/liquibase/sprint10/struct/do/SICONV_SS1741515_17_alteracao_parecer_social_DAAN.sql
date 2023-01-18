SET LOCAL vrpl.cpf_usuario TO 'SISTEMA';

UPDATE siconv.vrpl_pergunta
	SET titulo='O valor total do lote %s é menor ou igual ao orçamento utilizado para comparação?',
	versao = versao + 1
	WHERE id=17;
	
UPDATE siconv.vrpl_grupo_pergunta
	SET titulo='Aspectos Gerais do Lote',
	versao = versao + 1
	WHERE id=22;

UPDATE siconv.vrpl_pergunta
	SET valor_esperado='Sim',
	versao = versao + 1
	WHERE id=4;
	
UPDATE siconv.vrpl_pergunta
	SET valor_esperado='Sim',
	versao = versao + 1
	WHERE id=3;

UPDATE siconv.vrpl_pergunta
	SET titulo='Considera-se a verificação do resultado do processo licitatório',
	versao = versao + 1
	WHERE id=21;
	
	