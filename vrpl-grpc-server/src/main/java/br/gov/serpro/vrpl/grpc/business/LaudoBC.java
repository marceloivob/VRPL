package br.gov.serpro.vrpl.grpc.business;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.LaudoDAO;
import org.jdbi.v3.core.Handle;

import org.jdbi.v3.core.Jdbi;

public class LaudoBC {
    
    private Jdbi jdbi;

    public LaudoBC(){
        this.jdbi = JDBIProducer.getJdbi();
    }

    public void excluirLaudosVRPL(Long numeroProposta, Long anoProposta, Handle transaction) {

        //Excluir resposta dos laudos
        transaction.attach(LaudoDAO.class).excluirRespostaLaudo(numeroProposta, anoProposta);

    }

    public void excluirPendenciasLaudo(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LaudoDAO.class).excluirPendenciasLaudo(numeroProposta, anoProposta);
    }

    public void excluirLaudos(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LaudoDAO.class).excluirLaudos(numeroProposta, anoProposta);
    }

}
