package com.battlepass.api.security;

import com.battlepass.api.user.User;
import com.battlepass.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = createNewUserFromOAuth(oAuth2User);
            return userRepository.save(newUser);
        });

        user.setAttributes(oAuth2User.getAttributes());
        return user;
    }

    private User createNewUserFromOAuth(OAuth2User oAuth2User) {
        User newUser = new User();
        newUser.setEmail(oAuth2User.getAttribute("email"));
        String name = oAuth2User.getAttribute("name");
        if (name != null && !name.isEmpty()) {
            String[] names = name.split(" ", 2);
            newUser.setFirstName(names[0]);
            newUser.setLastName(names.length > 1 ? names[1] : "");
        } else {
            newUser.setFirstName("Usuário");
            newUser.setLastName("BattlePass");
        }
        newUser.setProfilePictureUrl(oAuth2User.getAttribute("picture"));
        newUser.setPassword("oauth2_placeholder_password_" + System.currentTimeMillis());
        return newUser;
    }
}