package com.github.alexkhromov.model.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.alexkhromov.model.enums.LocaleEnum;
import com.github.alexkhromov.model.error.ErrorCode;
import com.github.alexkhromov.model.error.exception.SamsDemoException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class LocaleDeserializer extends JsonDeserializer<LocaleEnum> {

    @Override
    public LocaleEnum deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        LocaleEnum localeEnum;
        try {
            localeEnum = LocaleEnum.valueOf(jsonParser.getText().toUpperCase());
        } catch (Exception ex) {

            log.error("Bad request exception: locale is invalid, locale = {}", jsonParser.getText());
            throw SamsDemoException.badRequestException(ErrorCode.LOCALE_INVALID, jsonParser.getText());
        }

        return localeEnum;
    }
}