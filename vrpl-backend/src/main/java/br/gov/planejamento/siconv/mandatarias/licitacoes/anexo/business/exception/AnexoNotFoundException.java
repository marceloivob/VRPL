package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;

public class AnexoNotFoundException extends BusinessException {

	private static final long serialVersionUID = -6076459707398404202L;

	private static final Logger logger = LoggerFactory.getLogger(AnexoNotFoundException.class);

	public AnexoNotFoundException(@NotNull Long identificadorDoAnexo, @NotNull String nomeDoArquivo,
			@NotNull String descricaoDoArquivo, @NotNull TipoAnexoEnum tipoDoAnexo, @NotNull Long versao) {
		super(ErrorInfo.ANEXO_NAO_ENCONTRADO);

		logger.warn(
				"O anexo informado não foi encontrado. Identificador do Anexo: '{}', Nome do Arquivo: '{}', Descrição: '{}', Tipo do Anexo: '{}', Versão: '{}'",
				identificadorDoAnexo, nomeDoArquivo, descricaoDoArquivo, tipoDoAnexo, versao);

	}

}
