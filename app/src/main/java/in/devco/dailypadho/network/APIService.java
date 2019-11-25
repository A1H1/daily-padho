package in.devco.dailypadho.network;

import in.devco.dailypadho.model.ArticleResponse;
import in.devco.dailypadho.model.SourceResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("/v2//top-headlines")
    Call<ArticleResponse> getHeadlines(
            @Query("country") String country,
            @Query("category") String category,
            @Query("sources") String sources,
            @Query("q") String q,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );

    @GET("/v2/everything")
    Call<ArticleResponse> getArticles(
            @Query("q") String q,
            @Query("qInTitle") String title,
            @Query("sources") String sources,
            @Query("from") String from,
            @Query("to") String to,
            @Query("language") String language,
            @Query("sortBy") String sortBy,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );

    @GET("/v2/sources")
    Call<SourceResponse> getSources(
            @Query("category") String category,
            @Query("language") String language,
            @Query("country") String country
    );
}
