package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.annotation.Log;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDetalhadaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.SubmetaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.PoMacroServicoServicosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.servico.entity.dto.ServicoDTO;

public class LicitacaoBCAux {

	@Inject
	private PoBC poBC;

	@Log
	public void preencherInformacoesDosLotes(LicitacaoDetalhadaDTO licitacaoDetalhada) {

		LicitacaoBD lic = new LicitacaoBD();
		lic.setId(licitacaoDetalhada.getId());

		List<PoVRPLDTO> pos = poBC.recuperarNovoPosPorLicitacao(lic);

		Map<Long, PoDetalhadaVRPLDTO> mapaPoSubmeta = new HashMap<>();
		Map<Long, PoVRPLDTO> posLicitacaoPorSubmeta = new HashMap<>();

		for (PoVRPLDTO poVRPLDTO : pos) {
			posLicitacaoPorSubmeta.put(poVRPLDTO.getIdSubmeta(), poVRPLDTO);

			PoDetalhadaVRPLDTO poDetalhada = poBC.findPoDetalhada(poVRPLDTO.getId());
			mapaPoSubmeta.put(poDetalhada.getIdSubmeta(), poDetalhada);
		}

		for (LoteDTO lote : licitacaoDetalhada.getLotes()) {
			BigDecimal valorTotalLicitado = BigDecimal.ZERO;
			BigDecimal valorTotalDatabase = BigDecimal.ZERO;
			BigDecimal valorTotalAnalise = BigDecimal.ZERO;

			for (SubmetaDTO s : lote.getSubmetas()) {

				if(posLicitacaoPorSubmeta.get(s.getId()) != null) {
					PoVRPLDTO poLicitacao = posLicitacaoPorSubmeta.get(s.getId());
					s.setValorLicitado( poLicitacao.getPrecoTotalLicitacao() );

					if(s.getVlRepasse().compareTo(s.getValorLicitado()) > 0) {
						s.setVlRepasse( s.getValorLicitado() );
					}

					s.setVlContrapartida(
						s.getValorLicitado().subtract( s.getVlRepasse() ).subtract( s.getVlOutros() )
					);
				}

				if(mapaPoSubmeta.get(s.getId()) != null) {
					PoDetalhadaVRPLDTO po = mapaPoSubmeta.get(s.getId());

					if(lote.getReferencia() == null) {
						lote.setReferencia(po.getReferencia());
					}

					if ((lote.getPrevisaoTermino() == null) || (lote.getPrevisaoTermino()
							.isBefore(po.getPrevisaoInicioVRPL().plusMonths(po.getQtMesesDuracaoObra())))) {
						lote.setPrevisaoTermino(po.getPrevisaoInicioVRPL().plusMonths(po.getQtMesesDuracaoObra()));
					}

					PoMacroServicoServicosDTO poMacroServicoServicosDTO = obterPoMacroServicoServicos(pos,po.getId());

					if (poMacroServicoServicosDTO != null) {
						List<MacroServicoServicosDTO> macroServicos = poMacroServicoServicosDTO.getMacroservicos();
						for (MacroServicoServicosDTO macrosservico : macroServicos) {
							for (ServicoDTO servico : macrosservico.getServicos()) {

								if (servico.getVlPrecoUnitarioNaDataBaseDaLicitacao() != null) {
									valorTotalDatabase = valorTotalDatabase.add( servico.getVlPrecoTotalDataBaseDaLicitacao() );
								} else {
									valorTotalDatabase = valorTotalAnalise.add( servico.getVlPrecoTotalAceitoNaAnalise() );
								}

								valorTotalAnalise = valorTotalAnalise.add( servico.getVlPrecoTotalAceitoNaAnalise() );
								valorTotalLicitado = valorTotalLicitado.add( servico.getVlPrecoTotalDaLicitacao() );

								if ("analise".equalsIgnoreCase(lote.getReferencia())
										&& servico.getVlPrecoUnitarioAceitoNaAnalise().compareTo(servico.getVlPrecoUnitarioDaLicitacao()) < 0) {
									lote.setPrecoUnitarioMenorOuIgualRef( Boolean.FALSE );
								} else if("database".equalsIgnoreCase(lote.getReferencia())) {
									if(servico.getVlPrecoUnitarioNaDataBaseDaLicitacao()!= null) {
										if (servico.getVlPrecoUnitarioNaDataBaseDaLicitacao()
												.compareTo(servico.getVlPrecoUnitarioDaLicitacao()) < 0) {
												lote.setPrecoUnitarioMenorOuIgualRef( Boolean.FALSE );
										}
									} else if(servico.getVlPrecoUnitarioAceitoNaAnalise().compareTo(servico.getVlPrecoUnitarioDaLicitacao()) < 0) {
											lote.setPrecoUnitarioMenorOuIgualRef( Boolean.FALSE );
									}
								}
							}
						}
					}
				}
			}

			if(lote.getPrecoUnitarioMenorOuIgualRef() == null) {
				lote.setPrecoUnitarioMenorOuIgualRef(Boolean.TRUE);
			}

			if("analise".equalsIgnoreCase(lote.getReferencia())) {
				lote.setVlTotalLicitadoMenorOuIgualRef(valorTotalAnalise.compareTo(valorTotalLicitado) >= 0);
			} else {
				lote.setVlTotalLicitadoMenorOuIgualRef(valorTotalDatabase.compareTo(valorTotalLicitado) >= 0);
			}

		}

	}

	private PoMacroServicoServicosDTO obterPoMacroServicoServicos(List<PoVRPLDTO> pos, Long poId) {

		return pos.stream().filter(poVRPL -> poVRPL.getId().equals(poId)).findFirst().orElseThrow().getPoMacroServicoServicos();
	}



}
