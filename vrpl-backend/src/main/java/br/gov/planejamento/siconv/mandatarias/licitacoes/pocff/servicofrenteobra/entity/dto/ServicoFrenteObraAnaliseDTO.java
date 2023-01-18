package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto;

import java.math.BigDecimal;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import lombok.Data;

@Data
public class ServicoFrenteObraAnaliseDTO implements Comparable<ServicoFrenteObraAnaliseDTO> {
	 
		private Long id;
	    private Long servicoFk;
	    private BigDecimal qtItens;
	    private Integer nrFrenteObra;
	    private String nmFrenteObra;
		private Long versao;
		
	

    public ServicoFrenteObraAnaliseBD converterParaBD() {
        ServicoFrenteObraAnaliseBD servicoFrenteObraAnaliseBD = new ServicoFrenteObraAnaliseBD();
        servicoFrenteObraAnaliseBD.setNrFrenteObra(this.nrFrenteObra);
        servicoFrenteObraAnaliseBD.setNmFrenteObra(this.nmFrenteObra);
        servicoFrenteObraAnaliseBD.setServicoFk(this.servicoFk);
        servicoFrenteObraAnaliseBD.setQtItens(this.qtItens);
        servicoFrenteObraAnaliseBD.setId(this.id);
        servicoFrenteObraAnaliseBD.setVersao(this.versao);

        return servicoFrenteObraAnaliseBD;
    }

	@Override
	public int compareTo(ServicoFrenteObraAnaliseDTO o) {
		return id.compareTo(o.id);
	}
	
	public static ServicoFrenteObraAnaliseDTO from(ServicoFrenteObraAnaliseBD fromBD) {
		ServicoFrenteObraAnaliseDTO sfo = new ServicoFrenteObraAnaliseDTO();
		
		sfo.setId(fromBD.getId());
		sfo.setVersao(fromBD.getVersao());
		sfo.setNmFrenteObra(fromBD.getNmFrenteObra());
		sfo.setNrFrenteObra(fromBD.getNrFrenteObra());
		sfo.setQtItens(fromBD.getQtItens());
		sfo.setServicoFk(fromBD.getServicoFk());
		
		return sfo;
	}

}
