package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.ArrayList;
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

public class ParecerVRPLSConstraintValidator {

	public void validate(LaudoDTO laudo) {
		if (!TipoDeParecerEnum.VRPLS.getId().equals(laudo.getTemplateFk())) {
			throw new IllegalArgumentException("Tipo de Parecer inválido: " + laudo.getTemplateFk()
					+ ". É esperado um Parecer do Tipo VRPLS para validar");
		}

		Map<LoteDTO, List<RespostaDTO>> mapaLoteRespostas = new HashMap<>();
		List<RespostaDTO> listaRespostasGrupo2 = new ArrayList<>();
		Map<LoteDTO, RespostaDTO> mapaComentariosLote = new HashMap<>();
		String comentariosGrupo2 = "";
		List<RespostaDTO> respostas = laudo.getRespostas();

		// indexando respostas
		for (RespostaDTO resposta : respostas) {
			PerguntaDTO pergunta = resposta.getPergunta();

			if (resposta.getLote() == null) {
				if (perguntaEhCampoParaComentarios(pergunta)) {
					comentariosGrupo2 = resposta.getResposta();

					validaTamanhoDoComentario(comentariosGrupo2, "Aspectos Gerais da Licitação");
				} else if (perguntaEhJustificativa(pergunta)) {
					validaTamanhoDaJustificativa(laudo.getJustificativa());
				} else {
					listaRespostasGrupo2.add(resposta);
				}
			} else {
				if (perguntaEhCampoParaComentarios(pergunta)) {
					mapaComentariosLote.put(resposta.getLote(), resposta);
				} else {
					if (!mapaLoteRespostas.containsKey(resposta.getLote())) {
						ArrayList<RespostaDTO> arrayrespostas = new ArrayList<>();
						arrayrespostas.add(resposta);
						mapaLoteRespostas.put(resposta.getLote(), arrayrespostas);

					} else {
						mapaLoteRespostas.get(resposta.getLote()).add(resposta);
					}
				}
			}
		}

		// validar se os comentários foram preenchidos,
		boolean comentarioObrigatorio = false;
		for (RespostaDTO resposta : listaRespostasGrupo2) {
			if (resposta.getResposta() != null) {

				if ("Não".equals(resposta.getResposta())) {
					comentarioObrigatorio = true;
				}
			}

		}
		if (comentarioObrigatorio && "".equals(comentariosGrupo2)) {
			throw new CampoDePreenchimentoObrigatorioException("Comentários", "da Seção Aspectos Gerais da Licitacao");
		}

		for (LoteDTO lote : mapaLoteRespostas.keySet()) {
			boolean comentarioLoteObrigatorio = false;
			
			//mapa criado para validar laudo
			Map<Long,String> mapaPerguntasRespostas = new HashMap<>();
			for (RespostaDTO resposta : mapaLoteRespostas.get(lote)) {
				mapaPerguntasRespostas.put(resposta.getPergunta().getPerguntaId(), resposta.getResposta());
			}

			for (RespostaDTO resposta : mapaLoteRespostas.get(lote)) {

				if ((resposta.getResposta() == null) || (resposta.getResposta().isEmpty())) {
					if (resposta.getPergunta().getPerguntaId().equals(16L)) {
						if(!mapaPerguntasRespostas.containsKey(15L) || "Não".equals(mapaPerguntasRespostas.get(15L))) {
							comentarioLoteObrigatorio = true;
						}
					} else {
						comentarioLoteObrigatorio = true;
					}
				} else if ("Não".equals(resposta.getResposta())) {
					if (!resposta.getPergunta().getPerguntaId().equals(15L)) {
						comentarioLoteObrigatorio = true;
					}  else if ("Não".equals(mapaPerguntasRespostas.get(16))) {
						comentarioLoteObrigatorio = true;
					}
				}
			}

			if (comentarioLoteObrigatorio) {
				RespostaDTO comentario = mapaComentariosLote.get(lote);

				if ((comentario.getResposta() == null) || (comentario.getResposta().isEmpty())) {
					throw new CampoDePreenchimentoObrigatorioException("Comentários", "do Lote " + lote.getNumero());
				}
			}

			validaTamanhoDoComentario(mapaComentariosLote.get(lote).getResposta(),
					"Aspectos Gerais do Lote " + lote.getNumero());

		}

	}

	private boolean perguntaEhJustificativa(PerguntaDTO pergunta) {
		return "Justificativa".equalsIgnoreCase(pergunta.getTitulo());
	}

	private void validaTamanhoDaJustificativa(RespostaDTO justificativa) {
		if (justificativa.getResposta().length() > 1500) {
			throw new TamanhoInvalidoException("Justificativa");
		}
	}

	private void validaTamanhoDoComentario(String comentario, String sessao) {
		if (comentario.length() > 1500) {
			throw new TamanhoInvalidoException("Comentário", sessao);
		}
	}

	private boolean perguntaEhCampoParaComentarios(PerguntaDTO pergunta) {
		return "Comentários".equalsIgnoreCase(pergunta.getTitulo());
	}

}
