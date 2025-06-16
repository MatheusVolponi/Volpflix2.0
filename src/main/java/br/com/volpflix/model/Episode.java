package br.com.volpflix.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episode {
    private Integer season;
    private String title;
    private Integer number;
    private Double rate;
    private LocalDate releaseDate;

    public Episode(Integer season, EpisodesData episodesData) {
        this.season = season;
        this.title = episodesData.title();
        this.number = episodesData.number();

        try {
            this.rate = Double.valueOf(episodesData.rate());
        } catch (NumberFormatException ex) {
            this.rate = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(episodesData.releaseDate());
        } catch (DateTimeParseException ex) {
            this.releaseDate = null;
        }
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Temporada: " + season +
                ", Título: " + title + '\'' +
                ", Episódio: " + number +
                ", Avaliação: " + rate +
                ", Data de Lançamento: " + releaseDate;
    }
}
