package com.github.alexkhromov.model.dto.validator;

import com.github.alexkhromov.model.dto.validator.annotation.EnumType;
import com.github.alexkhromov.model.enums.LevelType;
import com.github.alexkhromov.model.enums.LocaleEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static java.util.Arrays.asList;

public class EnumTypeValidator implements ConstraintValidator<EnumType, Enum> {

    public void initialize(EnumType enumType) {}

    public boolean isValid(Enum value, ConstraintValidatorContext context) {

        if (value instanceof LocaleEnum) {
            return asList(LocaleEnum.values()).contains(value);
        }

        if (value instanceof LevelType) {
            return asList(LevelType.values()).contains(value);
        }

        return false;
    }
}