package com.wny.wnyflix.movie.service;

import com.wny.wnyflix.movie.domain.Contents;
import com.wny.wnyflix.movie.domain.Terms;
import com.wny.wnyflix.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @Override
    public List<Contents> getContentsList() {
        return movieRepository.getContentsList();
    }

    @Override
    public Contents getContentsInfoById(String id) {
        return movieRepository.getContentsInfoById(id);
    }

    @Override
    public Map<String, Object> getContentsByQuery(String query) {
        return movieRepository.getContentsByQuery(query);
    }

    @Override
    public Map<String, Object> getContentsByQueryFilterGenres(String query) {
        return movieRepository.getContentsByQueryFilterGenres(query);
    }

    @Override
    public List<Terms> getTopQuery() {
        return movieRepository.getTopQuery();
    }

    @Override
    public List<Contents> getTopContentsList(String country_code) {
        return movieRepository.getTopContentsList(country_code);
    }

    @Override
    public List<Contents> getRecentContentsList() {
        return movieRepository.getRecentContentsList();
    }

    @Override
    public List<Contents> getPlayingContentsByUserId(String userId) {
        return movieRepository.getPlayingContentsByUserId(userId);
    }

    @Override
    public String fake() {
        return movieRepository.fake();
    }
}
