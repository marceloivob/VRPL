package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;

public class QuadroResumoBCTest {

	@InjectMocks
	private QuadroResumoBC quadroResumoBC;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void historicoInvalido() {
		HistoricoLicitacaoDTO historicoNulo = null;

		assertThrows(IllegalArgumentException.class, () -> quadroResumoBC.processarNovoHistorico(historicoNulo));

		HistoricoLicitacaoDTO historicoInvalido = new HistoricoLicitacaoDTO();

		assertThrows(IllegalArgumentException.class, () -> quadroResumoBC.processarNovoHistorico(historicoInvalido));

	}

}
