package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.CampoDePreenchimentoObrigatorioException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.ComentarioObrigatorioParaSecao;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;

/**
 * RN 566904: SICONV-DocumentosOrcamentarios-Pareceres-RN-Validacao
 */
public class ParecerVRPLSBusinessRuleValidator {

	public void validate(LaudoDTO laudo) {
		this.regra02(laudo);

	}

	/**
	 * Para Parecer de Trabalho Social:
	 * <p>
	 * - Existe seção em que a resposta não esteja de acordo com o esperado,
	 * conforme regra de negócio: 566891:
	 * SICONV-DocumentosOrcamentarios-ManterVRPL-RN-Formulario
	 * <p>
	 * E não existe o Comentário na respectiva seção.
	 * <p>
	 * Consequência: O comentário é obrigatório na seção <descrição da seção>, pela
	 * existência de resposta(s) que não está(estão) de acordo com o esperado.
	 */
	private void regra02(LaudoDTO laudo) {

		List<RespostaDTO> respostas = laudo.getRespostas();

		for (RespostaDTO respostaDTO : respostas) {

			if (perguntaPossuiTratamentoEspecial(respostaDTO)) {
				validaPerguntaEspecial(laudo, respostaDTO);
			} else {
				validaPerguntaSemTratamentoEspecial(laudo, respostaDTO);
			}
		}

	}

	protected void validaPerguntaSemTratamentoEspecial(LaudoDTO laudo, RespostaDTO respostaDTO) {
		PerguntaDTO pergunta = respostaDTO.getPergunta();

		if (respostaFornecidaEhDiferenteDaEsperadaParaAPergunta(respostaDTO, pergunta)) {
			if (naoExisteComentario(respostaDTO, laudo)) {
				throw new ComentarioObrigatorioParaSecao("Aspectos Gerais do Lote");
			} 
//			else {
////				throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
//			}
		}
	}

	private void validaPerguntaEspecial(LaudoDTO laudo, RespostaDTO respostaDTO) {
		PerguntaDTO perguntaEspecial = new PerguntaDTO();
		perguntaEspecial.setTitulo("O percentual global das Despesas Indiretas atende aos parâmetros estabelecidos ?");

		PerguntaDTO perguntaEspecial1 = new PerguntaDTO();
		perguntaEspecial1.setTitulo("Considera-se a verificação do resultado do processo licitatório");

		if (respostaDTO.getPergunta().getTitulo().equals(perguntaEspecial.getTitulo())) {

			if (respostaDTO.getResposta().equals("Não") || 
					respostaDTO.getResposta() == null || "".equals(respostaDTO.getResposta()) ) {

				PerguntaDTO perguntaDependente = new PerguntaDTO();
				perguntaDependente
						.setTitulo("Em caso negativo, foi apresentado relatório técnico circunstanciado e aceito ?");

				Optional<RespostaDTO> respostaNegativa = laudo.getRespostas().stream()
						.filter(resposta -> resposta.getPergunta().getTitulo().equals(perguntaDependente.getTitulo()))
						.findFirst();

				if (respostaNegativa.isPresent()) {
					RespostaDTO valor = respostaNegativa.get();

					if ((valor.getResposta() == null) || (valor.getResposta().isEmpty())) {
						throw new CampoDePreenchimentoObrigatorioException(perguntaDependente.getTitulo());
					}

				} else {
					throw new CampoDePreenchimentoObrigatorioException(perguntaDependente.getTitulo());
				}
			}
		} else if (respostaDTO.getPergunta().getTitulo().equals(perguntaEspecial1.getTitulo())) {
			PerguntaDTO pergunta = respostaDTO.getPergunta();

			if ((respostaDTO.getResposta() == null) || (respostaDTO.getResposta().isEmpty())) {
				throw new CampoDePreenchimentoObrigatorioException(pergunta.getTitulo());
			}
		}

	}

	/**
	 * Defeito 1925279
	 */
	private boolean perguntaPossuiTratamentoEspecial(RespostaDTO respostaDTO) {
		String perguntaEspecial = "O percentual global das Despesas Indiretas atende aos parâmetros estabelecidos ?";
		String perguntaEspecial1 = "Em caso negativo, foi apresentado relatório técnico circunstanciado e aceito ?";
		String perguntaEspecial2 = "Considera-se a verificação do resultado do processo licitatório";

		List<String> perguntasEspeciais = Arrays.asList(perguntaEspecial, perguntaEspecial1, perguntaEspecial2);

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
