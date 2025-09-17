package com.battlepass.api.user;

import com.battlepass.api.exception.EmailAlreadyExistsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- 1. IMPORTAR
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDTO registrationData) {
        userRepository.findByEmail(registrationData.email()).ifPresent(user -> {
            throw new EmailAlreadyExistsException("Email '" + registrationData.email() + "' is already registered.");
        });

        User newUser = mapDtoToEntity(registrationData);
        return userRepository.save(newUser);
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

    // --- 2. NOVO MÉTODO PARA ATUALIZAR O PERFIL ---
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