package br.com.volpflix.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonsData(@JsonAlias("Season") Integer season,
                          @JsonAlias("Episodes") List<EpisodesData> episodes) {
}
