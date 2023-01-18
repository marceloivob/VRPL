package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.rest;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.success;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ApplicationProperties.APPLICATION_JSON_UTF8;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.annotation.SimplyTimed;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.AnexoBC;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response.ResponseHelper.DefaultResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;

@SimplyTimed
@Path("/anexos")
public class AnexoRest {

	@Inject
	private AnexoBC anexoBC;

	@GET
	@Path("/{idLicitacao}/")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<AnexoDTO>> listarAnexos(@NotNull @PathParam("idLicitacao") Long idLicitacao) {
		List<AnexoDTO> anexos = anexoBC.listarAnexos(idLicitacao);
		return success(anexos);
	}

	@POST
	@Path("/{idLicitacao}")
	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<String> anexarArquivo(@NotNull @PathParam("idLicitacao") Long idLicitacao,
			@NotNull MultipartFormDataInput multipart) throws IOException {

		this.corrigeEncoding(multipart);

		InputStream arquivo = multipart.getFormDataPart("arquivo", InputStream.class, null);
		String nomeArquivo = multipart.getFormDataPart("nomeArquivo", String.class, null);
		String descricao = multipart.getFormDataPart("descricao", String.class, null);
		String tipoAnexo = multipart.getFormDataPart("tipoAnexo", String.class, null);
		Long tamanhoArquivo = multipart.getFormDataPart("tamanhoArquivo", Long.class, null);

		TipoAnexoEnum tipoDeAnexo = TipoAnexoEnum.valueOf(tipoAnexo);

		this.anexoBC.anexarArquivo(idLicitacao, nomeArquivo, descricao, tipoDeAnexo, arquivo, tamanhoArquivo);

		return success("ok");
	}

	@PUT
	@Path("/{idAnexo}")
	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<String> atualizarAnexo(@NotNull @PathParam("idAnexo") Long idAnexo,
			@NotNull MultipartFormDataInput multipart) throws IOException {

		this.corrigeEncoding(multipart);

		InputStream arquivo = multipart.getFormDataPart("arquivo", InputStream.class, null);
		String nomeArquivo = multipart.getFormDataPart("nomeArquivo", String.class, null);
		String descricao = multipart.getFormDataPart("descricao", String.class, null);
		String tipoAnexo = multipart.getFormDataPart("tipoAnexo", String.class, null);
		Long tamanhoArquivo = multipart.getFormDataPart("tamanhoArquivo", Long.class, null);
		Long versao = multipart.getFormDataPart("versao", Long.class, null);

		TipoAnexoEnum tipoDeAnexo = TipoAnexoEnum.valueOf(tipoAnexo);

		this.anexoBC.atualizarAnexo(idAnexo, nomeArquivo, descricao, tipoDeAnexo, versao, arquivo, tamanhoArquivo);

		return success("ok");
	}

	private void corrigeEncoding(MultipartFormDataInput multipart) {
		for (Map.Entry<String, List<InputPart>> entry : multipart.getFormDataMap().entrySet()) {
			for (InputPart part : entry.getValue()) {
				part.setMediaType(part.getMediaType().withCharset("utf-8"));
			}
		}
	}

	@DELETE
	@Path("/{idAnexo}/")
	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<String> apagarAnexo(@NotNull @PathParam("idAnexo") Long idAnexo) {
		anexoBC.apagarAnexo(idAnexo);
		return success("ok");
	}

	@GET
	@Path("/tipos")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<Map<String, String>> listarTiposDeAnexos() {
		Map<String, String> unsortMap = new HashMap<>();

		for (TipoAnexoEnum tipo : TipoAnexoEnum.values()) {
			unsortMap.put(tipo.name(), tipo.getDescription());
		}

		Map<String, String> sortedMap = unsortMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));

		return success(sortedMap);
	}

	@GET
	@Path("/{idLicitacao}/{tipoAnexo}")
	@Produces(APPLICATION_JSON_UTF8)
	public DefaultResponse<List<AnexoDTO>> listarAnexosPorTipo(@NotNull @PathParam("idLicitacao") Long idLicitacao,
			@NotNull @PathParam("tipoAnexo") String tipoAnexo) {
		List<AnexoDTO> anexos = anexoBC.listarAnexosPorTipo(idLicitacao, TipoAnexoEnum.valueOf(tipoAnexo));
		return success(anexos);
	}

}
