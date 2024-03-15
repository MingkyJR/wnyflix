package com.wny.wnyflix.movie.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.wny.wnyflix.es.EsClient;
import com.wny.wnyflix.movie.MovieApi;
import com.wny.wnyflix.movie.domain.Contents;
import com.wny.wnyflix.movie.domain.Terms;
import com.wny.wnyflix.movie.service.MovieService;
import com.wny.wnyflix.user.domain.User;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Controller
public class MovieDataController {

    private final MovieService movieService;


    @Autowired
    public MovieDataController(MovieService movieService) {
        this.movieService = movieService;
    }

    @WithSpan
    @GetMapping("/movie")
    public String getMovieData(Model model, Locale locale, HttpServletRequest request) {

        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("AUTHUSER");
        List<Contents> topContentsList = movieService.getTopContentsList(user.getCountry_iso_code());
        log.info("topContents : {}", topContentsList.size());
        List<Contents> recentContentsList = movieService.getRecentContentsList();
        List<Contents> playingContentsList = movieService.getPlayingContentsByUserId(user.getUserId());
        log.info("playing : {}", playingContentsList.size());
        List<Terms> topQuery = movieService.getTopQuery();
        model.addAttribute("topContentsList", topContentsList);
        model.addAttribute("topQueryTermsList", topQuery);
        model.addAttribute("recentContentsList", recentContentsList);
        model.addAttribute("playingContentsList", playingContentsList);
        model.addAttribute("country", user.getCountry_name());




        return "movieMain/movie";
    }

    @GetMapping("/contentInfo")
    public String contentInfo(Model model, String id) {
        Contents content = movieService.getContentsInfoById(id);
        log.info("contentInfo : {}, {}", content.getId(), content.getOriginal_title());
        model.addAttribute("content", content);
        return "movieMain/contentInfo";
    }

    @GetMapping("/play")
    @ResponseBody
    public Map<String, String> play(String playId, String userId) {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
        String formatedNow = now.format(formatter);
        Map<String, String> result = new HashMap<>();
        log.info("playId : {}, userId : {}", playId, userId);
        result.put("status", "재생됨");
        result.put("time", formatedNow);
        return result;
    }

    @GetMapping("/putMovieData")
    public String putMovieData(String type, int start, int end) {
        MovieApi movieApi = new MovieApi();


        for (int i = start; i <= end; i++){
            List<Contents> list = movieApi.getMovieData(type, i);

            try {
                BulkRequest.Builder br = new BulkRequest.Builder();
                EsClient esClient = new EsClient();
                ElasticsearchClient client = esClient.client();


                for (Contents infolist : list) {
                    br.operations(op -> op
                            .index(idx -> idx
                                    .index("wy_wnyflix_data_exx")
                                    .id(String.valueOf(infolist.getId()))
                                    .document(infolist)
                            )
                    );
                }
                BulkResponse result = client.bulk(br.build());
                log.info(result.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info("page {}",i);
        }

        return "home";

    }

    @GetMapping("/fake")
    public String fake(Model model) {
        String error = movieService.fake();

        log.error("API 요청 error 발생");
        return "movieMain/contentInfo";
    }
}
