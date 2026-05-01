package com.locacao.veiculos3.config;

import com.locacao.veiculos3.enums.RoleUsuario;
import com.locacao.veiculos3.model.primary.Usuario;
import com.locacao.veiculos3.repository.primary.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner inicializarUsuarios() {
        return args -> {
            criarUsuarioSeNaoExistir(
                "master",
                "master123",
                RoleUsuario.ROLE_MASTER
            );
            criarUsuarioSeNaoExistir(
                "contributor",
                "contributor123",
                RoleUsuario.ROLE_CONTRIBUTOR
            );
            criarUsuarioSeNaoExistir(
                "auditor",
                "auditor123",
                RoleUsuario.ROLE_AUDITOR
            );

            System.out.println("==============================================");
            System.out.println("  USUÁRIOS INICIALIZADOS COM SUCESSO");
            System.out.println("==============================================");
            System.out.println("  MASTER      → usuario: master      | senha: master123");
            System.out.println("  CONTRIBUTOR → usuario: contributor | senha: contributor123");
            System.out.println("  AUDITOR     → usuario: auditor     | senha: auditor123");
            System.out.println("==============================================");
        };
    }

    private void criarUsuarioSeNaoExistir(String username,
                                          String senha,
                                          RoleUsuario role) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            usuarioRepository.save(new Usuario(
                username,
                passwordEncoder.encode(senha),
                role
            ));
        }
    }
}