package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.dto;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import lombok.Data;

@Data
public class PendenciaDTO {

	private Long id;
	private Boolean inResolvida;
	private String prazo;
	private Long laudoFk;
	private String descricao;
	private Long versao;
	private Long submetaFk;
	private String prazoDescricao;
	private String submetaDescricao;
	

	public PendenciaBD converterParaBD() {
		PendenciaBD bd = new PendenciaBD();
		bd.setDescricao(descricao);
		bd.setId(id);
		bd.setInResolvida(inResolvida);
		bd.setLaudoFk(laudoFk);
		bd.setPrazo(prazo);
		bd.setSubmetaFk(submetaFk);
		bd.setVersao(versao);
		
		return bd;
	}
}
