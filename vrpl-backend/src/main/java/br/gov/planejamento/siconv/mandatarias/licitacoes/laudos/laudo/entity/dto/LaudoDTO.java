package br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.dto;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.ResultadoParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.StatusParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.TipoDeParecerEnum;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.laudo.entity.database.LaudoBD;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.pergunta.entity.dto.PerguntaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.resposta.entity.dto.RespostaDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.laudos.templatelaudo.entity.dto.TemplateLaudoDTO;
import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.entity.EventoQuadroResumoEnum;
import lombok.Data;

@Data
public class LaudoDTO {

	private Long id;

	private Long licitacaoFk;

	private Long templateFk;

	private String inResultado;

	private Integer inStatus;

	private Long versaoNr;

	private String versaoNmEvento;

	private String versaoId;

	private Long versao;

	private TemplateLaudoDTO template;

	private List<RespostaDTO> respostas;
	
	private String adtLogin;

	public LaudoBD converterParaBD() {
		LaudoBD bd = new LaudoBD();
		bd.setId(id);
		bd.setInResultado(inResultado);
		bd.setInStatus(inStatus);
		bd.setLicitacaoFk(licitacaoFk);
		bd.setTemplateFk(templateFk);
		bd.setVersao(versao);
		bd.setVersaoId(versaoId);
		bd.setVersaoNmEvento(versaoNmEvento);
		bd.setVersaoNr(versaoNr);
		return bd;
	}

	@JsonIgnore
	public boolean justificativaFoiFornecida() {
		Optional<RespostaDTO> justificativa = this.getRespostas().stream()
				.filter(resposta -> resposta.getPergunta().getTitulo().equalsIgnoreCase("Justificativa")).findFirst();

		if (justificativa.isPresent()) {
			boolean justificativaFoiFornecida = justificativa.get().getResposta() != null
					&& !justificativa.get().getResposta().isEmpty();

			return justificativaFoiFornecida;
		} else {
			return false;
		}
	}

	@JsonIgnore
	public RespostaDTO getJustificativa() {
		return this.getRespostas().stream()
				.filter(resposta -> resposta.getPergunta().getTitulo().equalsIgnoreCase("Justificativa")).findFirst()
				.get();
	}

	@JsonIgnore
	public String getEventoEmissao() {

		if (this.ehVRPL()) {
			return EventoQuadroResumoEnum.EMITIR_PARECER_ENGENHARIA.getSigla();
		} else if (this.ehVRPLS()) {
			return EventoQuadroResumoEnum.EMITIR_PARECER_SOCIAL.getSigla();
		}

		throw new IllegalArgumentException("Tipo de Parecer inválido: " + this.templateFk);
	}

	@JsonIgnore
	public String getComentarioEmissao() {
		PerguntaDTO perguntaJustificativa = new PerguntaDTO();
		perguntaJustificativa.setTitulo("Justificativa");

		Optional<RespostaDTO> respostaJustificativa = this.getRespostas().stream()
				.filter(resposta -> resposta.getPergunta().getTitulo().equals(perguntaJustificativa.getTitulo()))
				.findFirst();

		if (respostaJustificativa.isPresent()) {
			return respostaJustificativa.get().getResposta();
		}

		throw new IllegalArgumentException("Justificativa não fornecida: " + this);
	}

	@JsonIgnore
	public String getEventoAssinatura() {
		if (this.ehVRPL()) {
			return EventoQuadroResumoEnum.ASSINAR_PARECER_ENGENHARIA.getSigla();
		} else if (this.ehVRPLS()) {
			return EventoQuadroResumoEnum.ASSINAR_PARECER_SOCIAL.getSigla();
		}

		throw new IllegalArgumentException("Tipo de Parecer inválido: " + this.templateFk);

	}

	@JsonIgnore
	public String getEventoCancelamento() {
		if (this.ehVRPL()) {
			return EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_ENGENHARIA.getSigla();
		} else if (this.ehVRPLS()) {
			return EventoQuadroResumoEnum.CANCELAR_EMISSAO_PARECER_SOCIAL.getSigla();
		}

		throw new IllegalArgumentException("Tipo de Parecer inválido: " + this.templateFk);
	}

	@JsonIgnore
	public String getComentarioCancelamento() {
		if (this.ehVRPL()) {
			return "Cancelamento de Emissão de Parecer Técnico de Engenharia para retificação de informações.";
		} else if (this.ehVRPLS()) {
			return "Cancelamento de Emissão de Parecer Técnico Social para retificação de informações.";
		}

		throw new IllegalArgumentException("Tipo de Parecer inválido: " + this.templateFk);
	}

	@JsonIgnore
	public boolean ehVRPL() {
		return TipoDeParecerEnum.VRPL.getId().equals(this.templateFk);
	}

	@JsonIgnore
	public boolean ehVRPLS() {
		return TipoDeParecerEnum.VRPLS.getId().equals(this.templateFk);
	}

	@JsonIgnore
	public String getComentarioAssinatura() {
		if (this.ehVRPL()) {
			return "Parecer Técnico de Engenharia assinado pelo responsável indicado";
		} else if (this.ehVRPLS()) {
			return "Parecer Técnico Social assinado pelo responsável indicado.";
		}

		throw new IllegalArgumentException("Tipo de Parecer inválido: " + this.templateFk);
	}

	@JsonIgnore
	public boolean estaEmRascunho() {
		return this.getInStatus().equals(StatusParecerEnum.RASCUNHO.getCodigo());
	}

	@JsonIgnore
	public boolean estaCancelado() {
		return this.getInStatus().equals(StatusParecerEnum.CANCELADO.getCodigo());
	}

	@JsonIgnore
	public boolean estaAssinado() {
		return this.getInStatus().equals(StatusParecerEnum.ASSINADO.getCodigo());
	}

	@JsonIgnore
	public boolean estaEmitido() {
		return this.getInStatus().equals(StatusParecerEnum.EMITIDO.getCodigo());
	}

	@JsonIgnore
	public void definirResultadoDoLaudo() {
		Optional<RespostaDTO> respostaViavelOuInviavel = this.getRespostas().stream()
				.filter(resposta -> resposta.getPergunta().getTitulo()
						.startsWith("Considera-se a verificação do resultado do processo licitatório"))
				.findFirst();

		if (respostaViavelOuInviavel.isPresent()) {

			RespostaDTO resposta = respostaViavelOuInviavel.get();

			if ((resposta.getResposta() == null) || (resposta.getResposta().isEmpty())) {
				this.setInResultado(null);
			} else {
				ResultadoParecerEnum resultado = ResultadoParecerEnum.fromDescricao(resposta.getResposta());
				this.setInResultado(resultado.getSigla());
			}
		} else {
			this.setInResultado(null);
		}
	}

}
