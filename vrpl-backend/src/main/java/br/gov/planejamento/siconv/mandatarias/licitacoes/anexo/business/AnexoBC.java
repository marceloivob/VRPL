package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business;

import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.CONCEDENTE;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.MANDATARIA;
import static br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile.PROPONENTE;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.Handle;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.AnexoNotFoundException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.ArquivoAnexoSemExtensaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.ArquivoDuplicadoMesmaDescricaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.ArquivoDuplicadoMesmoNomeException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.FormatoArquivoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.LicitacaoAnexoNotFoundException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business.exception.TamanhoMaximoArquivoInvalidoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.dao.AnexoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.AnexoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.entity.database.TipoAnexoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.RefreshRowPolice;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.TabelasDoVRPLEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.core.cache.UserCanEditVerifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.AccessAllowed;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Role;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.ConstraintBeanValidation;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph.CephActions;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph.CephConfig;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.dao.LicitacaoDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;

public class AnexoBC {

	@Inject
	private DAOFactory daoFactory;

	@Inject
	private SiconvPrincipal user;

	@Inject
	private CephActions cephActions;

	@Inject
	private AmazonS3 cephClient;

	@Inject
	private CephConfig cephConfig;

	@Inject
	private ConstraintBeanValidation<AnexoBD> beanValidator;

	@Inject
	private UserCanEditVerifier checkPermission;

	@RefreshRowPolice
	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	public void anexarArquivo(@NotNull Long idLicitacao, @NotNull String nomeArquivo, @NotNull String descricao,
			@NotNull TipoAnexoEnum tipoAnexo, @NotNull InputStream arquivo, Long fileLength) {
		checkPermission.check(TabelasDoVRPLEnum.LICITACAO, idLicitacao);

		if (fileLength == null || fileLength > cephConfig.getMaxFileSize()) {
			throw new TamanhoMaximoArquivoInvalidoException();
		}

		LicitacaoBD licitacao = daoFactory.get(LicitacaoDAO.class).findLicitacaoByIdLicitacao(idLicitacao);

		if (licitacao == null) {
			throw new LicitacaoAnexoNotFoundException();
		}

		AnexoBD anexo = new AnexoBD();

		anexo.setIdentificadorDaLicitacao(licitacao.getId());
		anexo.setNomeDoArquivo(nomeArquivo);
		anexo.setDescricaoDoAnexo(descricao);
		anexo.setTipoDoAnexo(tipoAnexo);
		anexo.setCpfDoUsuarioQueEnviou(user.getCpf());
		anexo.setPerfilDoUsuarioQueEnviou(user.getProfile().toString());
		anexo.setNomeDoUsuarioQueEnviou(user.getName());
		anexo.setBucket(cephConfig.getBucketName());

		this.verificarDuplicidade(anexo, daoFactory.get(AnexoDAO.class).findAnexosByIdLicitacao(licitacao.getId()));
		this.validarFormatoAnexo(anexo);

		String chaveCeph = cephActions.uploadFile(arquivo, nomeArquivo, fileLength);

		anexo.setCaminhoDoArquivo(chaveCeph);

		beanValidator.validate(anexo);

		daoFactory.get(AnexoDAO.class).insertAnexo(anexo);
	}

	public List<AnexoDTO> listarAnexos(Long idLicitacao) {
		List<AnexoBD> listaAnexoBD = daoFactory.get(AnexoDAO.class).findAnexosByIdLicitacao(idLicitacao);

		List<AnexoDTO> listaAnexoDTO = toDTO(listaAnexoBD);

		return listaAnexoDTO;
	}

	public AnexoDTO recuperarAnexo(@NotNull Long identificadorDoAnexo) {
		AnexoBD anexoBD = daoFactory.get(AnexoDAO.class).recuperarAnexo(identificadorDoAnexo);

		return new AnexoDTO().from(anexoBD);
	}

	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	public void apagarAnexo(@NotNull Long identificadorDoAnexo) {
		checkPermission.check(TabelasDoVRPLEnum.ANEXO, identificadorDoAnexo);

		daoFactory.getJdbi().useTransaction(transaction -> apagarAnexo(identificadorDoAnexo, transaction));
	}

	public void apagarAnexo(Long identificadorDoAnexo, Handle transaction) {
		AnexoBD anexo = transaction.attach(AnexoDAO.class).recuperarAnexo(identificadorDoAnexo);

		transaction.attach(AnexoDAO.class).deleteAnexo(anexo);
	}

	public void verificarDuplicidade(AnexoBD anexo, List<AnexoBD> list) {
		if (list == null) {
			return;
		}

		for (AnexoBD arquivo : list) {
			if (jaExisteOutroArquivoComMesmoNome(anexo, arquivo)) {
				throw new ArquivoDuplicadoMesmoNomeException();
			}

			if (jaExisteOutroArquivoComMesmaDescricao(anexo, arquivo)) {
				throw new ArquivoDuplicadoMesmaDescricaoException();
			}
		}
	}

	private boolean jaExisteOutroArquivoComMesmaDescricao(AnexoBD anexo, AnexoBD arquivo) {
		return arquivo.getDescricaoDoAnexo().equals(anexo.getDescricaoDoAnexo());
	}

	private boolean jaExisteOutroArquivoComMesmoNome(AnexoBD anexo, AnexoBD arquivo) {
		return arquivo.getNomeDoArquivo().equals(anexo.getNomeDoArquivo());
	}

	public void validarFormatoAnexo(AnexoBD anexo) {

		List<String> extensoesPermitidas = Arrays.asList(".PDF", ".DOC", ".DOCX", ".XLS", ".XLSX", ".JPG", ".JPEG",
				".PNG", ".ODT", ".ODS", ".DWG");

		int posicaoUltimoSeparador = anexo.getNomeDoArquivo().lastIndexOf('.');

		if (posicaoUltimoSeparador == -1) {
			throw new ArquivoAnexoSemExtensaoException();
		}

		String extensaoArquivo = anexo.getNomeDoArquivo().substring(posicaoUltimoSeparador);

		if (!extensoesPermitidas.contains(extensaoArquivo.toUpperCase())) {
			throw new FormatoArquivoInvalidoException();
		}

	}

	@AccessAllowed(value = { CONCEDENTE, MANDATARIA, PROPONENTE }, roles = {Role.GESTOR_CONVENIO_CONVENENTE, Role.GESTOR_FINANCEIRO_CONVENENTE, Role.OPERADOR_FINANCEIRO_CONVENENTE, Role.FISCAL_CONVENENTE, Role.ANALISTA_TECNICO_INSTITUICAO_MANDATARIA, Role.GESTOR_CONVENIO_INSTITUICAO_MANDATARIA, Role.ANALISTA_TECNICO_CONCEDENTE, Role.GESTOR_CONVENIO_CONCEDENTE})
	public void atualizarAnexo(@NotNull Long identificadorDoAnexo, @NotNull String nomeArquivo, @NotNull String descricao,
			@NotNull TipoAnexoEnum tipoAnexo, @NotNull Long versao, InputStream arquivo, Long fileLength) {

		checkPermission.check(TabelasDoVRPLEnum.ANEXO, identificadorDoAnexo);

		AnexoBD anexo = daoFactory.get(AnexoDAO.class).recuperarAnexo(identificadorDoAnexo);

		if (anexo == null) {
			throw new AnexoNotFoundException(identificadorDoAnexo, nomeArquivo, descricao, tipoAnexo, versao);
		}

		validarFormatoAnexo(anexo);

		List<AnexoBD> anexosBD = daoFactory.get(AnexoDAO.class)
				.recuperarOutrosAnexosDaLicitacao(anexo.getIdentificadorDaLicitacao(), anexo.getIdentificadorDoAnexo());

		anexo.setNomeDoArquivo(nomeArquivo);
		anexo.setDescricaoDoAnexo(descricao);
		anexo.setTipoDoAnexo(tipoAnexo);
		anexo.setCpfDoUsuarioQueEnviou(user.getCpf());
		anexo.setPerfilDoUsuarioQueEnviou(user.getProfile().toString());
		anexo.setVersao(versao);

		verificarDuplicidade(anexo, anexosBD);

		if (arquivo != null) {
			if (fileLength == null || fileLength > cephConfig.getMaxFileSize()) {
				throw new TamanhoMaximoArquivoInvalidoException();
			}

			String chaveCeph = cephActions.uploadFile(arquivo, nomeArquivo, fileLength);

			anexo.setCaminhoDoArquivo(chaveCeph);
			anexo.setBucket(cephConfig.getBucketName());
		}

		beanValidator.validate(anexo);

		daoFactory.getJdbi().useTransaction(transaction -> transaction.attach(AnexoDAO.class).updateAnexo(anexo));

	}

	public DAOFactory getDao() {
		return daoFactory;
	}

	public void setDao(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public List<AnexoDTO> listarAnexosPorTipo(@NotNull Long idLicitacao, @NotNull TipoAnexoEnum tipoAnexo) {
		List<AnexoBD> listaAnexoBD = daoFactory.get(AnexoDAO.class).findAnexosByTipo(idLicitacao, tipoAnexo);

		List<AnexoDTO> listaAnexoDTO = toDTO(listaAnexoBD);

		return listaAnexoDTO;
	}

	private List<AnexoDTO> toDTO(List<AnexoBD> listaAnexoBD) {
		List<AnexoDTO> listaAnexoDTO = listaAnexoBD.stream().map(anexo -> new AnexoDTO().from(anexo)).map(dto -> {

			if (!(dto.getTipoDoAnexo().equals(TipoAnexoEnum.OUTROS) && user.isAcessoLivre()) ) {
                final String bucketName = dto.getBucket();
                final String key = dto.getKey();

                GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);

				URL link = cephClient.generatePresignedUrl(request);

				dto.setLinkToDownload(link.toString());
			}
			return dto;
		}).collect(Collectors.toList());
		return listaAnexoDTO;
	}

}
