package com.battlepass.api.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter; // Alterado de @Data
import lombok.NoArgsConstructor;
import lombok.Setter; // Alterado de @Data
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter // MELHORIA: Usar anotações específicas em vez de @Data
@Setter // MELHORIA: Usar anotações específicas em vez de @Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

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

    // --- Campos de Perfil ---
    private String profilePictureUrl;

    @Lob
    @Column(columnDefinition = "TEXT") // Diz ao Hibernate para usar o tipo TEXT
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

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.status == null) {
            if (this.password != null && this.password.startsWith("oauth2_")) {
                this.status = AccountStatus.ACTIVE;
            } else {
                this.status = AccountStatus.PENDING_VERIFICATION;
            }
        }
    }

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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != AccountStatus.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == AccountStatus.ACTIVE;
    }
}