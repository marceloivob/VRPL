package br.gov.serpro.vrpl.grpc.business;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.application.JDBIProducer;
import br.gov.serpro.vrpl.grpc.database.LicitacaoDAO;

public class LicitacaoBC {
    private Jdbi jdbi;

    public LicitacaoBC(){
        this.jdbi = JDBIProducer.getJdbi();
    }

    public void excluirFornecedorLicitacao(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LicitacaoDAO.class).excluirFornecedorLicitacao(numeroProposta, anoProposta);
    }

    public void excluirAnexosLicitacao(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LicitacaoDAO.class).excluirAnexosLicitacao(numeroProposta, anoProposta);
    }

    public void excluirHistoricoLicitacao(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LicitacaoDAO.class).excluirHistoricoLicitacao(numeroProposta, anoProposta);
    }

    public void excluirLicitacoes(Long numeroProposta, Long anoProposta, Handle transaction) {
        transaction.attach(LicitacaoDAO.class).excluirLicitacoes(numeroProposta, anoProposta);
    }

}
