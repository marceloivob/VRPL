package br.gov.serpro.vrpl.grpc.submeta;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class MacroServico {
	private Long id;
	private Integer numero;
	private String descricao;	
	private List<Servico> servicos = new ArrayList<>();
	
	public Servico addServico(Servico servico) {
        int pos = this.servicos.indexOf(servico);
        if(pos == -1) {
            this.servicos.add(servico);
            return servico;
        }

        return this.servicos.get(pos);
    }
}
