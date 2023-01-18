ALTER TABLE siconv.vrpl_po_log_rec ADD dt_previsao_inicio_obra_analise date;

COMMENT ON COLUMN siconv.vrpl_po_log_rec.dt_previsao_inicio_obra_analise IS 'Data de previsao do inicio da obra da Análise';
COMMENT ON COLUMN siconv.vrpl_po_log_rec.dt_previsao_inicio_obra IS 'Data de previsao do inicio da obra do VRPL';
