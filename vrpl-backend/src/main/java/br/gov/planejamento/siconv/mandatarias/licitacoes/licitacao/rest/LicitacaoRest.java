package br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.IgnorePrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.LicitacaoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.Permissoes;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.business.exception.ProcessoExecucaoNaoEncontradoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.AssociacaoLicitacaoLoteDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LicitacaoDetalhadaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.PermissaoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.business.PropostaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;

@SimplyTimed
@Path("/licitacao")
public class LicitacaoRest {

	@Inject
	private LicitacaoBC licitacaoBC;

	@Inject
	private PropostaBC propostaBC;

	@Inject
	private Permissoes consultarPermissoes;

	@Inject
	private SiconvPrincipal usuarioLogado;

	@GET
	@Path("/ativa/{versaoSelecionadaDaProposta}")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<LicitacaoDTO>> licitacoesAtivasDaProposta(
			@PathParam("versaoSelecionadaDaProposta") Long versaoSelecionadaDaProposta, @Context Request request) {

		PropostaBD propostaBD = propostaBC.recuperarProposta(usuarioLogado, versaoSelecionadaDaProposta);

		List<LicitacaoDTO> licitacoes = licitacaoBC.licitacoesDaProposta(propostaBD, true);

		return success(licitacoes);
	}

	@GET
	@Path("/rejeitada/{versaoSelecionadaDaProposta}")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<LicitacaoDTO>> licitacoesDaRejeitadasDaProposta(
			@PathParam("versaoSelecionadaDaProposta") Long versaoSelecionadaDaProposta, @Context Request request) {

		PropostaBD propostaBD = propostaBC.recuperarProposta(usuarioLogado, versaoSelecionadaDaProposta);

		List<LicitacaoDTO> licitacoes = licitacaoBC.licitacoesDaProposta(propostaBD, false);

		return success(licitacoes);

	}

	@POST
	@Path("/{idLicitacao}/lotes/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(APPLICATION_JSON_UTF8)
	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> salvarLotesAssociados(AssociacaoLicitacaoLoteDTO dadosAssociar) {

		this.licitacaoBC.salvarLotesAssociados(dadosAssociar);

		return success("ok");
	}

	@DELETE
	@Path("/{idLicitacao}/lotes")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(APPLICATION_JSON_UTF8)
	@AccessAllowed(value = { PROPONENTE }, roles = { Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE,
			Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> removerLotesAssociados(@PathParam("idLicitacao") Long identificadorDaLicitacao) {

		this.licitacaoBC.removeLotesLicitacao(identificadorDaLicitacao);

		return success("ok");
	}

	@GET
	@Path("/{idLicitacao}/detalhada")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<LicitacaoDetalhadaDTO> licitacao(@PathParam("idLicitacao") Long identificadorDaLicitacao) {

		LicitacaoDetalhadaDTO dto = this.licitacaoBC.recuperarLicitacao(identificadorDaLicitacao);

		return success(dto);
	}

	@GET
	@IgnorePrincipal
	@Path("/permissoes/{idProposta}")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<PermissaoDTO>> consultarPermissoesDasLicitacoesDaProposta(
			@PathParam("idProposta") Long identificadorDaProposta) {

		// Verifica Pré-Condição
		verificaSePropostaExiste(identificadorDaProposta);

		List<PermissaoDTO> licitacoes = this.consultarPermissoes
				.consultarPermissoesDasLicitacoesDaProposta(identificadorDaProposta);

		if (licitacoes.isEmpty()) {
			throw new ProcessoExecucaoNaoEncontradoException(identificadorDaProposta);
		}

		return success(licitacoes);
	}

	protected void verificaSePropostaExiste(Long identificadorDaProposta) {
		// O serviço é aberto, ou seja, não possui controle de acesso implementado com
		// um Token JWT e a consulta espera um SiconvPrincipal para obter o IdDaProposta
		// deste token
		SiconvPrincipal usuarioFake = criaUsuarioFake(identificadorDaProposta);

		// Se proposta não for encontrada, será lançada a exceção
		// PropostaNotFoundException
		this.propostaBC.recuperarProposta(usuarioFake, null);
	}

	private SiconvPrincipal criaUsuarioFake(Long identificadorDaProposta) {
		return new SiconvPrincipal() {

			@Override
			public Long getIdProposta() {
				return identificadorDaProposta;
			}

			@Override
			public Long getId() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getCpf() {
				return null;
			}

			@Override
			public String getCodigoEnte() {
				return null;
			}

			@Override
			public String getToken() {
				return null;
			}

			@Override
			public List<Profile> getProfiles() {
				return null;
			}

			@Override
			public List<String> getRoles() {
				return null;
			}

			@Override
			public boolean isPropostaDoUsuario(Long idProposta) {
				return false;
			}

		};
	}

	@GET
	@Path("/sincroniza-licitacoes/")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<LicitacaoDTO>> sincronizaLicitacoes() {
		List<LicitacaoDTO> licitacoes = licitacaoBC.recuperarLicitacoesSincronizadasComSICONV();

		return success(licitacoes);
	}


}
