package com.wny.wnyflix.movie.domain;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contents {

    //배경 사진
    private String backdrop_path;

    //장르(문자열)
    private List<String> genres;

    //영화 id
    private int id;

    //영화제목
    private String title;

    //제목 원문
    private String original_title;

    //줄거리
    private String overview;

    //감독
    private String director;

    //배우
    private List<String> cast;

    //인기도
    private float popularity;

    //포스터 링크
    private String poster_path;

    //개봉일
    private String release_date;

    //영화 혹은 TV시리즈
    private String contents_type;

    //평가 점수
    private float vote_average;

    //투표 수
    private int vote_count;

    //
    private List<String> keywords;

    private double score;

    private int show_count;


    public Contents(String backdrop_path, List<String> genres, int id, String title, String original_title, String overview, String director, List<String> cast, float popularity, String poster_path, String release_date, String contents_type, float vote_average, int vote_count, List<String> keywords, float score, int show_count) {
        this.backdrop_path = backdrop_path;
        this.genres = genres;
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.director = director;
        this.cast = cast;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.contents_type = contents_type;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.keywords = keywords;
        this.score = score;
        this.show_count = show_count;
    }

    public Contents(String backdrop_path, List<String> genres, int id, String title, String original_title, String overview, String director, List<String> cast, float popularity, String poster_path, String release_date, String contents_type, float vote_average, int vote_count, List<String> keywords, int show_count) {
        this.backdrop_path = backdrop_path;
        this.genres = genres;
        this.id = id;
        this.title = title;
        this.original_title = original_title;
        this.overview = overview;
        this.director = director;
        this.cast = cast;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.contents_type = contents_type;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
        this.keywords = keywords;
        this.show_count = show_count;
    }

    public Contents(Contents content, Double score) {
    }
}
