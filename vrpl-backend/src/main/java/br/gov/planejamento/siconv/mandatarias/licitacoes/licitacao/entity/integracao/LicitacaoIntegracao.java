package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.integracao;

import java.util.ArrayList;
import java.util.List;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.FornecedorIntegracao;
import br.gov.serpro.siconv.grpc.ListaProcessoExecucaoResponse.ProcessoExecucao;
import lombok.Data;

@Data
public class LicitacaoIntegracao {

    private Long id;
    private String numeroAno;
    private String descricaoProcesso;
    private String objeto;
    private List<FornecedorIntegracao> fornecedores = new ArrayList<>();

	public LicitacaoIntegracao(Long id) {
		this.id = id;
	}

    public LicitacaoIntegracao(ProcessoExecucao processoExecucao) {
        this.id = processoExecucao.getId();
        this.numeroAno = processoExecucao.getNumeroAno();
        this.descricaoProcesso = processoExecucao.getDescricaoProcesso();
        this.objeto = processoExecucao.getObjeto();
    }

}
