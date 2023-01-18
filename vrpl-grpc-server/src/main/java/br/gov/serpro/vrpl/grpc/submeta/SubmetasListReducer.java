package br.gov.serpro.vrpl.grpc.submeta;

import java.util.HashMap;
import java.util.Map;

import org.jdbi.v3.core.result.LinkedHashMapRowReducer;
import org.jdbi.v3.core.result.RowView;

public class SubmetasListReducer implements LinkedHashMapRowReducer<Long, Submeta> {
	private Map<Long, FrenteObra> frentesObras = new HashMap<>();
	private Map<Long, Evento> eventos = new HashMap<>();

	@Override
	public void accumulate(Map<Long, Submeta> container, RowView rowView) {
		Submeta submeta = container.computeIfAbsent(rowView.getColumn("subm_id", Long.class),
				id -> rowView.getRow(Submeta.class));

		Long idFrenteDeObra = rowView.getColumn("fo_id", Long.class);

		FrenteObra frenteObraEvento = frentesObras.computeIfAbsent(idFrenteDeObra, id -> {
			eventos = new HashMap<>();
			
			FrenteObra fo = rowView.getRow(FrenteObra.class);

			submeta.addFrentesObras(fo);

			return fo;
		});
		Long idEvento = rowView.getColumn("ev_id", Long.class);
		Evento evento = eventos.computeIfAbsent(idEvento, id -> {

			return rowView.getRow(Evento.class);
		});

		frenteObraEvento.addEventos(evento);
		evento.addServicos(rowView.getRow(Servico.class));

	}
}
