package com.example.lojacarro;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    public Usuario findBylogin(String login);

    public Usuario findBySenha(String senha);
}
