package com.restaurante.domain.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateAfterTodayValidator implements ConstraintValidator<DateAfterToday, LocalDateTime> {

    @Override
    public void initialize(DateAfterToday constraintAnnotation) {}

    @Override
    public boolean isValid(LocalDateTime valor, ConstraintValidatorContext context) {
        return valor != null && valor.toLocalDate().isAfter(LocalDate.now());
    }
}
