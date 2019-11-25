package in.devco.dailypadho.presenter;

import org.jetbrains.annotations.NotNull;

import in.devco.dailypadho.model.ArticleResponse;
import in.devco.dailypadho.network.APIClient;
import in.devco.dailypadho.network.APIService;
import in.devco.dailypadho.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter implements Callback<ArticleResponse> {
    private boolean loadMore = false;
    private boolean searchMore = false;
    private boolean isSearching = false;

    private int perPage = 10;
    private int page = 1;
    private int searchPage = 1;
    private int totalPost;
    private int searchResult;

    private String countryCode;
    private String q;

    private MainView view;
    private APIService api;

    public MainPresenter(MainView view, String countryCode) {
        this.view = view;
        this.countryCode = countryCode;

        api = APIClient.getClient().create(APIService.class);
    }

    public void fetchAllArticles() {
        Call<ArticleResponse> call = api.getHeadlines(countryCode, null, null, null, perPage, page);
        call.enqueue(this);
    }

    public void loadMore() {
        if (isSearching) {
            if (searchPage * perPage < searchResult) {
                searchMore = true;
                searchPage++;
                search(q);
            }
        } else {
            if (page * perPage < totalPost) {
                loadMore = true;
                page++;
                fetchAllArticles();
            }
        }
    }

    public void refresh() {
        if (isSearching) {
            searchMore = false;
            searchPage = 1;
            search(q);
        } else {
            loadMore = false;
            page = 1;
            fetchAllArticles();
        }
    }

    public void search(String q) {
        this.q = q;
        isSearching = true;
        Call<ArticleResponse> call = api.getArticles(q, null, null, null, null, null, null, perPage, searchPage);
        call.enqueue(this);
    }

    public void resetSearch() {
        isSearching = false;
        searchMore = false;
        searchPage = 1;
    }

    @Override
    public void onResponse(@NotNull Call<ArticleResponse> call, @NotNull Response<ArticleResponse> response) {
        if (loadMore || searchMore) {
            view.loadMoreArticles(response.body().getArticles());
        } else if (isSearching) {
            searchResult = response.body().getTotalResults();
            view.loadArticles(response.body().getArticles());
        } else {
            totalPost = response.body().getTotalResults();
            view.loadArticles(response.body().getArticles());
        }
    }

    @Override
    public void onFailure(@NotNull Call<ArticleResponse> call, @NotNull Throwable t) {

    }
}
