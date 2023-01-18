package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto;

import java.math.BigDecimal;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import lombok.Data;

@Data
public class ServicoFrenteObraDTO implements Comparable<ServicoFrenteObraDTO> {

    private Long frenteObraFk;
    private BigDecimal qtItens;
    private Long servicoFk;
	private Long versao;
	private boolean indicadorDadoAnalise = false;

    // info frenteObra
    private String nomeFrente;
    private Integer numeroFrente;
    private Long idPO;

    public ServicoFrenteObraBD converterParaBD() {
        ServicoFrenteObraBD servicoFrenteObraBD = new ServicoFrenteObraBD();
        servicoFrenteObraBD.setFrenteObraFk(this.getFrenteObraFk());
        servicoFrenteObraBD.setServicoFk(this.servicoFk);
        servicoFrenteObraBD.setQtItens(this.qtItens);

        return servicoFrenteObraBD;
    }

	@Override
	public int compareTo(ServicoFrenteObraDTO o) {
		return numeroFrente.compareTo(o.numeroFrente);
	}
	
	public static ServicoFrenteObraDTO from(ServicoFrenteObraBD fromBD) {
		ServicoFrenteObraDTO sfo = new ServicoFrenteObraDTO();
		
		sfo.setFrenteObraFk(fromBD.getFrenteObraFk());
		sfo.setQtItens(fromBD.getQtItens());
		sfo.setServicoFk(fromBD.getServicoFk());
		sfo.setVersao(fromBD.getVersao());
		
		return sfo;
	}

}
