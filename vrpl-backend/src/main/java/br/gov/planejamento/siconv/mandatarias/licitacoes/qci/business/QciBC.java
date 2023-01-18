package br.gov.planejamento.siconv.mandatarias.licitacoes.qci.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.business.SiconvBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.DadosBasicosIntegracao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NaturezaDespesa;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.business.MacroServicoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.dao.PoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.MetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.database.SubmetaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.MetaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.MetaQCIExternoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.QciDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.QciExternoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaQCIExternoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.entity.dto.SubmetaQciVRPLDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QciBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private SiconvPrincipal usuarioLogado;

	@Inject
	private SiconvBC siconvBC;

	@Inject
	private PoBC poBC;

	@Inject
	private MacroServicoBC macroServicoBC;

	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public void alterar(SubmetaQciVRPLDTO submeta) {

		verificaSeSubmetaExiste(submeta);

		SubmetaBD submetaBD = new SubmetaBD(submeta);

		getQciDAO().alterarDadosSubmeta(submetaBD);
	}

	private void verificaSeSubmetaExiste(SubmetaQciVRPLDTO submeta) {
		SubmetaBD submetaBD = getQciDAO().recuperarSubmetaVRPL(submeta.getIdSubmeta());

		if (submetaBD == null) {
			throw new SubmetaNaoEncontradaException();
		}
	}

	public QciDTO recuperarQCIInternoByIdLicitacao(Long idLicitacao) {
		List<MetaBD> metas = getQciDAO().recuperarQCIInternoByIdLicitacao(idLicitacao);

		DadosBasicosIntegracao dadosBasicos = this.siconvBC.recuperarDadosBasicos(usuarioLogado);

		QciDTO qciDto = new QciDTO(dadosBasicos, metas);

		List<SubmetaDTO> submetasDto = qciDto.getMetas().stream().flatMap(metaDto -> metaDto.getSubmetas().stream())
				.collect(Collectors.toList());

		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(idLicitacao);
		List<PoVRPLDTO> pos = poBC.recuperarNovoPosPorLicitacao(licitacao);
		Map<Long, PoVRPLDTO> poPorSubmeta = new HashMap<>();
		for (PoVRPLDTO po : pos) {
			poPorSubmeta.put(po.getIdSubmeta(), po);
		}

		for (MetaDTO meta : qciDto.getMetas() ) {
			for (SubmetaDTO submeta : meta.getSubmetas()) {
				PoVRPLDTO po = poPorSubmeta.get(submeta.getId());

				if (po != null) {
					submeta.setVlTotal( po.getPrecoTotalLicitacao() );
				}

				if( submeta.getVlRepasse().compareTo( submeta.getVlTotal() ) > 0 ) {
					submeta.setVlRepasse( submeta.getVlTotal() );
				}

				submeta.setVlContrapartida(
					submeta.getVlTotal().subtract( submeta.getVlRepasse() ).subtract( submeta.getVlOutros() )
				);
			}
		}

		List<Long> idsItemsDoPAD = submetasDto.stream().map(SubmetaDTO::getItemPad).collect(Collectors.toList());

		List<NaturezaDespesa> naturezasDeDespesa = siconvBC.loadListNaturezaDespesa(idsItemsDoPAD);

		submetasDto.forEach(submeta -> {
			for (NaturezaDespesa itemDoPad : naturezasDeDespesa) {
				if (itemDoPad.getId().compareTo(submeta.getItemPad()) == 0) {
					submeta.setDescricaoItemPad(itemDoPad.getDescricao());
				}
			}
		});

		for (MetaDTO meta : qciDto.getMetas()) {
			Collections.sort(meta.getSubmetas(),
					Comparator.comparing(SubmetaDTO::getLote).thenComparing(SubmetaDTO::getNumero));
		}

		return qciDto;
	}


	@Log
	public QciExternoDTO recuperarNovoQCIExternoByPropFk(SiconvPrincipal usuarioLogado, Long versaoDaProposta) {

		List<MetaQCIExternoDTO> metas = getQciDAO().recuperarMetasAtivasParaQCIExternoByPropFk(usuarioLogado,
				versaoDaProposta);

		List<Long> idsDasMetas = metas.stream().map(MetaQCIExternoDTO::getIdMetaAnalise).distinct()
				.collect(Collectors.toList());

		List<SubmetaQCIExternoDTO> todasSubmetas = getQciDAO().recuperarSubMetasParaQCIExternoByMetaId(usuarioLogado,
				versaoDaProposta, idsDasMetas);

		List<Long> listaIdPo = new ArrayList<>();
		for (SubmetaQCIExternoDTO submetaQCIExternoDTO : todasSubmetas) {
			listaIdPo.add(dao.get(PoDAO.class).recuperarIdPoPorSubmeta(submetaQCIExternoDTO.getId()));
		}

		Map<PoBD,PoMacroServicoServicosDTO> mapaPoMacroServicoServicos = macroServicoBC.recuperarMacroServicoServicosPorPo(listaIdPo);

		for (SubmetaQCIExternoDTO submetaQCIExternoDTO : todasSubmetas) {
			PoMacroServicoServicosDTO po = obterPo (mapaPoMacroServicoServicos, submetaQCIExternoDTO.getId());

			if(po != null) {
				submetaQCIExternoDTO.setValorTotalLicitado( po.getTotalGeralLicitado() );
				submetaQCIExternoDTO.setValorRepasseLicitado(
						submetaQCIExternoDTO.getValorTotalLicitado().subtract( submetaQCIExternoDTO.getValorContrapartidaLicitado() )
				);
			}
		}

		metas.forEach(meta -> {
			List<SubmetaQCIExternoDTO> apenasSubmetasAtivas = todasSubmetas.stream()
					.filter(s -> s.getIdMeta().compareTo(meta.getIdMetaAnalise()) == 0)
					.filter(submeta -> submeta.getSituacao() != SituacaoLicitacaoEnum.REJEITADA)
					.collect(Collectors.toList());

			List<SubmetaQCIExternoDTO> todasSubmetasRejeitadasDaMeta = todasSubmetas.stream()
					.filter(s -> s.getIdMeta().compareTo(meta.getIdMetaAnalise()) == 0)
					.filter(submeta -> submeta.getSituacao() == SituacaoLicitacaoEnum.REJEITADA)
					.collect(Collectors.toList());

			todasSubmetasRejeitadasDaMeta.forEach(submetaRejeitada -> {
				if (!apenasSubmetasAtivas.contains(submetaRejeitada)) {
					meta.getSubmetas().add(submetaRejeitada);
				}
			});

			meta.getSubmetas().addAll(apenasSubmetasAtivas);
		});

		PropostaBD proposta = getPropostaDAO().recuperaVersaoDaProposta(usuarioLogado, versaoDaProposta);
		QciExternoDTO qciDto = new QciExternoDTO(proposta, metas);

		return qciDto;
	}

	private PoMacroServicoServicosDTO obterPo(Map<PoBD, PoMacroServicoServicosDTO> mapaPoMacroServicoServicos, Long submetaId) {
		if (mapaPoMacroServicoServicos.isEmpty()) {
			return null;
		}

		Optional<PoBD> firstKey = mapaPoMacroServicoServicos.keySet().stream().filter(po -> po.getSubmetaId().equals(submetaId)).
						findFirst();

		if (firstKey.isPresent()) {
			return mapaPoMacroServicoServicos.get(firstKey.get());

		} else {
			return null;
		}
	}

	protected QciDAO getQciDAO() {
		return dao.get(QciDAO.class);
	}

	protected PropostaDAO getPropostaDAO() {
		return dao.get(PropostaDAO.class);
	}

}
