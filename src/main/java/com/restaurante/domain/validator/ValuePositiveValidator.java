package com.restaurante.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ValuePositiveValidator implements ConstraintValidator<ValuePositive, BigDecimal> {

    @Override
    public void initialize(ValuePositive constraintAnnotation) {}

    @Override
    public boolean isValid(BigDecimal valor, ConstraintValidatorContext context) {
        return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }
}
