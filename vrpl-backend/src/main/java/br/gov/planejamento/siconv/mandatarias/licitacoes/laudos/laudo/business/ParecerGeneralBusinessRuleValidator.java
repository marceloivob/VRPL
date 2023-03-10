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
	 * N??o foi fornecida resposta para o item de conclus??o: "Considera-se a
	 * verifica????o do resultado do processo licitat??rio"
	 * <p>
	 * Apresenta a mensagem: O campo "Considera-se a verifica????o do resultado do
	 * processo licitat??rio" deve ser preenchido!
	 */
	private void regra03(LaudoDTO laudo) {

		PerguntaDTO perguntaVialibilidadeVRPL = new PerguntaDTO();
		perguntaVialibilidadeVRPL.setTitulo("Considera-se a verifica????o do resultado do processo licitat??rio");

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
	 * - Existe se????o com a op????o "N??o se aplica" marcada;
	 * <p>
	 * - E n??o existe Justificativas na Conclus??o.
	 * <p>
	 * Consequ??ncia: Apresenta a mensagem A justificativa ?? obrigat??ria devido ??
	 * exist??ncia da op????o \"N??o se aplica\" marcada na(s) se????o(??es) {0}.
	 */
	private void regra04(LaudoDTO laudo) {

		Predicate<RespostaDTO> respostaValida = resposta -> resposta != null && resposta.getResposta() != null;
		Predicate<RespostaDTO> respostaNaoSeAplica = resposta -> resposta.getResposta()
				.equalsIgnoreCase("N??o se aplica");

		List<RespostaDTO> respostasNaoSeAplica = laudo.getRespostas().stream()
				.filter(respostaValida.and(respostaNaoSeAplica)).collect(Collectors.toList());

		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(laudo.getLicitacaoFk());

		if (!regimeDeContratacao.regimeDeContratacaoEhRDCOuLei13303(licitacao)) {
			Predicate<RespostaDTO> excecao = resposta -> resposta.getPergunta().getTitulo().equalsIgnoreCase(
					"Foi apresentada declara????o de que o edital da licita????o contempla todos os elementos necess??rios abaixo dispostos?");

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
	 * A Conclus??o seja "Invi??vel"
	 * <p>
	 * E n??o exista Justificativas na Conclus??o.
	 * <p>
	 * Apresentar a mensagem: Justificar a causa de inviabilidade.
	 * 
	 */
	private void regra05(LaudoDTO laudo) {
		Predicate<RespostaDTO> respostaValida = resposta -> resposta != null && resposta.getResposta() != null;
		Predicate<RespostaDTO> inviavel = resposta -> resposta.getResposta().equalsIgnoreCase("Invi??vel");

		Optional<RespostaDTO> respostaInviavel = laudo.getRespostas().stream().filter(respostaValida.and(inviavel))
				.findFirst();

		if (respostaInviavel.isPresent() && !laudo.justificativaFoiFornecida()) {
			throw new JustificarInviabilidadeException();
		}
	}

	/**
	 * Regra 6
	 * <p>
	 * Se a situa????o da licita????o (processo de execu????o) For diferente de
	 * "Conclu??da",
	 * <p>
	 * A situa????o do processo de execu????o ser?? verificada atrav??s do Servi??o:
	 * SICONV-Licita????es.
	 * <p>
	 * Consequ??ncia: Apresentar a mensagem "A Licita????o (processo de execu????o) (<<
	 * Numero da Licita????o >>) n??o est?? "Conclu??da"
	 */
	private void regra06(LaudoDTO laudo) {
		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(laudo.getLicitacaoFk());

		regra06.validarSeProcessoLicitatorioEstaConcluido(licitacao);

	}

	//////////////////////////////////////////////////////////////////////////////////////////
	// M??todos Auxiliares
	//////////////////////////////////////////////////////////////////////////////////////////

	private boolean existeRespostaNaoSeAplica(List<RespostaDTO> respostasNaoSeAplica) {
		boolean existe = respostasNaoSeAplica != null && !respostasNaoSeAplica.isEmpty();

		return existe;
	}

}
