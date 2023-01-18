package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class CodigoEFonteDeServico {
	
	private String cdServico;
	private String inFonte;
	
	public static CodigoEFonteDeServico from(ServicoBD servico) {
		CodigoEFonteDeServico objeto = new CodigoEFonteDeServico();
		objeto.setCdServico(servico.getCdServico());
		objeto.setInFonte(servico.getInFonte());
		
		return objeto;
	}

}
