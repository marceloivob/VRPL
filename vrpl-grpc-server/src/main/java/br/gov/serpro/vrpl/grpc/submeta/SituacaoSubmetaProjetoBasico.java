package br.gov.serpro.vrpl.grpc.submeta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SituacaoSubmetaProjetoBasico {

	HOM("SPA Homologada - Fase de Análise"),

	HAS("SPA Concluída Automaticamente pelo Sistema - Fase de Análise");

	private final String descricao;
}