package br.com.alura.screenmatch;

import br.com.alura.screenmatch.principal.Principal;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class ScreenmatchApplicationSemWeb {

	public static void main(String[] args) {
		try (ConfigurableApplicationContext context = new SpringApplicationBuilder(ScreenmatchApplication.class)
				.web(WebApplicationType.NONE)
				.run(args)) {
			SerieRepository repositorio = context.getBean(SerieRepository.class);
			Principal principal = new Principal(repositorio);
			principal.exibeMenu();
		}
	}
}
