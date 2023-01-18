package br.gov.serpro.vrpl.grpc.database.bean;

import br.gov.serpro.vrpl.grpc.services.SituacaoLicitacaoEnum;
import lombok.Data;

@Data
public class LicitacaoBD {

	private Long prop_fk;

	private Long idLicitacao;

	private String situacaoVRPL;

	private String situacaoAnalise;

	public SituacaoLicitacaoEnum getSituacao() {
		if (situacaoVRPL != null) {
			return SituacaoLicitacaoEnum.fromSigla(this.situacaoVRPL);
		} else if (situacaoAnalise != null) {
			return SituacaoLicitacaoEnum.fromSigla(this.situacaoAnalise);
		}

		return null;
	}

}
