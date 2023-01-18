package br.gov.serpro.vrpl.grpc.business;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.LoteDAO;

public class LoteBC {
    private Jdbi jdbi;

    public LoteBC(){
        this.jdbi = JDBIProducer.getJdbi();
    }

    public void excluirMeta(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LoteDAO.class).excluirMeta(numeroProposta, anoProposta);
    }

    public void excluirSubmetas(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LoteDAO.class).excluirSubmetas(numeroProposta, anoProposta);
    }
    
    public void excluirLotesLicitacao(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LoteDAO.class).excluirLotesLicitacao(numeroProposta, anoProposta);
    }
}
