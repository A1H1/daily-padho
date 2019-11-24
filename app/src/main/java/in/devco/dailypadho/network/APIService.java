package in.devco.dailypadho.network;

import in.devco.dailypadho.model.ArticleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("/v2//top-headlines")
    Call<ArticleResponse> getArticles(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("page") int page
    );
}
