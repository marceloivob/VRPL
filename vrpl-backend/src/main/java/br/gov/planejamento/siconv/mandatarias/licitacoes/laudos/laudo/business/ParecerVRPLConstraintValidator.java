package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.CampoDePreenchimentoObrigatorioException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.TamanhoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.TipoDeParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;

public class ParecerVRPLConstraintValidator {

	public void validate(LaudoDTO laudo) {

		if (!TipoDeParecerEnum.VRPL.getId().equals(laudo.getTemplateFk())) {
			throw new IllegalArgumentException("Tipo de Parecer inválido: " + laudo.getTemplateFk()
					+ ". É esperado um Parecer do Tipo VRPL para validar");
		}

		Map<LoteDTO, Boolean> mapaComentariosOrcamento = new HashMap<>();
		Map<LoteDTO, Boolean> mapaComentariosCronograma = new HashMap<>();
		List<RespostaDTO> respostas = laudo.getRespostas();

		for (RespostaDTO respostaDTO : respostas) {

			if (respostaDTO.getLote() == null) {
				if (laudo.justificativaFoiFornecida()) {
					validaTamanhoDaJustificativa(laudo.getJustificativa());
				}
			} else {
				if (mapaComentariosOrcamento.get(respostaDTO.getLote()) == null) {
					if ("Não".equals(respostaDTO.getResposta())) {
						mapaComentariosOrcamento.put(respostaDTO.getLote(), false);
					}
					
					
				}

				if (mapaComentariosCronograma.get(respostaDTO.getLote()) == null) {
					if ("Não".equals(respostaDTO.getResposta())) {
						mapaComentariosCronograma.put(respostaDTO.getLote(), false);
					}
					
					
				}

				if (perguntaEhValorTotalDoLoteEhMenorOuIgualAOrcamentoUtilizadoNaComparacao(respostaDTO)) {
					if ("Não".equals(respostaDTO.getResposta())) {
						mapaComentariosOrcamento.put(respostaDTO.getLote(), true);
					}
				}

				if (perguntaEhPrecoUnitarioCadaItemDoOrcamentoEhMenorOuIgualAOrcamentoUtilizadoNacomparacao(
						respostaDTO)) {
					if ("Não".equals(respostaDTO.getResposta())) {
						mapaComentariosOrcamento.put(respostaDTO.getLote(), true);
					}
				}

				if (perguntaEhCronogramaCumpreAsExigenciasDoPrograma(respostaDTO)) {
					if ("Não".equals(respostaDTO.getResposta())) {
						mapaComentariosCronograma.put(respostaDTO.getLote(), true);
					}
				}
			}

			validarCampoComentario(respostaDTO, mapaComentariosOrcamento, mapaComentariosCronograma);
		}

	}

	private void validaTamanhoDaJustificativa(RespostaDTO justificativa) {
		if (justificativa.getResposta().length() > 1500) {
			throw new TamanhoInvalidoException("Justificativa");
		}
	}

	private void validaTamanhoDoComentario(String comentario, String secao) {
		if (comentario != null && comentario.length() > 1500) {
			throw new TamanhoInvalidoException("Comentários", secao);
		}
	}

	private boolean perguntaEhValorTotalDoLoteEhMenorOuIgualAOrcamentoUtilizadoNaComparacao(RespostaDTO respostaDTO) {
		return respostaDTO.getPergunta().getTitulo().equalsIgnoreCase(
				"O valor total do lote de %s é menor ou igual ao orçamento utilizado para comparação?");
	}

	private boolean perguntaEhPrecoUnitarioCadaItemDoOrcamentoEhMenorOuIgualAOrcamentoUtilizadoNacomparacao(
			RespostaDTO respostaDTO) {
		return respostaDTO.getPergunta().getTitulo().equalsIgnoreCase(
				"O preço unitário de cada item do orçamento da empresa vencedora da licitação é menor ou igual ao orçamento utilizado para comparação?");
	}

	private boolean perguntaEhCronogramaCumpreAsExigenciasDoPrograma(RespostaDTO respostaDTO) {
		return respostaDTO.getPergunta().getTitulo().equalsIgnoreCase(
				"O cronograma cumpre as exigências do Programa (prazo máximo de construção, percentual mínimo nas últimas parcelas, etc.)?");
	}

	private void validarCampoComentario(RespostaDTO respostaDTO, Map<LoteDTO, Boolean> mapaComentariosOrcamento,
			Map<LoteDTO, Boolean> mapaComentariosCronograma) {

		PerguntaDTO pergunta = respostaDTO.getPergunta();
		LoteDTO lote = respostaDTO.getLote();
		String comentario = respostaDTO.getResposta();

		if (pergunta != null && lote != null && comentarioEhDoOrcamento(pergunta) && mapaComentariosOrcamento.containsKey(lote) && mapaComentariosOrcamento.get(lote)) {
			String tituloDaPergunta = pergunta.getTitulo();

			String mensagem = String.format("da Seção Orçamento do Lote %d ", lote.getNumero());

			if ("Não".equals(respostaDTO.getResposta()) && (comentario == null || comentario.trim().isEmpty())) {
				throw new CampoDePreenchimentoObrigatorioException(tituloDaPergunta, mensagem);
			}

			validaTamanhoDoComentario(comentario, mensagem);

		} else if (pergunta != null && lote != null  && comentarioEhDoCronograma(pergunta) && mapaComentariosCronograma.containsKey(lote) && mapaComentariosCronograma.get(lote)) {
			String tituloDaPergunta = pergunta.getTitulo();

			String mensagem = String.format("da Seção Cronograma Físico-Financeiro do Lote %d", lote.getNumero());

			if ("Não".equals(respostaDTO.getResposta()) && (comentario == null || comentario.trim().isEmpty())) {
				throw new CampoDePreenchimentoObrigatorioException(tituloDaPergunta, mensagem);
			}

			validaTamanhoDoComentario(comentario, mensagem);
		}
	}

	private boolean comentarioEhDoOrcamento(PerguntaDTO pergunta) {
		return pergunta.getNumero().equals(4L);
	}

	private boolean comentarioEhDoCronograma(PerguntaDTO pergunta) {
		return pergunta.getNumero().equals(7L);
	}

}
