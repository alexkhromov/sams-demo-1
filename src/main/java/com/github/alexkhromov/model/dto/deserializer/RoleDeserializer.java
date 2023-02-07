package com.github.alexkhromov.model.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.alexkhromov.model.enums.Role;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.alexkhromov.model.error.ErrorCode.ROLE_INVALID;
import static com.github.alexkhromov.model.error.exception.SamsDemoException.badRequestException;

@Slf4j
public class RoleDeserializer extends JsonDeserializer<List<Role>> {

    @Override
    public List<Role> deserialize(
            JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        List<Role> roles;
        try {
            List<String> strings = jsonParser.readValueAs(List.class);
            roles = strings
                    .stream()
                    .map(s -> Role.valueOf(s.toUpperCase()))
                    .collect(Collectors.toList());
        } catch (Exception ex) {

            log.error("Bad request exception: role is invalid, role = {}", jsonParser.getText());
            throw badRequestException(ROLE_INVALID, jsonParser.getText());
        }

        return roles;
    }
}