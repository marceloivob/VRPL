package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.cancelaraceite;

import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.exception.BusinessExceptionContext;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.ContratoDocLiquidacaoResponse;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRest;
import br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient.SiconvRestQualifier;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.proposta.entity.database.PropostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.QuadroResumoValidator;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoAceiteExisteDocumentoLiquidacaoException;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.exceptions.CancelamentoAceiteExisteInstrumentoContratualException;
import br.gov.serpro.siconv.contratos.grpc.services.ContratosGRPCClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class CancelarAceiteRules implements QuadroResumoValidator {
	
	@Inject
	@SiconvRestQualifier
	private SiconvRest siconvRest;
	
	@Inject 
	private ContratosGRPCClient contratosGRPCClient;
	
	@Getter
	@Setter
	@Inject
	private SiconvPrincipal usuarioLogado;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private LicitacaoBD licitacao;
	
	@Getter(AccessLevel.PUBLIC)
	@Setter(AccessLevel.PUBLIC)
	private PropostaBD proposta;
	
	@Inject
	private BusinessExceptionContext businessExceptionContext;
	
	@Override
	public void validate() {
		this.verificarExistenciaDocumentoLiquidacao(licitacao, proposta);
	}
	
	private void verificarExistenciaDocumentoLiquidacao(LicitacaoBD licitacao, PropostaBD proposta) {
		List<String> listaContratos = contratosGRPCClient.listaContratosPorIdLicitacao(proposta.getIdSiconv(),
				licitacao.getNumeroAno());
		
		if(listaContratos.isEmpty()) {
			ContratoDocLiquidacaoResponse contratoLiquidacao = siconvRest
					.consultarContratoDocLiquidacaoVinculadoProcessoExecucao(licitacao.getIdLicitacaoFk(),
							usuarioLogado);
	    	
	    	if(contratoLiquidacao.existeInstrumentoContratualOuDocumentoDeLiquidacao()) {	    			    		
	    		if(contratoLiquidacao.getDocLiquidacao()!=null) {	    			
	    			businessExceptionContext.add(new CancelamentoAceiteExisteDocumentoLiquidacaoException(contratoLiquidacao.getDocLiquidacao()));	    			
	    		}	    			    		
	    	}
	    } else {
	    	businessExceptionContext.add(new CancelamentoAceiteExisteInstrumentoContratualException(listaContratos));
	    }
	}
}
