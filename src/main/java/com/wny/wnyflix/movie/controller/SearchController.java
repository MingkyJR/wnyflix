package com.wny.wnyflix.movie.controller;


import com.wny.wnyflix.movie.domain.Terms;
import com.wny.wnyflix.movie.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class SearchController {

    private final MovieService movieService;

    @Autowired
    public SearchController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/search")
    public String searchByQuery(Model model, String query) {
        Map<String, Object> map = movieService.getContentsByQuery(query);
        List<Terms> terms = movieService.getTopQuery();
        model.addAttribute("query", query);
        model.addAttribute("contentsList", map.get("contentsList"));
        model.addAttribute("genresTermsList", map.get("genresTermsList"));
        model.addAttribute("topQueryTermsList", terms);
        log.info("search query : {}", query);


        return "search";
    }

    @GetMapping("/searchFilterGenres")
    public String searchByQueryFilterGenres(Model model, String query) {
        Map<String, Object> map = movieService.getContentsByQueryFilterGenres(query);
        model.addAttribute("query", query);
        model.addAttribute("contentsList", map.get("contentsList"));
        model.addAttribute("genreList", map.get("genresList"));

        return "search";
    }

}