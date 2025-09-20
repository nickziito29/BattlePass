package com.battlepass.api.user;

import com.battlepass.api.config.EmailService;
import com.battlepass.api.exception.EmailAlreadyExistsException;
import com.battlepass.api.security.VerificationToken;
import com.battlepass.api.security.VerificationTokenRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- 1. IMPORTAR
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       VerificationTokenRepository tokenRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDTO registrationData) {
        userRepository.findByEmail(registrationData.email()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email '" + registrationData.email() + "' is already registered.");
        });

        User newUser = mapDtoToEntity(registrationData);
        User savedUser = userRepository.save(newUser);
        String tokenValue = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(tokenValue, savedUser);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(savedUser.getEmail(), tokenValue);

        return savedUser;
    }

    @Transactional
    public void verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token de verificação inválido."));

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("Token de verificação expirado.");
        }

        User user = verificationToken.getUser();
        user.setStatus(AccountStatus.ACTIVE); // Ativa o usuário
        userRepository.save(user);

        tokenRepository.delete(verificationToken); // Opcional: limpa o token após o uso
    }

    private User mapDtoToEntity(UserRegistrationDTO dto) {
        var user = new User();
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setNickname(dto.nickname());
        user.setEmail(dto.email());
        user.setBirthDate(dto.birthDate());
        user.setGender(dto.gender());
        user.setPassword(passwordEncoder.encode(dto.password()));

        if (dto.gender() == Gender.CUSTOM) {
            user.setPronoun(dto.pronoun());
            user.setCustomGender(dto.customGender());
        }

        return user;
    }

    @Transactional
    public User updateUserProfile(String email, ProfileUpdateDTO profileData) {
        // Busca o usuário no banco de dados. Se não encontrar, lança uma exceção.
        User userToUpdate = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Atualiza os campos do usuário com os dados do DTO
        userToUpdate.setBio(profileData.bio());
        userToUpdate.setPronoun(profileData.pronoun());
        userToUpdate.setHometown(profileData.hometown());
        userToUpdate.setCurrentCity(profileData.currentCity());

        // O JPA é inteligente: como o objeto userToUpdate está em um contexto transacional,
        // o método save() vai atualizar o registro existente em vez de criar um novo.
        return userRepository.save(userToUpdate);
    }
    // ---------------------------------------------
}