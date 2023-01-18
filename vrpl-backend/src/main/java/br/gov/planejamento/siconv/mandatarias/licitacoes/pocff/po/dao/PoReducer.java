package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;

public class PoReducer implements RowReducer<Map<Long, PoReducerDTO>, PoReducerDTO> {

	private Map<Long, PoReducerDTO> container = new LinkedHashMap<>();
	private Map<Long, MacroServicoReducerDTO> macroServicos = new HashMap<>();
	private Map<Long, ServicoReducerDTO> servicos = new HashMap<>();
	private Map<String, ServicoFrenteObraDTO> servicosFrenteObra = new HashMap<>();
	private Map<Long, ServicoFrenteObraAnaliseDTO> servicosFrenteObraAnalise = new HashMap<>();

	@Override
	public Map<Long, PoReducerDTO> container() {
		return this.container;
	}

	@Override
	public void accumulate(Map<Long, PoReducerDTO> poReducer, RowView rowView) {
		PoReducerDTO poReducerDTO = container.computeIfAbsent(rowView.getColumn("po_id", Long.class),
				id -> rowView.getRow(PoReducerDTO.class));

		Long idMacroServicoDTO = rowView.getColumn("mc_id", Long.class);

		if (idMacroServicoDTO != null) {
			MacroServicoReducerDTO macroServicoDTO = macroServicos.computeIfAbsent(idMacroServicoDTO, id -> {
				MacroServicoReducerDTO itemMacroServico = rowView.getRow(MacroServicoReducerDTO.class);

				poReducerDTO.getMacroServicos().add(itemMacroServico);

				return itemMacroServico;
			});

			Long idServicoDTO = rowView.getColumn("srv_id", Long.class);

			if (idServicoDTO != null) {
				ServicoReducerDTO servicoReducerDTO = servicos.computeIfAbsent(idServicoDTO, id -> {

					ServicoReducerDTO itemServico = rowView.getRow(ServicoReducerDTO.class);

					macroServicoDTO.getServicos().add(itemServico);

					return itemServico;
				});

				String idServicoFrenteObraDTO = rowView.getColumn("vsfo_id", String.class);

				if (idServicoFrenteObraDTO != null) {
					servicosFrenteObra.computeIfAbsent(idServicoFrenteObraDTO, id -> {

						ServicoFrenteObraDTO itemServicoFrenteObra = rowView.getRow(ServicoFrenteObraDTO.class);

						servicoReducerDTO.getFrentesObra().add(itemServicoFrenteObra);

						return itemServicoFrenteObra;
					});
				}

				Long idServicoFrenteObraAnaliseDTO = rowView.getColumn("vsroa_id", Long.class);

				if (idServicoFrenteObraAnaliseDTO != null) {
					servicosFrenteObraAnalise.computeIfAbsent(idServicoFrenteObraAnaliseDTO, id -> {

						ServicoFrenteObraAnaliseDTO itemServicoFrenteObraAnalise = rowView.getRow(ServicoFrenteObraAnaliseDTO.class);

						servicoReducerDTO.getFrentesObraAnalise().add(itemServicoFrenteObraAnalise);

						return itemServicoFrenteObraAnalise;
					});
				}


			}
		}
	}

	@Override
	public Stream<PoReducerDTO> stream(Map<Long, PoReducerDTO> container) {
		return container.values().stream();
	}
}
