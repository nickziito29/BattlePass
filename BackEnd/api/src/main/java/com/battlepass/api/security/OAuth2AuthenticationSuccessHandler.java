package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // IMPORTAR
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final String frontendSuccessUrl; // MELHORIA: Campo para a URL

    @Autowired
    public OAuth2AuthenticationSuccessHandler(TokenService tokenService,
                                              UserRepository userRepository,
                                              @Value("${oauth2.redirect.success.url}") String frontendSuccessUrl) { // MELHORIA: Injetar via @Value
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.frontendSuccessUrl = frontendSuccessUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found in database, which should not happen."));

        String token = tokenService.generateToken(user);

        // MELHORIA: Usa a URL injetada ao invés de uma "hardcoded"
        String targetUrl = UriComponentsBuilder.fromUriString(frontendSuccessUrl)
                .queryParam("token", token)
                .build().toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}