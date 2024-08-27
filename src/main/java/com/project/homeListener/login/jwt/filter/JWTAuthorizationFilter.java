package com.project.homeListener.login.jwt.filter;

import com.project.homeListener.login.jwt.entity.UserInformInToken;
import com.project.homeListener.login.jwt.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final RequestMatcher excludeRequestMatcher;


    public JWTAuthorizationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.excludeRequestMatcher = new OrRequestMatcher(
                new AntPathRequestMatcher("/user", HttpMethod.POST.name()),

                new AntPathRequestMatcher("/token/reIssue", HttpMethod.POST.name()),

                new AntPathRequestMatcher("/", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/login", HttpMethod.GET.name()), //TODO form login 제외 시 해제

                // swagger
                new AntPathRequestMatcher("/swagger-ui/**", HttpMethod.GET.name()),
                new AntPathRequestMatcher("/v3/api-docs/**", HttpMethod.GET.name())
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optionalAccessToken = jwtUtil.getAccessTokenByRequest(request);
        if (optionalAccessToken.isEmpty()) {
            log.error("Access Token is not exist");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Access Token is not exist");
            return;
        }

        String accessToken = optionalAccessToken.get();
        if (!jwtUtil.validateAccessToken(accessToken)) {
            log.error("Access Token is not valid");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Token is not valid");
            return;
        }

        // security context에 유저 정보 저장
        UserInformInToken userInform = jwtUtil.getUserInformInAccessToken(accessToken);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userInform,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeRequestMatcher.matches(request);
    }
}
