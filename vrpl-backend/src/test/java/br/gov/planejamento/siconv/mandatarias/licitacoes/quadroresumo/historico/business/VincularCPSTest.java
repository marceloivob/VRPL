package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.gov.planejamento.siconv.grpc.CpsGRPCClient;
import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.grpc.VerificarCpsResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions.ErrorInfo;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.Profile;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.DateUtil;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvClientGRPCProducer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.client.SiconvGRPCConsumer;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.CriterioParaSubmeterValidacaoEGT4;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.VincularCPS;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception.VincularCpsException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.SituacaoLicitacaoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.test.core.MockSiconvPrincipal;

@EnableWeld
public class VincularCPSTest {

	@WeldSetup
	public WeldInitiator weld = WeldInitiator
			.from (BusinessExceptionContext.class, BusinessExceptionProducer.class, 
					SiconvClientGRPCProducer.class, SiconvGRPCConsumer.class, SiconvGRPCClient.class)
			.activate(RequestScoped.class, SessionScoped.class).build();
	
	
	@InjectMocks
	private VincularCPS cps;

	@InjectMocks
	private CriterioParaSubmeterValidacaoEGT4 criterios;

	@Mock
	private CpsGRPCClient clientCps;
	
	@Mock
	private SiconvGRPCConsumer siconvGRPCConsumer;

	
	
	@Inject
	private BusinessExceptionContext businessExceptionContext;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		cps.setCriterioEGT4(criterios);
		cps.setBusinessExceptionContext(businessExceptionContext);
	}

	@Test
	public void vincularConvenio() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);
		PropostaBD proposta = new PropostaBD();
		proposta.setModalidade(ModalidadePropostaEnum.CONVENIO.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.when(siconvGRPCConsumer.existeHistoricoAceiteLicitacaoPossuiSituacao(licitacao.getId(), br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum.ACEITO)).thenReturn(true);

		cps.vincular(usuarioLogado, proposta, licitacao);

	}

	@Test
	public void vincularPorProponente() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.PROPONENTE);
		PropostaBD proposta = new PropostaBD();
		proposta.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());

		Mockito.when(siconvGRPCConsumer.existeHistoricoAceiteLicitacaoPossuiSituacao(licitacao.getId(), br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum.ACEITO)).thenReturn(true);

		cps.vincular(usuarioLogado, proposta, licitacao);

	}

	@Test
	public void cpsNaoVigente() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);
		PropostaBD proposta = new PropostaBD();
		proposta.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());

		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());
		
		Mockito.when(siconvGRPCConsumer.existeHistoricoAceiteLicitacaoPossuiSituacao(licitacao.getId(), br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum.ACEITO)).thenReturn(false);
		
		// Dupla assertiva que garante de fato que o cenario foi atingido
		assertThrows(BusinessException.class, () -> cps.vincular(usuarioLogado, proposta, licitacao));
		
		Assertions.assertEquals(VincularCpsException.class,businessExceptionContext.getException(VincularCpsException.class).stream()
								.findFirst().orElseGet(() -> new BusinessException(ErrorInfo.CPS_NAO_ATIVO_VIGENTE)).getClass());
	}


	@Test
	public void vincularContratoDeRepassePelaMandataria() {

		SiconvPrincipal usuarioLogado = new MockSiconvPrincipal(Profile.MANDATARIA);
		PropostaBD proposta = new PropostaBD();
		proposta.setIdSiconv(1L);
		proposta.setAnoProposta(2021);
		proposta.setModalidade(ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo());

		String data = DateUtil.formatNoTimeZone(DateUtil.today(), "dd-MM-yyyy");
		
		LicitacaoBD licitacao = new LicitacaoBD();
		licitacao.setSituacaoDaLicitacao(SituacaoLicitacaoEnum.DOCUMENTACAO_ACEITA.getSigla());
		
		VerificarCpsResponse cpsVerificado = VerificarCpsResponse.newBuilder().setIsCpsVerificado(true).build();

		Mockito.when(siconvGRPCConsumer.existeHistoricoAceiteLicitacaoPossuiSituacao(licitacao.getId(), br.gov.planejamento.siconv.grpc.SituacaoLicitacaoRequest.SituacaoLicitacaoEnum.ACEITO)).thenReturn(true);
		Mockito.when(clientCps.verificarCps(proposta.getIdSiconv(), proposta.getAnoProposta() ,data, "EGT4" )).thenReturn(cpsVerificado);

		cps.vincular(usuarioLogado, proposta, licitacao);

	}

}
