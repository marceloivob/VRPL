package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.CampoDePreenchimentoObrigatorioException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.ComentarioObrigatorioParaSecao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.RegimeDeContratacaoDaLicitacaoEhRDCOuLei13303;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;

/**
 * RN 566904: SICONV-DocumentosOrcamentarios-Pareceres-RN-Validacao
 */
public class ParecerVRPLBusinessRuleValidator {

	@Inject
	private DAOFactory dao;

	@Inject
	private RegimeDeContratacaoDaLicitacaoEhRDCOuLei13303 regimeDeContratacao;

	public void validate(LaudoDTO laudo) {
		this.regra01(laudo);

	}

	/**
	 * Para Parecer Técnico de Engenharia:
	 * <p>
	 * - Existe seção em que a resposta não esteja de acordo com o esperado,
	 * conforme regra de negócio: 566891:
	 * SICONV-DocumentosOrcamentarios-ManterVRPL-RN-Formulario
	 * <p>
	 * E não existe o Comentário na respectiva seção.
	 * 
	 * <p>
	 * Consequência: O comentário é obrigatório na seção <descrição da seção>, pela
	 * existência de resposta(s) que não está(estão) de acordo com o esperado.
	 */
	private void regra01(LaudoDTO laudo) {

		List<RespostaDTO> respostas = laudo.getRespostas();

		for (RespostaDTO respostaDTO : respostas) {

			if (perguntaPossuiTratamentoEspecial(respostaDTO)) {
				validaPerguntaEspecial(laudo, respostaDTO);
			} else {
				validaPerguntaSemTratamentoEspecial(laudo, respostaDTO);
			}

		}
	}

	private void validaPerguntaSemTratamentoEspecial(LaudoDTO laudo, RespostaDTO respostaDTO) {
		PerguntaDTO pergunta = respostaDTO.getPergunta();

		if (respostaFornecidaEhDiferenteDaEsperadaParaAPergunta(respostaDTO, pergunta)) {
			if (naoExisteComentario(respostaDTO, laudo)) {
				throw new ComentarioObrigatorioParaSecao("Orçamento");
			} 
//				else {
//				throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
//			}
		}

	}

	private void validaPerguntaEspecial(LaudoDTO laudo, RespostaDTO respostaDTO) {

		PerguntaDTO perguntaEspecial = new PerguntaDTO();
		perguntaEspecial.setTitulo(
				"Foi apresentada declaração de que o edital da licitação contempla todos os elementos necessários abaixo dispostos?");

		PerguntaDTO perguntaEspecial2 = new PerguntaDTO();
		perguntaEspecial2.setTitulo("Considera-se a verificação do resultado do processo licitatório");

		if (respostaDTO.getPergunta().getTitulo().equals(perguntaEspecial.getTitulo())) {
			LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(laudo.getLicitacaoFk());

			boolean regimeDeContratacaoEhRDCOuLei13303 = regimeDeContratacao
					.regimeDeContratacaoEhRDCOuLei13303(licitacao);

			if (regimeDeContratacaoEhRDCOuLei13303) {
				PerguntaDTO pergunta = respostaDTO.getPergunta();

				if ((respostaDTO.getResposta() == null) || (respostaDTO.getResposta().isEmpty())) {
					throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
				}
			}
		} else if (respostaDTO.getPergunta().getTitulo().equals(perguntaEspecial2.getTitulo())) {
			PerguntaDTO pergunta = respostaDTO.getPergunta();

			if ((respostaDTO.getResposta() == null) || (respostaDTO.getResposta().isEmpty())) {
				throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
			}
		}

	}

	/**
	 * Defeito 1925326
	 */
	private boolean perguntaPossuiTratamentoEspecial(RespostaDTO respostaDTO) {
		String perguntaEspecial = "Foi apresentada declaração de que o edital da licitação contempla todos os elementos necessários abaixo dispostos?";
		String perguntaEspecial1 = "Considera-se a verificação do resultado do processo licitatório";

		List<String> perguntasEspeciais = Arrays.asList(perguntaEspecial, perguntaEspecial1);

		String tituloDaPergunta = respostaDTO.getPergunta().getTitulo();

		return (perguntasEspeciais.contains(tituloDaPergunta));
	}

	private boolean naoExisteComentario(RespostaDTO resposta, LaudoDTO laudo) {

		List<RespostaDTO> respostasDoGrupo = laudo.getRespostas().stream()
				.filter(r -> r.getGrupo().equals(resposta.getGrupo())).collect(Collectors.toList());

		Optional<RespostaDTO> comentario = respostasDoGrupo.stream()
				.filter(r -> r.getPergunta().getTitulo().equals("Comentários")).findFirst();

		if (comentario.isPresent()) {
			RespostaDTO d = comentario.get();

			if ((d.getResposta() == null) || (d.getResposta().isEmpty())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	protected boolean respostaFornecidaEhDiferenteDaEsperadaParaAPergunta(RespostaDTO respostaDTO,
			PerguntaDTO pergunta) {

		if ((pergunta.getValorEsperado() == null) || (pergunta.getValorEsperado().isEmpty())) {
			return false;
		}

		if ((respostaDTO.getResposta() == null) && (pergunta.getValorEsperado() != null)) {
			//return true;
			throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
		}

		return "Não".equals(respostaDTO.getResposta());
	}
}
