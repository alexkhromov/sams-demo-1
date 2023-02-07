package com.github.alexkhromov.web.filter;

import com.github.alexkhromov.common.ApplicationConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class SkipUriFilter {

    private String basePath;

    private Pattern skipGetUriCheckPattern;
    private Pattern skipPostUriCheckPattern;

    public SkipUriFilter(@Value("${base.path}") String basePath,
                         @Value("${skipGetUri}") String [] skipGetUri,
                         @Value("${skipPostUri}") String [] skipPostUri) {

        this.basePath = basePath;
        this.skipGetUriCheckPattern = getPattern(skipGetUri);
        this.skipPostUriCheckPattern = getPattern(skipPostUri);
    }

    public boolean skipUriCheck(RequestWrapper requestWrapper) {

        String authorizationHeader = requestWrapper.getRequest().getHeader(AUTHORIZATION);

        boolean isAuthorizationPresent =
                isNotBlank(authorizationHeader) && authorizationHeader.startsWith(ApplicationConstant.BEARER_PREFIX);

            boolean skipUri =
                (GET.name().equals(requestWrapper.getRequest().getMethod())
                    && skipGetUriCheckPattern.matcher(requestWrapper.getRequest().getRequestURI()).matches())
                ||
                (POST.name().equals(requestWrapper.getRequest().getMethod())
                    && skipPostUriCheckPattern.matcher(requestWrapper.getRequest().getRequestURI()).matches());

        return skipUri && !isAuthorizationPresent;
    }

    private Pattern getPattern(String [] uri) {
        return compile(of(uri).collect(joining("|", format("^%s(", basePath), ")$")));
    }
}