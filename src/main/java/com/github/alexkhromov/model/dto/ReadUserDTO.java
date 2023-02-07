package com.github.alexkhromov.model.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ReadUserDTO extends BaseDTO {

    private Long id;
    private String email;
    private String username;
    private Boolean isDeleted;
    private List<String> roles;
    private List<String> questionLinks;
}