package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceType.ANALISE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.customizer.BindList.EmptyHandling;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DataSourceFor;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Perfil;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.dao.SiconvDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.CtefIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.EventoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.FornecedorIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MacroServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.MetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NaturezaDespesa;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.PoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraDetalheIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoFrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ServicoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubItemInvestimento;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubitemInvestimentoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SubmetaIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.UF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.exception.ErroEnvioEmailException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.integracao.EventoIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.integracao.FrenteObraIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.UpdatePropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class SiconvBC {

	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	@Inject
	@DataSourceFor(ANALISE)
	private DAOFactory dao;

	/**
	 * Recupera a lista de UFs
	 *
	 * @return lista com as UFs
	 */
	public List<UF> findUFS() {
		List<UF> listaUfs = dao.get(SiconvDAO.class).findUFS();
		return listaUfs;
	}

	public NaturezaDespesa loadNaturezaDespesa(Long idItemPad) {
		NaturezaDespesa naturezaDespesa = dao.get(SiconvDAO.class).loadNaturezaDespesa(idItemPad);

		return naturezaDespesa;
	}

	public List<NaturezaDespesa> loadListNaturezaDespesa(List<Long> idsItemPad) {

		if (idsItemPad.isEmpty())
			return new ArrayList<>();

		List<Long> idsDistintosItemPad = idsItemPad.stream().distinct().collect(Collectors.toList());

		List<NaturezaDespesa> naturezasDeDespesas = dao.get(SiconvDAO.class)
				.loadNaturezasDeDespesas(idsDistintosItemPad);

		return naturezasDeDespesas;
	}

	public SubItemInvestimento loadSubItemInvestimento(Long idSubItemInvestimento) {
		SubItemInvestimento subItemInvestimento = dao.get(SiconvDAO.class)
				.loadSubItemInvestimento(idSubItemInvestimento);

		return subItemInvestimento;
	}

	public DadosBasicosIntegracao recuperarDadosBasicos(SiconvPrincipal usuarioLogado) {
		return dao.get(SiconvDAO.class).recuperarDadosBasicos(usuarioLogado);
	}

	/**
	 * <b>Caso de Uso:</b> 540583 -
	 * SICONV-DocumentosOrcamentarios-ECU-ENTRADA-VerificaçãoResultadoProcessoLicitatorio
	 * <p>
	 * <b>Fluxo:</b> E4. Documentos Orçamentários sem SPA Homologado
	 * <p>
	 * E4.1. No passo P1, o sistema verifica que os documentos orçamentários ainda
	 * não tem SPA homologado, exibindo a tela de erro, conforme regra de negócio:
	 * <p>
	 * <b>RN:</b> 540930 -
	 * SICONV-DocumentosOrcamentarios-Geral-MSG-Info-NaoPermitirAcessoVerificacaoProcessoLicitatorio
	 */
	public boolean existeSPAHomologadoParaProposta(SiconvPrincipal usuarioLogado) {
		Boolean existeSPAHomologado = dao.get(SiconvDAO.class).existeSPAHomologadoParaProposta(usuarioLogado);

		return existeSPAHomologado;
	}

	public List<String> recuperarLotesDaProposta(Long identificadorDaProposta) {
		return dao.get(SiconvDAO.class).recuperarLotesDaProposta(identificadorDaProposta);
	}

	@Log
	public CtefIntegracao findCtefByIdLicitacaoFK(Long idLicitacaoFK) {
		return dao.get(SiconvDAO.class).findCtefByIdLicitacaoFK(idLicitacaoFK);
	}

	public List<Map<String, Object>> findCtefByIdsLicitacoesFK(
			@BindList("idsLicitacoesFK") List<Long> idsLicitacoesFK) {
		return dao.get(SiconvDAO.class).findCtefByIdsLicitacoesFK(idsLicitacoesFK);
	}

	public List<SubmetaIntegracao> recuperarSubmetasDaPropostaPorLote(Long identificadorDaProposta,
			List<Long> numerosLote) {
		return dao.get(SiconvDAO.class).recuperarSubmetasDaPropostaPorLote(identificadorDaProposta, numerosLote);
	}

	public List<SubmetaIntegracao> recuperarSubmetasDaProposta(Long identificadorDaProposta) {
		return dao.get(SiconvDAO.class).recuperarSubmetasDaProposta(identificadorDaProposta);
	}

	public List<MetaIntegracao> recuperarMetasProposta(Long identificadorDaProposta) {
		return dao.get(SiconvDAO.class).recuperarMetasProposta(identificadorDaProposta);
	}

	public List<PoIntegracao> recuperarPosPropostaPorLote(Long identificadorDaProposta, List<Long> numerosLote) {
		return dao.get(SiconvDAO.class).recuperarPosPropostaPorLote(identificadorDaProposta, numerosLote);
	}

	public List<PoIntegracao> recuperarPosPorIdSubmeta(List<Long> idsSubmeta) {
		return dao.get(SiconvDAO.class).recuperarPosPorIdSubmeta(idsSubmeta);
	}

	public List<EventoIntegracao> recuperarListaEventosAnalisePorIdPoIdProposta(List<Long> idsPo, Long idProposta) {
		return dao.get(SiconvDAO.class).recuperarListaEventosAnalisePorIdPoIdProposta(idsPo, idProposta);
	}

	public List<FrenteObraIntegracao> recuperarListaFrentesObraAnalisePorIdPoIdProposta(List<Long> idsPo,
			Long idProposta) {
		return dao.get(SiconvDAO.class).recuperarListaFrentesObraAnalisePorIdPoIdProposta(idsPo, idProposta);
	}

	public List<MacroServicoIntegracao> recuperarMacroServicosPorIdsPos(List<Long> idsPo) {
		return dao.get(SiconvDAO.class).recuperarMacroServicosPorIdsPos(idsPo);
	}

	public List<ServicoIntegracao> recuperarServicosPorIdsMacroServico(List<Long> idsMacroServico) {
		return dao.get(SiconvDAO.class).recuperarServicosPorIdsMacroServico(idsMacroServico);
	}

	public List<EventoFrenteObraIntegracao> recuperarEventoFrenteObraPorIdEvento(List<Long> idsEvento) {
		return dao.get(SiconvDAO.class).recuperarEventoFrenteObraPorIdEvento(idsEvento);
	}

	public List<ServicoFrenteObraIntegracao> recuperarServicoFrenteObraPorIdServico(List<Long> idsServico) {
		return dao.get(SiconvDAO.class).recuperarServicoFrenteObraPorIdServico(idsServico);
	}
	
	public List<ServicoFrenteObraDetalheIntegracao>recuperarServicoFrenteObraDetalhePorIdServico(
			 List<Long> idsServico) {
		return dao.get(SiconvDAO.class).recuperarServicoFrenteObraDetalhePorIdServico(idsServico);
	}

	public List<SubitemInvestimentoIntegracao> recuperarSubitemInvestimentoPorMetas(List<Long> idsMetas) {
		return dao.get(SiconvDAO.class).recuperarSubitemInvestimentoPorMetas(idsMetas);
	}

	public List<FornecedorIntegracao> recuperarFornecedoresProposta(Long idProposta) {
		return dao.get(SiconvDAO.class).recuperarFornecedoresProposta(idProposta);
	}

	public Set<Address> buscarEmails(PropostaBD proposta, Profile profile) {
		List<String> emails = new ArrayList<>();

		if (profile.equals(Profile.PROPONENTE)) {
			emails = dao.get(SiconvDAO.class).recuperarEmailsPerfilProponente(proposta.getIdSiconv(),
					Perfil.proponentes);
		} else if (profile.equals(Profile.CONCEDENTE)) {
			emails = dao.get(SiconvDAO.class).recuperarEmailsPerfilConcedente(proposta.getIdSiconv(),
					Perfil.concedentes);
		} else if (profile.equals(Profile.MANDATARIA)) {
			emails = dao.get(SiconvDAO.class).recuperarEmailsPerfilMandataria(proposta.getIdSiconv(),
					Perfil.mandatarias);
		} else {
			throw new IllegalArgumentException("O perfil " + profile + " não pode enviar emails.");
		}

		Set<Address> emailsSet = new HashSet<>();
		for (String email : emails) {
			try {
				emailsSet.add(new InternetAddress(email));
			} catch (AddressException e) {
				throw new ErroEnvioEmailException(e);
			}
		}

		if (emailsSet.isEmpty()) {
			throw new IllegalArgumentException(
					"Não foram encontrados os emails para a Proposta:" + proposta + " e Perfil: " + profile);
		}

		return emailsSet;
	}

	public void atualizarDadosProposta(PropostaBD proposta, Handle transaction, SiconvPrincipal usuarioLogado) {
		boolean deveAtualizar = this.atualizarDadosProposta(proposta, usuarioLogado);
		
		if(deveAtualizar) {
			UpdatePropostaDAO updatePropostaDao = transaction.attach(UpdatePropostaDAO.class);
			updatePropostaDao.atualizaDadosProposta(proposta);
		}
	}

	public boolean atualizarDadosProposta(PropostaBD proposta, SiconvPrincipal usuarioLogado) {
		boolean deveAtualizar = false;
		
		if (proposta == null) {
			throw new IllegalArgumentException("A proposta não pode ser nula.");
		}
		
		PropostaBD propostaAtualizada = recuperarDadosProposta(usuarioLogado);

		if (propostaAtualizada != null) {
			
			if (proposta.getDataAssinaturaConvenio() == null) {
				LocalDate dataAssinaturaConvenio = propostaAtualizada.getDataAssinaturaConvenio();
				if (dataAssinaturaConvenio != null) {
					proposta.setDataAssinaturaConvenio(dataAssinaturaConvenio);
					deveAtualizar = true;
				}
			}
			
			if (proposta.getNumeroConvenio() == null) {
				proposta.setNumeroConvenio(propostaAtualizada.getNumeroConvenio());
				deveAtualizar = true;
			}
			
			if (proposta.getAnoConvenio() == null) {
				proposta.setAnoConvenio(propostaAtualizada.getAnoConvenio());
				deveAtualizar = true;
			}
			
			if (StringUtils.isEmpty(proposta.getNomeMandataria())) {
				proposta.setNomeMandataria(propostaAtualizada.getNomeMandataria());
				deveAtualizar = true;
			}
			
			if (! proposta.getModalidade().equals( propostaAtualizada.getModalidade() )) {
				proposta.setModalidade(propostaAtualizada.getModalidade());
				deveAtualizar = true;
			}
			
			if (! proposta.getValorGlobal().equals( propostaAtualizada.getValorGlobal() )) {
				proposta.setValorGlobal(propostaAtualizada.getValorGlobal());
				deveAtualizar = true;
			}
			
			if (! proposta.getValorRepasse().equals( propostaAtualizada.getValorRepasse() )) {
				proposta.setValorRepasse(propostaAtualizada.getValorRepasse());
				deveAtualizar = true;
			}

			if (! proposta.getValorContrapartida().equals( propostaAtualizada.getValorContrapartida() )) {
				proposta.setValorContrapartida(propostaAtualizada.getValorContrapartida());
				deveAtualizar = true;
			}
		
		}
		
		return deveAtualizar;
	}

	/**
	 * Recupera dados no SICONV da proposta que eventualmente não foram trazidos na importação
	 * dos dados básicos
	 */
	public PropostaBD recuperarDadosProposta(SiconvPrincipal usuarioLogado) {
		PropostaBD propostaNoSiconvComTermoAditivoPendente = 
				dao.get(SiconvDAO.class).recuperarDadosPropostaComTermoAditivoPendente(usuarioLogado);
		
		if (propostaNoSiconvComTermoAditivoPendente != null) {
			return propostaNoSiconvComTermoAditivoPendente;
		}
		
		return dao.get(SiconvDAO.class).recuperarDadosProposta(usuarioLogado);
	}

}
