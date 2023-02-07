package com.github.alexkhromov.web.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
@AllArgsConstructor
class RequestWrapper {

    private HttpServletRequest request;
    private HttpServletResponse response;
}