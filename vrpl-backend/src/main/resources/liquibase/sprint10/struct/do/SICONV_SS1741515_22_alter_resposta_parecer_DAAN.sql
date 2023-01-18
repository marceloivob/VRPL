SET LOCAL vrpl.cpf_usuario TO 'SISTEMA';

-- Auto-generated SQL script #201912111123
UPDATE siconv.vrpl_pergunta
    SET titulo='O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?',
    versao = versao + 1 
    WHERE titulo='O valor total do lote %s é menor ou igual ao orçamento utilizado para comparação?';
