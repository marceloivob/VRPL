package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.ceph;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.MandatariasException;

@ApplicationScoped
public class CephActions {

	@Inject
	private Logger logger;

	@Inject
	private AmazonS3 cephClient;

	@Inject
	private CephConfig cephConfig;

	public String uploadFile(@NotNull final InputStream inputStream, @NotNull final String filename,
			@NotNull final Long fileLength) {

		try {
			String encodedFilename = URLEncoder.encode(filename, "UTF-8").replace("+", "%20");
			final String bucketName = cephConfig.getBucketName();

			String key = filename + "_" + UUID.randomUUID();
			key = URLEncoder.encode(key, "UTF-8").replace("+", "%20");

			ObjectMetadata objectMetadata = new ObjectMetadata();

			objectMetadata.setContentLength(fileLength);
			objectMetadata.addUserMetadata("filename", encodedFilename);
			objectMetadata.setContentDisposition("attachment; filename=" + encodedFilename);

			logger.info("Upload do arquivo '{}' com tamanho de '{}' bytes no bucket '{}' usando a key '{}'",
					encodedFilename, fileLength, bucketName, key);

			cephClient.createBucket(bucketName);
			cephClient.putObject(bucketName, key, inputStream, objectMetadata);
			cephClient.setObjectAcl(bucketName, key, CannedAccessControlList.Private);

			return key;
		} catch (IOException ioe) {
			throw new MandatariasException(ioe);
		}
	}

	public void setCephClient(AmazonS3 cephClient) {
		this.cephClient = cephClient;
	}

	public void setCephConfig(CephConfig cephConfig) {
		this.cephConfig = cephConfig;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
