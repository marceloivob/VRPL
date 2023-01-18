package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvGRPCConsumer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.extern.slf4j.Slf4j;

/**
 * RN: 566037 -
 * SICONV-DocumentosOrcamentarios-ManterQuadroResumo-RN-CriterioParaSubmeterValidacaoEGT-3e4
 */
@Slf4j
public class CriterioParaSubmeterValidacaoEGT4 {

	@Inject
	private SiconvGRPCConsumer siconvGRPCConsumer;

	public Boolean saoAtendidosPor(PropostaBD proposta, LicitacaoBD licitacao, SiconvPrincipal usuarioLogado) {
		log.info("Parâmetros para EGT4: modalidade: {}, Usuario É Mandatária: {}, idLicitacaoFk: {}",
				proposta.getModalidade(), usuarioLogado.isMandataria(), licitacao.getIdLicitacaoFk());

		boolean propostaEhContratoDeRepasse = getModalidade(proposta);

		boolean usuarioEhMandataria = usuarioLogado.isMandataria();

		boolean naoHouveAceite = getNaoHouveAceite(licitacao);

		log.info(
				"Verificando se atende requisitos para validar o EGT4: ehContratoDeRepasse: {}, usuarioEhMandataria: {}, naoHouveAceite: {}",
				propostaEhContratoDeRepasse, usuarioEhMandataria, naoHouveAceite);

		boolean atende = propostaEhContratoDeRepasse;

		atende = atende && usuarioEhMandataria;

		atende = atende && naoHouveAceite;

		log.info("Pode vincular EGT4: {}", atende);

		return atende;
	}

	private boolean getModalidade(PropostaBD proposta) {
		boolean ehContratoDeRepasse = ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo() == proposta.getModalidade()
				.intValue();

		return ehContratoDeRepasse;
	}

	private boolean getNaoHouveAceite(LicitacaoBD licitacao) {

		return !siconvGRPCConsumer.existeHistoricoAceiteLicitacaoPossuiSituacao(licitacao.getIdLicitacaoFk(),
				br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum.ACEITO);

	}

}
