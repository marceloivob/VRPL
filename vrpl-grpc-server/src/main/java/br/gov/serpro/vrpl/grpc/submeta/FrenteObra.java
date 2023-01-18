package br.gov.serpro.vrpl.grpc.submeta;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FrenteObra {
	private Long id;
	private String descricao;
	private List<Evento> eventos = new ArrayList<>();
	private List<MacroServico> macroServicos = new ArrayList<>();
	
	public Evento addEventos(Evento evento) {
        int pos = this.eventos.indexOf(evento);
        if(pos == -1) {
            this.eventos.add(evento);
            return evento;
        }

        return this.eventos.get(pos);
    }
	
	public MacroServico addMacroServico(MacroServico macroServico) {
        int pos = this.macroServicos.indexOf(macroServico);
        if(pos == -1) {
            this.macroServicos.add(macroServico);
            return macroServico;
        }

        return this.macroServicos.get(pos);
    }
}
