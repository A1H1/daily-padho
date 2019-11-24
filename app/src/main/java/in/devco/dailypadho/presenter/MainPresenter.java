package in.devco.dailypadho.presenter;

import in.devco.dailypadho.model.ArticleResponse;
import in.devco.dailypadho.network.API;
import in.devco.dailypadho.view.MainView;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainPresenter {
    private MainView view;
    private API api;

    public MainPresenter(MainView view) {
        this.view = view;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);

    }


    public void fetchAllArticles() {
        Call<ArticleResponse> call = api.getArticles("in");

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                view.loadArticles(response.body().getArticles());
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {

            }
        });
    }
}
