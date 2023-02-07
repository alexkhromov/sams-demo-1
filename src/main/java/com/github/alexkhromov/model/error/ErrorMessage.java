package com.github.alexkhromov.model.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
@JsonPropertyOrder({"field", "message", "description"})
public class ErrorMessage {

    private String message;
    private String field;
    private String description;
}