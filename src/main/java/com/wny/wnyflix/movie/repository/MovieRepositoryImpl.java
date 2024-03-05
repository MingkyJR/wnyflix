package com.wny.wnyflix.movie.repository;

import com.wny.wnyflix.es.EsApi;
import com.wny.wnyflix.movie.domain.Contents;
import com.wny.wnyflix.movie.domain.Terms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class MovieRepositoryImpl implements MovieRepository{

    @Autowired
    EsApi esApi = new EsApi();

    @Override
    public List<Contents> getContentsList() {
        return esApi.esGetContentsList();
    }

    @Override
    public Contents getContentsInfoById(String id) {
         return esApi.esGetContentsInfoById(id);
    }

    @Override
    public Map<String, Object> getContentsByQuery(String query) {
        return esApi.esGetContentsByQuery(query);
    }

    @Override
    public Map<String, Object> getContentsByQueryFilterGenres(String query) {
        return esApi.esGetContentsByQueryFilterGenres(query);
    }

    @Override
    public List<Terms> getTopQuery() {
        return esApi.esGetTopQuery();
    }

    @Override
    public List<Contents> getTopContentsList(String country_code) {
        return esApi.esGetTopContentsList(country_code);
    }

    @Override
    public List<Contents> getRecentContentsList() {
        return esApi.esGetRecentContentsList();
    }

    @Override
    public List<Contents> getPlayingContentsByUserId(String userId) {
        return esApi.esGetPlayingContentsByUserId(userId);
    }

    @Override
    public String fake() {
        return esApi.fake();
    }


}
