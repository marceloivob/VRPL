package br.gov.serpro.vrpl.grpc.submeta;

import java.util.HashMap;
import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class SubmetasServicoListReducer implements LinkedHashMapRowReducer<Long, Submeta>  {
	private Map<Long, FrenteObra> frentesObras = new HashMap<>();
	private Map<String, MacroServico> macroServicos = new HashMap<>();
	
	@Override
	public void accumulate(Map<Long, Submeta> container, RowView rowView) {
		Submeta submeta = container.computeIfAbsent(rowView.getColumn("subm_id", Long.class),
				id -> rowView.getRow(Submeta.class));

		Long idFrenteDeObra = rowView.getColumn("fo_id", Long.class);
		FrenteObra frenteObraMacroServico = frentesObras.computeIfAbsent(idFrenteDeObra, 
				id -> {
					FrenteObra fo = rowView.getRow(FrenteObra.class);
            		submeta.addFrentesObras(fo);
         			return fo;
					}
        );
		
		Long idMacroServico = rowView.getColumn("macro_id", Long.class);
		MacroServico macroServico = macroServicos.computeIfAbsent(idFrenteDeObra + "|" + idMacroServico, 
				id -> {
					MacroServico macro = rowView.getRow(MacroServico.class);
					frenteObraMacroServico.addMacroServico(macro);
         			return macro;
					}
        );	
		macroServico.addServico(rowView.getRow(Servico.class));
	}
}
