package br.com.volpflix.model;

import br.com.volpflix.service.IaConsult;

import java.util.OptionalDouble;

public class Series {
    private String title;
    private Integer seasons;
    private Double rate;
    private Category genre;
    private String actors;
    private String poster;
    private String plot;

    public Series(SeriesData data){
        this.title = data.title();
        this.seasons = data.seasons();
        this.rate = OptionalDouble.of(Double.valueOf(data.rate())).orElse(0);
        this.genre = Category.fromString(data.genre().split(",")[0].trim());
        this.actors = data.actors();
        this.poster = data.poster();
        this.plot = IaConsult.getTranslate(data.plot()).trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Título: " + title +
                ", Temporadas: " + seasons +
                ", Avaliação: " + rate +
                ", Gênero: " + genre +
                ", Atores: '" + actors +
                ", Poster: " + poster +
                ", Sinopse: " + plot;
    }
}
