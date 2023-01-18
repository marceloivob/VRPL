package br.gov.serpro.vrpl.grpc.business;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.PODAO;

public class POBC {
    private Jdbi jdbi;

    public POBC(){
        this.jdbi = JDBIProducer.getJdbi();
    }

    public void excluirEventoFrenteObras(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(PODAO.class).excluirEventoFrenteObras(numeroProposta, anoProposta);
    }

    public void excluirFrentesObra(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(PODAO.class).excluirFrentesObra(numeroProposta, anoProposta);
    }
    
    public void excluirEventos(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(PODAO.class).excluirEventos(numeroProposta, anoProposta);
    }

    public void excluirPO(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(PODAO.class).excluirPO(numeroProposta, anoProposta);
    }
}
