package br.com.volpflix.main;

import br.com.volpflix.model.*;
import br.com.volpflix.repository.SeriesRepository;
import br.com.volpflix.service.ApiConsuption;
import br.com.volpflix.service.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;

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
    private List<SeriesData> seriesData = new ArrayList<>();
    private SeriesRepository repository;

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
        seriesData.add(data);
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
        SeriesData seriesData = getSeriesData();
        List<SeasonsData> seasons = new ArrayList<>();

        for (int i = 1; i <= seriesData.seasons(); i++) {
            var json = apiConsuption.getData(ADDRESS + seriesData.title().replace(" ", "+") + "&season=" + i + API_KEY);
            SeasonsData seasonsData = converter.getData(json, SeasonsData.class);
            seasons.add(seasonsData);
        }
        seasons.forEach(System.out::println);
    }

    private void listSearchSeries(){
        List<Series> series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }
}