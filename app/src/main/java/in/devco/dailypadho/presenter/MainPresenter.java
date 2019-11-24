package in.devco.dailypadho.presenter;

import in.devco.dailypadho.model.ArticleResponse;
import in.devco.dailypadho.network.APIClient;
import in.devco.dailypadho.network.APIService;
import in.devco.dailypadho.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements Callback<ArticleResponse> {
    private boolean loadMore = false;
    private int page = 1;
    private int totalPost;
    private String countryCode;

    private MainView view;
    private APIService api;

    public MainPresenter(MainView view, String countryCode) {
        this.view = view;
        this.countryCode = countryCode;

        api = APIClient.getClient().create(APIService.class);
    }

    public void fetchAllArticles() {
        Call<ArticleResponse> call = api.getArticles(countryCode, 10, page);
        call.enqueue(this);
    }

    public void loadMore() {
        if (page * 10 < totalPost) {
            loadMore = true;
            page++;
            fetchAllArticles();
        }
    }

    @Override
    public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
        if (loadMore) {
            view.loadMoreArticles(response.body().getArticles());
        } else {
            totalPost = response.body().getTotalResults();
            view.loadArticles(response.body().getArticles());
        }
    }

    @Override
    public void onFailure(Call<ArticleResponse> call, Throwable t) {

    }
}
