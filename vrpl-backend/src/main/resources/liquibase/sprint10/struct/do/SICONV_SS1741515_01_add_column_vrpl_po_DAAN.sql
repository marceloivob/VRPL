ALTER TABLE siconv.vrpl_po ADD dt_previsao_inicio_obra_analise date;

UPDATE siconv.vrpl_po SET dt_previsao_inicio_obra_analise = dt_previsao_inicio_obra, versao = versao + 1;

ALTER TABLE siconv.vrpl_po ALTER COLUMN dt_previsao_inicio_obra_analise SET NOT NULL;

COMMENT ON COLUMN siconv.vrpl_po.dt_previsao_inicio_obra_analise IS 'Data de previsao do inicio da obra da Análise';
COMMENT ON COLUMN siconv.vrpl_po.dt_previsao_inicio_obra IS 'Data de previsao do inicio da obra do VRPL';
