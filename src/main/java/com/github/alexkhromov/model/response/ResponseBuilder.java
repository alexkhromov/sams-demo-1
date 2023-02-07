package com.github.alexkhromov.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alexkhromov.model.dto.BaseDTO;
import com.github.alexkhromov.model.entity.BaseEntity;
import com.github.alexkhromov.model.error.ErrorMessage;
import com.github.alexkhromov.model.mapper.IDTOMapper;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.github.alexkhromov.model.response.enums.ResponseStatus.FAILURE;
import static com.github.alexkhromov.model.response.enums.ResponseStatus.SUCCESS;

@Data
public class ResponseBuilder<DTO extends BaseDTO, ENTITY extends BaseEntity> {

    private SamsDemoResponse<DTO> response;
    private HttpHeaders headers;
    private HttpStatus httpStatus;

    private ResponseBuilder() {
        this.headers = new HttpHeaders();
    }

    private ResponseBuilder(SamsDemoResponse<DTO> response) {

        this();
        this.response = response;
    }

    @SuppressWarnings("unchecked")
    public static  ResponseBuilder failure() {

        SamsDemoResponse response = new SamsDemoResponse<>(FAILURE);
        return new ResponseBuilder(response);
    }

    public static <DTO extends BaseDTO, ENTITY extends BaseEntity> ResponseBuilder<DTO, ENTITY> success() {

        SamsDemoResponse<DTO> response = new SamsDemoResponse<>(SUCCESS);
        return new ResponseBuilder<>(response);
    }

    public static ResponseBuilder empty() {
        return new ResponseBuilder();
    }

    public ResponseBuilder<DTO, ENTITY> withPageData(Page<DTO> page) {

        this.response.setData(page.getContent());
        this.response.setTotal(page.getTotalElements());

        return this;
    }

    public ResponseBuilder<DTO, ENTITY> withPageData(Page<ENTITY> page, IDTOMapper<DTO, ENTITY> mapper) {

        this.response.setData(mapper.mapToDTOList(page.getContent()));
        this.response.setTotal(page.getTotalElements());

        return this;
    }

    public ResponseBuilder<DTO, ENTITY> withData(List<ENTITY> list, IDTOMapper<DTO, ENTITY> mapper) {

        this.response.setData(mapper.mapToDTOList(list));
        this.response.setTotal((long) list.size());

        return this;
    }

    public ResponseBuilder withErrorMessage(List<ErrorMessage> errors) {

        this.response.setErrorData(errors);

        return this;
    }

    public ResponseBuilder withLocation(String path, Long id) {

        URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path(path)
                .build()
                .expand(id)
                .toUri();

        this.headers.setLocation(location);

        return this;
    }

    public ResponseBuilder withAuthorization(String token) {

        this.headers.setBearerAuth(token);

        return this;
    }

    public ResponseBuilder<DTO, ENTITY> withHttpStatus(HttpStatus httpStatus) {

        this.httpStatus = httpStatus;

        return this;
    }

    public ResponseEntity<SamsDemoResponse<DTO>> build() {
        return new ResponseEntity<>(this.response, this.headers, this.httpStatus);
    }

    public String toJson() throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.build().getBody());
    }
}