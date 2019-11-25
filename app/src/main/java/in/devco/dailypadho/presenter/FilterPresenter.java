package in.devco.dailypadho.presenter;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import in.devco.dailypadho.R;
import in.devco.dailypadho.model.SourceResponse;
import in.devco.dailypadho.network.APIClient;
import in.devco.dailypadho.network.APIService;
import in.devco.dailypadho.view.FilterView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.devco.dailypadho.utils.AppConst.CATEGORY;
import static in.devco.dailypadho.utils.AppConst.LOG_NETWORK;

public class FilterPresenter implements Callback<SourceResponse> {
    private FilterView view;
    private APIService api;

    public FilterPresenter(FilterView view) {
        this.view = view;

        api = APIClient.getClient().create(APIService.class);
    }

    public void fetchSources(String language, String category) {
        String c = category;
        if (category.equals(CATEGORY[0].toLowerCase())) {
            c = null;
        }

        Call<SourceResponse> call = api.getSources(c, language, null);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NotNull Call<SourceResponse> call, @NotNull Response<SourceResponse> response) {
        if (response.body() != null && response.isSuccessful()) {
            view.setSources(response.body().getSources());
        } else {
            Log.e(LOG_NETWORK, "code " + response.code());
            view.error(R.string.something_wrong);
        }
    }

    @Override
    public void onFailure(@NotNull Call<SourceResponse> call, @NotNull Throwable t) {
        Log.e(LOG_NETWORK, Objects.requireNonNull(t.getMessage()));
        view.error(R.string.connection_problem);
    }
}
