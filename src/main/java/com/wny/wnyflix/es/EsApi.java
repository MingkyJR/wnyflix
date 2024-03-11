package com.wny.wnyflix.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.SearchTemplateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.wny.wnyflix.movie.domain.Contents;
import com.wny.wnyflix.movie.domain.Terms;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EsApi {

    @Autowired
    private final EsClient esClient = new EsClient();

    String index_name = "wy_wnyflix_data";
    String searchTemplateTitle = "wy_search_title";
    String searchTemplateCast = "wy_search_cast";


    public List<Contents> esGetContentsList() {
        List<Contents> contentsList = new ArrayList<>();
        try {
            ElasticsearchClient client = esClient.client();
            SearchResponse<Contents> response = client.search(s -> s
                    .index(index_name)
                    .size(20),
                    Contents.class
            );


            List<Hit<Contents>> hits = response.hits().hits();
            for (Hit<Contents> hit: hits) {
                Contents contents = hit.source();
                contentsList.add(contents);
            }

        } catch (Exception e) {
            log.error("esGetContentsList error : {}", e.toString());
        }
        log.debug("found contents : {}",contentsList.size());
        return contentsList;
    }

    //콘텐츠 상세, 재생 페이지
    public Contents esGetContentsInfoById(String id) {
        Contents contents = new Contents();
        try {
            ElasticsearchClient client = esClient.client();
            GetResponse<Contents> response = client.get(g -> g
                            .index(index_name)
                            .id(id),
                    Contents.class
            );

            if (response.found()) {
                Contents res_contents = response.source();
                log.info("Contents name : {}", res_contents.getTitle());
                contents = res_contents;
            } else {
                log.info ("Contents not found");
            }

        } catch (Exception e) {
            log.error("esGetContentsList error : {}", e.toString());
        }
        return contents;

    }

    public Map<String, Object> esGetContentsByQuery(String query) {
        Map<String, Object> map = new HashMap<>();

        List<Contents> contentsList = new ArrayList<>();

        List<Terms> genresTermsList = new ArrayList<>();

        try {
            ElasticsearchClient client = esClient.client();

            //title 검색 가중
            SearchTemplateResponse<Contents> titleResponse = client.searchTemplate(r -> r
                            .index(index_name)
                            .id(searchTemplateTitle)
                            .params("query_string", JsonData.of(query))
                            .params("from", JsonData.of(0))
                            .params("size", JsonData.of(30)),
                    Contents.class
            );

            //cast 검색 가중
            SearchTemplateResponse<Contents> castResponse = client.searchTemplate(r -> r
                            .index(index_name)
                            .id(searchTemplateCast)
                            .params("query_string", JsonData.of(query))
                            .params("from", JsonData.of(0))
                            .params("size", JsonData.of(30)),
                    Contents.class
            );


            List<Hit<Contents>> thits = titleResponse.hits().hits();
            for (Hit<Contents> hit: thits) {
                Contents content = hit.source();
                log.debug("Found titleResponse {} , score {}", content.getTitle(), hit.score());
                Contents contentf = new Contents(content.getBackdrop_path(),content.getGenres(),content.getId(),content.getTitle(),content.getOriginal_title(),content.getOverview(),content.getDirector(),content.getCast(),content.getPopularity(),content.getPoster_path(),content.getRelease_date(),content.getContents_type(),content.getVote_average(),content.getVote_count(),content.getKeywords(), hit.score());
                contentsList.add(contentf);
            }

//            List<StringTermsBucket> buckets = titleResponse.aggregations()
//                    .get("genres_agg")
//                    .sterms()
//                    .buckets().array();
//            log.info("buckets : {}", buckets.toString());
//
//            for (StringTermsBucket bucket: buckets) {
//                Terms terms = new Terms(bucket.key().stringValue(), bucket.docCount());
//                genresTermsList.add(terms);
//                log.info("genresTermsList : {}", genresTermsList.size());
//            }

            List<Hit<Contents>> chits = castResponse.hits().hits();
            for (Hit<Contents> hit: chits) {
                Contents content = hit.source();
                log.debug("Found castResponse {} , score {}", content.getTitle(), hit.score());
                Contents contentf = new Contents(content.getBackdrop_path(),content.getGenres(),content.getId(),content.getTitle(),content.getOriginal_title(),content.getOverview(),content.getDirector(),content.getCast(),content.getPopularity(),content.getPoster_path(),content.getRelease_date(),content.getContents_type(),content.getVote_average(),content.getVote_count(),content.getKeywords(), hit.score());
                contentsList.add(contentf);
            }

        } catch (Exception e) {
            log.error("esGetContentsByQuery error : {}", e.toString());
        }
        Collections.sort( contentsList, (o1, o2) -> (int) (o2.getScore() - o1.getScore()));

        map.put("contentsList", contentsList);
//        map.put("genresTermsList", genresTermsList);

        return map;
    }

    public Map<String, Object> esGetContentsByQueryFilterGenres(String query) {
        Map<String, Object> map = new HashMap<>();

        List<Contents> contentsList = new ArrayList<>();

        List<Terms> termsList = new ArrayList<>();

        Query multiMatch = MultiMatchQuery.of(m ->m
                .fields("title", "original_title", "overview", "cast", "director", "genres")
                .query(query)
        )._toQuery();

        Query termQuery = TermQuery.of(t -> t
                .field("genres")
                .value("액션")
        )._toQuery();

        Query boolQuery = BoolQuery.of(s -> s
                .must(multiMatch)
                .must(termQuery)
        )._toQuery();

        try {
            ElasticsearchClient client = esClient.client();
            SearchResponse<Contents> response = client.search(b -> b
                            .index(index_name)
                            .query(boolQuery)
                            .aggregations("genres_aggs", a -> a
                                    .terms(t -> t
                                            .field("genres")
                                    )
                            ),
                    Contents.class
            );

            List<Hit<Contents>> hits = response.hits().hits();
            for (Hit<Contents> hit: hits) {
                Contents content = hit.source();
                log.debug("Found product {} , score {}", content.getTitle(), hit.score());
                contentsList.add(content);
            }

            List<StringTermsBucket> buckets = response.aggregations()
                    .get("genres_aggs")
                    .sterms()
                    .buckets().array();

            for (StringTermsBucket bucket: buckets) {
                Terms terms = new Terms(bucket.key().stringValue(), bucket.docCount());
                termsList.add(terms);
            }

        }catch (Exception e){
            log.error("esGetContentByQueryFilterGenres error : {}", e.toString());

        }

        map.put("contentsList", contentsList);
        map.put("genresList", termsList);

        return map;

    }


    public List<Terms> esGetTopQuery() {
        List<Terms> topQueryTermsList = new ArrayList<>();
        try {
            ElasticsearchClient client = esClient.client();


            SearchTemplateResponse<Terms> topQueryResponse = client.searchTemplate(r -> r
                            .index("logs-apache_tomcat.access-wonyoung")
                            .id("wy_day_top_query"),
                    Terms.class
            );

            List<StringTermsBucket> topQuerybuckets = topQueryResponse.aggregations()
                    .get("TOP10")
                    .sterms()
                    .buckets().array();

            for (StringTermsBucket bucket: topQuerybuckets) {
                log.debug("top_query {} : {} ",bucket.key().stringValue(), bucket.docCount());
                Terms terms = new Terms(bucket.key().stringValue(), bucket.docCount());
                topQueryTermsList.add(terms);
            }


        } catch (Exception e) {
            log.error("esGetContentsByQuery error : {}", e.toString());
        }
        return topQueryTermsList;

    }

    @WithSpan
    public List<Contents> esGetTopContentsList(String country_code) {
        List<Contents> topContentsList = new ArrayList<>();
        try {
            ElasticsearchClient client = esClient.client();

            SearchTemplateResponse<Terms> topContentsResponse = client.searchTemplate(r -> r
                            .index("logs-apache_tomcat.access-wonyoung")
                            .id("wy_day_top_playId")
                            .params("country_code", JsonData.of(country_code)),
                    Terms.class
            );

            List<StringTermsBucket> topContentsbuckets = topContentsResponse.aggregations()
                    .get("TOP10")
                    .sterms()
                    .buckets().array();
            List<String> topPlayIds = new ArrayList<>();
            for (StringTermsBucket bucket: topContentsbuckets) {
                log.info("top_playId {} : {} ",bucket.key().stringValue(), bucket.docCount());
                topPlayIds.add(bucket.key().stringValue());
            }

            for(String id: topPlayIds) {
                GetResponse<Contents> response = client.get(g -> g
                                .index(index_name)
                                .id(id),
                        Contents.class
                );
                Contents content = response.source();
                topContentsList.add(content);
            }
        } catch (Exception e) {
            log.error("esGetTopContents error : {}", e.toString());
        }
        return topContentsList;
    }

    @WithSpan
    public List<Contents> esGetRecentContentsList() {
        List<Contents> recentContentsList = new ArrayList<>();
        try {
            ElasticsearchClient client = esClient.client();
            SearchTemplateResponse<Contents> recentContentsResponse = client.searchTemplate(r -> r
                            .index(index_name)
                            .id("wy_recent_contents"),
                    Contents.class
            );

            List<Hit<Contents>> hits = recentContentsResponse.hits().hits();
            for (Hit<Contents> hit: hits) {
                Contents contents = hit.source();
                log.debug("Found recentContents {} , score {}",contents.getTitle() , hit.score());
                recentContentsList.add(contents);
            }

        } catch (Exception e) {
            log.error("esGetRecentContentsList error : {}", e.toString());
        }
        return recentContentsList;
    }

    @WithSpan
    public List<Contents> esGetPlayingContentsByUserId(String userId) {
        List<Contents> playingContentsList = new ArrayList<>();
        try {
            ElasticsearchClient client = esClient.client();
            SearchTemplateResponse<Contents> playingContentsResponse = client.searchTemplate(r -> r
                            .index("logs-apache_tomcat.access-wonyoung")
                            .id("wy_playing_byUserId")
                            .params("userId", JsonData.of(userId)),
                    Contents.class
            );

            List<StringTermsBucket> playingContentsbuckets = playingContentsResponse.aggregations()
                    .get("playing")
                    .sterms()
                    .buckets().array();
            List<String> playingContentsIds = new ArrayList<>();
            for (StringTermsBucket bucket: playingContentsbuckets) {
//                log.info("playingContents {} : {} ",bucket.key().stringValue(), bucket.docCount());
                playingContentsIds.add(bucket.key().stringValue());
                log.info("시청중인 콘텐츠 id : {}", bucket.key().stringValue());
            }

            for(String id: playingContentsIds) {
                GetResponse<Contents> response = client.get(g -> g
                                .index(index_name)
                                .id(id),
                        Contents.class
                );
                Contents content = response.source();
                playingContentsList.add(content);
            }
        } catch (Exception e) {
            log.error("esGetTopContents error : {}", e.toString());
        }
        return playingContentsList;
    }

    public String fake() {
        try {
            ElasticsearchClient client = esClient.client();
            SearchTemplateResponse<Contents> playingContentsResponse = client.searchTemplate(r -> r
                            .index("asdfasfasdf")
                            .id("asdfasdfasdfasdf"),
                    Contents.class
            );

        } catch (Exception e) {
            log.error("esGetTopContents error : {}", e.toString());
        }
        return "error발생";
    }



}
