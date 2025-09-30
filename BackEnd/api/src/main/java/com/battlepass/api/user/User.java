package com.battlepass.api.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String profilePictureUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String bio;

    private String pronoun;
    private String customGender;
    private String hometown;
    private String currentCity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isNewUser = true;

    @Transient
    private Map<String, Object> attributes;

    // --- Métodos da Interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Contas não expiram
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != AccountStatus.SUSPENDED; // A conta está "não bloqueada" se não estiver suspensa
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais (senhas) não expiram
    }

    // ==============================================================================
    // A MUDANÇA CRÍTICA ESTÁ AQUI
    // ==============================================================================
    @Override
    public boolean isEnabled() {
        // A conta está "habilitada" para login contanto que não esteja suspensa.
        // O status de verificação de e-mail (PENDING_VERIFICATION vs ACTIVE)
        // é uma checagem separada que faremos na UI, mas não impede o login inicial.
        return this.status != AccountStatus.SUSPENDED;
    }

    // --- Métodos da Interface OAuth2User ---

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    // --- Callbacks do JPA ---

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = this.password != null && this.password.startsWith("oauth2_")
                    ? AccountStatus.ACTIVE
                    : AccountStatus.PENDING_VERIFICATION;
        }
        if (this.isNewUser == null) {
            this.isNewUser = true;
        }
    }
}