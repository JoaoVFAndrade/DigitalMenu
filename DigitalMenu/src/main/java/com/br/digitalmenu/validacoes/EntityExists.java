package com.br.digitalmenu.validacoes;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EntityExistsValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityExists {
    String message() default "Não Cadastrado no banco de dados";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class <?> entityClass();
}
