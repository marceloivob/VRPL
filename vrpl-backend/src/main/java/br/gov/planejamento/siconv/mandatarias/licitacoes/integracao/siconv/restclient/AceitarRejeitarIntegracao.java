package br.gov.planejamento.siconv.mandatarias.licitacoes.integracao.siconv.restclient;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.planejamento.siconv.mandatarias.licitacoes.quadroresumo.historico.business.rules.aceitelicitacao.EventoAceiteRejeicao;
import lombok.Data;
import lombok.ToString;

@Data
public class AceitarRejeitarIntegracao implements Serializable {

	private static final long serialVersionUID = -1412309275816645489L;

	@NotNull
	private String licitacao;

	@NotNull
	private String evento;

	@NotNull
	private String atribuicaoResponsavel;

	@NotNull
	private String dataAnalise;

	@NotNull
	private String justificativa;

	@Inject
	@JsonIgnore
	@ToString.Exclude
	private transient Validator beanValidator;

	/**
	 * Construtor Padrão
	 */
	public AceitarRejeitarIntegracao() {
		// Construtor Padrão para o CDI
	}

	public AceitarRejeitarIntegracao(Long licitacao, EventoAceiteRejeicao evento, String atribuicaoResponsavel,
			Date dataAnalise, String justificativa) {
		Objects.requireNonNull(licitacao);
		Objects.requireNonNull(evento);
		Objects.requireNonNull(atribuicaoResponsavel);
		Objects.requireNonNull(dataAnalise);

		if (evento == EventoAceiteRejeicao.REJEITAR) {
			Objects.requireNonNull(justificativa);
		} else {
			if (justificativa == null) {
				// FIXME O serviço do SICONV espera que a justificativa sempre seja fornecida.
				// Aguardando a resolução do Defeito 2097485 para remover esse trecho
				// https://alm.serpro/ccm/web/projects/Novo%20Siconv%20(Gerenciamento%20de%20Mudan%C3%A7as)#action=com.ibm.team.workitem.viewWorkItem&id=2097485
				justificativa = "Justificativa não fornecida.";
			}
		}

		SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy-MM-dd");

		this.licitacao = licitacao.toString();
		this.evento = evento.getDominio();
		this.atribuicaoResponsavel = atribuicaoResponsavel;
		this.dataAnalise = simpleDateformat.format(dataAnalise);
		this.justificativa = justificativa;

		this.validate();
	}

	public void validate() {
		if (this.beanValidator == null) {
			this.beanValidator = CDI.current().select(Validator.class).get();
		}

		Set<ConstraintViolation<AceitarRejeitarIntegracao>> violacoes = beanValidator.validate(this);

		List<String> valoresInvalidos = new ArrayList<>();

		violacoes.forEach(violacao -> {
			String propriedade = violacao.getPropertyPath().toString();
			String mensagem = violacao.getMessage();

			valoresInvalidos.add(propriedade + " - " + mensagem);
		});

	}

}
