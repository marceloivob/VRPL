package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MudancaDeEstadoNaoPermitidaException extends BusinessException {

	private static final long serialVersionUID = -941154011893699087L;

	public MudancaDeEstadoNaoPermitidaException(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SiconvPrincipal usuarioLogado) {
		super(ErrorInfo.MUDANCA_ESTADO_DOCUMENTACAO_NAO_PERMITIDA);

		log.debug(
				"Mensagem: [{}], Proposta é versão Atual: [{}], Situacao da Documentação: [{}], Usuário: [{}], Perfil do Usuário: [{}]",
				ErrorInfo.MUDANCA_ESTADO_DOCUMENTACAO_NAO_PERMITIDA, propostaAtual.ehVersaoAtual(),
				licitacao.getSituacaoDaLicitacao(), usuarioLogado.getCpf(), usuarioLogado.getProfiles());
	}

	public MudancaDeEstadoNaoPermitidaException(PropostaBD propostaAtual, LicitacaoBD licitacao,
			SituacaoLicitacaoEnum situacaoAnterior, SiconvPrincipal usuarioLogado) {
		super(ErrorInfo.MUDANCA_ESTADO_DOCUMENTACAO_NAO_PERMITIDA);

		log.debug(
				"Mensagem: [{}], Proposta é versão Atual: [{}], Situacao Atual da Documentação: [{}], Situacao Anterior da Documentação: [{}], Usuário: [{}], Perfil do Usuário: [{}]",
				ErrorInfo.MUDANCA_ESTADO_DOCUMENTACAO_NAO_PERMITIDA, propostaAtual.ehVersaoAtual(),
				licitacao.getSituacaoDaLicitacao(), situacaoAnterior, usuarioLogado.getCpf(),
				usuarioLogado.getProfiles());
	}
}
