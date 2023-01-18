package br.gov.serpro.vrpl.grpc.business;

import java.util.Optional;

import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.VrplDAO;
import br.gov.serpro.vrpl.grpc.database.CacheDAO;
import br.gov.serpro.vrpl.grpc.database.bean.PropostaLotesBD;
import br.gov.serpro.vrpl.grpc.database.bean.VerificarExclusaoVrplDTO;
import br.gov.serpro.vrpl.grpc.services.VerificarExclusaoVrplEnum;

import org.jdbi.v3.core.Handle;

public class VrplBC {
    private Jdbi jdbi;

	//private LaudoDAO laudoDAO;

	private LaudoBC laudoBC;

	private ServicoBC servicoBC;

	private POBC poBC;

	private LoteBC loteBC;

	private LicitacaoBC licitacaoBC;

	private PropostaBC propostaBC;

    public VrplBC(){
        this.jdbi = JDBIProducer.getJdbi();
		this.laudoBC = new LaudoBC();
		this.servicoBC = new ServicoBC();
		this.poBC = new POBC();
		this.loteBC = new LoteBC();
		this.licitacaoBC = new LicitacaoBC();
		this.propostaBC = new PropostaBC();
    }

	public void excluirDadosVRPL(long numeroProposta, long anoProposta, String usuarioLogado) {
		
		try (Handle handle = jdbi.open()) {

			handle.getJdbi().useTransaction(transaction -> {

				transaction.attach(CacheDAO.class).definirUsuarioLogado(usuarioLogado);

				laudoBC.excluirLaudosVRPL(numeroProposta, anoProposta, transaction);

				laudoBC.excluirPendenciasLaudo(numeroProposta, anoProposta, transaction);

				laudoBC.excluirLaudos(numeroProposta, anoProposta, transaction);

				servicoBC.excluirServicoFrenteObra(numeroProposta, anoProposta, transaction);

				servicoBC.excluirServicoFrenteObraAnalise(numeroProposta, anoProposta, transaction);

				servicoBC.excluirServicos(numeroProposta, anoProposta, transaction);

				servicoBC.excluirMacroServicoParcela(numeroProposta, anoProposta, transaction);

				servicoBC.excluirMacroServicos(numeroProposta, anoProposta, transaction);

				poBC.excluirEventoFrenteObras(numeroProposta, anoProposta, transaction);

				poBC.excluirFrentesObra(numeroProposta, anoProposta, transaction);

				poBC.excluirEventos(numeroProposta, anoProposta, transaction);

				poBC.excluirPO(numeroProposta, anoProposta, transaction);

				loteBC.excluirSubmetas(numeroProposta, anoProposta, transaction);

				loteBC.excluirMeta(numeroProposta, anoProposta, transaction);

				loteBC.excluirLotesLicitacao(numeroProposta, anoProposta, transaction);

				licitacaoBC.excluirFornecedorLicitacao(numeroProposta, anoProposta, transaction);

				licitacaoBC.excluirAnexosLicitacao(numeroProposta, anoProposta, transaction);

				licitacaoBC.excluirHistoricoLicitacao(numeroProposta, anoProposta, transaction);

				licitacaoBC.excluirLicitacoes(numeroProposta, anoProposta, transaction);

				propostaBC.excluirProposta(numeroProposta, anoProposta, transaction);

			});

		} 		
	}

    public VerificarExclusaoVrplDTO verificarExclusaoVRPL(long numeroProposta, long anoProposta){
    	
    	VerificarExclusaoVrplDTO definirSituacao = null;
    	
    	if (!verificarSeHouveRejeicaoLicitacao(numeroProposta, anoProposta)) {
    		
    		Optional<PropostaLotesBD> situacao = jdbi.onDemand(VrplDAO.class).verificarExclusaoVRPL(numeroProposta, anoProposta);
    		definirSituacao = this.definirSituacao(situacao);
    		
    	} else {
    		//ja existe licitacao rejeitada
    		definirSituacao = new VerificarExclusaoVrplDTO();
    		definirSituacao.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_LOTES_VINCULADOS.getCodigo());
    		definirSituacao.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_LOTES_VINCULADOS.getDescricao());
    	}

		return definirSituacao;

	}

	private VerificarExclusaoVrplDTO definirSituacao(Optional<PropostaLotesBD> proposta) {

		VerificarExclusaoVrplDTO retorno = new VerificarExclusaoVrplDTO();
		
		
		if(proposta.isPresent()) {
			if(proposta.get().getQtdLotes() > 0) {
				retorno.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_LOTES_VINCULADOS.getCodigo());
				retorno.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_LOTES_VINCULADOS.getDescricao());
			} else if(proposta.get().getVersao() > 0) {
				retorno.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_VERSIONADA.getCodigo());
				retorno.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_VERSIONADA.getDescricao());
			} else {
				retorno.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_PODE_SER_EXCLUIDA.getCodigo());
				retorno.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_PODE_SER_EXCLUIDA.getDescricao());
			}
		} else {
			retorno.setSituacao(VerificarExclusaoVrplEnum.PROPOSTA_NAO_EXISTE.getCodigo());
			retorno.setMensagem(VerificarExclusaoVrplEnum.PROPOSTA_NAO_EXISTE.getDescricao());
		}
		
		return retorno;

	}
	
	private boolean verificarSeHouveRejeicaoLicitacao(long numeroProposta, long anoProposta) {
		Optional<Long> propID = jdbi.onDemand(VrplDAO.class).verificarSeHouveRejeicaoLicitacao(numeroProposta, anoProposta);
		return propID.isPresent();
	}
}
