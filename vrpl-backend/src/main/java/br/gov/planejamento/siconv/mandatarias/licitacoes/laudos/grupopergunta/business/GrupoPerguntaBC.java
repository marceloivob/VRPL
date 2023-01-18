package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.business;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.dao.GrupoPerguntaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database.GrupoPerguntaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.business.PerguntaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;

public class GrupoPerguntaBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private PerguntaBC perguntaBC;

	public GrupoPerguntaDTO recuperarGrupoPerguntaPorId(Long id) {
		GrupoPerguntaBD grupoPerguntaBD = dao.get(GrupoPerguntaDAO.class).recuperarGrupoPerguntaPorId(id);
		if (grupoPerguntaBD == null) {
			throw new GrupoPerguntaNaoEncontradoException();
		}

		GrupoPerguntaDTO grupoPerguntaDTO = grupoPerguntaBD.converterParaDTO();
		return grupoPerguntaDTO;
	}

	public List<GrupoPerguntaDTO> recuperarGruposPerguntaPorTemplate(Long idTemplateLaudo, Long idLicitacao,
			Long idVersaoDaLicitacao) {
		List<GrupoPerguntaDTO> lista = new ArrayList<>();

		List<GrupoPerguntaBD> listaBD = dao.get(GrupoPerguntaDAO.class)
				.recuperarGruposPerguntaPorTemplate(idTemplateLaudo);
		for (GrupoPerguntaBD grupoPerguntaBD : listaBD) {
			GrupoPerguntaDTO grupoPerguntaDTO = grupoPerguntaBD.converterParaDTO();
			List<PerguntaDTO> perguntas = perguntaBC.recuperarPerguntasPorGrupo(grupoPerguntaDTO, idLicitacao,
					idVersaoDaLicitacao);

			grupoPerguntaDTO.setPerguntas(perguntas);

			lista.add(grupoPerguntaDTO);
		}

		return lista;
	}

}
