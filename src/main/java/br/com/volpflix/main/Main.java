package br.com.volpflix.main;

import br.com.volpflix.model.EpisodesData;
import br.com.volpflix.model.SeriesData;
import br.com.volpflix.model.SeasonsData;
import br.com.volpflix.model.Episode;
import br.com.volpflix.service.ApiConsuption;
import br.com.volpflix.service.DataConverter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner read = new Scanner(System.in);
    private ApiConsuption apiConsuption = new ApiConsuption();
    private DataConverter converter = new DataConverter();

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=bd022199";

    public void showMenu(){
        System.out.println("Digite o nome de uma série para busca:");
        var search = read.nextLine();
        String encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8);
        var json = apiConsuption.getData(ADDRESS + encodedSearch + API_KEY);
        SeriesData data = converter.getData(json, SeriesData.class);
        System.out.println(data);

        List<SeasonsData> seasons = new ArrayList<>();

        for (int i = 1; i<= data.seasons(); i++){
            json = apiConsuption.getData(ADDRESS + encodedSearch +"&season=" + i + API_KEY);
            SeasonsData seasonsData = converter.getData(json, SeasonsData.class);
            seasons.add(seasonsData);
        }
        seasons.forEach(System.out::println);

        seasons.forEach(t -> t.episodes().forEach(e -> System.out.println(e.title())));

        List<EpisodesData> episodesData = seasons.stream()
                .flatMap(t -> t.episodes().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episódios");
        episodesData.stream()
                .filter(e -> !e.rate().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(EpisodesData::rate).reversed())
                .limit(10)
                .map(e -> e.title().toUpperCase())
                .forEach(System.out::println);

        List<Episode> episodes = seasons.stream()
                .flatMap(t -> t.episodes().stream()
                        .map(d -> new Episode(t.season(), d)))
                .collect(Collectors.toList());

        episodes.forEach(System.out::println);

        System.out.println("Digite um título de episódio: ");
        var excertTitle = read.nextLine();
        Optional<Episode> searchedEpisode = episodes.stream()
                .filter(e -> e.getTitle().toUpperCase().contains(excertTitle.toUpperCase()))
                .findFirst();
        if (searchedEpisode.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + searchedEpisode.get().getSeason());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var year = read.nextInt();
        read.nextLine();

        LocalDate searchDay = LocalDate.of(year, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodes.stream()
                .filter(e -> e.getReleaseDate() != null && e.getReleaseDate().isAfter(searchDay))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getSeason() +
                                " Episódio: " + e.getTitle() +
                                " Data de Lançamento: " + e.getReleaseDate().format(dtf)
                ));

        Map<Integer, Double> reviewsPerSeason = episodes.stream()
                .filter(e -> e.getRate()> 0.0)
                .collect(Collectors.groupingBy(Episode::getSeason, Collectors.averagingDouble(Episode::getRate)));
        System.out.println(reviewsPerSeason);

        DoubleSummaryStatistics est = episodes.stream()
                .filter(e -> e.getRate()> 0.0)
                .collect(Collectors.summarizingDouble(Episode::getRate));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade de episódios avaliados: " + est.getCount());
    }
}