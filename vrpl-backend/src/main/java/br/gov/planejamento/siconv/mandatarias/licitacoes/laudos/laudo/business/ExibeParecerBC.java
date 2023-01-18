package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.business;

import java.util.Objects;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.dao.LaudoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;

/**
 * 567920 -
 * SICONV-DocumentosOrcamentarios-PareceresVRPL-RN-PoliticaEdicaoVisualizacaoPareceres
 * 
 */
public class ExibeParecerBC {

	@Inject
	private DAOFactory daoFactory;

	private LicitacaoBD licitacao;
	private SiconvPrincipal usuarioLogado;

	public ExibeParecerBC dadaALicitacao(LicitacaoBD licitacao) {
		Objects.requireNonNull(licitacao, "É necessário indicar uma Licitação");
		Objects.requireNonNull(licitacao.getId(), "É necessário indicar uma Licitação");

		this.licitacao = licitacao;

		return this;
	}

	public ExibeParecerBC eOUsuario(SiconvPrincipal usuarioLogado) {
		Objects.requireNonNull(usuarioLogado, "É necessário fornecer o Usuario Logado");

		this.usuarioLogado = usuarioLogado;

		return this;
	}

	public Boolean devoExibirOParecer() {
		Objects.requireNonNull(licitacao, "É necessário indicar uma Licitação");
		Objects.requireNonNull(usuarioLogado, "É necessário fornecer um Usuario Logado");

		if (this.usuarioLogado.isAcessoLivre() || this.usuarioLogado.isGuest()) {
			if (existeParecerEmitido()) {
				return true;
			} else {
				return false;
			}
		}

		if (deveExibirParecerParaProponente()) {
			return true;
		}

		if (this.usuarioLogado.isConcedente() || this.usuarioLogado.isMandataria()) {
			return true;
		}

		return false;
	}

	protected boolean deveExibirParecerParaProponente() {
		SituacaoLicitacaoEnum situacaoDaLicitacao = SituacaoLicitacaoEnum.fromSigla(licitacao.getSituacaoDaLicitacao());

		Boolean usuarioEhProponente = this.usuarioLogado.isProponente();
		Boolean licitacaoAceita = (situacaoDaLicitacao == SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA) || 
				                  (situacaoDaLicitacao == SituacaoLicitacaoEnum.REJEITADA);
		
		Boolean existeParecerEmitido = existeParecerEmitido();

		Boolean deveExibir = usuarioEhProponente && (licitacaoAceita || existeParecerEmitido);

		return deveExibir;

	}

	public Boolean existeParecerEmitido() {
		Boolean existeParecerEmitido = this.getLaudoDAO()
				.existeAlgumParecerEmitidoParaALicitacao(licitacao.getId());

		return existeParecerEmitido;
	}

	protected LaudoDAO getLaudoDAO() {
		return this.daoFactory.get(LaudoDAO.class);
	}

	public void setDaoFactory(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}


}
