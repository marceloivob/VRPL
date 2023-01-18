package br.gov.serpro.vrpl.grpc.submeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Submeta {
	
	private Long id;
	private String numero;	
	private String descricao;	
	private BigDecimal valorLicitado;
	private List<FrenteObra> frentesObras = new ArrayList<>();
	
	public FrenteObra addFrentesObras(FrenteObra frenteObra) {
        int pos = this.frentesObras.indexOf(frenteObra);
        if(pos == -1) {
            this.frentesObras.add(frenteObra);
            return frenteObra;
        }

        return this.frentesObras.get(pos);
    }
}
