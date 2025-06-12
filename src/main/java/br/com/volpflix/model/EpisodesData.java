package br.com.volpflix.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodesData(@JsonAlias("Title") String title,
                           @JsonAlias("Episode") Integer number,
                           @JsonAlias("imdbRating") String rate,
                           @JsonAlias("Released") String releaseDate) {
}