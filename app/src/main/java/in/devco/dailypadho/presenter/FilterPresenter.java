package in.devco.dailypadho.presenter;

import org.jetbrains.annotations.NotNull;

import in.devco.dailypadho.model.SourceResponse;
import in.devco.dailypadho.network.APIClient;
import in.devco.dailypadho.network.APIService;
import in.devco.dailypadho.view.SourceView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.devco.dailypadho.utils.AppConst.CATEGORY;

public class SourcePresenter implements Callback<SourceResponse> {
    private SourceView view;
    private APIService api;

    public SourcePresenter(SourceView view) {
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

        } else {

        }
    }

    @Override
    public void onFailure(@NotNull Call<SourceResponse> call, @NotNull Throwable t) {

    }
}
