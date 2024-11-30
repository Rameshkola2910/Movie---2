/*
 * You can use the following import statements
 *
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.http.HttpStatus;
 * import org.springframework.jdbc.core.JdbcTemplate;
 * import org.springframework.stereotype.Service;
 * import org.springframework.web.server.ResponseStatusException;
 * import java.util.*;
 *
 */

package com.example.movie.service;

import com.example.movie.model.Movie;
import com.example.movie.model.MovieRowMapper;
import com.example.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class MovieH2Service implements MovieRepository {
    @Autowired
    private JdbcTemplate db;

    public ArrayList<Movie> getMovies() {
        return (ArrayList<Movie>) db.query("select * from movieList", new MovieRowMapper());
    }

    public Movie getMovieById(int movieId) {
        try {
            return db.queryForObject("select * from movieList where movieId = ?", new MovieRowMapper(), movieId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Movie addMovie(Movie movie) {
        db.update("insert into movieList(movieName, leadActor) values(?,?)", movie.getMovieName(),
                movie.getLeadActor());
        return db.queryForObject("select * from movieList where movieName = ? and leadActor = ?", new MovieRowMapper(),
                movie.getMovieName(), movie.getLeadActor());
    }

    public Movie updateMovie(int movieId, Movie movie) {
        if (movie.getMovieName() != null) {
            db.update("update movieList set movieName = ? where movieId = ?", movie.getMovieName(), movieId);
        }
        if (movie.getLeadActor() != null) {
            db.update("update movieList set leadActor = ? where movieId = ?", movie.getLeadActor(), movieId);
        }
        return getMovieById(movieId);
    }

    public void deleteMovie(int movieId) {
        db.update("delete from movieList where movieId = ?", movieId);
    }
}
