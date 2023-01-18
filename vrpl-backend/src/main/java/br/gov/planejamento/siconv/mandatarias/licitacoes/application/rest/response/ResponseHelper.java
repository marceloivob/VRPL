package br.gov.planejamento.siconv.mandatarias.licitacoes.application.rest.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public final class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> DefaultResponse<T> success(T data) {
        return new DefaultResponse<>(data, ResponseStatus.SUCCESS);
    }

    @ApiModel
    public static class DefaultResponse<T> {
        public final T data;

        @ApiModelProperty(required = true, value = "Resultado da requisição. Possíveis valores: success, fail, error.")
        public final ResponseStatus status;

        public DefaultResponse(T data, ResponseStatus status) {
            this.data = data;
            this.status = status;
        }
    }

}
