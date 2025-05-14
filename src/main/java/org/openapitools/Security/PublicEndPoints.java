package org.openapitools.Security;

import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;


@Component
@Getter
public final class PublicEndPoints {
    private final RequestMatcher[] matchers = new RequestMatcher[]{
            new AntPathRequestMatcher("/public/**"),
            new AntPathRequestMatcher("/auth/login", HttpMethod.POST.name()),
            new AntPathRequestMatcher("/usuarios", HttpMethod.POST.name())
    };
}
