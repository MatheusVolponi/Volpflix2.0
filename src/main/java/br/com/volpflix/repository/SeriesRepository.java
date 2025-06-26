package br.com.volpflix.repository;

import br.com.volpflix.model.Category;
import br.com.volpflix.model.Episode;
import br.com.volpflix.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTitleContainingIgnoreCase(String seriesName);

    List<Series> findByActorsContainingIgnoreCase(String actorName);

    List<Series> findTop5ByOrderByRateDesc();

    List<Series> findByGenre(Category category);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE e.title ILIKE %:excerptEpisode%")
    List<Episode> episodesPerExcerpt(String excerptEpisode);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.rate DESC LIMIT 5")
    List<Episode> top5EpisodesBySerie(Series series);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series AND YEAR(e.releaseDate) >= :releasedYear")
    List<Episode> episodesAndSeriesPerYear(Series series, int releasedYear);
}
