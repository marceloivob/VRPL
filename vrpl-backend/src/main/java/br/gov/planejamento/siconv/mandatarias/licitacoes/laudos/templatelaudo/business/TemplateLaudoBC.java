package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.business;

import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.business.GrupoPerguntaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.dao.TemplateLaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.database.TemplateLaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.dto.TemplateLaudoDTO;

public class TemplateLaudoBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private GrupoPerguntaBC grupoPerguntaBC;

	public TemplateLaudoDTO recuperarTemplateLaudoPorId(Long idTemplate) {
		TemplateLaudoBD templateLaudoBD = dao.get(TemplateLaudoDAO.class).recuperarTemplateLaudoPorId(idTemplate);
		if (templateLaudoBD == null) {
			throw new TemplateLaudoNaoEncontradoException();
		}

		TemplateLaudoDTO templateLaudoDTO = templateLaudoBD.converterParaDTO();
		return templateLaudoDTO;
	}

	public TemplateLaudoDTO recuperarTemplateLaudoPorTipo(String tipoTemplateLaudo, Long idLicitacao,
			Long idVersaoDaLicitacao) {
		TemplateLaudoBD templateBD = dao.get(TemplateLaudoDAO.class).recuperarTemplateLaudoPorTipo(tipoTemplateLaudo);
		TemplateLaudoDTO resultado = templateBD.converterParaDTO();
		List<GrupoPerguntaDTO> gruposPergunta = grupoPerguntaBC.recuperarGruposPerguntaPorTemplate(templateBD.getId(),
				idLicitacao, idVersaoDaLicitacao);
		resultado.setGrupos(gruposPergunta);

		return resultado;
	}

}
