package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business;

import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.PermissoesDaLicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.PermissaoDTO;

public class Permissoes {

	@Inject
	private DAOFactory dao;

	public List<PermissaoDTO> consultarPermissoesDasLicitacoesDaProposta(Long identificadorDaProposta) {

		List<PermissaoDTO> licitacoes = dao.get(PermissoesDaLicitacaoDAO.class)
				.consultarPermissoesDasLicitacoesDaProposta(identificadorDaProposta);

		return licitacoes;

	}

}
