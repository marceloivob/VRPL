package br.gov.planejamento.siconv.mandatarias.licitacoes.application.security;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Interface para ser utilizada em métodos que necessitam
 * validar o Profile do usuário logado. Por exemplo:
 * <code>
 * <p><br>
 * &#64;PermissaoUsuario(Profile.MANDATARIA)<br>
 * void execute() {<br>
 * &nbsp;&nbsp;&nbsp;...<br>
 * }<br>
 * </p>
 * </code>
 * @author 61603945504
 */
@Target({ METHOD, CONSTRUCTOR })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { PermissaoValidator.class })
public @interface PermissaoUsuario {

	String message() default "Sem permissão";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	/**
	 * Profiles válidos para o usuário logado..
	 */
	Profile[] value();

	/**
	 * Se existir mais de um profile válido, o tipo de connector
	 * indicará como a validação é realizada. Se for "false" (padrão)
	 * a validação passará se o usuário tiver qualquer um dos profiles.
	 * Caso seja "true", o usuário dever TODOS os profiles indicados.
	 */
	boolean andConnector() default false;
	
}