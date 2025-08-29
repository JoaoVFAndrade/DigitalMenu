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

}
