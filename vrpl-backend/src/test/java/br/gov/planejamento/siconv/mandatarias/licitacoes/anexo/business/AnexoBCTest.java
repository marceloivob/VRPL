package br.gov.planejamento.siconv.mandatarias.licitacoes.anexo.business;

import java.net.URL;

import org.junit.Before;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class AnexoBCTest {

    private AmazonS3 cephClient;

    @Before
    public void setup() {
        // AWSCredentials credentialsDesenv = new BasicAWSCredentials("9b32d0b70e3840c2a5e607f238f1916a",
        // "54e740daf2ed4b05b0accd6b38b6f77f");

        AWSCredentials credentialsProd = new BasicAWSCredentials("5ZQC7KLFWYJI4F0HPF7K",
                "O4LF28u8EZHh8BMpngTvOgPiQAZYRRcfSFxf5OPs");
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentialsProd);

        EndpointConfiguration epc = new AwsClientBuilder.EndpointConfiguration(
                "https://storagegw.estaleiro.serpro.gov.br", "undefined");

        this.cephClient = AmazonS3ClientBuilder.standard().withEndpointConfiguration(epc)
                .withCredentials(credentialsProvider).withPathStyleAccessEnabled(true).build();
    }

    // @Test
    public void createURL() {

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest("mandatarias-05-2021",
                "Despacho%20de%20Homologa%C3%A7%C3%A3o.pdf_791e4e04-d661-4158-974f-32b24f28c76f");
        URL link = cephClient.generatePresignedUrl(request);

        System.out.println(link.toString());
    }

}
