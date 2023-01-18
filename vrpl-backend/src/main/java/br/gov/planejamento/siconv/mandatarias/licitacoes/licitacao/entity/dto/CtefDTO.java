package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.CtefIntegracao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CtefDTO {

    private String numero;

    private String objetoDoContrato;

    private BigDecimal valorTotal;

    private LocalDate dataAssinatura;

    private LocalDate dataFimDaVigencia;

    public CtefDTO from(CtefIntegracao ctefIntegracao) {
        this.numero = ctefIntegracao.numeroDoCtef;
        this.objetoDoContrato = ctefIntegracao.objetoDoContrato;
        this.valorTotal = ctefIntegracao.valorTotal;
        this.dataAssinatura = ctefIntegracao.dataDeAssinatura;
        this.dataFimDaVigencia = ctefIntegracao.dataFimDaVigencia;

        return this;
    }

}
