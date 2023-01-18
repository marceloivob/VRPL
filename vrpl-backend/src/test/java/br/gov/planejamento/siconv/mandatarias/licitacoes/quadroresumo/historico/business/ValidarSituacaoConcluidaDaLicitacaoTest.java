package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.LicitacaoNaoEncontradaException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.LicitacaoNaoConcluidaException;
import br.gov.planejamento.siconv.mandatarias.test.core.MockClientLicitacoesInterface;

public class ValidarSituacaoConcluidaDaLicitacaoTest {

	@InjectMocks
	private ValidarSituacaoConcluidaDaLicitacao validarSituacaoConcluidaDaLicitacao;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);

		validarSituacaoConcluidaDaLicitacao = new ValidarSituacaoConcluidaDaLicitacao(
				new MockClientLicitacoesInterface());

	}

//	@DisplayName("Regra de Validação Número 1 - Nenhuma licitação encontrada para a proposta.")
//	@Test
//	public void enviaParaAnaliseLicitacaoNaoEncontrada() {
//		assertThrows(LicitacaoNaoEncontradaException.class,
//				() -> validarSituacaoConcluidaDaLicitacao.validarSeProcessoLicitatorioEstaConcluido(new LicitacaoBD()));
//	}

//	@DisplayName("Regra de Validação Número 1 - A situação da licitação (processo de execução) não pode ser diferente de Concluída")
//	@Test
//	public void enviaParaAnaliseLicitacaoNaoConcluida() {
//		// Preparação dos Dados do Cenário a ser testado
//		LicitacaoBD licitacao = new LicitacaoBD();
//		licitacao.setIdLicitacaoFk(5L);
//
//		// Execução do cenário
//		assertThrows(LicitacaoNaoConcluidaException.class,
//				() -> validarSituacaoConcluidaDaLicitacao.validarSeProcessoLicitatorioEstaConcluido(licitacao));
//	}

}
