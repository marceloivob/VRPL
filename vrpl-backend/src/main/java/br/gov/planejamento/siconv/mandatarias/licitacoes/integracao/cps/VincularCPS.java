package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps;

import javax.inject.Inject;

import br.gov.planejamento.siconv.grpc.CpsGRPCClient;
import br.gov.planejamento.siconv.grpc.DataDaPropostaResponse;
import br.gov.planejamento.siconv.grpc.ReferenciaPrecoResponse;
import br.gov.planejamento.siconv.grpc.SiconvGRPCClient;
import br.gov.planejamento.siconv.grpc.VerificarCpsResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.util.DateUtil;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception.ErroIntegracaoCPS;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception.PropostaSemNivelCPSCorrespondenteException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.cps.exception.VincularCpsException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.ModalidadePropostaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.entity.NivelContratoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.Setter;

/**
 * VincularCPS
 */
public class VincularCPS {

	//////////////////////////////////////////////////////////////////////////////////////////////
	// Atributos
	//////////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	@Setter
	private CriterioParaSubmeterValidacaoEGT4 criterioEGT4;

	@Inject
	private CpsGRPCClient clientCps;
	
	@Inject
	private SiconvGRPCClient siconvGRPCClient;

	@Inject
	@Setter
	private BusinessExceptionContext businessExceptionContext;

	//////////////////////////////////////////////////////////////////////////////////////////////
	// Construtor
	//////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Construtor Padrão
	 */
	public VincularCPS() {
		// Construtor Padrão para o CDI
	}

	/**
	 * Construtor para os testes unitários
	 */
	public VincularCPS(CriterioParaSubmeterValidacaoEGT4 criterioEGT4, CpsGRPCClient clientCps) {
		this.clientCps = clientCps;
		this.criterioEGT4 = criterioEGT4;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	// Métodos de Negócio
	//////////////////////////////////////////////////////////////////////////////////////////////
	public void vincular(SiconvPrincipal usuarioLogado, PropostaBD proposta, LicitacaoBD licitacao) {
		if (criterioEGT4.saoAtendidosPor(proposta, licitacao, usuarioLogado)) {
			try {

				String data = DateUtil.formatNoTimeZone(DateUtil.today(), "dd-MM-yyyy");

				VerificarCpsResponse resp = clientCps.verificarCps(proposta.getIdSiconv(),
						proposta.getAnoProposta(), data, "EGT4");

				if (resp == null) {
					businessExceptionContext.add(new VincularCpsException());
					businessExceptionContext.throwException();
				}

				if (!resp.getIsCpsVerificado()) {
					businessExceptionContext.add(new VincularCpsException( VincularCpsException.getErrorInfo(resp.getMensagem()), proposta ));
					businessExceptionContext.throwException();
				}

			} catch (StatusRuntimeException sre) {
				businessExceptionContext.add(new ErroIntegracaoCPS(sre));
				businessExceptionContext.throwException();
			}
		}
		
		getNivelProposta(proposta);
		
		if (isPropostaContratoRepasse(proposta) && isPropostaNaoEnquadraEmNivel(proposta)) {
			businessExceptionContext.add(new PropostaSemNivelCPSCorrespondenteException(proposta));
			businessExceptionContext.throwException();
		}
	}
	
	private boolean isPropostaContratoRepasse(PropostaBD proposta) {
		return ModalidadePropostaEnum.CONTRATO_DE_REPASSE.getCodigo() == proposta.getModalidade().intValue();
	}
	
	private boolean isPropostaNaoEnquadraEmNivel(PropostaBD proposta) {
		return NivelContratoEnum.NA.name().equals(proposta.getNivelContrato()) 
				|| NivelContratoEnum.NA.getDescricao().equals(proposta.getNivelContrato());
	}
	
	private void getNivelProposta(PropostaBD propostaBD) {
		DataDaPropostaResponse dpr = siconvGRPCClient.consultarDataDaProposta(propostaBD.getIdSiconv());
		Integer CATEGORIA = 1;

		try {
			ReferenciaPrecoResponse rpr = clientCps.consultarNivelPorData(Double.valueOf(propostaBD.getValorRepasse().doubleValue()),
					CATEGORIA, dpr.getDataDaProposta());

			propostaBD.setNivelContrato( rpr.getDescricao() );

		} catch (StatusRuntimeException sre) {
			if (naoTemEnquadramentoEmNenhumNivelDeContrato(sre)) {
				propostaBD.setNivelContrato( NivelContratoEnum.NA.getDescricao() );
			} else {
				businessExceptionContext.add(new ErroIntegracaoCPS(sre));
				businessExceptionContext.throwException();
			}
		} catch (Exception e) {
			businessExceptionContext.add(new ErroIntegracaoCPS(e));
			businessExceptionContext.throwException();
		}
	}
	
	private boolean naoTemEnquadramentoEmNenhumNivelDeContrato(StatusRuntimeException sre) {
		// Serviço CPS-GRPC consultarNivelPorData retorna exceção se a proposta não se
		// enquadrar em nenhum nível
		return sre.getStatus().getCode() == Status.NOT_FOUND.getCode();
	}

}
