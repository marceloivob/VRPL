package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropostaSemDadosImportados extends BusinessException{

	private static final long serialVersionUID = -6711836617986885900L;

	public PropostaSemDadosImportados(SiconvPrincipal usuarioLogado) {
		
        super(ErrorInfo.PROPOSTA_SEM_DADOS_IMPORTADOS);

		log.debug("Mensagem: [{}], IdProposta: {}, Usuario: {}]", ErrorInfo.PROPOSTA_SEM_DADOS_IMPORTADOS,
				usuarioLogado.getIdProposta(), usuarioLogado.getCpf());

	}

}
