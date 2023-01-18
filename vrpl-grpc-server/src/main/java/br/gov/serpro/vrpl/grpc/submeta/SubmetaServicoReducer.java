package br.gov.serpro.vrpl.grpc.submeta;

import java.util.HashMap;
import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class SubmetaServicoReducer implements LinkedHashMapRowReducer<Long, Submeta> {

	private Map<Long, FrenteObra> frentesObras = new HashMap<>();
	private Map<Long, Servico> servicos = new HashMap<>();
	private Map<Long, MacroServico> macroServicos = new HashMap<>();;

	@Override
	public void accumulate(Map<Long, Submeta> container, RowView rowView) {
		Submeta submeta = container.computeIfAbsent(rowView.getColumn("subm_id", Long.class),id -> rowView.getRow(Submeta.class));

		Long idFrenteDeObra = rowView.getColumn("fo_id", Long.class);
		FrenteObra frenteObraMacroServico = frentesObras.computeIfAbsent(idFrenteDeObra, id -> { return rowView.getRow(FrenteObra.class);});		
		submeta.addFrentesObras(frenteObraMacroServico);
		
		Long idMacroServico = rowView.getColumn("macro_id", Long.class);
		MacroServico macroServico = macroServicos.computeIfAbsent(idMacroServico, id -> {return rowView.getRow(MacroServico.class);});
		frenteObraMacroServico.addMacroServico(macroServico);
		
		Long idServico = rowView.getColumn("serv_id", Long.class);
		Servico servico = servicos.computeIfAbsent(idServico, id -> {return rowView.getRow(Servico.class);});
		macroServico.addServico(servico);		
	}
	
}
