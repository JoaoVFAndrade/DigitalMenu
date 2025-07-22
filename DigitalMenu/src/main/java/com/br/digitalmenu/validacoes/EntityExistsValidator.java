package com.br.digitalmenu.validacoes;

import jakarta.persistence.EntityManager;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityExistsValidator implements ConstraintValidator<EntityExists,Object> {

    @Autowired
    private EntityManager entityManager;

    private Class<?> entityClass;

    @Override
    public void initialize(EntityExists constraintAnnotation) {
        this.entityClass = constraintAnnotation.entityClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (value instanceof Number || value instanceof String) {
            Object entity = entityManager.find(entityClass, value);
            return entity != null;
        }

        if (entityClass.isInstance(value)) {
            return entityManager.contains(value);
        }

        return false;
    }
}


