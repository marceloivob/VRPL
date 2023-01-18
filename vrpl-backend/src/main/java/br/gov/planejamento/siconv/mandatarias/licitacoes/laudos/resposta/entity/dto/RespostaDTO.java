package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.database.RespostaBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.dto.LoteDTO;
import lombok.Data;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "respostaId", scope = RespostaDTO.class)
public class RespostaDTO {

	private Long respostaId;

	private String resposta;

	private Long laudoFk;

	private PerguntaDTO pergunta;

	private Long grupo;

	private Long versao;
	
	private LoteDTO lote;

	public RespostaBD converterParaBD() {
		RespostaBD bd = new RespostaBD();
		bd.setId(respostaId);
		bd.setResposta(resposta);
		bd.setVersao(versao);
		bd.setLaudoFk(laudoFk);
		bd.setPerguntaFk(pergunta.getPerguntaId());
		if(lote != null) {
			bd.setLoteFk(lote.getId());
		}
		return bd;
	}
}
