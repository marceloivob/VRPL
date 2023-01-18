package br.gov.planejamento.siconv.mandatarias.licitacoes.application.exceptions;

public class MandatariasException extends RuntimeException {
    private static final long serialVersionUID = -7089099401571929796L;

    public MandatariasException() {
    }

    public MandatariasException(String mensagem) {
        super(mensagem);
    }

    public MandatariasException(Throwable t) {
        super(t);
    }

}
