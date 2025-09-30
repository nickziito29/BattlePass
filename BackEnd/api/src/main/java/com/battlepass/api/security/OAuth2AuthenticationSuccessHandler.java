package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserRepository; // <-- NOVO IMPORT
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User; // <-- NOVO IMPORT
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository; // <-- ADICIONADO
    private final String frontendSuccessUrl;

    @Autowired
    public OAuth2AuthenticationSuccessHandler(TokenService tokenService,
                                              UserRepository userRepository, // <-- ADICIONADO
                                              @Value("${oauth2.redirect.success.url}") String frontendSuccessUrl) {
        this.tokenService = tokenService;
        this.userRepository = userRepository; // <-- ADICIONADO
        this.frontendSuccessUrl = frontendSuccessUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 1. Pega o objeto padrão do Spring Security
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // 2. USA O EMAIL PARA BUSCAR NOSSO USUÁRIO NO BANCO (AGORA VAI FUNCIONAR!)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário do Google não encontrado no banco de dados após o login."));

        // 3. Gera o token a partir do nosso objeto User
        String token = tokenService.generateToken(user);

        String targetUrl = UriComponentsBuilder.fromUriString(frontendSuccessUrl)
                .queryParam("token", token)
                .build().toUriString();

        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}