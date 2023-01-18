package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.enviarparaanalise;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LoteLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.FornecedorBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LoteBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.dao.MacroServicoParcelaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.database.MacroServicoParcelaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.dao.EventoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.database.EventoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.dao.MacroServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.database.MacroServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.TipoFonteEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraAnaliseDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.ValidarSituacaoConcluidaDaLicitacao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CffComEventoFaltandoAssociacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.DataBaseAnaliseInvalidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.DataBaseLicitacaoInvalidaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.EventoSemServicoAssociadoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExisteLoteComPosDeOrcamentosDiferentesException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExistePoSemEventosNaLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExistePoSemFrenteDeObraNaLicitacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ExisteSubmetaComContrapartidaNegativaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.FrenteDeObraComQuantidadeZeroException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.FrenteDeObraSemQuantidadeException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ItensDePoDeUmLoteComPrecosDiferenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.MacroServicoSemPercentualParcelaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.PlanilhaOrcamentariaNaoCadastrada;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.PosDeUmLoteComDataBaseDiferenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemEventoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemFrenteObraException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.ServicoSemQuantidadesInformadasException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.SomatorioLicitacaoExcedeValorAprovadoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.CodigoEFonteDeServico;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class EnviarParaAnaliseEAceiteRules {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ValidarSituacaoConcluidaDaLicitacao validarLicitacaoRules;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private PoBC poBC;

	@Inject
	private SiconvBC siconvBC;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private BusinessExceptionContext businessExceptionContext;

	// Conjunto de Validacoes do Envio para Análise (um subconjunto das validacoes para aceite)
	public void realizarValidacoesParaEnviarParaAnalise(LicitacaoBD licitacaoEstadoAtual) {
		// Verifica as pré-condições
		validarLicitacaoRules.validarSeProcessoLicitatorioEstaConcluido(licitacaoEstadoAtual);

		List<PoVRPLDTO> posDaLicitacao = this.recuperaPOsDaLicitacao(licitacaoEstadoAtual);

		// Validações da PO - Regras 2 a 11 e 16 a 18
		this.validarPOsDaLicitacao(posDaLicitacao);

		// Validações dos Lotes - Regras 14, 15 e 19
		this.validarLotes(licitacaoEstadoAtual);
		
		// Validação dos fornecedores - Regra 21
		this.validarFornecedoresObsoletos(licitacaoEstadoAtual);
	}

	// Conjunto de Validacoes do Aceitar Documentacao
	public void realizarValidacoesParaAceite(LicitacaoBD licitacaoEstadoAtual) {
		// Verifica as pré-condições
		validarLicitacaoRules.validarSeProcessoLicitatorioEstaConcluido(licitacaoEstadoAtual);

		List<PoVRPLDTO> posDaLicitacao = this.recuperaPOsDaLicitacao(licitacaoEstadoAtual);

		// Validações da PO - Regras 2 a 11 e 16 a 18
		this.validarPOsDaLicitacao(posDaLicitacao);

//		// FIXME pendente de arredondamento
//		// Regras 12 e 13
		this.validarSeSomatorioLicitacaoExcedeValorAprovado(licitacaoEstadoAtual, posDaLicitacao);

		// Validações dos Lotes - Regras 14, 15 e 19
		this.validarLotes(licitacaoEstadoAtual);
		
		// Validação dos fornecedores - Regra 21
		this.validarFornecedoresObsoletos(licitacaoEstadoAtual);
	}

	public List<PoVRPLDTO> recuperaPOsDaLicitacao(LicitacaoBD licitacao) {
		List<PoVRPLDTO> listaPoLicitacao = this.poBC.recuperarNovoPosPorLicitacao(licitacao);

		return listaPoLicitacao;
	}

	private void validarPOsDaLicitacao(List<PoVRPLDTO> planilhasOrcamentarias) {

		if (planilhasOrcamentarias == null || planilhasOrcamentarias.isEmpty()) {
			businessExceptionContext.add(new PlanilhaOrcamentariaNaoCadastrada());
			businessExceptionContext.throwException();
		}

		for (PoVRPLDTO po : planilhasOrcamentarias) {

			// Validacao 02 e 06, 11
			this.validarEventosDaPO(po, planilhasOrcamentarias);

			// Validacoes 03 e 20
			this.validarFrentesDeObraDaPo(po);

			// validacoes 04, 06, 07, 08 e 09
			this.validarServicosDaPo(po);

			// validacoes 11
			this.validarMacroServicosDaPo(po);

			// validacao 16
			this.validarDataBaseDaPOComOrcamentoReferenciaAnalise(po);

			// validacao 17
			this.validarDataBaseDaPOComOrcamentoReferenciaDataBase(po);

			// validacao 18
			this.validarContrapartidaDaSubmetaDaPo(po);
		}
	}


	// Regra 02 e Regra 05
	public void validarEventosDaPO(PoVRPLDTO po, List<PoVRPLDTO> planilhasOrcamentarias) {
		if (po.getIndicadorAcompanhamentoPorEvento()) {
			// Regra 2 - Existe pelo menos uma PO na licitação, sem evento cadastrado
			if (!encontrouEventoParaPO(po, planilhasOrcamentarias)) {
				businessExceptionContext.add(new ExistePoSemEventosNaLicitacaoException(po.getNomeIdentificador()));
			}

			// TODO as duas rotinas abaixo dizem respeito à Validação 10. necessita refatorar
			this.validarEventosEstaoAssociadosAumServico(po);
			this.validarEventosFrenteObraPO(po);

		}
	}

	// Regra 03 e 20
	public void validarFrentesDeObraDaPo(PoVRPLDTO po) {
		List<FrenteObraBD> frentesDeObraPo = getFrenteDeObraDAO().recuperarListaFrentesDeObraIdPo(po.getId());

		// Regra 03 - Existe pelo menos uma PO na licitação, sem frente de obra cadastrada
		if (frentesDeObraPo == null || frentesDeObraPo.isEmpty()) {
			businessExceptionContext.add (new ExistePoSemFrenteDeObraNaLicitacaoException(po.getNomeIdentificador()));
		}

		// Validação 20 - Existe Frente de Obra sem quantidade de serviço informada
		List<FrenteObraBD> frentesObraSemServico = new ArrayList<>();
		for (FrenteObraBD frente : frentesDeObraPo) {
			List<ServicoFrenteObraBD> listaSFO = dao.get(ServicoFrenteObraDAO.class)
					.recuperarServicoFrenteObraPorIdFrenteObra(frente.getId());

			if (listaSFO == null || listaSFO.isEmpty()) {
				frentesObraSemServico.add(frente);
			}
		}
		// TODO melhorar a formatação dessa mensagem
		if (!frentesObraSemServico.isEmpty()) {
			businessExceptionContext.add(new FrenteDeObraSemQuantidadeException(
					po.getNomeIdentificador(), this.formatarStringFrentesObra(frentesObraSemServico)));
		}

	}

	// Regra 04 - Existe serviço sem Evento associado
	private void validarServicoSemEvento(ServicoBD servico, PoVRPLDTO po) {
		if (servico.getEventoFk() == null) {
			businessExceptionContext.add (new ServicoSemEventoException(servico, po));
		}
	}

	// Regra 05 - Existe evento sem serviço associado
	private void verificaSeExisteEventoSemServicoAssociado(List<EventoBD> eventosPo,
			List<PoVRPLDTO> planilhasOrcamentarias) {
		Boolean existeAlgumEventoSemServioAssociado = false;

		for (EventoBD evento : eventosPo) {
			List<ServicoBD> servicos = getServicoDAO().recuperarServicosPorEvento(evento.getId());

			existeAlgumEventoSemServioAssociado = servicos == null || servicos.isEmpty();

			if (existeAlgumEventoSemServioAssociado) {
				break;
			}
		}

		if (existeAlgumEventoSemServioAssociado) {
			throw new EventoSemServicoAssociadoException(
					this.recuperarRelatorioSubmetasSemServicoAssociado(planilhasOrcamentarias));
		}
	}

	private String recuperarRelatorioSubmetasSemServicoAssociado(List<PoVRPLDTO> planilhasOrcamentarias) {
		String retorno = "";

		for (PoVRPLDTO po : planilhasOrcamentarias) {
			List<EventoBD> eventosPo = getEventoDAO().recuperarListaEventosVRPLIdPo(po.getId());

			String infosPO = "";

			for (EventoBD evento : eventosPo) {
				List<ServicoBD> servicos = getServicoDAO().recuperarServicosPorEvento(evento.getId());

				if (servicos == null || servicos.isEmpty()) {

					if (infosPO.equals("")) {
						infosPO = po.getNomeIdentificador() + "<br>";
					}

					infosPO = infosPO + "<ul><li>" + evento.getNumeroEvento() + ". " + evento.getNomeEvento()
							+ "</li></ul>";
				}
			}

			if (!infosPO.equals("")) {
				retorno = retorno + "<br>" + infosPO;
			}
		}

		return retorno;
	}

	// Regra 06 - Existe serviço sem Frente de Obra associada
	private void validarServicoSemFrenteDeObra(List<ServicoFrenteObraBD> listaServicoFrenteObra, ServicoBD servico) {
		if (listaServicoFrenteObra == null || listaServicoFrenteObra.isEmpty()) {
			// validacao 06
			businessExceptionContext.add (new ServicoSemFrenteObraException(servico.getTxDescricao()));
			businessExceptionContext.throwException();
		}
	}

	// Regra 07 - O total (somatório dos quantitativos) dos serviços informados nas
	// frentes de obra difere do total (somatório dos quantitativos) aceito na
	// análise.
	private void validarTotalDoServicoFrenteDeObraComparadoComTotalFaseDeAnalise(
			List<ServicoFrenteObraBD> listaServicoFrenteObra, List<ServicoFrenteObraAnaliseBD> listaServicoFrenteObraAnalise, ServicoBD servico, PoVRPLDTO po) {
		BigDecimal totalItensFrenteDeObra = listaServicoFrenteObra.stream().map(ServicoFrenteObraBD::getQtItens)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal totalItensFrenteDeObraFaseDeAnalise = listaServicoFrenteObraAnalise.stream()
				.map(ServicoFrenteObraAnaliseBD::getQtItens).reduce(BigDecimal.ZERO, BigDecimal::add);

		if (totalItensFrenteDeObra.compareTo(totalItensFrenteDeObraFaseDeAnalise) != 0) {
			SubmetaBD submetaBD = getPoDAO().recuperarSubmetaPorPO(po.getId());
				businessExceptionContext.add(new QuantidadeTotalDoServicoDiferenteDoAceitoNaAnalise(servico, submetaBD));
		}

	}

	// Validação 08 - Existe Serviço Frente Obra com quantidade de itens zerada?
	private void validarFrenteDeObra(List<ServicoFrenteObraBD> listaServicoFrenteObra, PoVRPLDTO po) {
		List<String> frenteObraAssociada = new ArrayList<>();

		for (ServicoFrenteObraBD servicoFrenteDeObra : listaServicoFrenteObra) {
			if (servicoFrenteDeObra.getQtItens().compareTo(BigDecimal.ZERO) == 0) {

				FrenteObraBD frenteObraNova = new FrenteObraBD();
				frenteObraNova.setId(servicoFrenteDeObra.getFrenteObraFk());

				FrenteObraBD frenteObraRecuperada = getFrenteDeObraDAO().recuperarFrenteObraPorId(frenteObraNova);

				frenteObraAssociada.add(frenteObraRecuperada.getNmFrenteObra());
			}
		}

		if (!frenteObraAssociada.isEmpty()) {
			businessExceptionContext.add(new FrenteDeObraComQuantidadeZeroException(po.getNomeIdentificador(),frenteObraAssociada));
		}

	}

	// Regra 09 - Existe serviço sem quantidades informadas por frente de obra
	private void validarTotalItensFrenteDeObra(List<ServicoFrenteObraBD> listaServicoFrenteObra, ServicoBD servico) {
		BigDecimal totalItensFrenteDeObra = listaServicoFrenteObra.stream().map(ServicoFrenteObraBD::getQtItens)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		if (totalItensFrenteDeObra.compareTo(BigDecimal.ZERO) == 0) {
			businessExceptionContext.add(new ServicoSemQuantidadesInformadasException(servico));
		}
	}

	// Regra 10 - Na tabela 'Visão Frentes de Obra por Evento' devem estar
	// associados todos os eventos cadastrados.
	// TODO averiguar a necessidade dessa validação; a validacao de mes de conclusao no CFF é feita na rotina validarEventosFrenteObraPO
	private void validarEventosEstaoAssociadosAumServico(PoVRPLDTO po) {

		// recuperar todos os eventos
		List<EventoBD> eventosDaPO = getEventoDAO().recuperarEventosPorPo(po.getId());
		// recuperar se existe pelo menos um servico que utiliza este evento
		if (eventosDaPO != null) {
			for (EventoBD evento : eventosDaPO) {
				List<ServicoBD> servicos = getServicoDAO().recuperarServicosPorEvento(evento.getId());
				if (servicos == null || servicos.isEmpty()) {
					businessExceptionContext.add(new CffComEventoFaltandoAssociacaoException(po.getNomeIdentificador(),
														evento.getNumeroEvento() + " " + evento.getNomeEvento()));
				}
			}
		} else {
			businessExceptionContext.add(new CffComEventoFaltandoAssociacaoException(po.getNomeIdentificador(),""));
			businessExceptionContext.throwException();
		}

	}

	// Regra 10 - Na tabela 'Visão Frentes de Obra por Evento' devem estar
	// associados todos os eventos cadastrados. Aqui valida se tem evento sem mes de conclusao
	private void validarEventosFrenteObraPO(PoVRPLDTO po) {

		List<FrenteObraBD> frentes = getFrenteDeObraDAO().recuperarListaFrentesDeObraIdPo(po.getId());
		for (FrenteObraBD frente : frentes) {
			List<EventoFrenteObraBD> eventosFrentesObra = getEventoFrenteObraDAO()
					.recuperarEventoFrenteObraPorIdFrenteObra(frente.getId());
			for (EventoFrenteObraBD eventoFrenteObra : eventosFrentesObra) {
				if (eventoFrenteObra.getNrMesConclusao() == null || eventoFrenteObra.getNrMesConclusao() < 1) {
					EventoBD evento = getEventoDAO().recuperarEventoPorId(eventoFrenteObra.getEventoFk());
					businessExceptionContext.add (new CffComEventoFaltandoAssociacaoException(po.getNomeIdentificador(),
														evento.getNumeroEvento() + " " + evento.getNomeEvento()));
				}
			}
		}
	}

	// Regra 11 - No CFF (sem evento), na tabela 'Visão Parcelas por Macrosserviço'
	// deve existir uma percentual de parcela cadastrado para cada macrosserviço
	// cadastrado para o respectivo PO.
	private void validarPercentualParcelaMacroServico(PoVRPLDTO po, MacroServicoBD macroServico) {
		List<MacroServicoParcelaBD> msParcelas = getMacroServicoParcelaDAO()
				.recuperarParcelasDoMacroServicoPorIdsMacroServico(Arrays.asList(macroServico.getId()));

		if (msParcelas == null || msParcelas.isEmpty()) {
			businessExceptionContext.add (new MacroServicoSemPercentualParcelaException(po.getNomeIdentificador(), macroServico.getTxIdentificador()));
		}
	}

	// Regra 12 e Regra 13
	private void validarSeSomatorioLicitacaoExcedeValorAprovado(LicitacaoBD licitacaoEstadoAtual,
			List<PoVRPLDTO> posDaLicitacao) {

		PropostaBD proposta = this.dao.get(PropostaDAO.class).recuperaUltimaVersaoDaProposta(usuarioLogado);

		List<Long> listaIdsLicitacoes = new ArrayList<>();
		listaIdsLicitacoes.add(licitacaoEstadoAtual.getId());

		BigDecimal totalDeRepasseDaLicitacao = BigDecimal.ZERO;
		for(PoVRPLDTO po : posDaLicitacao) {
			SubmetaBD submeta = this.dao.get(QciDAO.class).recuperarSubmetaVRPL(po.getIdSubmeta());
			if(submeta.getVlRepasse().compareTo(po.getPrecoTotalLicitacao()) > 0) {
				submeta.setVlRepasse( po.getPrecoTotalLicitacao() );
			}

			totalDeRepasseDaLicitacao = totalDeRepasseDaLicitacao.add( submeta.getVlRepasse() );
		}

		BigDecimal totalDeRepasseDaProposta = this.ignorarMilesimosBigDecimal(proposta.getValorRepasse());

		// Regra 12
		if (totalDeRepasseDaLicitacao.compareTo(totalDeRepasseDaProposta) > 0) {
			businessExceptionContext.add(new SomatorioLicitacaoExcedeValorAprovadoException("de repasse", totalDeRepasseDaLicitacao,
					"de repasse", proposta.getDescricaoModalidade(), totalDeRepasseDaProposta));
		}

		BigDecimal valorTotalQci = this.getSomatorioTotalQci(posDaLicitacao);
		BigDecimal valorGlobalProposta = this.ignorarMilesimosBigDecimal(proposta.getValorGlobal());

		// Regra 13
		if (valorTotalQci.compareTo(valorGlobalProposta) > 0) {
			businessExceptionContext.add(new SomatorioLicitacaoExcedeValorAprovadoException("", valorTotalQci, "global",
					proposta.getDescricaoModalidade(), valorGlobalProposta));
		}
	}

	// Regra 14 - Planilhas orçamentárias cujas submetas estejam associadas a um
	// mesmo lote não podem ter valores diferentes para a data base.
	private void validarDataBaseDoLote(LoteBD lote, List<PoBD> posDaLicitacaoPorLote) {

		LocalDate database = posDaLicitacaoPorLote.get(0).getDtBaseVrpl();

		String databaseStr = "";
		if (database != null) {
			databaseStr = database.getMonthValue() + "" + database.getYear();
		}

		for (int i = 1; i < posDaLicitacaoPorLote.size(); i++) {

			PoBD po = posDaLicitacaoPorLote.get(i);

			String dataPO = "";
			if (po.getDtBaseVrpl() != null) {
				dataPO = po.getDtBaseVrpl().getMonthValue() + "" + po.getDtBaseVrpl().getYear();
			}

			// validacao 15
			if ("".equals(databaseStr) || "".equals(dataPO) || !dataPO.equals(databaseStr)) {
				List<String> submetasString = this.recuperarStringSubmetasComDatabase(posDaLicitacaoPorLote);
				businessExceptionContext.add (new PosDeUmLoteComDataBaseDiferenteException(lote.getNumeroDoLote().toString(), submetasString));
			}
		}
	}

	// Regra 15 - Para itens de PO de um mesmo lote (mesmo que de PO diferentes no
	// lote), com mesmos valores de Fonte e Código, os preços unitários não podem
	// ser diferentes.
	private void validarPrecoUnitarioDoLote(LoteBD lote, List<PoBD> posDaLicitacaoPorLote) {

		List<ServicoBD> servicosDasPos = this.recuperarServicosDasPos(posDaLicitacaoPorLote);
		List<CodigoEFonteDeServico> listaCodigoEFonte = this.recuperarCodigoEFonteDeServico(servicosDasPos);

		for (CodigoEFonteDeServico codigoEFonte : listaCodigoEFonte) {

			List<ServicoBD> servicosFiltrados = servicosDasPos.stream()
					.filter(servico -> servico.getCdServico().equals(codigoEFonte.getCdServico())
							&& servico.getInFonte().equals(codigoEFonte.getInFonte()))
					.collect(Collectors.toList());

			if (servicosFiltrados.size() > 1) {

				BigDecimal preco = servicosFiltrados.get(0).getVlPrecoUnitarioLicitado();

				for (int i = 1; i < servicosFiltrados.size(); i++) {

					ServicoBD servico = servicosFiltrados.get(i);

					// regra valida somente se o tipo da fonte for SINAPI (Relato 1985054)
					if (TipoFonteEnum.SINAPI.getSigla().equals(servicosFiltrados.get(0).getInFonte())
							&& TipoFonteEnum.SINAPI.getSigla().equals(servico.getInFonte())) {
					// Validação 16
						if (preco == null || servico.getVlPrecoUnitarioLicitado() == null
								|| !preco.equals(servico.getVlPrecoUnitarioLicitado())) {

							businessExceptionContext.add (new ItensDePoDeUmLoteComPrecosDiferenteException(lote.getNumeroDoLote().toString(),
															codigoEFonte.getInFonte(), codigoEFonte.getCdServico(),
															this.recuperarStringItensPoComPreco(servicosFiltrados)));
						}
					}
				}
			}
		}
	}

	// Regra 16
	protected void validarDataBaseDaPOComOrcamentoReferenciaAnalise(PoVRPLDTO po) {

		if (po.isOrcamentoReferenciaAceitoNaAnalise() && !po.isDataBaseDaLicitacaoIgualADataBaseDaAnalise()) {

			businessExceptionContext.add (new DataBaseAnaliseInvalidaException());
		}
	}

	// Regra 17
	protected void validarDataBaseDaPOComOrcamentoReferenciaDataBase(PoVRPLDTO po) {

		if (po.isOrcamentoReferenciaDataBaseLicitacao() && po.isDataBaseDaLicitacaoIgualADataBaseDaAnalise()) {

			businessExceptionContext.add (new DataBaseLicitacaoInvalidaException());
		}
	}

	// Validacao 18
	private void validarContrapartidaDaSubmetaDaPo(PoVRPLDTO po) {

		SubmetaBD submetaDaPo = getPoDAO().recuperarSubmetaPorPO(po.getId());

		submetaDaPo.setVlTotalLicitado( po.getPrecoTotalLicitacao() );

		if(submetaDaPo.getVlRepasse().compareTo( submetaDaPo.getVlTotalLicitado() ) > 0) {
			submetaDaPo.setVlRepasse(submetaDaPo.getVlTotalLicitado());
		}

		submetaDaPo.setVlContrapartida(
				submetaDaPo.getVlTotalLicitado().subtract( submetaDaPo.getVlRepasse() )
		);

		if (submetaDaPo.getVlContrapartida().compareTo(BigDecimal.ZERO) < 0) {
			businessExceptionContext.add (new ExisteSubmetaComContrapartidaNegativaException());
		}
	}

	// Regra 19
	private String validarOrcamentoDeReferenciaPorLote(LoteBD lote, List<PoBD> posDaLicitacaoPorLote) {

		String orcamentoReferenciaDoLote = posDaLicitacaoPorLote.get(0).getReferencia();

		for (PoBD po : posDaLicitacaoPorLote) {
			if (!orcamentoReferenciaDoLote.equals(po.getReferencia())) {
				return this.criarRelatorioPOsComOrcamentoDiferente(lote, posDaLicitacaoPorLote);
			}
		}

		return "";
	}

	private String criarRelatorioPOsComOrcamentoDiferente(LoteBD lote, List<PoBD> posDaLicitacaoPorLote) {

		String relatorio = "</br></br><ul></ul> " + "<i>Lote de Licitação nº " + lote.getNumeroDoLote() + ":</i>";

		for (PoBD po : posDaLicitacaoPorLote) {
			PoVRPLDTO poDTO = PoVRPLDTO.from(po);
			relatorio = relatorio + "</br>" + "<i>Submeta: " + poDTO.getNumeroMeta() + "." + poDTO.getNumeroSubmeta()
					+ " " + poDTO.getDescricaoSubmeta() + " - Orçamento de Referência: "
					+ poDTO.getTextoOrcamentoReferencia() + "</i>";
		}

		return relatorio;
	}


	private void validarLotes(LicitacaoBD licitacaoEstadoAtual) {

		List<LoteBD> lotesDaLicitacao = getLoteLicitacaoDAO().findLotesByIdLicitacao(licitacaoEstadoAtual.getId());

		String lotesComPoComOrcamentoDiferente = "";

		for (LoteBD lote : lotesDaLicitacao) {
			List<PoBD> posDaLicitacaoPorLote = getPoDAO().recuperarPosPorLicitacaoELote(licitacaoEstadoAtual.getId(),
					lote.getId());

			// Validação 14
			this.validarDataBaseDoLote(lote, posDaLicitacaoPorLote);

			// Validação 15
			this.validarPrecoUnitarioDoLote(lote, posDaLicitacaoPorLote);

			// Criando Relatorio da Validacao 19
			lotesComPoComOrcamentoDiferente = lotesComPoComOrcamentoDiferente
					+ this.validarOrcamentoDeReferenciaPorLote(lote, posDaLicitacaoPorLote);
		}

		// Validacao 19
		if (!lotesComPoComOrcamentoDiferente.equals("")) {
			businessExceptionContext.add (new ExisteLoteComPosDeOrcamentosDiferentesException(lotesComPoComOrcamentoDiferente));
		}
	}

	private boolean encontrouEventoParaPO(PoVRPLDTO po, List<PoVRPLDTO> planilhasOrcamentarias) {
		List<EventoBD> eventosPo = getEventoDAO().recuperarListaEventosVRPLIdPo(po.getId());

		Boolean encontrou = eventosPo != null && !eventosPo.isEmpty();

		if (encontrou) {
			verificaSeExisteEventoSemServicoAssociado(eventosPo, planilhasOrcamentarias);
		}

		return encontrou;
	}

	private void validarServicosDaPo(PoVRPLDTO po) {
		List<ServicoBD> servicosDaPo = getServicoDAO().recuperarServicosPorPo(po);

		// Para agilizar a verificacao vamos consultar todos os registros associados primeiro
		// e depois separar conforme a necessidade
		List<Long> idsServicos = servicosDaPo.stream().map(ServicoBD::getId).collect(Collectors.toList());

		List<ServicoFrenteObraBD> listaServicoFrenteObraTodosServicos = idsServicos.isEmpty() ? new ArrayList<>()
				: getServicoDAO().recuperaListaServicoFrenteObraPorListaIdsServicos(idsServicos);

		List<ServicoFrenteObraAnaliseBD> listaServicoFrenteObraAnaliseTodosServicos = idsServicos.isEmpty() ? new ArrayList<>()
				: getServicoFrenteObraAnaliseDAO().recuperarTodosServicoFrenteObraAnalisePorListaIdsServicos(idsServicos);

		for (ServicoBD servico : servicosDaPo) {

			if (po.getIndicadorAcompanhamentoPorEvento()) {
				// Validação 04
				this.validarServicoSemEvento(servico, po);

				// validar se eventos
			}

			List<ServicoFrenteObraBD> listaServicoFrenteObra = listaServicoFrenteObraTodosServicos.stream()
					.filter(servicoFrenteObra -> servicoFrenteObra.getServicoFk().equals(servico.getId()))
					.collect(Collectors.toList());

			List<ServicoFrenteObraAnaliseBD> listaServicoFrenteObraAnalise = listaServicoFrenteObraAnaliseTodosServicos.stream()
					.filter(servicoFrenteObraAnalise -> servicoFrenteObraAnalise.getServicoFk().equals(servico.getId()))
					.collect(Collectors.toList());

			// Validação 06
			this.validarServicoSemFrenteDeObra(listaServicoFrenteObra, servico);

			// Validação 07
			this.validarTotalDoServicoFrenteDeObraComparadoComTotalFaseDeAnalise(listaServicoFrenteObra, listaServicoFrenteObraAnalise, servico, po);

			// Validação 09
			this.validarTotalItensFrenteDeObra(listaServicoFrenteObra, servico);

			// Validação 08
			this.validarFrenteDeObra(listaServicoFrenteObra, po);
		}
	}

	private void validarMacroServicosDaPo(PoVRPLDTO po) {
		List<MacroServicoBD> macroServicosPo = getMacroServicoDAO().recuperarMacroServicosPorIdPo(po.getId());

		for (MacroServicoBD macroServico : macroServicosPo) {

			if (!po.getIndicadorAcompanhamentoPorEvento()) {
				// Validação 11
				this.validarPercentualParcelaMacroServico(po, macroServico);
			}
		}
	}

	public BigDecimal getSomatorioTotalQci(List<PoVRPLDTO> listaPoLicitacao) {
		BigDecimal somatorioTotalQci = BigDecimal.ZERO;

		for (PoVRPLDTO po : listaPoLicitacao) {
			BigDecimal valorTotalPorPO =  po.getPrecoTotalLicitacao();
			if(valorTotalPorPO == null) {
				valorTotalPorPO = BigDecimal.ZERO;
			}

			somatorioTotalQci = somatorioTotalQci.add(valorTotalPorPO);
		}

		return this.ignorarMilesimosBigDecimal(somatorioTotalQci);
	}

	public List<ServicoBD> recuperarServicosDasPos(List<PoBD> pos) {

		List<ServicoBD> servicosDasPos = new ArrayList<>();

		for (PoBD po : pos) {
			PoVRPLDTO dto = PoVRPLDTO.from(po);
			servicosDasPos.addAll(getServicoDAO().recuperarServicosPorPo(dto));
		}

		return servicosDasPos;
	}

	public List<CodigoEFonteDeServico> recuperarCodigoEFonteDeServico(List<ServicoBD> servicos) {

		List<CodigoEFonteDeServico> listaCodigoEFonte = new ArrayList<>();

		if (!servicos.isEmpty()) {

			listaCodigoEFonte.add(CodigoEFonteDeServico.from(servicos.get(0)));

			for (int i = 1; i < servicos.size(); i++) {
				CodigoEFonteDeServico codigoEFonte = CodigoEFonteDeServico.from(servicos.get(i));
				if (!listaCodigoEFonte.contains(codigoEFonte)) {
					listaCodigoEFonte.add(codigoEFonte);
				}
			}
		}

		return listaCodigoEFonte;
	}

	private String recuperarStringItensPoComPreco(List<ServicoBD> servicos) {
		StringBuilder sb = new StringBuilder();

		NumberFormat formatter = NumberFormat.getCurrencyInstance();

		for (ServicoBD servico : servicos) {
			String linha = String.format("<li>Item: %s - Valor Unitário: %s </li>", servico.getTxDescricao(),
					formatter.format(servico.getVlPrecoUnitarioLicitado()));

			sb.append(linha);
		}

		return sb.toString();
	}

	private String formatarStringFrentesObra(List<FrenteObraBD> frentesObra) {
		Collections.sort(frentesObra);
		StringBuilder sb = new StringBuilder();

		for (FrenteObraBD frenteObra : frentesObra) {
			String linha = String.format("<li> %s - %s </li>", frenteObra.getNrFrenteObra(),
					frenteObra.getNmFrenteObra());

			sb.append(linha);
		}

		return sb.toString();
	}

	private List<String> recuperarStringSubmetasComDatabase(List<PoBD> posDaLicitacaoPorLote) {
		List<String> submetasString = new ArrayList<>();

		for (PoBD po : posDaLicitacaoPorLote) {
			PoVRPLDTO dto = PoVRPLDTO.from(po);
			submetasString.add(dto.getNomeIdentificadorComDatabase());
		}

		return submetasString;
	}

	private BigDecimal ignorarMilesimosBigDecimal(BigDecimal valor) {
		String[] partes = valor.toPlainString().split("\\.");
		String fracao = "";
		if (partes.length > 1) {
			fracao = "." + partes[1].substring(0, (partes[1].length() > 1 ? 2 : 1));
		}
		return new BigDecimal(partes[0] + fracao);
	}
	
	private void validarFornecedoresObsoletos(LicitacaoBD licitacaoEstadoAtual) {
		List<Long> ids = new ArrayList<>();
		ids.add(licitacaoEstadoAtual.getId());
		
		List<FornecedorBD> obsoletos = new ArrayList<>();
		List<FornecedorBD> fornecedores = getLicitacaoDAO().recuperaFornecedoresDasLicitacoes(ids);
		for (FornecedorBD fornecedorBD : fornecedores) {
			Boolean fornecedorAssociado = getLoteLicitacaoDAO().existeLoteAssociadoAoFornecedor(fornecedorBD.getId());
			if(fornecedorAssociado && fornecedorBD.getObsoleto()) {
				obsoletos.add(fornecedorBD);
			}
		}
		
		if(!obsoletos.isEmpty()) {
			String erro = this.formatarStringFornecedores(licitacaoEstadoAtual, obsoletos);
			businessExceptionContext.add (new BusinessException(erro));
		}
		
	}

	private String formatarStringFornecedores(LicitacaoBD licitacaoEstadoAtual, List<FornecedorBD> obsoletos) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Licitação: ");
		sb.append(licitacaoEstadoAtual.getNumeroAno());
		sb.append(" [");
		
		for (FornecedorBD fornecedorBD : obsoletos) {
			sb.append("Fornecedor: ");
			sb.append(fornecedorBD.getIdentificacao());
			sb.append(" - ");
			sb.append(fornecedorBD.getRazaoSocial());
			sb.append(", ");
		}
		
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		
		String msgErro = MessageFormat.format(ErrorInfo.FORNECEDORES_OBSOLETOS.getMensagem(), sb.toString());
		msgErro = msgErro.concat(ErrorInfo.FORNECEDORES_OBSOLETOS_ENVIO_ANALISE_ACEITE.getMensagem());
		
		return msgErro;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Gets/Sets
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	protected LicitacaoDAO getLicitacaoDAO() {
		return dao.get(LicitacaoDAO.class);
	}

	protected LoteLicitacaoDAO getLoteLicitacaoDAO() {
		return dao.get(LoteLicitacaoDAO.class);
	}

	protected EventoDAO getEventoDAO() {
		return dao.get(EventoDAO.class);
	}

	protected ServicoDAO getServicoDAO() {
		return dao.get(ServicoDAO.class);
	}

	protected ServicoFrenteObraAnaliseDAO getServicoFrenteObraAnaliseDAO() {
		return dao.get(ServicoFrenteObraAnaliseDAO.class);
	}

	protected FrenteObraDAO getFrenteDeObraDAO() {
		return dao.get(FrenteObraDAO.class);
	}

	protected MacroServicoDAO getMacroServicoDAO() {
		return dao.get(MacroServicoDAO.class);
	}

	protected ServicoFrenteObraDAO getServicoFrenteObraDAO() {
		return dao.get(ServicoFrenteObraDAO.class);
	}

	protected MacroServicoParcelaDAO getMacroServicoParcelaDAO() {
		return dao.get(MacroServicoParcelaDAO.class);
	}

	protected PoDAO getPoDAO() {
		return dao.get(PoDAO.class);
	}

	protected EventoFrenteObraDAO getEventoFrenteObraDAO() {
		return dao.get(EventoFrenteObraDAO.class);
	}

}
