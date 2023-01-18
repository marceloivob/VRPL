package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static java.util.stream.Collectors.toSet;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jdbi.v3.core.Handle;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao.SiconvDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SinapiIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.dao.EventoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.database.EventoFrenteObraDetalheBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.dao.FrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.ConsultaSinapiDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.dao.ServicoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.database.ServicoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraAnaliseDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.dao.ServicoFrenteObraDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraAnaliseBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.database.ServicoFrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraAnaliseDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servicofrenteobra.entity.dto.ServicoFrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServicoBC {

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private ConstraintBeanValidation<ServicoFrenteObraBD> beanValidator;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private DAOFactory dao;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Inject
	private UserCanEditVerifier checkPermission;

	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterar(ServicoDTO servicoDTO) {

		checkPermission.check(TabelasDoVRPLEnum.SERVICO, servicoDTO.getId());

		// converterDTO
		ServicoBD servicoBD = servicoDTO.converterParaBD();

		recuperarServicoPorId(servicoBD);

		dao.getJdbi().useTransaction(transaction -> {

			ServicoBD servicoAnteriorBD = transaction.attach(ServicoDAO.class).recuperarServicoPorId(servicoDTO.getId());

			boolean mudouEvento = false;
			if( servicoAnteriorBD.getEventoFk() == null ||
					!servicoAnteriorBD.getEventoFk().equals(servicoBD.getEventoFk()) ) {

				mudouEvento = true;
			}

			List<ServicoFrenteObraBD> frentesObraCadastradas = transaction.attach(ServicoDAO.class)
					.recuperaListaServicoFrenteObraPorIdServico(servicoDTO.getId());

			transaction.attach(ServicoDAO.class).alterarServico(servicoBD);

			List<ServicoFrenteObraBD> frentesObraAlterarBD = new ArrayList<>();
			List<ServicoFrenteObraBD> frentesObraRemoverBD = new ArrayList<>();
			List<ServicoFrenteObraBD> frentesObraInserirBD = new ArrayList<>();
			servicoDTO.converteFrenteObrasParaBD().stream().forEach(servicoFrenteObraBD -> {
				Optional<ServicoFrenteObraBD> servicoFrenteOpt = frentesObraCadastradas.stream()
						.filter(servicoFrenteCadastrada -> servicoFrenteCadastrada.equals(servicoFrenteObraBD))
						.findFirst();

				if (servicoFrenteOpt.isPresent()) {
					ServicoFrenteObraBD frenteAlterar = servicoFrenteOpt.get();
					frenteAlterar.setQtItens(servicoFrenteObraBD.getQtItens());

					if( BigDecimal.ZERO.compareTo( frenteAlterar.getQtItens() ) < 0 ) {
						frentesObraAlterarBD.add(frenteAlterar);
					} else {
						// Remover serviço frente de obra com quantidade de itens igual a zero
						frentesObraRemoverBD.add(frenteAlterar);
					}

				} else if( BigDecimal.ZERO.compareTo( servicoFrenteObraBD.getQtItens() ) < 0 ) {
					beanValidator.validate(servicoFrenteObraBD);
					frentesObraInserirBD.add(servicoFrenteObraBD);
				}
			});

			// SE A PO FOR POR EVENTO:
			// inserir relacao do evento com as frente de obra que vão ser inseridas
			if (servicoDTO.getEventoFk() != null) {
				List<EventoFrenteObraDetalheBD> listaEventosFrenteObra = transaction.attach(EventoFrenteObraDAO.class)
						.recuperarTodasAsFrentesObraAssociadasAUmEvento(servicoDTO.getEventoFk());

				inserirEventosFrenteObra(servicoDTO, frentesObraInserirBD, listaEventosFrenteObra, transaction);

				if(mudouEvento) {
					inserirEventosFrenteObra(servicoDTO, frentesObraAlterarBD, listaEventosFrenteObra, transaction);
				}

			//recuperar a po do servico, ver se eh por evento
		    // e lancar excecao ja que o evento nao esta presente
			} else if (transaction.attach(ServicoDAO.class).servicoEhPorEvento(servicoBD.getId())){
				throw new EventoDoServicoNaoEncontradoException(servicoBD);
			}

			transaction.attach(ServicoDAO.class).insertServicoFrenteObra(frentesObraInserirBD);
			transaction.attach(ServicoDAO.class).alterarServicoFrenteObra(frentesObraAlterarBD);
			transaction.attach(ServicoDAO.class).removerServicoFrenteObra(frentesObraRemoverBD);

			// Se não tinha evento anteriormente, não tem Evento Frente de Obra para apagar
			if(servicoAnteriorBD.getEventoFk() != null) {

				List<ServicoFrenteObraBD> eventosFrenteObraRemover = new ArrayList<>();
				for (ServicoFrenteObraBD servicoFrenteObraRemover : frentesObraRemoverBD) {

					int qntsServicosFrenteObraMesmoEvento = transaction.attach(ServicoDAO.class)
							.qntsServicosFrenteObraMesmoEvento(servicoAnteriorBD.getEventoFk(), servicoFrenteObraRemover.getFrenteObraFk());

					if(qntsServicosFrenteObraMesmoEvento == 0) {
						eventosFrenteObraRemover.add(servicoFrenteObraRemover);
					}
				}

				if(mudouEvento) {
					for(ServicoFrenteObraBD servicoFrenteObraAlterado : frentesObraAlterarBD) {

						int qntsServicosFrenteObraMesmoEvento = transaction.attach(ServicoDAO.class)
								.qntsServicosFrenteObraMesmoEvento(servicoAnteriorBD.getEventoFk(), servicoFrenteObraAlterado.getFrenteObraFk());

						if(qntsServicosFrenteObraMesmoEvento == 0) {
							eventosFrenteObraRemover.add(servicoFrenteObraAlterado);
						}
					}
				}

				transaction.attach(ServicoDAO.class).removerEventosFrentesObraDosServicosPorEvento(servicoAnteriorBD.getEventoFk(),
						eventosFrenteObraRemover);
				transaction.attach(ServicoDAO.class).removerEventosFrentesObraSemServicoFrenteObra(servicoDTO.getIdPo());
			}

			PoBD po = transaction.attach(PoDAO.class).recuperarPoPorId(servicoDTO.getIdPo());
			servicoDTO.setIdSubmeta(po.getSubmetaId());

			List<Long> listaComIdDaPO  = new ArrayList<>();
			listaComIdDaPO.add(servicoDTO.getIdPo());

			List<PoReducerDTO> poAtualizada = transaction.attach(PoDAO.class).findByListId(listaComIdDaPO);

			PoMacroServicoServicosDTO poMacroservicoServicosDTO = this.calculaTotaisDosServicos(transaction, poAtualizada.get(0), obterMapaSinapi(poAtualizada));

			List<MacroServicoServicosDTO> macroServicoServicosDTO = poMacroservicoServicosDTO.getMacroservicos();

			BigDecimal somaValoresServicosPO = BigDecimal.ZERO;

			for (MacroServicoServicosDTO macroServicoDTO : macroServicoServicosDTO) {
				for (ServicoDTO servicosDoMacroServicoDTO : macroServicoDTO.getServicos()) {

					somaValoresServicosPO = somaValoresServicosPO
							.add(servicosDoMacroServicoDTO.getVlPrecoTotalDaLicitacao());
				}
			}

			SubmetaBD submeta = transaction.attach(QciDAO.class).recuperarSubmetaVRPL(servicoDTO.getIdSubmeta());

			submeta = this.atualizarValoresSubmetaDeAcordoComSomaServicosPO(submeta, somaValoresServicosPO);

			transaction.attach(QciDAO.class).atualizaValorTotalLicitadoDaSubmetaByID(submeta);

		});

	}
	
	public SubmetaBD atualizarValoresSubmetaDeAcordoComSomaServicosPO(SubmetaBD submeta, BigDecimal somaValoresServicosPO) {
		
		submeta.setVlTotalLicitado(somaValoresServicosPO);
		
		BigDecimal vlOutros = submeta.getVlOutros();

		BigDecimal vlContrapartida = submeta.getVlTotalLicitado().subtract(submeta.getVlRepasse()).subtract(submeta.getVlOutros());
		
		if (vlContrapartida.compareTo(BigDecimal.ZERO) <= 0) {
		
			vlContrapartida = BigDecimal.ZERO;
			vlOutros = submeta.getVlTotalLicitado().subtract(submeta.getVlRepasse());
			
			if (vlOutros.compareTo(BigDecimal.ZERO) <= 0) {
			
				vlOutros = BigDecimal.ZERO;
				submeta.setVlRepasse(submeta.getVlTotalLicitado());
			}
		}
		
		submeta.setVlContrapartida(vlContrapartida);
		submeta.setVlOutros(vlOutros);
		
		return submeta;
	}


	public Map<ConsultaSinapiDTO, SinapiIntegracao> obterMapaSinapi(List<PoReducerDTO> listaPo) {
		Set<ConsultaSinapiDTO> codigosServicoSINAPI = listaPo.stream().
							map(po ->
								po.getMacroServicos().stream().
									flatMap(macroServico -> macroServico.getServicos().stream()).
									map(servicoReducerDTO ->
										new ConsultaSinapiDTO(servicoReducerDTO.getCdServico(),
																po.getSgLocalidade(),
																po.getDtBaseVrpl()))
							).flatMap(stream -> stream).collect(toSet());


		if (codigosServicoSINAPI.isEmpty()) {
			return Collections.emptyMap();
		}


		Map<ConsultaSinapiDTO,SinapiIntegracao> mapaSinapiIntengracao = Collections.emptyMap();

		try {
			mapaSinapiIntengracao = dao.get(SiconvDAO.class).recuperarListaSinapiPorCodigoItemData(codigosServicoSINAPI);
		} catch (IllegalStateException illegalStateException) {
			log.error("Possível erro de Carga SINAPI. ", illegalStateException);
		}

		return mapaSinapiIntengracao;
	}


	private void inserirEventosFrenteObra(ServicoDTO servicoDTO, List<ServicoFrenteObraBD> frentesObra,
			List<EventoFrenteObraDetalheBD> eventosFrenteObra, Handle transaction) {
		for (ServicoFrenteObraBD frente : frentesObra) {
			boolean contem = false;
			for (EventoFrenteObraDetalheBD eventoFrenteDeObra : eventosFrenteObra) {
				if (frente.getFrenteObraFk().equals(eventoFrenteDeObra.getIdFrenteObra())
						&& servicoDTO.getEventoFk().equals(eventoFrenteDeObra.getIdEvento())) {
					contem = true;
					break;
				}

			}

			if (!contem) {
				EventoFrenteObraBD eventoFrenteObraBDAIncluir = new EventoFrenteObraBD();
				eventoFrenteObraBDAIncluir.setEventoFk(servicoDTO.getEventoFk());
				eventoFrenteObraBDAIncluir.setFrenteObraFk(frente.getFrenteObraFk());
				eventoFrenteObraBDAIncluir.setNrMesConclusao(0);
				transaction.attach(EventoFrenteObraDAO.class)
						.inserirEventoFrenteObra(eventoFrenteObraBDAIncluir);
			}
		}
	}

	@Log
	public PoMacroServicoServicosDTO calculaTotaisDosServicos(Handle transaction, PoReducerDTO po, Map<ConsultaSinapiDTO,SinapiIntegracao> mapaSinapiIntengracao) {

		BigDecimal totalGeralLicitado = BigDecimal.ZERO;
		BigDecimal totalGeralAceitoNaAnalise = BigDecimal.ZERO;
		BigDecimal totalGeralNaDataBaseDaLicitacao = BigDecimal.ZERO;

		List<MacroServicoServicosDTO> macroservicos = new ArrayList<>();

		for (MacroServicoReducerDTO macroServicoDTO : po.getMacroServicos()) {

			MacroServicoServicosDTO dto = new MacroServicoServicosDTO();
			dto.setMacroServico(macroServicoDTO.convert());
			dto.setServicos(macroServicoDTO.convertListaServicos());
			dto.setSubmetaId(po.getSubmetaId());

			for (ServicoDTO servicoDTO : dto.getServicos()) {

				servicoDTO.setIdSubmeta(po.getSubmetaId());

				SinapiIntegracao sinapi = mapaSinapiIntengracao.get(new ConsultaSinapiDTO(servicoDTO.getCdServico(),
																							po.getSgLocalidade(),
															 								po.getDtBaseVrpl()) );

				servicoDTO.defineSinapi(sinapi);

				servicoDTO.definePo(po.convert());
				
				BigDecimal custo = BigDecimal.ZERO;
				if (po != null && sinapi != null) {
					custo = po.getInDesonerado() ? sinapi.getVlDesonerado() : sinapi.getVlNaoDesonerado();
				}
				
				if (servicoDTO.getVlCustoUnitarioDataBase().compareTo(BigDecimal.ZERO) == 0) {
					if (custo.compareTo(BigDecimal.ZERO) == 0) {
						servicoDTO.setCustoUnitarioDataBase(servicoDTO.getVlCustoUnitario());
					} else {
						servicoDTO.setCustoUnitarioDataBase(custo);
					}
				} else {
					servicoDTO.setCustoUnitarioDataBase(servicoDTO.getVlCustoUnitarioDataBase());
				}
					
				dto.setPrecoTotalAnalise(dto.getPrecoTotalAnalise().add(servicoDTO.getVlPrecoTotalAceitoNaAnalise()));
				dto.setPrecoTotalLicitado(dto.getPrecoTotalLicitado().add(servicoDTO.getVlPrecoTotalDaLicitacao()));

				dto.setPrecoTotalAceitoNaAnalise(
						dto.getPrecoTotalAceitoNaAnalise().add(servicoDTO.getVlPrecoTotalAceitoNaAnalise()));
				dto.setPrecoTotalNaDataBaseDaLicitacao(
						dto.getPrecoTotalNaDataBaseDaLicitacao().add(servicoDTO.getVlPrecoTotalDataBaseDaLicitacao()));
				

			}

			totalGeralLicitado = totalGeralLicitado.add( dto.getPrecoTotalLicitado() );
			totalGeralAceitoNaAnalise = totalGeralAceitoNaAnalise.add( dto.getPrecoTotalAceitoNaAnalise() );
			totalGeralNaDataBaseDaLicitacao = totalGeralNaDataBaseDaLicitacao.add( dto.getPrecoTotalNaDataBaseDaLicitacao() );

			macroservicos.add(dto);
		}

		PoMacroServicoServicosDTO retorno = new PoMacroServicoServicosDTO();
		retorno.setPoId(po.getId());
		retorno.setMacroservicos(macroservicos);
		retorno.setTotalGeralLicitado(totalGeralLicitado);
		retorno.setTotalGeralAceitoNaAnalise(totalGeralAceitoNaAnalise);
		retorno.setTotalGeralNaDataBaseDaLicitacao(totalGeralNaDataBaseDaLicitacao);

		return retorno;
	}

	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void excluir(ServicoDTO servicoDTO) {
		checkPermission.check(TabelasDoVRPLEnum.SERVICO, servicoDTO.getId());

		dao.getJdbi().useTransaction(transaction -> {
			this.excluir(servicoDTO, transaction);
		});
	}

	public void excluir(ServicoDTO servicoDTO, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.SERVICO, servicoDTO.getId());

		ServicoBD servico = servicoDTO.converterParaBD();
		// levanta excecao caso Servico nao exista
		recuperarServicoPorId(servico);

		transaction.attach(ServicoDAO.class).excluirServico(servico);
	}

	public void removeAssociacaoComEvento(ServicoDTO servicoDTO, Handle transaction) {
		checkPermission.check(TabelasDoVRPLEnum.SERVICO, servicoDTO.getId());

		ServicoBD servico = servicoDTO.converterParaBD();
		// levanta excecao caso Servico nao exista
		recuperarServicoPorId(servico);

		transaction.attach(ServicoDAO.class).removeAssociacaoComEvento(servico);
	}

	public ServicoDTO recuperarServicoPorId(ServicoBD servico) {
		ServicoBD servicoBD = getServicoDAO().recuperarServicoPorId(servico.getId());
		if (servicoBD == null) {
			throw new ServicoNaoEncontradoException(servico);
		}

		ServicoDTO servicoDTO = servicoBD.converterParaDTO();
		return servicoDTO;
	}

	public List<ServicoDTO> recuperarServicoPorMacroServico(Handle transaction, Long macroServicoId) {
		List<ServicoBD> listaServicosBD = transaction.attach(ServicoDAO.class)
				.recuperarServicoPorMacroServico(macroServicoId);

		List<ServicoDTO> retorno = new ArrayList<>();
		for (ServicoBD servicoBD : listaServicosBD) {

			List<ServicoFrenteObraBD> listaServicoFrenteObraBD = transaction.attach(ServicoDAO.class)
					.recuperaListaServicoFrenteObraPorIdServico(servicoBD.getId());
			List<ServicoFrenteObraDTO> listaSfoDto = listaServicoFrenteObraBD.stream().map(ServicoFrenteObraDTO::from).collect(Collectors.toList());

			List<ServicoFrenteObraAnaliseBD> listaSfoaBD = transaction.attach(ServicoFrenteObraAnaliseDAO.class)
					.recuperarTodosServicoFrenteObraAnalisePorChaveServico(servicoBD.getId());
			List<ServicoFrenteObraAnaliseDTO> listaSfoaDTO = listaSfoaBD.stream().map(ServicoFrenteObraAnaliseDTO::from).collect(Collectors.toList());

			ServicoDTO servicoDTO = servicoBD.converterParaDTO();
			servicoDTO.setFrentesObra(listaSfoDto);
			servicoDTO.setFrentesObraAnalise(listaSfoaDTO);

			retorno.add(servicoDTO);
		}

		return retorno;
	}

	public List<ServicoFrenteObraDTO> recuperarListaFrentes(Long idServico) {
		List<FrenteObraBD> frentesObra = dao.get(FrenteObraDAO.class).recuperarListaFrentesDeObraIdServico(idServico);

		List<FrenteObraBD> frentesObraPo = dao.get(FrenteObraDAO.class)
				.recuperarListaFrentesDeObraPoIdServico(idServico).stream()
				.filter(frenteObra -> !frentesObra.contains(frenteObra)).collect(Collectors.toList());

		frentesObra.addAll(frentesObraPo);

		List<ServicoFrenteObraDTO> servicoFrentesObra = frentesObra.stream().map(frenteObraBD -> {
			ServicoFrenteObraDTO servicoFrenteObra = new ServicoFrenteObraDTO();
			servicoFrenteObra.setServicoFk(idServico);
			servicoFrenteObra.setFrenteObraFk(frenteObraBD.getId());
			servicoFrenteObra.setIdPO(frenteObraBD.getPoFk());
			servicoFrenteObra.setVersao(frenteObraBD.getVersao());
			servicoFrenteObra.setIndicadorDadoAnalise(false);

			if (frenteObraBD.getQtdServicoFrenteObra() == null) {
				servicoFrenteObra.setQtItens(BigDecimal.ZERO);
			} else {
				servicoFrenteObra.setQtItens(frenteObraBD.getQtdServicoFrenteObra());
			}
			servicoFrenteObra.setNomeFrente(frenteObraBD.getNmFrenteObra());
			servicoFrenteObra.setNumeroFrente(frenteObraBD.getNrFrenteObra());

			return servicoFrenteObra;
		}).collect(Collectors.toList());

		Collections.sort(servicoFrentesObra);
		return servicoFrentesObra;
	}

	public List<ServicoDTO> recuperarServicosPorEvento(Long eventoId) {
		List<ServicoDTO> retorno = new ArrayList<>();

		List<ServicoBD> servicos = getServicoDAO().recuperarServicosPorEvento(eventoId);
		for (ServicoBD servicoBD : servicos) {
			retorno.add(servicoBD.converterParaDTO());
		}

		return retorno;
	}

	@Deprecated
	@Log
	public SinapiIntegracao recuperarSinapiPorCodItemData(String cdItem, LocalDate data, String local) {
		return dao.get(SiconvDAO.class).recuperarSinapiPorCodigoItemData(cdItem, data, local);
	}

	public ServicoDAO getServicoDAO() {
		return dao.get(ServicoDAO.class);
	}

	public ServicoFrenteObraDAO getServicoFrenteObraDAO() {
		return dao.get(ServicoFrenteObraDAO.class);
	}

	public List<ServicoFrenteObraDTO> recuperarListaFrentesDeObraAnalise(Long idServico) {

		List<ServicoFrenteObraDTO> retorno = new ArrayList<>();
		List<ServicoFrenteObraAnaliseBD> listaServicoFrentesObraAnalise = dao.get(ServicoFrenteObraAnaliseDAO.class).recuperarTodosServicoFrenteObraAnalisePorChaveServico(idServico);
		Long idPo = dao.get(ServicoDAO.class).recuperarIdPoPorServicoId(idServico);

		for (ServicoFrenteObraAnaliseBD servicoFrenteObraAnalise : listaServicoFrentesObraAnalise) {
			ServicoFrenteObraDTO dto = new ServicoFrenteObraDTO();
			dto.setIdPO(idPo);
			dto.setFrenteObraFk(servicoFrenteObraAnalise.getServicoFk());
			dto.setVersao(servicoFrenteObraAnalise.getVersao());
			dto.setNomeFrente(servicoFrenteObraAnalise.getNmFrenteObra());
			dto.setNumeroFrente(servicoFrenteObraAnalise.getNrFrenteObra());
			dto.setQtItens(servicoFrenteObraAnalise.getQtItens());
			retorno.add(dto);
		}

		return retorno;
	}

}
