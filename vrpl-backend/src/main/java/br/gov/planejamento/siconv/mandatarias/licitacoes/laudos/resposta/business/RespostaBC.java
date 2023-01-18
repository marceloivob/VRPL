package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.business;

import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.dao.RespostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;

public class RespostaBC {

	@Inject
	private DAOFactory dao;

	public void inserir(RespostaDTO resposta) {

		// inserir regras de validacao
		// converterDTO
		RespostaBD respostaConvertido = resposta.converterParaBD();

		dao.get(RespostaDAO.class).inserirResposta(respostaConvertido);
	}

	public void alterar(RespostaDTO resposta) {

		// inserir regras de validacao

		// valida se o Resposta nao exista
		recuperarRespostaPorId(resposta.getRespostaId());

		// converterDTO
		RespostaBD respostaConvertido = resposta.converterParaBD();

		dao.get(RespostaDAO.class).alterarResposta(respostaConvertido);

	}

	public void excluir(Long id) {
		// levanta excecao caso Resposta nao exista
		recuperarRespostaPorId(id);

		dao.get(RespostaDAO.class).excluirResposta(id);
	}

	public RespostaDTO recuperarRespostaPorId(Long id) {
		RespostaBD respostaBD = dao.get(RespostaDAO.class).recuperarRespostaPorId(id);

		if (respostaBD == null) {
			throw new RespostaNaoEncontradoException();
		}

		RespostaDTO respostaDTO = respostaBD.converterParaDTO();
		return respostaDTO;
	}

	public void inserirRespostas(List<RespostaDTO> respostas) {
		for (RespostaDTO r : respostas) {
			if (r.getRespostaId() != null) {
				this.alterar(r);
			} else {
				this.inserir(r);
			}
		}
	}

}
