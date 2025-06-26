package br.com.volpflix.main;

import br.com.volpflix.model.*;
import br.com.volpflix.repository.SeriesRepository;
import br.com.volpflix.service.ApiConsuption;
import br.com.volpflix.service.DataConverter;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner read = new Scanner(System.in);
    private ApiConsuption apiConsuption = new ApiConsuption();
    private DataConverter converter = new DataConverter();

    private final String ADDRESS = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=bd022199";
    private List<SeriesData> seriesData = new ArrayList<>();
    private SeriesRepository repository;
    private List<Series> series = new ArrayList<>();
    private Optional<Series> researchedSeries;

    public Main(SeriesRepository repository) {
        this.repository = repository;
    }

    public void showMenu() {
        var option = -1;
        while (option != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 Séries
                    7 - Buscar séries por categoria
                    8 - Buscar episódio por trecho
                    9 - Top 5 Episódios por Série
                    10 - Buscar episódios a partir de uma data.
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            option = read.nextInt();
            read.nextLine();

            switch (option) {
                case 1:
                    searchSeriesWeb();
                    break;
                case 2:
                    searchEpisodePerSeries();
                    break;
                case 3:
                    listSearchSeries();
                    break;
                case 4:
                    searchSeriesPerTitle();
                    break;
                case 5:
                    searchSeriesPerActor();
                    break;
                case 6:
                    searchTop5Series();
                    break;
                case 7:
                    searchSeriesPerGenre();
                    break;
                case 8:
                    searchEpisodeByExcerpt();
                    break;
                case 9:
                    searchTop5EpisodesPerSeries();
                    break;
                case 10:
                    searchEpisodesAfterDate();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void searchSeriesWeb() {
        SeriesData data = getSeriesData();
        Series series = new Series(data);
        repository.save(series);
        System.out.println(data);
    }


    private SeriesData getSeriesData() {
        System.out.println("Digite uma série para busca: ");
        var search = read.nextLine();
        String encodedSearch = URLEncoder.encode(search, StandardCharsets.UTF_8);
        var json = apiConsuption.getData(ADDRESS + encodedSearch + API_KEY);
        SeriesData data = converter.getData(json, SeriesData.class);
        return data;
    }

    private void searchEpisodePerSeries() {
        listSearchSeries();
        System.out.println("Escolha uma série pelo nome: ");
        var nameSeries = read.nextLine();
        Optional<Series> first =  repository.findByTitleContainingIgnoreCase(nameSeries);

        if(first.isPresent()){
            var seriesFound = first.get();
            List<SeasonsData> seasons = new ArrayList<>();

            for (int i = 1; i <= seriesFound.getSeasons(); i++) {
            var json = apiConsuption.getData(ADDRESS + seriesFound.getTitle().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonsData seasonsData = converter.getData(json, SeasonsData.class);
            seasons.add(seasonsData);
            }
            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodes().stream()
                            .map(e -> new Episode(d.season(), e)))
                    .collect(Collectors.toList());
            seriesFound.setEpisodes(episodes);
            repository.save(seriesFound);
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void searchSeriesPerTitle() {
        System.out.println("Escolha uma série pelo nome: ");
        var nameSeries = read.nextLine();
        researchedSeries = repository.findByTitleContainingIgnoreCase(nameSeries);

        if(researchedSeries.isPresent()){
            System.out.println("Dados da série: " + researchedSeries.get());
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void searchSeriesPerActor() {
        System.out.println("Qual o nome para busca?: ");
        var actorName = read.nextLine();
        List<Series> foundedSeries = repository.findByActorsContainingIgnoreCase(actorName);
        System.out.println("Séries em que " + actorName + " atuou: ");
        foundedSeries.forEach(s -> System.out.println(s.getTitle() + " Avaliação: " + s.getRate()));
    }

    private void searchTop5Series() {
        List<Series> topSeries = repository.findTop5ByOrderByRateDesc();
        topSeries.forEach(s -> System.out.println(s.getTitle() + " Avaliação: " + s.getRate()));
    }

    private void searchSeriesPerGenre() {
        System.out.println("Qual categoria você deseja pesquisar?: ");
        var genreName = read.nextLine();
        Category category = Category.fromPortuguese(genreName);
        List<Series> seriesPerGenre = repository.findByGenre(category);
        System.out.println("Séries da categoria " + genreName);
        seriesPerGenre.forEach(System.out::println);
    }

    private void listSearchSeries(){
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }


    private void searchEpisodeByExcerpt() {
        System.out.println("Qual o nome do episódio para busca?: ");
        var excerptEpisode = read.nextLine();
        List<Episode> episodesFound = repository.episodesPerExcerpt(excerptEpisode);
        episodesFound.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSeries().getTitle(), e.getSeason(),
                        e.getNumber(), e.getTitle()));
    }

    private void searchTop5EpisodesPerSeries() {
        searchSeriesPerTitle();
        if (researchedSeries.isPresent()){
            Series series = researchedSeries.get();
            List<Episode> topEpisodes = repository.top5EpisodesBySerie(series);
            topEpisodes.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSeries().getTitle(), e.getSeason(),
                            e.getNumber(), e.getTitle(), e.getRate()));
        }
    }

    private void searchEpisodesAfterDate() {
        searchSeriesPerTitle();
        if (researchedSeries.isPresent()){
            Series series = researchedSeries.get();
            System.out.println("Digite o ano limite de lançamento para busca: ");
            var releasedYear = read.nextInt();
            read.nextLine();

            List<Episode> dateEpisodes = repository.episodesAndSeriesPerYear(series, releasedYear);
            dateEpisodes.forEach(System.out::println);
        }
    }
}