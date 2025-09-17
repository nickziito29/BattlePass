package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Pega os dados brutos do Google
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 2. Extrai os atributos essenciais
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String profilePictureUrl = oAuth2User.getAttribute("picture");

        // 3. Garante que o usuário exista no nosso banco de dados.
        //    Se não existir, o método createNewUserFromOAuth irá criá-lo.
        userRepository.findByEmail(email).orElseGet(() ->
                createNewUserFromOAuth(email, name, profilePictureUrl)
        );

        // 4. RETORNA O OBJETO OAUTH2 PADRÃO.
        //    A transação garante que o usuário já foi salvo no banco antes
        //    do SuccessHandler ser chamado.
        return oAuth2User;
    }

    private User createNewUserFromOAuth(String email, String name, String profilePictureUrl) {
        User newUser = new User();
        newUser.setEmail(email);

        if (name != null && !name.isEmpty()) {
            String[] names = name.split(" ", 2);
            newUser.setFirstName(names[0]);
            newUser.setLastName(names.length > 1 ? names[1] : "");
        } else {
            newUser.setFirstName("Usuário");
            newUser.setLastName("BattlePass");
        }

        newUser.setProfilePictureUrl(profilePictureUrl);
        newUser.setPassword("oauth2_placeholder_password_" + System.currentTimeMillis());

        return userRepository.save(newUser);
    }
}