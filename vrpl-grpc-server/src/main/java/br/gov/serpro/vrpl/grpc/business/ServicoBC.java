package br.gov.serpro.vrpl.grpc.business;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.ServicoDAO;

public class ServicoBC {
    
    private Jdbi jdbi;

    public ServicoBC(){
        this.jdbi = JDBIProducer.getJdbi();
    }

    public void excluirServicoFrenteObra(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(ServicoDAO.class).excluirServicoFrenteObra(numeroProposta, anoProposta);
    }

    public void excluirServicoFrenteObraAnalise(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(ServicoDAO.class).excluirServicoFrenteObraAnalise(numeroProposta, anoProposta);
    }

    public void excluirServicos(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(ServicoDAO.class).excluirServicos(numeroProposta, anoProposta);
    }

    public void excluirMacroServicoParcela(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(ServicoDAO.class).excluirMacroServicoParcela(numeroProposta, anoProposta);
    }

    public void excluirMacroServicos(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(ServicoDAO.class).excluirMacroServicos(numeroProposta, anoProposta);
    }

}
