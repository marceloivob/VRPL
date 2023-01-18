package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.business.MacroServicoParcelaBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.CFFSemEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.cffparcela.entity.dto.MacroServicoCFFDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.business.EventoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventos.entity.dto.EventoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.business.EventoFrenteObraBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.CFFComEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.eventosfrenteobras.entity.dto.EventoCFFcomEventosDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.business.FrenteObraBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.dto.FrenteObraDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.bc.PLQBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.plq.entity.dto.PLQDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.business.PoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoDetalhadaVRPLDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto.PoVRPLDTO;
import io.swagger.annotations.ApiOperation;

@SimplyTimed
@Path("/po")
public class PoRest {

	@Inject
	private PoBC poBC;

	@Inject
	private EventoBC eventoBC;

	@Inject
	private FrenteObraBC frenteDeObraBC;

	@Inject
	private MacroServicoParcelaBC macroServicoParcelaBC;

	@Inject
	private EventoFrenteObraBC eventoFrenteObraBC;

	@Inject
	private PLQBC plqBC;

	@GET
	@Path("/{idPo}")
	@Produces(APPLICATION_JSON_UTF8)
	@ApiOperation(value = "Recupera PO por id")
	public DefaultResponse<PoDetalhadaVRPLDTO> findPoDetalhada(@PathParam("idPo") Long idPo) {
		PoDetalhadaVRPLDTO po = poBC.findPoDetalhada(idPo);

		return success(po);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@ApiOperation(value = "Recupera a PO da Proposta vinculada ao usuário logado")
	public DefaultResponse<List<PoVRPLDTO>> find(@QueryParam("idLicitacao") Long idLicitacao) {

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setId(idLicitacao);

		List<PoVRPLDTO> pos = poBC.recuperarNovoPosPorLicitacao(licitacao);

		return success(pos);
	}

	@POST
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Inclusão/alteração de PO")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> include(PoDetalhadaVRPLDTO po) {

		poBC.alterar(po);

		return success("OK");
	}

	@POST
	@Path("/database")
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Inclusão/alteração de PO")
	public DefaultResponse<String> validarDataBase(PoDetalhadaVRPLDTO po) {
		poBC.validarDataBasePo(po);
		return success("OK");
	}


	@POST
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Inclusão/alteração de Evento da PO")
	@Path("/{idPO}/evento")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> salvarEvento(@PathParam("idPO") Long idPO, EventoDTO evento) {
		evento.setIdPO(idPO);

		if (evento.getId() == null) {
			eventoBC.inserir(evento);
		} else {
			eventoBC.alterar(evento);
		}

		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consultar eventos de uma PO")
	@Path("/{idPO}/eventos/")
	public DefaultResponse<List<EventoDTO>> consultarListaEventos(@PathParam("idPO") Long idPO) {

		// TODO idPOAnalise é obrigatorio
		List<EventoDTO> eventos = eventoBC.consultarListaEventos(idPO);
		return success(eventos);
	}

	/**
	 * remove um Evento
	 *
	 * @param idEvento - id do evento
	 */
	@DELETE
	@Path("/{idPO}/evento/")
	@Produces(APPLICATION_JSON_UTF8)
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> deleteEvento(@QueryParam("idEvento") Long idEvento,
			@QueryParam("versao") Long versao) {
		eventoBC.excluir(idEvento, versao);
		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera um Evento")
	@Path("/{idPO}/evento/")
	public DefaultResponse<EventoDTO> consultarEventoPorId(@QueryParam("idEvento") Long idEvento) {

		EventoDTO evento = eventoBC.recuperarEventoPorId(idEvento);
		return success(evento);
	}

	/**
	 * Implementa a RN: 511405 -
	 * SICONV-DocumentosOrcamentarios-ManterEvento-RN-FormularioInclusaoAlteracao
	 * <p>
	 * Valor associado à ordem de exibição de um evento.
	 * </p>
	 * <p>
	 * O número do evento é sequencial, editável e virá preenchido com o próximo
	 * valor a partir do último evento já cadastrado (último número + 1). Caso seja
	 * o primeiro evento a ser incluído, receberá o valor 1 como sugestão do
	 * sistema.
	 * </p>
	 * <p>
	 * Não é permitida a repetição de números. Possibilidade do usuário informar um
	 * valor que denote uma ordem mais conveniente.
	 * </p>
	 *
	 * @param idPO Identificador da Planilha Orçamentária
	 */
	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera o sequencial de um Evento")
	@Path("/{idPO}/evento/sequencial/")
	public DefaultResponse<Long> recuperarSequencialEvento(@PathParam("idPO") Long idPO) {

		Long sequencial = eventoBC.recuperarValorSequencialEvento(idPO);

		return success(sequencial);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Consultar frentes de obra de uma PO")
	@Path("/{idPO}/frentes/")
	public DefaultResponse<List<FrenteObraDTO>> consultarFrentesDeObra(@PathParam("idPO") Long idPO) {

		List<FrenteObraDTO> frentes = frenteDeObraBC.consultarListaFrentesDeObra(idPO);
		return success(frentes);
	}

	@POST
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Inclusão/alteração de Frente de Obra da PO")
	@Path("/{idPO}/frente")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> salvarFrenteDeObra(@PathParam("idPO") Long idPO, FrenteObraDTO frente) {
		frente.setIdPO(idPO);

		if (frente.getId() == null) {
			frenteDeObraBC.inserir(frente);
		} else {
			frenteDeObraBC.alterar(frente);
		}

		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera o sequencial de uma Frente de Obra")
	@Path("/{idPO}/frente/sequencial/")
	public DefaultResponse<Long> recuperarSequencialFrenteDeObra(@PathParam("idPO") Long idPO) {

		Long sequencial = frenteDeObraBC.recuperarValorSequencialFrenteDeObra(idPO);

		return success(sequencial);
	}

	@DELETE
	@Path("/{idPO}/frente/")
	@Produces(APPLICATION_JSON_UTF8)
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> deleteFrenteDeObra(@QueryParam("idFrenteDeObra") Long idFrenteDeObra,
			@QueryParam("versao") Long versao) {

		FrenteObraBD frenteDeObra = new FrenteObraBD();
		frenteDeObra.setId(idFrenteDeObra);
		frenteDeObra.setVersao(versao);

		frenteDeObraBC.excluir(frenteDeObra);
		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera uma Frente de Obra")
	@Path("/{idPO}/frente/")
	public DefaultResponse<FrenteObraDTO> consultarFrenteDeObraPorId(
			@QueryParam("idFrenteDeObra") Long idFrenteDeObra) {

		FrenteObraBD frenteDeObra = new FrenteObraBD();
		frenteDeObra.setId(idFrenteDeObra);

		FrenteObraDTO frente = frenteDeObraBC.recuperarFrenteObraPorId(frenteDeObra);
		return success(frente);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera cff sem eventos")
	@Path("/{idPO}/cffsemeventos/")
	public DefaultResponse<CFFSemEventosDTO> consultarCFFSemEventosPeloIdPO(@PathParam("idPO") Long idPO) {

		CFFSemEventosDTO cff = macroServicoParcelaBC.recuperarCFFPorIdPO(idPO);
		return success(cff);
	}

	@PUT
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Alterar Parcelas")
	@Path("/{idPO}/cffsemeventos/{idMacroServico}")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> alterarParcelasMacroservico(@PathParam("idPO") Long idPO,
			@PathParam("idMacroServico") Long idMacroServico, MacroServicoCFFDTO macroServicoCffDTO) {

		macroServicoParcelaBC.alterarParcelas(macroServicoCffDTO);
		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera cff com eventos")
	@Path("/{idPO}/cffcomeventos/")
	public DefaultResponse<CFFComEventosDTO> consultarCFFComEventosPeloIdPO(@PathParam("idPO") Long idPO) {

		CFFComEventosDTO cff = eventoFrenteObraBC.recuperarCFFPorIdPO(idPO);
		return success(cff);
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera frente obra de um Evento ")
	@Path("/{idPO}/cffcomeventos/frentesobra/{idEvento}")
	public DefaultResponse<EventoCFFcomEventosDTO> consultarFrentesObradeEventoPeloIdPOIdEvento(
			@PathParam("idPO") Long idPO, @PathParam("idEvento") Long idEvento) {

		EventoCFFcomEventosDTO eventoCFF = eventoFrenteObraBC.recuperarFrenteObrasEvento(idEvento);
		return success(eventoCFF);
	}

	@PUT
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Alterar Parcelas")
	@Path("/{idPO}/cffcomeventos/{idEvento}")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> alterarMesesEventosFrenteObra(@PathParam("idPO") Long idPO,
			@PathParam("idEvento") Long idEvento, EventoCFFcomEventosDTO eventoCFFDTO) {

		eventoFrenteObraBC.alterarEventosFrenteObra(eventoCFFDTO);
		return success("OK");
	}

	@PUT
	@Produces(APPLICATION_JSON_UTF8)
	@ApiOperation(value = "Alterar Referencia da PO")
	@Path("/{idPO}/")
	@AccessAllowed(value = { PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE })
	public DefaultResponse<String> alterarReferenciaPo(@PathParam("idPO") Long idPO,
			@QueryParam("referencia") String referencia, @QueryParam("versao") Long versao) {

		poBC.alterarReferenciaPo(idPO, referencia, versao);
		return success("OK");
	}

	@GET
	@Produces(APPLICATION_JSON_UTF8)
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Recupera plq de uma po ")
	@Path("/{idPO}/plq/")
	public DefaultResponse<PLQDTO> consultarPLQPo(@PathParam("idPO") Long idPO) {

		PLQDTO plqDTO = plqBC.recuperarPLQ(idPO);
		return success(plqDTO);
	}

}
