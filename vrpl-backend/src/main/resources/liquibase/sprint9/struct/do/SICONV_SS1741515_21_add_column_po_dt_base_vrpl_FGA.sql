ALTER TABLE siconv.vrpl_po RENAME COLUMN dt_base TO dt_base_analise;
ALTER TABLE siconv.vrpl_po ADD COLUMN dt_base_vrpl date;

COMMENT ON COLUMN siconv.vrpl_po.dt_base_analise IS 'Data base oriunda da fase de análise';
COMMENT ON COLUMN siconv.vrpl_po.dt_base_vrpl IS 'Data base preenchida no VRPL';

