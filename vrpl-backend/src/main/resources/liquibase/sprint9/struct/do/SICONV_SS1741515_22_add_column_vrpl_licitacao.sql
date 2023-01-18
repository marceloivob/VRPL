ALTER TABLE siconv.vrpl_licitacao ADD COLUMN modalidade varchar(50);
ALTER TABLE siconv.vrpl_licitacao ADD COLUMN regime_contratacao varchar(20);
ALTER TABLE siconv.vrpl_licitacao ADD COLUMN data_publicacao date;
ALTER TABLE siconv.vrpl_licitacao ADD COLUMN data_homologacao date;
ALTER TABLE siconv.vrpl_licitacao ADD COLUMN processo_de_execucao varchar(50);

COMMENT ON COLUMN siconv.vrpl_licitacao.modalidade IS 'Modalidade, depende do Recurso Nacional ou Internacional:Recurso - Lei 8.666/1993:  Convite, Tomada de Preços, Concorrência e Concurso. Recurso - Lei 10.520/2002 - Pregão: Pregão.';
COMMENT ON COLUMN siconv.vrpl_licitacao.regime_contratacao IS 'Regime Contratacao depende da Origem do Recurso: Recurso Financeiro Nacional: Lei 8.666/1993, Lei 10.520/2002 - Pregão. Recurso Financeiro Internacional: Lei 8.666/1993, Lei 10.520/2002 - Pregão, Shopping, NCB (LPN), ICB (LPI). Demais recursos financeiros: Decreto 93.872/1986, Lei 8.629/1993, Lei 10.847/2004, Lei 11.652/2008, Lei 11.947/2009, Lei 12.462/2011, Lei 12.512/2011, Lei 12.663/2012, Lei 12.865/2013, Lei 12.873/2013 e Lei 13.303/2016';
COMMENT ON COLUMN siconv.vrpl_licitacao.data_publicacao IS 'Data de publicação do Processo de Execução.'; 
COMMENT ON COLUMN siconv.vrpl_licitacao.data_homologacao IS 'Data de homologação do Processo de Execução.';
COMMENT ON COLUMN siconv.vrpl_licitacao.processo_de_execucao IS 'Define o processo de execução: Licitação, Inexigibilidade, Dispensa, Cotação de Resgistro, Contação de Divulgação e Pesquisa de Mercado.'
