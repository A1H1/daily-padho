package in.devco.dailypadho.network;

import in.devco.dailypadho.model.ArticleResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface API {
    @Headers("Authorization: aa171b6eb7364eb796bab5f173e34358")
    @GET("/v2//top-headlines")
    Call<ArticleResponse> getArticles(@Query("country") String country);
}
