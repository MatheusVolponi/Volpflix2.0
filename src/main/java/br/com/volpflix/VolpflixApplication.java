package br.com.volpflix;

import br.com.volpflix.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VolpflixApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(VolpflixApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main();
		main.showMenu();
	}
}
