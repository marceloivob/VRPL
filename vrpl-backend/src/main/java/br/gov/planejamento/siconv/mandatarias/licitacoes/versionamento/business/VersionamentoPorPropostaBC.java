package br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business.LiberarLoteBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.HistoricoLicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database.HistoricoLicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

public class VersionamentoPorPropostaBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private LiberarLoteBC liberarLote;

	@Inject
	private HistoricoLicitacaoBC historicoBC;

	@Log
	public void versionarProposta(Handle transaction, PropostaBD proposta, LicitacaoBD licitacaoEstadoAtual,
			HistoricoLicitacaoDTO novoHistorico, boolean jaSalvouHistorico) {

			Clone<PropostaBD> propostaClonada = this.criarVersaoDaProposta(proposta, novoHistorico, transaction);

			List<LicitacaoBD> licitacoesClonadas = transaction.attach(VersionamentoDAO.class)
					.selectLicitacaoParaClonarPorProposta(propostaClonada.getObjetoClonado());

			Optional<LicitacaoBD> licitacaoClonada = licitacoesClonadas.stream()
					.filter(licitacao -> licitacao.getVersaoId().equals(licitacaoEstadoAtual.getId().toString()))
					.findFirst();

			if (licitacaoClonada.isPresent()) {
				if (EventoQuadroResumoEnum.REJEITAR.equals(novoHistorico.getEventoGerador())) {

					this.liberarLote.liberaLotesDa(licitacaoClonada.get()).comAProposta(propostaClonada)
							.usandoATransacao(transaction).executar();

				} else if (EventoQuadroResumoEnum.CANCELAR_REJEITE.equals(novoHistorico.getEventoGerador())) {

					this.liberarLote.liberaLotesDa(licitacaoClonada.get()).comAProposta(propostaClonada)
							.usandoATransacao(transaction).desfazerExecutar();
				}

				if (!jaSalvouHistorico) {
					historicoBC.gerarHistoricoEmTransacao(licitacaoClonada.get(), novoHistorico, transaction);
				}

			} else {
				throw new IllegalArgumentException(String.format(
						"É esperado que exista uma Licitação Clonada para a licitacao %s. Relação de Licitacoes Clonadas: %s ",
						licitacaoEstadoAtual, licitacoesClonadas));
			}
	}

	public Clone<PropostaBD> criarVersaoDaProposta(PropostaBD propostaOriginal, HistoricoLicitacaoDTO novoHistorico,
			Handle transaction) {

		Objects.requireNonNull(propostaOriginal);
		Objects.requireNonNull(propostaOriginal.getId());
		Objects.requireNonNull(propostaOriginal.getVersao());
		Objects.requireNonNull(novoHistorico);
		Objects.requireNonNull(novoHistorico.getEventoGerador());
		Objects.requireNonNull(transaction);

		EventoQuadroResumoEnum eventoGerador = novoHistorico.getEventoGerador();

		VersionamentoDAO dao = transaction.attach(VersionamentoDAO.class);

		propostaOriginal = transaction.attach(PropostaDAO.class).loadById(propostaOriginal.getId());

		ClonadorDeProposta clonadorDeProposta = new ClonadorDeProposta(dao, eventoGerador);

		ClonadorDeSubItemDeInvestimento clonadorDeSubItemDeInvestimento = new ClonadorDeSubItemDeInvestimento(dao,
				eventoGerador);
		ClonadorDeMeta clonadorDeMeta = new ClonadorDeMeta(dao, eventoGerador);

		ClonadorDeLicitacaoPorProposta clonadorDeLicitacaoPorProposta = new ClonadorDeLicitacaoPorProposta(dao,
				eventoGerador);

		ClonadorDeFornecedor clonadorDeFornecedor = new ClonadorDeFornecedor(dao, eventoGerador);

		ClonadorDeAnexo clonadorDeAnexo = new ClonadorDeAnexo(dao, eventoGerador);

		ClonadorDeHistorico clonadorDeHistorico = new ClonadorDeHistorico(dao, eventoGerador);

		ClonadorDeLote clonadorDeLote = new ClonadorDeLote(dao, eventoGerador);

		ClonadorDeSubmeta clonadorDeSubmeta = new ClonadorDeSubmeta(dao, eventoGerador);

		ClonadorDeLaudo clonadorDeLaudo = new ClonadorDeLaudo(dao, eventoGerador);

		ClonadorDePendencia clonadorDePendencia = new ClonadorDePendencia(dao, eventoGerador);

		ClonadorDeResposta clonadorDeResposta = new ClonadorDeResposta(dao, eventoGerador);

		ClonadorDePO clonadorDePO = new ClonadorDePO(dao, eventoGerador);

		ClonadorDeEvento clonadorDeEvento = new ClonadorDeEvento(dao, eventoGerador);

		ClonadorDeFrenteDeObra clonadorDeFrenteDeObra = new ClonadorDeFrenteDeObra(dao, eventoGerador);

		ClonadorDeEventoFrenteDeObra clonadorDeEventoFrenteDeObra = new ClonadorDeEventoFrenteDeObra(dao,
				eventoGerador);

		ClonadorDeMacroServico clonadorDeMacroServico = new ClonadorDeMacroServico(dao, eventoGerador);

		ClonadorDeMacroServicoParcela clonadorDeMacroServicoParcela = new ClonadorDeMacroServicoParcela(dao,
				eventoGerador);

		ClonadorDeServico clonadorDeServico = new ClonadorDeServico(dao, eventoGerador);

		ClonadorDeServicoFrenteDeObra clonadorDeServicoFrenteDeObra = new ClonadorDeServicoFrenteDeObra(dao,
				eventoGerador);

		ClonadorDeServicoFrenteDeObraAnalise clonadorDeServicoFrenteDeObraAnalise =
				new ClonadorDeServicoFrenteDeObraAnalise(dao, eventoGerador);

		/////////////////////////

		//proposta
		Clone<PropostaBD> cloneDeProposta = clonadorDeProposta.clone(propostaOriginal);

		//subitemInvestimento
		List<Clone<SubitemInvestimentoBD>> cloneDeSubItemDeInvestimento = clonadorDeSubItemDeInvestimento
				.clone(propostaOriginal);

		//Meta
		List<Clone<MetaBD>> cloneDeMeta = clonadorDeMeta.clone(propostaOriginal, cloneDeSubItemDeInvestimento);

		//Licitacao
		List<Clone<LicitacaoBD>> cloneDeLicitacao = clonadorDeLicitacaoPorProposta.clone(cloneDeProposta);

		//fornecedor
		List<Clone<FornecedorBD>> cloneDeFornecedor = clonadorDeFornecedor.clone(cloneDeLicitacao);

		//anexo
		List<Clone<AnexoBD>> cloneDeAnexo = clonadorDeAnexo.clone(cloneDeLicitacao);

		//historico
		List<Clone<HistoricoLicitacaoBD>> cloneDeHistorico = clonadorDeHistorico.clone(cloneDeLicitacao);

		//Lote
		List<Clone<LoteBD>> cloneDeLote = clonadorDeLote.clone(cloneDeProposta, cloneDeLicitacao, cloneDeFornecedor);

		//Submeta
		List<Clone<SubmetaBD>> cloneDeSubmeta = clonadorDeSubmeta.clone(cloneDeProposta, cloneDeMeta, cloneDeLote);

		//laudo
		List<Clone<LaudoBD>> cloneDeLaudo = clonadorDeLaudo.clone(cloneDeLicitacao);

		//pendencia
		List<Clone<PendenciaBD>> cloneDePendencia = clonadorDePendencia.clone(cloneDeLaudo, cloneDeSubmeta);

		//resposta
		List<Clone<RespostaBD>> cloneDeResposta = clonadorDeResposta.clone(cloneDeLaudo, cloneDeLote);

		//PO
		List<Clone<PoBD>> cloneDePO = clonadorDePO.clone(cloneDeSubmeta);

		//evento
		List<Clone<EventoBD>> cloneDeEvento = clonadorDeEvento.clone(cloneDePO);

		//frenteObra
		List<Clone<FrenteObraBD>> cloneDeFrenteDeObra = clonadorDeFrenteDeObra.clone(cloneDePO);

		//eventoFrenteObra
		List<Clone<EventoFrenteObraBD>> cloneDeEventoFrenteDeObra = clonadorDeEventoFrenteDeObra.clone(cloneDeEvento,
				cloneDeFrenteDeObra);

		//macroservico
		List<Clone<MacroServicoBD>> cloneDeMacroServico = clonadorDeMacroServico.clone(cloneDePO);

		//macroServicoParcela
		List<Clone<MacroServicoParcelaBD>> cloneDeMacroServicoParcela = clonadorDeMacroServicoParcela
				.clone(cloneDeMacroServico);

		//servico
		List<Clone<ServicoBD>> cloneDeServico = clonadorDeServico.clone(cloneDeEvento, cloneDeMacroServico);

		//servicoFrenteObra
		List<Clone<ServicoFrenteObraBD>> cloneDeServicoFrenteDeObra = clonadorDeServicoFrenteDeObra
				.clone(cloneDeServico, cloneDeFrenteDeObra);

		//servicoFrenteObraAnalise
		List<Clone<ServicoFrenteObraAnaliseBD>> cloneDeServicoFrenteDeObraAnalise = clonadorDeServicoFrenteDeObraAnalise.clone(cloneDeServico);

		return cloneDeProposta;
	}

}
