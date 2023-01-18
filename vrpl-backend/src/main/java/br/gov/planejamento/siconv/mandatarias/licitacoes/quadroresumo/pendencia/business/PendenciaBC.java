package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.business;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.database.DAOFactory;
import br.gov.planejamento.siconv.mandatarias.licitacoes.qci.dao.QciDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.dao.PendenciaDAO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.PrazoPendenciaEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.database.PendenciaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.pendencia.entity.dto.PendenciaDTO;

public class PendenciaBC {

    @Inject
    private DAOFactory dao;

    public void inserir(PendenciaDTO pendencia) {

        //inserir regras de validacao
        //converterDTO
        PendenciaBD pendenciaConvertido = pendencia.converterParaBD();
        dao.get(PendenciaDAO.class).inserirPendencia(pendenciaConvertido);

    }

    public void alterar(PendenciaDTO pendencia) {

      //inserir regras de validacao


        // valida se o Pendencia nao exista
        recuperarPendenciaPorId(pendencia.getId());

        //converterDTO
        PendenciaBD pendenciaConvertido = pendencia.converterParaBD();

        dao.get(PendenciaDAO.class).alterarPendencia(pendenciaConvertido);

    }


    public void excluir(Long id) {

        // levanta excecao caso Pendencia nao exista
        recuperarPendenciaPorId(id);
        dao.get(PendenciaDAO.class).excluirPendencia(id);
    }



    public PendenciaDTO recuperarPendenciaPorId (Long id ) {
        PendenciaBD pendenciaBD = dao.get(PendenciaDAO.class).recuperarPendenciaPorId(id);
        if (pendenciaBD == null) {
            throw new PendenciaNaoEncontradoException();
        }

        PendenciaDTO pendenciaDTO = pendenciaBD.converterParaDTO();
        return pendenciaDTO;
    }
    
    public List<PendenciaDTO> recuperarPendenciaPorLaudo(Long laudoFk) {
        List<PendenciaBD> pendencias = dao.get(PendenciaDAO.class).recuperarPendenciaPorLaudo(laudoFk);
        List<PendenciaDTO> pendenciasDTO = new ArrayList<PendenciaDTO>();
        for (PendenciaBD pendencia : pendencias) {
        	pendenciasDTO.add(this.recuperarPendenciaDetalhada(pendencia.converterParaDTO()));
		}
        
        return pendenciasDTO;
    }
    
    public PendenciaDTO recuperarPendenciaDetalhada(PendenciaDTO pendencia){
    	
    	pendencia.setSubmetaDescricao(dao.get(QciDAO.class).recuperarSubmetaVRPL(pendencia.getSubmetaFk()).getDescricao());
    	pendencia.setPrazoDescricao(PrazoPendenciaEnum.fromSigla(pendencia.getPrazo()).getDescricao());
    	
    	return pendencia;
    	
    }


}
