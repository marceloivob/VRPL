package br.gov.serpro.vrpl.grpc.submeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Evento {
	private Long id;
	private String descricao;
	private BigDecimal valorTotal;
	private List<Servico> servicos = new ArrayList<>();
	
	public Servico addServicos(Servico servico) {
        int pos = this.servicos.indexOf(servico);
        if(pos == -1) {
            this.servicos.add(servico);
            return servico;
        }

        return this.servicos.get(pos);
    }

}
