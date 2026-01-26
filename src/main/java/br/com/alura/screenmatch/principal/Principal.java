package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibMenu() {

        System.out.println("Digite o nome da série para a busca:");
        String nomeSerie = leitura.nextLine();

        String json = consumo.obterDados(
                ENDERECO + nomeSerie.replace(" ", "+") + API_KEY
        );

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        // 🔹 Conversão segura do total de temporadas
        int totalTemporadas;
        try {
            totalTemporadas = Integer.parseInt(dados.totalTemporadas());
        } catch (Exception e) {
            System.out.println("Não foi possível obter o total de temporadas.");
            return;
        }

        List<DadosTemporada> temporadas = new ArrayList<>();

        // 🔹 Busca das temporadas
        for (int i = 1; i <= totalTemporadas; i++) {
            json = consumo.obterDados(
                    ENDERECO + nomeSerie.replace(" ", "+") +
                            "&season=" + i + API_KEY
            );

            DadosTemporada dadosTemporada =
                    conversor.obterDados(json, DadosTemporada.class);

            temporadas.add(dadosTemporada);
        }

        // 🔹 Exibe temporadas
        temporadas.forEach(System.out::println);

        // 🔹 Exibe títulos dos episódios
        temporadas.forEach(t ->
                t.episodios().forEach(e ->
                        System.out.println(e.titulo())
                )
        );

        // 🔹 Lista única de episódios
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        // 🔹 Top 10 episódios
//        System.out.println("\nTop 10 melhores episódios:");
//        dadosEpisodios.stream()
//                .filter(e -> !"N/A".equalsIgnoreCase(e.avaliacao()))
//                .peek(e-> System.out.println("Primeiro filtro(N/A " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e-> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e-> System.out.println("Limite " + e))
//                .map(e-> e.titulo().toUpperCase())
//                .peek(e-> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        // 🔹 Converte para classe Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                )
                .collect(Collectors.toList());

        System.out.println("\nEpisódios convertidos:");
        episodios.forEach(System.out::println);

//        System.out.println("Digite um trecho do titulo do episodio que voce quer buscar! ");
//        var trechoTitulo = leitura.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//    if(episodioBuscado.isPresent()){
//        System.out.println("Episodio encontrado! ");
//        System.out.println("Temporada: "+ episodioBuscado.get().getTemporada());
//    } else {
//        System.out.println("Episódio nao encontrado");
//    }


//
//        System.out.println("A partir de que ano voce deseja ver todos os episodios? ");
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano,1,1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e-> e.getDataLancamento() != null &&  e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e-> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                        " Episodio: " + e.getTitulo() +
//                        " Data lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e-> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Média: "+ est.getAverage());
        System.out.println("Melhor episodio: "+ est.getMax());
        System.out.println("Pior episodio: "+ est.getMin());
        System.out.println("Quantidade: "+ est.getCount());
    }
}
