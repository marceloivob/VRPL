package br.gov.serpro.vrpl.grpc.services;

import br.gov.serpro.vrpl.grpc.PermissoesDasLicitacoesResponse.Licitacao.SituacaoLicitacaoVRPLEnum;

public class SituacaoLicitacaoEnumWrapper {

	public SituacaoLicitacaoVRPLEnum convert(SituacaoLicitacaoEnum estadoNoVRPL) {
		if (estadoNoVRPL == null) {
			throw new IllegalArgumentException("Estado não permitido");
		}

		return SituacaoLicitacaoVRPLEnum.valueOf(estadoNoVRPL.name());
	}

}
