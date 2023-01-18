package br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.macroservico.entity.dto.MacroServicoReducerDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.pocff.po.entity.database.PoBD;
import lombok.Data;

@Data
public class PoReducerDTO {

	
	@NotNull
	private Long id;

	@NotNull
	private LocalDate dtPrevisaoInicioObraAnalise;

	@NotNull
	private LocalDate dtPrevisaoInicioObra;

	@NotNull
	private LocalDate dtBaseAnalise;
	
	private LocalDate dtBaseVrpl;

	private Boolean inTrabalhoSocial;

	@NotNull
	private Long idPoAnalise;

	@NotNull
	private Long qtMesesDuracaoObra;
	
	private Boolean inDesonerado;

	private Boolean inAcompanhamentoEventos;
	private String sgLocalidade;

	private Long submetaId;
	
	private String referencia;

	@NotNull
	private Long versaoNr;
	
	private Long versao;
	
	private String versaoId;
	
	private String versaoNmEvento;
	
	private List<MacroServicoReducerDTO> macroServicos = new ArrayList<>();
	
	
	public PoBD convert() {
		PoBD po = new PoBD();
		po.setId(this.getId());
		po.setDtPrevisaoInicioObraAnalise (dtPrevisaoInicioObraAnalise);
		po.setDtPrevisaoInicioObra (dtPrevisaoInicioObra);
		po.setDtBaseAnalise (dtBaseAnalise);
		po.setDtBaseVrpl (dtBaseVrpl);
		po.setIdPoAnalise(idPoAnalise);
		po.setQtMesesDuracaoObra(qtMesesDuracaoObra);
		po.setInDesonerado(inDesonerado);
		po.setInAcompanhamentoEventos(inAcompanhamentoEventos);
		po.setSgLocalidade(sgLocalidade);
		po.setSubmetaId (submetaId);
		po.setReferencia(referencia);
		po.setVersaoNr(versaoNr);
		po.setVersao(versao);
		po.setVersaoId(versaoId);
		po.setVersaoNmEvento(versaoNmEvento);
		
		return po;
	}

}
