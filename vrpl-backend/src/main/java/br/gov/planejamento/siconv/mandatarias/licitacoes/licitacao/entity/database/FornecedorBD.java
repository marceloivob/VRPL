package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.FornecedorIntegracao;
import br.gov.serpro.siconv.grpc.Fornecedor;
import lombok.Data;

@Data
public class FornecedorBD {
    private Long id;
    private Long licitacaoFk;
    private String razaoSocial;
    private PersonalidadeJuridica tipoIdentificacao;
    private String identificacao;
    private Boolean obsoleto;
    private Integer versaoNr;
    private String versaoId;
	private String versaoNmEvento;
    private Long versao;

    public static FornecedorBD from(FornecedorIntegracao fornecedorIntegracao) {
        FornecedorBD fornecedor = new FornecedorBD();

        fornecedor.id = fornecedorIntegracao.getId();
        fornecedor.razaoSocial = fornecedorIntegracao.getRazaoSocial();
        fornecedor.tipoIdentificacao = PersonalidadeJuridica.valueOf(fornecedorIntegracao.getTipoIdentificacao());
        fornecedor.identificacao = fornecedorIntegracao.getIdentificacao();
        fornecedor.obsoleto = false;

        return  fornecedor;
    }

	public void updateFrom(Fornecedor fornecedorServico) {
		this.razaoSocial = fornecedorServico.getRazaoSocial();
		this.tipoIdentificacao = PersonalidadeJuridica.valueOf(fornecedorServico.getTipoIdentificacao());
		this.identificacao = fornecedorServico.getIdentificacao();
		this.obsoleto = false;
	}
	
	public void updateFrom(FornecedorBD fornecedorServico) {
		this.razaoSocial = fornecedorServico.getRazaoSocial();
		this.tipoIdentificacao = fornecedorServico.getTipoIdentificacao();
		this.identificacao = fornecedorServico.getIdentificacao();
		this.obsoleto = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FornecedorBD other = (FornecedorBD) obj;
		return java.util.Objects.equals(identificacao, other.identificacao)
				&& java.util.Objects.equals(licitacaoFk, other.licitacaoFk)
				&& java.util.Objects.equals(razaoSocial, other.razaoSocial)
				&& tipoIdentificacao == other.tipoIdentificacao;
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(identificacao, licitacaoFk, razaoSocial, tipoIdentificacao);
	}

   

}
