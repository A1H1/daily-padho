package in.devco.dailypadho.presenter;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import in.devco.dailypadho.R;
import in.devco.dailypadho.model.ArticleResponse;
import in.devco.dailypadho.network.APIClient;
import in.devco.dailypadho.network.APIService;
import in.devco.dailypadho.utils.AppUtils;
import in.devco.dailypadho.view.MainView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.devco.dailypadho.utils.AppConst.LOG_NETWORK;

public class FilterResultPresenter implements Callback<ArticleResponse> {
    private boolean loadMore = false;

    private int perPage = 10;
    private int page = 1;
    private int totalPost;

    private String countryCode;
    private String q;
    private String language;
    private String sortBy;

    private List<String> sources;

    private MainView view;
    private APIService api;

    public FilterResultPresenter(MainView view) {
        this.view = view;
        this.countryCode = countryCode;

        api = APIClient.getClient().create(APIService.class);
    }

    public void fetchAllArticles(String language, String sortBy, List<String> sources, String q) {
        this.q = q;
        this.language = language;
        this.sortBy = sortBy;
        this.sources = sources;

        String s;

        if (sources == null || sources.isEmpty()) {
            s = null;
        } else {
            s = AppUtils.stringListToString(sources);
        }

        Call<ArticleResponse> call = api.getArticles(q, null, s, null, null, language, sortBy, perPage, page);
        call.enqueue(this);
    }

    public void loadMore() {
        if (page * perPage < totalPost) {
            loadMore = true;
            page++;
            fetchAllArticles(language, sortBy, sources, q);
        }
    }

    public void refresh() {
        reset();
        fetchAllArticles(language, sortBy, sources, q);
    }

    public void search(String q) {
        reset();
        fetchAllArticles(language, sortBy, sources, q);
    }

    private void reset() {
        loadMore = false;
        page = 1;
    }

    @Override
    public void onResponse(@NotNull Call<ArticleResponse> call, @NotNull Response<ArticleResponse> response) {
        if (response.body() != null && response.isSuccessful()) {
            if (loadMore) {
                view.loadMoreArticles(response.body().getArticles());
            } else {
                totalPost = response.body().getTotalResults();
                view.loadArticles(response.body().getArticles());
            }
        } else {
            Log.e(LOG_NETWORK, "code " + response.code());
            view.error(R.string.something_wrong);
        }
    }

    @Override
    public void onFailure(@NotNull Call<ArticleResponse> call, @NotNull Throwable t) {
        Log.e(LOG_NETWORK, Objects.requireNonNull(t.getMessage()));
        view.error(R.string.connection_problem);
    }
}
