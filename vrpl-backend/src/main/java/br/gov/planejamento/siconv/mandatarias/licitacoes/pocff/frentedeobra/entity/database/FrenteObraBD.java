package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.database;

import java.math.BigDecimal;
import java.util.Objects;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.frentedeobra.entity.dto.FrenteObraDTO;
import lombok.Data;

@Data
public class FrenteObraBD implements Comparable<FrenteObraBD> {

    @NotNull
    @ColumnName("nr_frente_obra")
    @Max(value = 999, message="Número da Frente de Obra não pode ser superior a 999")
    @Min(value = 1, message="Número da Frente de Obra não pode ser inferior a 1")
	private Integer nrFrenteObra;

    @NotNull
    @ColumnName("nm_frente_obra")
	// TODO Confirmar se esse campo realmente é String
	private String nmFrenteObra;

    @ColumnName("id")
	private Long id;

    @NotNull
    @ColumnName("po_fk")
	private Long poFk;

    @NotNull
	@ColumnName("versao")
	private Long versao;

	@ColumnName("versao_nr")
	private Integer numeroVersao;

	@ColumnName("versao_id")
	private String versaoId;

	@ColumnName("versao_nm_evento")
	private String versaoNmEvento;
    
    // transient usado na importacao
    private Long idAnalise;

    // utilizado em join com serviço para retornar quantidade
    private BigDecimal qtdServicoFrenteObra;
    private BigDecimal qtdServicoFrenteObraAnalise;

    public FrenteObraDTO converterParaDTO(){
        FrenteObraDTO dto = new FrenteObraDTO();
        dto.setId(this.id);
        dto.setIdPO(this.poFk);
        dto.setNomeFrente(this.nmFrenteObra);
        dto.setNumeroFrente(this.getNrFrenteObra().longValue());
        dto.setVersao(this.getVersao());
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FrenteObraBD that = (FrenteObraBD) o;
        return Objects.equals(nmFrenteObra, that.nmFrenteObra) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nmFrenteObra, id);
    }
    
    public String getDescricao() {
    	return this.getNrFrenteObra() + " - " + this.getNmFrenteObra();
    }

	@Override
	public int compareTo(FrenteObraBD o) {
		if(!this.nrFrenteObra.equals(o.nrFrenteObra)) {
			return this.nrFrenteObra.compareTo(o.nrFrenteObra);
		}
		
		return this.nmFrenteObra.compareTo(o.nmFrenteObra);
	}
    
}
