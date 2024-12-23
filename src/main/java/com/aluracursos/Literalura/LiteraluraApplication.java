package com.aluracursos.Literalura;

import com.aluracursos.Literalura.Principal.Principal;
import com.aluracursos.Literalura.Repository.AutorRepository;
import com.aluracursos.Literalura.Repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner
{

	public static void main(String[] args)
	{
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Autowired
	private LibroRepository repository;
	@Autowired
	private AutorRepository autorRepository;
	public void run(String[] args) {
		Principal principal = new Principal(repository, autorRepository);
		principal.MuestraElMenu();
	}
}


