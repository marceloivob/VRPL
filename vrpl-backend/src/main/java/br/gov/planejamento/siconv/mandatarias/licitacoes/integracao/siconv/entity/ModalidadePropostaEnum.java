package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity;

import javax.management.RuntimeErrorException;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.TipoOrgao;


public enum ModalidadePropostaEnum {
	CONVENIO(1, "Convênio") {
		@Override
		public TipoOrgao getTipoOrgao() {
			return TipoOrgao.CONCEDENTE;
		}
	},
	CONTRATO_DE_REPASSE(2, "Contrato de Repasse") {
		@Override
		public TipoOrgao getTipoOrgao() {
			return TipoOrgao.INST_MANDATARIA;
		}
	},
	CONVENIO_CONTRATO_DE_REPASSE(6, "Convênio ou Contrato de Repasse") {
		@Override
		public TipoOrgao getTipoOrgao() {
			return TipoOrgao.CONCEDENTE;
		}
	},
	TERMO_DE_COMPROMISSO_MANDATARIA (11,"Termo de Compromisso", true) {
		@Override
		public TipoOrgao getTipoOrgao() {
			return TipoOrgao.INST_MANDATARIA;
		}
	},
	TERMO_DE_COMPROMISSO_CONCEDENTE (11,"Termo de Compromisso", false) {
		@Override
		public TipoOrgao getTipoOrgao() {
			return TipoOrgao.CONCEDENTE;
		}
	};

	private final int codigo;
	private final String descricao;
	private boolean possuiMandataria = false;

	public abstract TipoOrgao getTipoOrgao();

	private ModalidadePropostaEnum(final int codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	/**
	 * construtor para Termo de compromisso
	 * @param codigo
	 * @param descricao
	 * @param possuiMandataria
	 */
	private ModalidadePropostaEnum(final int codigo, final String descricao, boolean possuiMandataria) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.setPossuiMandataria(possuiMandataria);
	}

	public String getDescricao() {
		return descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public static ModalidadePropostaEnum fromCodigo(Integer codigo, Boolean possuiMandataria) {

		if (codigo == null) {
			throw new RuntimeErrorException(new Error(), "Código de modalidade de proposta inválido");
		}

		for (ModalidadePropostaEnum obj : ModalidadePropostaEnum.values()) {
			if (codigo == ModalidadePropostaEnum.TERMO_DE_COMPROMISSO_MANDATARIA.getCodigo()) {
				if (possuiMandataria == null || !possuiMandataria) {
					return ModalidadePropostaEnum.TERMO_DE_COMPROMISSO_CONCEDENTE;
				}
				else {
					return ModalidadePropostaEnum.TERMO_DE_COMPROMISSO_MANDATARIA;
				}
			} else if (obj.getCodigo() == codigo) {
				return obj;
			}
		}
		 throw new IllegalArgumentException("Modalidade desconhecida: " + codigo);
	}

	public boolean isPossuiMandataria() {
		return possuiMandataria;
	}

	public void setPossuiMandataria(boolean possuiMandataria) {
		this.possuiMandataria = possuiMandataria;
	}
}
