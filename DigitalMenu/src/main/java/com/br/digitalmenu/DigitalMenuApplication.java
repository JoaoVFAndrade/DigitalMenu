package com.br.digitalmenu;

import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.model.RoleNome;
import com.br.digitalmenu.repository.FuncionarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class DigitalMenuApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalMenuApplication.class, args);
	}
    //@Todo deixar esse comandline para poder acessar a aplicacao
    @Bean
    CommandLineRunner init(FuncionarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@email.com").isEmpty()) {
                Funcionario admin = new Funcionario();
                admin.setNome("Administrador");
                admin.setEmail("admin@email.com");
                admin.setSenha(encoder.encode("admin123"));
                admin.setRoles(Set.of(RoleNome.FUNCIONARIO_ADM));
                repo.save(admin);

                System.out.println("✅ Usuário ADMIN criado com sucesso!");
            }
        };
    }

}
