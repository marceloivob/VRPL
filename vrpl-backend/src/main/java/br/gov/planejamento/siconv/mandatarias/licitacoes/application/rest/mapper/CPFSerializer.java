package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.mapper;

import java.io.IOException;
import java.util.Objects;

import javax.enterprise.inject.spi.CDI;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.gov.planejamento.siconv.mandatarias.licitacoes.application.security.SiconvPrincipal;

public class CPFSerializer extends StdSerializer<CPF> {

    protected CPFSerializer() {
        this(null);
    }

    public CPFSerializer(Class<CPF> t) {
        super(t);
    }

    private static final long serialVersionUID = 7515841648303001748L;

    @Override
    public void serialize(CPF cpf, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (Objects.isNull(cpf)) {
            return;
        }

        SiconvPrincipal usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.isAcessoLivre()) {
			gen.writeString(String.format("%s", "***" + cpf.getValor().substring(3, 8) + "***"));
        } else {
			gen.writeString(cpf.getValor());
        }

    }

    public SiconvPrincipal getUsuarioLogado() {
        return CDI.current().select(SiconvPrincipal.class).get();
    }

}
