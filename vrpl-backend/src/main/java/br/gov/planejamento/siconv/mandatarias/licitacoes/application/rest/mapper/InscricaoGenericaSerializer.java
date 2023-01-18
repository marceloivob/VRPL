package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import java.io.IOException;
import java.util.Objects;

import javax.enterprise.inject.spi.CDI;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;
import br.gov.planejamento.siconv.mandatarias.licitacoes.licitacao.entity.database.PersonalidadeJuridica;

public class InscricaoGenericaSerializer extends StdSerializer<IG> {

	protected InscricaoGenericaSerializer() {
		this(null);
	}

	public InscricaoGenericaSerializer(Class<IG> t) {
		super(t);
	}

	private static final long serialVersionUID = 7515841648303001748L;

	@Override
	public void serialize(IG ig, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("tipo", PersonalidadeJuridica.IG.name());

		if (Objects.isNull(ig)) {
			return;
		}

		SiconvPrincipal usuarioLogado = getUsuarioLogado();

		if (usuarioLogado.isAcessoLivre()) {
			if (ig.getValor().length() < 6) {
				gen.writeStringField("valor", String.format("%s", "***" + ig.getValor() + "***"));

			} else {
				final int inicio = Integer.min(3, ig.getValor().length());
				final int fim = ig.getValor().length() - 3;

				gen.writeStringField("valor",
						String.format("%s", "***" + ig.getValor().substring(inicio, fim) + "***"));

			}
		} else {
			gen.writeStringField("valor", ig.getValor());
		}

		gen.writeEndObject();

	}

	public SiconvPrincipal getUsuarioLogado() {
		return CDI.current().select(SiconvPrincipal.class).get();
	}

}
