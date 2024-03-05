package com.wny.wnyflix.movie.repository;

import com.wny.wnyflix.movie.domain.Contents;
import com.wny.wnyflix.movie.domain.Terms;

import java.util.List;
import java.util.Map;

public interface MovieRepository {

    public List<Contents> getContentsList();

    public Contents getContentsInfoById(String id);

    public Map<String, Object> getContentsByQuery(String query);

    public Map<String, Object> getContentsByQueryFilterGenres(String query);

    public List<Terms> getTopQuery();

    public List<Contents> getTopContentsList(String country_code);

    public List<Contents> getRecentContentsList();

    public List<Contents> getPlayingContentsByUserId(String userId);

    public String fake();

}
