package br.gov.serpro.vrpl.grpc.submeta;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jdbi.v3.core.Jdbi;

import br.gov.serpro.vrpl.grpc.PropostaLote;

public class SubmetaRepository {

	private SubmetaDAO submetaDAO;

	public SubmetaRepository(Jdbi jdbi) {
		this.submetaDAO = jdbi.onDemand(SubmetaDAO.class);
	}
	
	public Boolean isContratoAcompEvento(Long id) {
		return submetaDAO.isContratoAcompEvento(id);
	}
	
	public List<Boolean> getListaIndicadorAcompEvento (List<Long> idsList){
		return submetaDAO.getListaIndicadorAcompEvento(idsList);
	}

	public List<Submeta> getListaSubmetas(List<Long> idsList) {
		return submetaDAO.getListaSubmetas(idsList);
	}
	
	public List<Submeta> getServicoListaSubmetas(List<Long> idsList) {
		return submetaDAO.getServicoListaSubmetas(idsList);
	}	

	public PropostaLote getListaLotesComSubmetas(Long idProposta) {
		return submetaDAO.getListaLotesComSubmetas(idProposta);
	}

	public Map<Long, Long> getRecuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(List<Long> submetasId) {
		Map<Long, Long> dePara = submetaDAO.recuperarSubmetasDoProjetoBasicoAPartirDasSubmetasDoVRPL(submetasId);

		return dePara;
	}

}
