package br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.business;

import java.util.List;
import java.util.Objects;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.liberacaodelotes.dao.LiberarLoteDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
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
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubitemInvestimentoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.business.Clone;
import br.gov.planejamento.siconv.mandatarias.licitacoes.versionamento.dao.VersionamentoDAO;

/**
 * <b>A8.4.4</b>. Atribui a nova situação da associação "Licitação x Lote",
 * conforme regra de negócio:
 * <p>
 * 567688 -
 * SICONV-DocumentosOrcamentarios-ManterQuadroResumo-VRPL-RN-NovaSituacaoRejeitar
 * <p>
 * Situação "Rejeitada – Fase de Licitação" atribuída à licitação.
 * <p>
 * <b>A8.4.5</b>. Libera os lotes associados à licitação rejeitada, para uso em
 * novas associações, conforme regra de negócio:
 * <p>
 * 595773 -
 * SICONV-DocumentosOrcamentarios-ManterQuadroResumo-VRPL-RN-LiberacaoLotesRejeicao
 */
public class LiberarLoteBC {

	private LicitacaoBD licitacao;

	private Clone<PropostaBD> propostaClonada;

	private Handle transaction;

	private ClonadorDeLotePorLicitacao clonadorDeLotePorLicitacao;

	private ClonadorDeSubItemDeInvestimentoPorLicitacao clonadorDeSubItemDeInvestimentoPorLicitacao;

	private ClonadorDeMetaPorLicitacao clonadorDeMetaPorLicitacao;

	private ClonadorDeSubmetaPorLicitacao clonadorDeSubmetaPorLicitacao;

	private ClonadorDePO clonadorDePO;

	private ClonadorDeEvento clonadorDeEvento;

	private ClonadorDeFrenteDeObra clonadorDeFrenteDeObra;

	private ClonadorDeEventoFrenteDeObra clonadorDeEventoFrenteDeObra;

	private ClonadorDeMacroServico clonadorDeMacroServico;

	private ClonadorDeMacroServicoParcela clonadorDeMacroServicoParcela;

	private ClonadorDeServico clonadorDeServico;

	private ClonadorDeServicoFrenteDeObra clonadorDeServicoFrenteDeObra;

	private ClonadorDeServicoFrenteDeObraAnalise clonadorDeServicoFrenteDeObraAnalise;

	public void executar() {

		SituacaoLicitacaoEnum situacao = SituacaoLicitacaoEnum.EM_PREENCHIMENTO;

		EventoQuadroResumoEnum evento = EventoQuadroResumoEnum.LOTE_LIBERADO_APOS_REJEICAO;

		LicitacaoBD licitacaoOriginal = transaction.attach(LicitacaoDAO.class)
				.findLicitacaoById(this.licitacao.getId());

		LiberarLoteDAO dao = transaction.attach(LiberarLoteDAO.class);

		this.clonadorDeLotePorLicitacao = new ClonadorDeLotePorLicitacao(dao, evento);

		this.clonadorDeSubItemDeInvestimentoPorLicitacao = new ClonadorDeSubItemDeInvestimentoPorLicitacao(
				dao, evento);

		this.clonadorDeMetaPorLicitacao = new ClonadorDeMetaPorLicitacao(dao, evento);

		this.clonadorDeSubmetaPorLicitacao = new ClonadorDeSubmetaPorLicitacao(dao, evento, situacao);

		this.clonadorDePO = new ClonadorDePO(dao, evento);

		this.clonadorDeEvento = new ClonadorDeEvento(dao, evento);

		this.clonadorDeFrenteDeObra = new ClonadorDeFrenteDeObra(dao, evento);

		this.clonadorDeEventoFrenteDeObra = new ClonadorDeEventoFrenteDeObra(dao, evento);

		this.clonadorDeMacroServico = new ClonadorDeMacroServico(dao, evento);

		this.clonadorDeMacroServicoParcela = new ClonadorDeMacroServicoParcela(dao, evento);

		this.clonadorDeServico = new ClonadorDeServico(dao, evento);

		this.clonadorDeServicoFrenteDeObra = new ClonadorDeServicoFrenteDeObra(dao, evento);

		this.clonadorDeServicoFrenteDeObraAnalise = new ClonadorDeServicoFrenteDeObraAnalise(dao, evento);

		/////////////////////////////////////////////////
		//
		/////////////////////////////////////////////////
		List<Clone<LoteBD>> cloneDeLote = clonadorDeLotePorLicitacao.clone(licitacaoOriginal);

		List<Clone<SubitemInvestimentoBD>> cloneDeSubItemDeInvestimento = clonadorDeSubItemDeInvestimentoPorLicitacao.clone(licitacaoOriginal);

		List<Clone<MetaBD>> cloneDeMeta = clonadorDeMetaPorLicitacao.clone(licitacaoOriginal, cloneDeSubItemDeInvestimento);

		List<Clone<SubmetaBD>> cloneDeSubmeta = clonadorDeSubmetaPorLicitacao.clone(licitacaoOriginal, propostaClonada, cloneDeMeta, cloneDeLote);

		List<Clone<PoBD>> cloneDePO = clonadorDePO.clone(cloneDeSubmeta);

		List<Clone<EventoBD>> cloneDeEvento = clonadorDeEvento.clone(cloneDePO);

		List<Clone<FrenteObraBD>> cloneDeFrenteDeObra = clonadorDeFrenteDeObra.clone(cloneDePO);

		List<Clone<EventoFrenteObraBD>> cloneDeEventoFrenteDeObra = clonadorDeEventoFrenteDeObra.clone(cloneDeEvento, cloneDeFrenteDeObra);

		List<Clone<MacroServicoBD>> cloneDeMacroServico = clonadorDeMacroServico.clone(cloneDePO);

		List<Clone<MacroServicoParcelaBD>> cloneDeMacroServicoParcela = clonadorDeMacroServicoParcela.clone(cloneDeMacroServico);

		List<Clone<ServicoBD>> cloneDeServico = clonadorDeServico.clone(cloneDeEvento, cloneDeMacroServico);

		List<Clone<ServicoFrenteObraBD>> cloneDeServicoFrenteDeObra = clonadorDeServicoFrenteDeObra.clone(cloneDeServico, cloneDeFrenteDeObra);

		List<Clone<ServicoFrenteObraAnaliseBD>> cloneDeServicoFrenteDeObraAnalise = clonadorDeServicoFrenteDeObraAnalise.clone(cloneDeServico);
	}

	public void desfazerExecutar() {

		LiberarLoteDAO dao = transaction.attach(LiberarLoteDAO.class);

		VersionamentoDAO versionamentoDAO = transaction.attach(VersionamentoDAO.class);

		this.clonadorDeLotePorLicitacao = new ClonadorDeLotePorLicitacao(dao, versionamentoDAO);

		this.clonadorDeSubItemDeInvestimentoPorLicitacao = new ClonadorDeSubItemDeInvestimentoPorLicitacao(dao);

		this.clonadorDeMetaPorLicitacao = new ClonadorDeMetaPorLicitacao(dao);

		this.clonadorDeSubmetaPorLicitacao = new ClonadorDeSubmetaPorLicitacao(dao);

		this.clonadorDePO = new ClonadorDePO(dao);

		this.clonadorDeEvento = new ClonadorDeEvento(dao);

		this.clonadorDeFrenteDeObra = new ClonadorDeFrenteDeObra(dao);

		this.clonadorDeEventoFrenteDeObra = new ClonadorDeEventoFrenteDeObra(dao);

		this.clonadorDeMacroServico = new ClonadorDeMacroServico(dao);

		this.clonadorDeMacroServicoParcela = new ClonadorDeMacroServicoParcela(dao);

		this.clonadorDeServico = new ClonadorDeServico(dao);

		this.clonadorDeServicoFrenteDeObra = new ClonadorDeServicoFrenteDeObra(dao);

		this.clonadorDeServicoFrenteDeObraAnalise = new ClonadorDeServicoFrenteDeObraAnalise(dao);

		/////////////////////////////////////////////////
		//
		/////////////////////////////////////////////////

		//Consulta Lotes
		List<LoteBD> lotesApagar = this.clonadorDeLotePorLicitacao.consultarLotesApagar(propostaClonada.getObjetoClonado(), licitacao);

		//Consulta Submetas
		List<SubmetaBD> submetasApagar = this.clonadorDeSubmetaPorLicitacao.consultarClonesGerados(lotesApagar);

		//Consulta POs
		List<PoBD> posApagar = this.clonadorDePO.consultarClonesGerados(submetasApagar);

		//Consulta MacroServicos
		List<MacroServicoBD> macroServicosApagar = this.clonadorDeMacroServico.consultarClonesGerados(posApagar);

		//Consulta Servicos
		List<ServicoBD> servicosApagar = clonadorDeServico.consultarClonesGerados(macroServicosApagar);

		//Consulta Evento
		List<EventoBD> eventosApagar = this.clonadorDeEvento.consultarClonesGerados(posApagar);

		//Consulta Frente de Obra
		List<FrenteObraBD> frenteObraApagar = this.clonadorDeFrenteDeObra.consultarClonesGerados(posApagar);

		//Consulta Meta
		List<MetaBD> metasApagar = this.clonadorDeMetaPorLicitacao.consultarClonesGerados(submetasApagar, propostaClonada.getObjetoClonado());

		//Consulta SubitemInvestimentos
		List<SubitemInvestimentoBD> subitemInvestimentoBDs = this.clonadorDeSubItemDeInvestimentoPorLicitacao.consultarClonesGerados(metasApagar, propostaClonada.getObjetoClonado());


		//Apagar Servico Frente de Obra Analise
		this.clonadorDeServicoFrenteDeObraAnalise.apagarClone(servicosApagar);

		//Apagar Servico Frente de Obra
		this.clonadorDeServicoFrenteDeObra.apagarClone(servicosApagar, frenteObraApagar);

		//Apagar Servico
		this.clonadorDeServico.apagarClone(servicosApagar);

		//Apagar MacroServicoParcela
		this.clonadorDeMacroServicoParcela.apagarClone(macroServicosApagar);

		//Apagar MacroServico
		this.clonadorDeMacroServico.apagarClone(macroServicosApagar);

		//Apagar EventoFrenteObra
		this.clonadorDeEventoFrenteDeObra.apagarClone(eventosApagar, frenteObraApagar);

		//Apagar FrenteObra
		this.clonadorDeFrenteDeObra.apagarClone(frenteObraApagar);

		//Apagar Evento
		this.clonadorDeEvento.apagarClone(eventosApagar);

		//Apagar PO
		this.clonadorDePO.apagarClone(posApagar);

		//Apagar Submeta
		this.clonadorDeSubmetaPorLicitacao.apagarClone(submetasApagar);

		//Apagar Meta
		this.clonadorDeMetaPorLicitacao.apagarClone(metasApagar);

		//Apagar Subitem Investimento
		this.clonadorDeSubItemDeInvestimentoPorLicitacao.apagarClone(subitemInvestimentoBDs);

		//Apagar Licitacao Lote
		this.clonadorDeLotePorLicitacao.apagarClone(lotesApagar);

	}


	public LiberarLoteBC liberaLotesDa(LicitacaoBD licitacao) {
		Objects.requireNonNull(licitacao);
		Objects.requireNonNull(licitacao.getId());

		this.licitacao = licitacao;

		return this;
	}

	public LiberarLoteBC comAProposta(Clone<PropostaBD> propostaClonada) {
		this.propostaClonada = propostaClonada;

		return this;
	}

	public LiberarLoteBC usandoATransacao(Handle transaction) {
		Objects.requireNonNull(transaction);
		this.transaction = transaction;

		return this;
	}

}
