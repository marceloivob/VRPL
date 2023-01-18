package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.GeradorDeTicket;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.VersionamentoPorPropostaBC;
import lombok.Data;

@Data
public abstract class QuadroResumoBaseAction implements QuadroResumoAction {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Inject
	private DAOFactory dao;

	@Inject
	private GeradorDeTicket geradorDeTicket;

	@Inject
	private MailerUtils mailer;

	@Inject
	private HistoricoLicitacaoBC historicoBC;

	@Inject
	private SiconvPrincipal principal;

	@Inject
	private SiconvBC siconvBC;

	@Inject
	private VersionamentoPorPropostaBC versionamentoPorProposta;

	@Inject
	private BusinessExceptionContext businessExceptionContext;


	@Override
	public void validar(@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao, HistoricoLicitacaoDTO historico) {
		List<QuadroResumoValidator> validators = this.getValidators(proposta, licitacao, historico);
		if (validators != null && !validators.isEmpty()) {
			validators.forEach(validator -> validator.validate());
		}
	}

	@Override
	public void executarIntegracoes(@NotNull Handle transaction, @NotNull HistoricoLicitacaoDTO historico,
			@NotNull PropostaBD proposta, @NotNull LicitacaoBD licitacao) {
	}

	@Override
	public EmailInfo prepararEmail(PropostaBD proposta, LicitacaoBD licitacao) {
		return null;
	}

	@Override
	public void execute(@NotNull HistoricoLicitacaoDTO historicoRecebido, @NotNull PropostaBD proposta,
			@NotNull LicitacaoBD licitacao) {
		// TODO: Concatenar todos os erros de validação
		// TODO: Verificar no frontend a exibição genérica do ticket

		preProcess(historicoRecebido, proposta, licitacao);

		dao.getJdbi().useTransaction(transaction -> process(transaction, historicoRecebido, proposta, licitacao));

		postProcess(proposta, licitacao);
	}

	private void preProcess(HistoricoLicitacaoDTO historicoRecebido, PropostaBD proposta, LicitacaoBD licitacao) {
		this.validar(proposta, licitacao, historicoRecebido);
		businessExceptionContext.throwException();
	}

	private void postProcess(PropostaBD proposta, LicitacaoBD licitacao) {
		try {
			if (executarEnvioEmail()) {
				EmailInfo mailInfo = this.prepararEmail(proposta, licitacao);
				this.getMailer().send(mailInfo);
			}
		} catch (RuntimeException ex) {
			log.error("Exception: ", ex);
			throw ex;
		}
	}

	private void process(Handle transaction, HistoricoLicitacaoDTO historicoRecebido, PropostaBD proposta,
			LicitacaoBD licitacao) {

		try {
			if (executarRegistroDeHistorico()) {
				this.gerarHistorico(transaction, historicoRecebido, licitacao);
			}

			if (executarVersionamentoComHistorico()) {
				this.versionamentoPorProposta.versionarProposta(transaction, proposta, licitacao, historicoRecebido, false);
			}

			if (executarVersionamentoSemHistorico()) {
				this.versionamentoPorProposta.versionarProposta(transaction, proposta, licitacao, historicoRecebido, true);
			}

			if (executarChamadaServicos()) {
				this.executarIntegracoes(transaction, historicoRecebido, proposta, licitacao);
			}
		} catch (RuntimeException ex) {
			log.error("Exception: ", ex);
			transaction.rollback();
			throw ex;
		}
	}

	@Override
	public void gerarHistorico(Handle transaction, HistoricoLicitacaoDTO historico, LicitacaoBD licitacao) {
		HistoricoLicitacaoBC hb = this.getHistoricoBC();
		hb.gerarHistoricoEmTransacao(licitacao, historico, transaction);
	}

	protected SiconvPrincipal getUsuarioLogado() {
		return principal;
	}

	private boolean executarRegistroDeHistorico() {
		boolean historico = true;
		if (this.getClass().isAnnotationPresent(QuadroResumoEventConfig.class)) {
			historico = this.getClass().getAnnotation(QuadroResumoEventConfig.class).historico();
		}
		return historico;
	}

	private boolean executarEnvioEmail() {
		boolean email = false;
		if (this.getClass().isAnnotationPresent(QuadroResumoEventConfig.class)) {
			email = this.getClass().getAnnotation(QuadroResumoEventConfig.class).email();
		}
		return email;
	}

	private boolean executarVersionamentoComHistorico() {
		boolean versionamentoComHistorico = false;
		if (this.getClass().isAnnotationPresent(QuadroResumoEventConfig.class)) {
			versionamentoComHistorico = this.getClass().getAnnotation(QuadroResumoEventConfig.class).versionamentoComHistorico();
		}
		return versionamentoComHistorico;
	}

	private boolean executarVersionamentoSemHistorico() {
		boolean versionamentoSemHistorico = false;
		if (this.getClass().isAnnotationPresent(QuadroResumoEventConfig.class)) {
			versionamentoSemHistorico = this.getClass().getAnnotation(QuadroResumoEventConfig.class).versionamentoSemHistorico();
		}
		return versionamentoSemHistorico;
	}

	private boolean executarChamadaServicos() {
		boolean servicos = false;
		if (this.getClass().isAnnotationPresent(QuadroResumoEventConfig.class)) {
			servicos = this.getClass().getAnnotation(QuadroResumoEventConfig.class).servicos();
		}
		return servicos;
	}
}
