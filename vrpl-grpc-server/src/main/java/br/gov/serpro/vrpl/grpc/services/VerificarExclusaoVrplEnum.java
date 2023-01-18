package br.gov.serpro.vrpl.grpc.services;

public enum VerificarExclusaoVrplEnum {

    /**
	 * "1" - "Proposta não existe"
	 */
	PROPOSTA_NAO_EXISTE(1, "Proposta não existe"),

	/**
	 * "2" - "Proposta existe e pode ser excluída"
	 */
	PROPOSTA_PODE_SER_EXCLUIDA(2, "Proposta existe e pode ser excluída"),

	/**
	 * "3" - "Proposta possui lotes vinculados"
	 */
	PROPOSTA_LOTES_VINCULADOS(3, "Proposta com lotes vinculados"),

    /**
	 * "4" - "Proposta versionada, mas nenhum vínculo"
	 */
	PROPOSTA_VERSIONADA(4, "Proposta versionada, mas nenhum vínculo"),

	/** 
	 * "5" - "Proposta excluída"
	*/
	PROPOSTA_EXCLUIDA(5, "Proposta excluída");

    private final Integer codigo;
    private final String descricao;

    VerificarExclusaoVrplEnum(final Integer codigo, final String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static VerificarExclusaoVrplEnum fromCodigo(final Integer codigo) {
		for (VerificarExclusaoVrplEnum situacao : VerificarExclusaoVrplEnum.values()) {
			if (situacao.getCodigo().equals(codigo)) {
				return situacao;
			}
		}

		throw new IllegalArgumentException(codigo.toString());
	}
    
}
