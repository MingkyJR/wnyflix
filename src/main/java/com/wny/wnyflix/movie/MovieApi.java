package com.wny.wnyflix.movie;

import com.wny.wnyflix.movie.domain.Contents;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MovieApi {

        @Value("${tmdb.api}")
        String key;

    public List<Contents> getMovieData(String type, int page) {
        List<Contents> infoList = null;
        List<String> genreList = null;

        try {
            infoList = new ArrayList<Contents>();
            //for (int i = start; i <= end; i++) {
                String apiURL = "https://api.themoviedb.org/3/discover/" + type + "?api_key=" + key
                        + "&include_adult=false&include_video=false&sort_by=popularity.desc&language=ko-KR&page=" + page;
                URL url = new URL(apiURL);
                BufferedReader bf;
                String result = "";
                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                result = bf.readLine();
                String date = "0001-01-01";

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONArray list = (JSONArray) jsonObject.get("results");

                for (int j = 0; j < list.size(); j++) {
                    Contents vo = new Contents();
                    JSONObject contents = (JSONObject) list.get(j);

                    vo.setId(Integer.parseInt(String.valueOf(contents.get("id"))));
                    vo.setContents_type(type);
                    vo.setOverview(contents.get("overview").toString());
                    vo.setVote_average(Float.parseFloat(String.valueOf(contents.get("vote_average"))));
                    vo.setVote_count(Integer.parseInt(String.valueOf(contents.get("vote_count"))));
                    vo.setPopularity(Float.parseFloat(String.valueOf(contents.get("popularity"))));




                    if (type.equals("movie")) {

                        // 시리즈일 경우 release_date를 key로 데이터 파싱
                        if (contents.get("release_date") == null || contents.get("release_date").equals("")) {
                            vo.setRelease_date(date);
                        } else {
                            vo.setRelease_date(String.valueOf(contents.get("release_date")));
                        }
                        vo.setTitle(contents.get("title").toString());
                        vo.setOriginal_title(contents.get("original_title").toString());
                    } else if (type.equals("tv")) {

                        // 시리즈일 경우 first_air_date를 key로 데이터 파싱
                        if (contents.get("first_air_date") == null || contents.get("first_air_date").equals("")) {
                            vo.setRelease_date(date);
                        } else {
                            vo.setRelease_date(String.valueOf(contents.get("first_air_date")));
                        }

                        // 시리즈일 경우 title이 아닌 name을 key로 데이터 파싱
                        vo.setTitle(contents.get("name").toString());
                        vo.setOriginal_title(contents.get("original_name").toString());
                    }
                    if (contents.get("poster_path") == null || contents.get("poster_path").toString().equals("")) {
                        vo.setPoster_path("");
                    } else {
                        vo.setPoster_path(contents.get("poster_path").toString());
                    }
                    if (contents.get("backdrop_path") == null || contents.get("backdrop_path").toString().equals("")) {
                        vo.setBackdrop_path("");
                    } else {
                        vo.setBackdrop_path(contents.get("backdrop_path").toString());
                    }

                    // 장르 id를 List<integer> 형태로 저장 → 장르 비교를 위한 작업
                    JSONArray genre_list = (JSONArray) contents.get("genre_ids");
                    genreList = new ArrayList<String>();

                    String[][] genreMapping = {
                            {"12", "어드벤처"},
                            {"16", "애니메이션"},
                            {"18", "드라마"},
                            {"28", "액션"},
                            {"35", "코미디"},
                            {"80", "범죄"},
                            {"99", "다큐멘터리"},
                            {"10751", "가족"},
                            {"14", "판타지"},
                            {"36", "역사"},
                            {"10402", "음악"},
                            {"9648", "미스터리"},
                            {"10749", "로맨스"},
                            {"878", "공상과학"},
                            {"10770", "TV무비"},
                            {"53", "스릴러"},
                            {"10752", "전쟁"},
                            {"37", "Western"}
                    };

                    for (int k = 0; k < genre_list.size(); k++) {
                        for (int ge = 0; ge <genreMapping.length; ge++){
                            if(genreMapping[ge][0].equals(String.valueOf(genre_list.get(k)))) {
                                genreList.add(genreMapping[ge][1]);
                            }
                        }
                    }

                    vo.setGenres(genreList);

                    String creditURL = "https://api.themoviedb.org/3/"+ type +"/" + String.valueOf(contents.get("id")) + "/credits?language=ko-KR&api_key=" + key;
                    String creditResult = "";
                    URL url2 = new URL(creditURL);
                    BufferedReader bf2 = new BufferedReader(new InputStreamReader(url2.openStream(), "UTF-8"));
                    creditResult = bf2.readLine();
                    JSONObject jsonObject2 = (JSONObject) jsonParser.parse(creditResult);
                    JSONArray list2 = (JSONArray) jsonObject2.get("cast");
                    List<String> cast = new ArrayList<>();

                    for (int c = 0; c < list2.size(); c++){
                        JSONObject contents2 = (JSONObject) list2.get(c);
                        cast.add(String.valueOf(contents2.get("name")));
                    }
                    vo.setCast(cast);

                    list2 = (JSONArray) jsonObject2.get("crew");
                    for (int c = 0; c < list2.size(); c++){
                        JSONObject contents2 = (JSONObject) list2.get(c);
                        if (String.valueOf(contents2.get("job")).equals("Director")) {
                            vo.setDirector(String.valueOf(contents2.get("name")));
                        }
                    }

                    String keywordURL = "https://api.themoviedb.org/3/"+ type +"/" + String.valueOf(contents.get("id")) + "/keywords?api_key=" + key;
                    String keywordResult = "";
                    URL keywordurl = new URL(keywordURL);
                    BufferedReader keywordbf = new BufferedReader(new InputStreamReader(keywordurl.openStream(), "UTF-8"));
                    keywordResult = keywordbf.readLine();
                    JSONObject jsonObjectKeyword = (JSONObject) jsonParser.parse(keywordResult);
                    JSONArray keywordList = null;
                    List<String> keywords = new ArrayList<>();

                    if (type.equals("movie")) {
                        keywordList = (JSONArray) jsonObjectKeyword.get("keywords");
                        // 시리즈일 경우 release_date를 key로 데이터 파싱
                        if (keywordList != null) {
                            for (int c = 0; c < keywordList.size(); c++){
                                JSONObject contents2 = (JSONObject) keywordList.get(c);
                                keywords.add(String.valueOf(contents2.get("name")));
                            }
                        }

                    } else if (type.equals("tv")) {
                        keywordList = (JSONArray) jsonObjectKeyword.get("resutls");
                        // 시리즈일 경우 first_air_date를 key로 데이터 파싱
                        if (keywordList != null) {
                            for (int c = 0; c < keywordList.size(); c++){
                                JSONObject contents2 = (JSONObject) keywordList.get(c);
                                keywords.add(String.valueOf(contents2.get("name")));
                            }
                        }

                    }
                    vo.setKeywords(keywords);


                    infoList.add(vo);
                }

            //}
        } catch (Exception e) {
            e.printStackTrace();
        }

        return infoList;


    }




}
