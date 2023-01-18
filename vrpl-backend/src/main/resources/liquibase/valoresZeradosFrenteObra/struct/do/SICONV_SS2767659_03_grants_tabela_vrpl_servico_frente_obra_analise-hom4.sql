ALTER TABLE siconv.vrpl_servico_frente_obra_analise                    						OWNER TO owner_siconv4_h;
GRANT SELECT, USAGE  ON SEQUENCE siconv.vrpl_servico_frente_obra_analise_seq	        			TO usr_siconv4_h;
GRANT SELECT, INSERT, DELETE  ON TABLE siconv.vrpl_servico_frente_obra_analise	         				TO usr_siconv4_h;

ALTER TABLE siconv.vrpl_servico_frente_obra_analise_log_rec            						OWNER TO owner_siconv4_h;
GRANT SELECT, USAGE  ON SEQUENCE siconv.vrpl_servico_frente_obra_analise_log_rec_seq        			TO usr_siconv4_h;
GRANT SELECT, INSERT  ON TABLE siconv.vrpl_servico_frente_obra_analise_log_rec         				TO usr_siconv4_h;

