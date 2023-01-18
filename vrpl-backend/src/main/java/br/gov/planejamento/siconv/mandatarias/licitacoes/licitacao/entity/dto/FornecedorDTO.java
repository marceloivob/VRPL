package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.CNPJ;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.CPF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.IG;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.TipoDeIdentificacao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;
import br.gov.serpro.siconv.grpc.Fornecedor;
import br.gov.serpro.siconv.grpc.ProcessoExecucaoResponse;
import lombok.Data;

@Data
public class FornecedorDTO {
	private Long id;
	private TipoDeIdentificacao identificacao;
	private String nome;
	private Boolean obsoleto;

	public FornecedorDTO from(ProcessoExecucaoResponse detalheDaLicitacao) {
		if ((detalheDaLicitacao == null) || (detalheDaLicitacao.getListaFornecedoresVencedoresList() == null)
				|| (detalheDaLicitacao.getListaFornecedoresVencedoresList().isEmpty())) {
			return null;
		}

		Fornecedor fornecedor = detalheDaLicitacao.getListaFornecedoresVencedoresList().get(0);

		this.nome = fornecedor.getRazaoSocial();
		PersonalidadeJuridica personalidadeJuridica = PersonalidadeJuridica.valueOf(fornecedor.getTipoIdentificacao());
		switch (personalidadeJuridica) {
		case CPF:
			this.identificacao = new CPF(fornecedor.getIdentificacao());
			break;
		case CNPJ:
			this.identificacao = new CNPJ(fornecedor.getIdentificacao());
			break;
		case IG:
			this.identificacao = new IG(fornecedor.getIdentificacao());
			break;
		}
		
		this.obsoleto = false;

		return this;
	}

	public static FornecedorDTO from(FornecedorBD fornecedorBD) {
		FornecedorDTO fornecedor = new FornecedorDTO();
		fornecedor.setNome(fornecedorBD.getRazaoSocial());

		switch (fornecedorBD.getTipoIdentificacao()) {
		case CPF:
			fornecedor.setIdentificacao(new CPF(fornecedorBD.getIdentificacao()));
			break;
		case CNPJ:
			fornecedor.setIdentificacao(new CNPJ(fornecedorBD.getIdentificacao()));
			break;
		case IG:
			fornecedor.setIdentificacao(new IG(fornecedorBD.getIdentificacao()));
			break;
		}

		fornecedor.setId(fornecedorBD.getId());
		fornecedor.setObsoleto(fornecedorBD.getObsoleto());

		return fornecedor;
	}
	
	public String getIdentificacaoNome() {
		return identificacao.getValor() + " - " + this.nome;
	}

}
