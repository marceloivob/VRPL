package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.business;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.grupopergunta.entity.dto.GrupoPerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.dao.PerguntaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.database.PerguntaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.dao.PropostaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

public class PerguntaBC {

	@Inject
	private DAOFactory dao;

	@Inject
	private PoBC poBC;

	public PerguntaDTO recuperarPerguntaPorId(Long id) {
		PerguntaBD perguntaBD = dao.get(PerguntaDAO.class).recuperarPerguntaPorId(id);
		if (perguntaBD == null) {
			throw new PerguntaNaoEncontradoException();
		}

		return perguntaBD.converterParaDTO();
	}

	public List<PerguntaDTO> recuperarPerguntasPorGrupo(GrupoPerguntaDTO grupo, Long idLicitacao,
			Long idVersaoDaLicitacao) {
		List<PerguntaDTO> resultado = new ArrayList<>();

		List<PerguntaBD> listaBD = dao.get(PerguntaDAO.class).recuperarPerguntaPorGrupo(grupo.getGrupoId());
		for (PerguntaBD perguntaBD : listaBD) {

			if (perguntaTemTratamentoEspecial(perguntaBD)) {
				atualiza(perguntaBD, idLicitacao, idVersaoDaLicitacao);
			}

			PerguntaDTO p = perguntaBD.converterParaDTO();
			p.setGrupo(grupo);

			resultado.add(p);
		}

		return resultado;
	}

	private void atualiza(PerguntaBD perguntaBD, Long idLicitacao, Long idVersaoDaLicitacao) {
		LicitacaoBD licitacao = dao.get(LicitacaoDAO.class).findLicitacaoById(idLicitacao);
		List<PoVRPLDTO> pos = poBC.recuperarNovoPosPorLicitacao(licitacao);

		BigDecimal valorTotalDaPO = BigDecimal.ZERO;

		for (PoVRPLDTO poVRPLDTO : pos) {
			valorTotalDaPO = valorTotalDaPO.add( poVRPLDTO.getPrecoTotalLicitacao() );
		}

		var formatter = NumberFormat.getCurrencyInstance();

		try {
			String tituloAtualizado = String.format(perguntaBD.getTitulo(), formatter.format(valorTotalDaPO));

			perguntaBD.setTitulo(tituloAtualizado);
		} catch (IllegalArgumentException e) {
			//TODO retirar após identificar o erro de IllegalArgumentException quando a consulta recuperarSomatorioDosValoresDasPOsLicitadas retorna null

			LicitacaoBD licitacaoBD = dao.get(LicitacaoDAO.class).findLicitacaoByIdLicitacao(idLicitacao);

			PropostaBD propostaBD = dao.get(PropostaDAO.class).loadById(licitacaoBD.getIdentificadorDaProposta());

			var message = "Parâmetros consultados Proposta: "+propostaBD.getIdSiconv()+", idLicitacao: "+idLicitacao+" idVersaoDaLicitacao: "+idVersaoDaLicitacao;

			throw new IllegalArgumentException(message, e);
		}

	}

	private boolean perguntaTemTratamentoEspecial(PerguntaBD perguntaBD) {
		return perguntaEhDoParecerDeEngenharia(perguntaBD) || perguntaEhDoParecerSocial(perguntaBD);

	}

	private boolean perguntaEhDoParecerDeEngenharia(PerguntaBD perguntaBD) {
		// Grupo 04 == Aspectos da Análise do Lote, tem ID = 14
		final Long grupo04 = 14L;

		// Pergunta 02, do grupo 04 == O valor total do lote de %s é menor ou igual ao
		// orçamento utilizado para comparação?
		final Long pergunta02 = 2L;

		boolean perguntaTemTratamentoEspecial = perguntaBD.getGrupoFk().compareTo(grupo04) == 0;
		perguntaTemTratamentoEspecial = perguntaTemTratamentoEspecial
				&& perguntaBD.getNumero().compareTo(pergunta02) == 0;

		return perguntaTemTratamentoEspecial;
	}

	private boolean perguntaEhDoParecerSocial(PerguntaBD perguntaBD) {
		// Grupo 03 == Lote, tem ID = 22
		final Long grupo03 = 22L;

		// Pergunta 05, do grupo 03 == O valor total do lote de %s é menor ou igual ao
		// orçamento utilizado para comparação?
		final Long pergunta05 = 5L;

		boolean perguntaTemTratamentoEspecial = perguntaBD.getGrupoFk().compareTo(grupo03) == 0;
		perguntaTemTratamentoEspecial = perguntaTemTratamentoEspecial
				&& perguntaBD.getNumero().compareTo(pergunta05) == 0;

		return perguntaTemTratamentoEspecial;
	}

}
