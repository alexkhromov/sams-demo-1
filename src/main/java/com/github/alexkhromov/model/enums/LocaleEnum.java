package com.github.alexkhromov.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.github.alexkhromov.model.error.ErrorCode.PROCESS_LOCALE_ERROR;
import static com.github.alexkhromov.model.error.exception.SamsDemoException.internalServerException;
import static java.util.stream.Stream.of;

@Slf4j
@Getter
@AllArgsConstructor
public enum LocaleEnum {

    EN("en-US"),
    RU("ru-RU");

    private String value;

    public static LocaleEnum fromCode(String code) {

        Optional<LocaleEnum> locale = of(LocaleEnum.values())
                .filter(le -> le.value.equalsIgnoreCase(code))
                .findAny();

        if (locale.isPresent()) {
            return locale.get();
        } else {

            log.error("Internal server exception: process locale error, code = {}", code);
            throw internalServerException(PROCESS_LOCALE_ERROR, code);
        }
    }
}