package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.dto;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database.FrenteObraBD;
import lombok.Data;

@Data
public class FrenteObraDTO {

    private Long id;
    private String nomeFrente;
    private Long numeroFrente;
    private Long idPO;
    private Long versao;

    public FrenteObraBD converterParaBD(){
        FrenteObraBD frenteBD = new FrenteObraBD();

		frenteBD.setId(this.id);
		frenteBD.setNmFrenteObra(this.nomeFrente);
		frenteBD.setNrFrenteObra(this.numeroFrente.intValue());
		frenteBD.setPoFk(this.idPO);
        frenteBD.setVersao(this.versao);


        return frenteBD;
    }
}
