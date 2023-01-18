package br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.database;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper.CPF;
import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.LicitacaoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.dto.HistoricoLicitacaoDTO;
import lombok.Data;

@Data
public class HistoricoLicitacaoBD {

	@ColumnName("id")
	private Long id;

	@ColumnName("licitacao_fk")
	private Long identificadorDaLicitacao;

	@ColumnName("in_evento")
	private String eventoGerador;

	@ColumnName("in_situacao")
	private String situacaoDaLicitacao;

	@ColumnName("tx_consideracoes")
	@Size(max = 1500)
	private String consideracoes;

	@ColumnName("dt_registro")
	private Date dataDeRegistro;

	@ColumnName("nm_responsavel")
	private String nomeDoResponsavel;

	@ColumnName("nr_cpf_responsavel")
	private String cpfDoResponsavel;
	
	private Long versao;

	@NotNull
	@ColumnName("versao_nr")
	private Long versaoNr;

	@ColumnName("versao_nm_evento")
	@JsonIgnore
	private String versaoNmEvento;

	@ColumnName("versao_id")
	@JsonIgnore
	private String versaoId;

    public HistoricoLicitacaoDTO converterParaDTO(){
    	HistoricoLicitacaoDTO historicoDTO = new HistoricoLicitacaoDTO();

    	historicoDTO.setId(this.id);
    	historicoDTO.setIdentificadorDaLicitacao(this.identificadorDaLicitacao);
		historicoDTO.setEventoGerador(this.eventoGerador);
		historicoDTO.setSituacaoDaLicitacao(this.situacaoDaLicitacao);
    	historicoDTO.setConsideracoes(this.consideracoes);
    	historicoDTO.setDataDeRegistro(this.dataDeRegistro);
    	historicoDTO.setNomeDoResponsavel(this.nomeDoResponsavel);
		historicoDTO.setCpfDoResponsavel(new CPF(this.cpfDoResponsavel));
		historicoDTO.setVersaoDoHistorico(this.versao);

        return historicoDTO;
    }

	public HistoricoLicitacaoBD() {

	}

	public HistoricoLicitacaoBD(LicitacaoBD licitacao, SiconvPrincipal usuarioLogado) {

		this.identificadorDaLicitacao = licitacao.getId();
		this.situacaoDaLicitacao = licitacao.getSituacaoDaLicitacao();
		this.eventoGerador = EventoQuadroResumoEnum.ASSOCIAR_LOTE_LICITACAO.getSigla();
		this.cpfDoResponsavel = usuarioLogado.getCpf();
		this.nomeDoResponsavel = usuarioLogado.getName();
		this.identificadorDaLicitacao = licitacao.getId();

	}

}
