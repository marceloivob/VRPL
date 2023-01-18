package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.dao.GrupoPerguntaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.database.GrupoPerguntaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.CampoDePreenchimentoObrigatorioException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.JustificarInviabilidadeException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business.exception.JustificativaObrigatoriadException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto.LaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.RegimeDeContratacaoDaLicitacaoEhRDCOuLei13303;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.ValidarSituacaoConcluidaDaLicitacao;

/**
 * RN 566904: SICONV-DocumentosOrcamentarios-Pareceres-RN-Validacao
 */
public class ParecerGeneralBusinessRuleValidator {

	@Inject
	private ValidarSituacaoConcluidaDaLicitacao regra06;

	@Inject
	private RegimeDeContratacaoDaLicitacaoEhRDCOuLei13303 regimeDeContratacao;

	@Inject
	private DAOFactory dao;

	public void validate(LaudoDTO laudo) {
		this.regra03(laudo);

		//this.regra04(laudo);

		this.regra05(laudo);

		this.regra06(laudo);
	}

	/***
	 * Regra 3
	 * <p>
	 * Não foi fornecida resposta para o item de conclusão: "Considera-se a
	 * verificação do resultado do processo licitatório"
	 * <p>
	 * Apresenta a mensagem: O campo "Considera-se a verificação do resultado do
	 * processo licitatório" deve ser preenchido!
	 */
	private void regra03(LaudoDTO laudo) {

		PerguntaDTO perguntaVialibilidadeVRPL = new PerguntaDTO();
		perguntaVialibilidadeVRPL.setTitulo("Considera-se a verificação do resultado do processo licitatório");

		Predicate<RespostaDTO> respostaValida = resposta -> resposta != null && resposta.getResposta() != null;
		Predicate<RespostaDTO> perguntaEhConsideraAViabilidadeDoVRPL = resposta -> resposta.getPergunta().getTitulo()
				.equals(perguntaVialibilidadeVRPL.getTitulo());

		Optional<RespostaDTO> resposta = laudo.getRespostas().stream()
				.filter(respostaValida.and(perguntaEhConsideraAViabilidadeDoVRPL)).findFirst();

		if (resposta.isPresent()) {
			RespostaDTO r = resposta.get();
			
			if ((r.getResposta() == null) ||(r.getResposta().isEmpty())) {
				throw new CampoDePreenchimentoObrigatorioException(perguntaVialibilidadeVRPL.getTitulo());	
			}
		} else {
			throw new CampoDePreenchimentoObrigatorioException(perguntaVialibilidadeVRPL.getTitulo());
		}
	}

	/**
	 * Regra 4
	 * <p>
	 * - Existe seção com a opção "Não se aplica" marcada;
	 * <p>
	 * - E não existe Justificativas na Conclusão.
	 * <p>
	 * Consequência: Apresenta a mensagem A justificativa é obrigatória devido à
	 * existência da opção \"Não se aplica\" marcada na(s) seção(ões) {0}.
	 */
	private void regra04(LaudoDTO laudo) {

		Predicate<RespostaDTO> respostaValida = resposta -> resposta != null && resposta.getResposta() != null;
		Predicate<RespostaDTO> respostaNaoSeAplica = resposta -> resposta.getResposta()
				.equalsIgnoreCase("Não se aplica");

		List<RespostaDTO> respostasNaoSeAplica = laudo.getRespostas().stream()
				.filter(respostaValida.and(respostaNaoSeAplica)).collect(Collectors.toList());

		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(laudo.getLicitacaoFk());

		if (!regimeDeContratacao.regimeDeContratacaoEhRDCOuLei13303(licitacao)) {
			Predicate<RespostaDTO> excecao = resposta -> resposta.getPergunta().getTitulo().equalsIgnoreCase(
					"Foi apresentada declaração de que o edital da licitação contempla todos os elementos necessários abaixo dispostos?");

			respostasNaoSeAplica.removeIf(excecao);
		}

		if (existeRespostaNaoSeAplica(respostasNaoSeAplica) && !laudo.justificativaFoiFornecida()) {
			List<String> secoes = new ArrayList<>();

			respostasNaoSeAplica.forEach(resposta -> {
				GrupoPerguntaBD grupo = dao.get(GrupoPerguntaDAO.class)
						.recuperarGrupoPerguntaPorId(resposta.getGrupo());

				secoes.add(grupo.getTitulo());

			});

			throw new JustificativaObrigatoriadException(secoes);
		}

	}



	/***
	 * Regra 5
	 * <p>
	 * A Conclusão seja "Inviável"
	 * <p>
	 * E não exista Justificativas na Conclusão.
	 * <p>
	 * Apresentar a mensagem: Justificar a causa de inviabilidade.
	 * 
	 */
	private void regra05(LaudoDTO laudo) {
		Predicate<RespostaDTO> respostaValida = resposta -> resposta != null && resposta.getResposta() != null;
		Predicate<RespostaDTO> inviavel = resposta -> resposta.getResposta().equalsIgnoreCase("Inviável");

		Optional<RespostaDTO> respostaInviavel = laudo.getRespostas().stream().filter(respostaValida.and(inviavel))
				.findFirst();

		if (respostaInviavel.isPresent() && !laudo.justificativaFoiFornecida()) {
			throw new JustificarInviabilidadeException();
		}
	}

	/**
	 * Regra 6
	 * <p>
	 * Se a situação da licitação (processo de execução) For diferente de
	 * "Concluída",
	 * <p>
	 * A situação do processo de execução será verificada através do Serviço:
	 * SICONV-Licitações.
	 * <p>
	 * Consequência: Apresentar a mensagem "A Licitação (processo de execução) (<<
	 * Numero da Licitação >>) não está "Concluída"
	 */
	private void regra06(LaudoDTO laudo) {
		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(laudo.getLicitacaoFk());

		regra06.validarSeProcessoLicitatorioEstaConcluido(licitacao);

	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// Métodos Auxiliares
	//////////////////////////////////////////////////////////////////////////////////////////

	private boolean existeRespostaNaoSeAplica(List<RespostaDTO> respostasNaoSeAplica) {
		boolean existe = respostasNaoSeAplica != null && !respostasNaoSeAplica.isEmpty();

		return existe;
	}

}
