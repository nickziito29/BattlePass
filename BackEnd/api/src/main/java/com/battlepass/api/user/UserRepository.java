package com.battlepass.api.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

//Esta interface nos fornece todas as operações
// padrão de banco de dados (salvar, localizar, excluir, atualizar)
// gratuitamente.
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);
}
