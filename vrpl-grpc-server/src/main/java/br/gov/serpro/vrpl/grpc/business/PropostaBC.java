package br.gov.serpro.vrpl.grpc.business;

import java.util.Optional;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.PropostaDAO;
import br.gov.serpro.vrpl.grpc.database.bean.VrplAceitaDTO;

public class PropostaBC {
	
	private Jdbi jdbi;

	public PropostaBC() {
		this.jdbi = JDBIProducer.getJdbi();
	}

	public Optional<VrplAceitaDTO> existeVrplAceita(long idProposta) {
		
		return jdbi.onDemand (PropostaDAO.class).existePropostaVrplAceita(idProposta);
	}

	public void excluirProposta(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(PropostaDAO.class).excluirProposta(numeroProposta, anoProposta);
    }

}
