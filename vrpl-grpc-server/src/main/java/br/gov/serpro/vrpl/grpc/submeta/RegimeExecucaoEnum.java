package br.gov.serpro.vrpl.grpc.submeta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RegimeExecucaoEnum {
    EPG("Empreitada por Preço Global"),

    EPU("Empreitada por Preço Unitário"),

    EPI("Empreitada Integral"),

    TRF("Tarefa"),

    RDC("RDC - Contratação Integrada"),

    RSI("RDC - Contratação Semi-integrada");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    public static RegimeExecucaoEnum fromSigla(final String sigla) {
        for (RegimeExecucaoEnum tipo : RegimeExecucaoEnum.values()) {
            if (tipo.name().equalsIgnoreCase(sigla)) {
                return tipo;
            }
        }

        return null;
    }
}
