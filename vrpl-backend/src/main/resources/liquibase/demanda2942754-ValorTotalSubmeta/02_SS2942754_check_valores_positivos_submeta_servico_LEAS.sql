alter table siconv.vrpl_submeta
add constraint submeta_vl_positivo check (
	vl_total_licitado >= 0 and 
	vl_repasse >= 0 and 
	vl_contrapartida >= 0 and 
	vl_outros >= 0
);

alter table siconv.vrpl_servico
add constraint servico_vl_positivo check (
	vl_custo_unitario_ref_analise >= 0 and
	pc_bdi_analise >= 0 and
	qt_total_itens_analise >= 0 and
	vl_custo_unitario_analise >= 0 and
	vl_preco_unitario_analise >= 0 and
	vl_preco_total_analise >= 0 and
	pc_bdi_licitado >= 0 and
	vl_preco_unitario_licitado >= 0
);
