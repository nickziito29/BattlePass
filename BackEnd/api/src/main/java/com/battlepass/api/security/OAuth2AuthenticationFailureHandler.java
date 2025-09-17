package com.battlepass.api.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value; // IMPORTAR
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String frontendFailureUrl; // MELHORIA: Campo para a URL

    // MELHORIA: Injetar via construtor com @Value
    public OAuth2AuthenticationFailureHandler(@Value("${oauth2.redirect.failure.url}") String frontendFailureUrl) {
        this.frontendFailureUrl = frontendFailureUrl;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        System.err.println("Authentication Failed: " + exception.getMessage());
        // MELHORIA: Usa a URL injetada ao invés de uma "hardcoded"
        getRedirectStrategy().sendRedirect(request, response, frontendFailureUrl);
    }
}