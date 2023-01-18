package br.gov.serpro.vrpl.grpc.submeta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SituacaoSubmetaVRPL {

	EPE("Em Preenchimento - Fase de Licitação"),

	EAN("Enviada para Análise - Fase de Licitação"),

	ANL("Em Análise - Fase de Licitação"),

	SCP("Solicitada Complementação - Fase de Licitação"),

	EMC("Em Complementação pelo Convenente"),

	COM("Em Complementação pelo Proponente - Fase de Licitação"),

	ACT("Aceito - Fase de Licitação"),

	ACL("Aceitar / A Licitar"),

	EEX("Em Execução - Fase de Licitação"),

	EXT("Executada - Fase de Licitação"),

	REJ("Rejeitada - Fase de Licitação"),

	EMH("Em homologação - Fase de Licitação"),

	SCC("Solicitada Complementação pela Concedente - Fase de Licitação"),

	ECM("Em Complementação pela Mandatária - Fase de Licitação"),

	HOM("Homologada - Fase de Licitação");

	private final String descricao;
}