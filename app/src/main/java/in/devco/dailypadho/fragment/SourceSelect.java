package in.devco.dailypadho.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.devco.dailypadho.R;
import in.devco.dailypadho.adapter.SourceAdapter;
import in.devco.dailypadho.model.Source;
import in.devco.dailypadho.widget.ViewLoadingDotsBounce;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SourceSelect extends DialogFragment implements SourceAdapter.ClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.loader)
    ViewLoadingDotsBounce loader;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.no_source)
    TextView noSource;

    private Unbinder unbinder;

    private SourceAdapter adapter = new SourceAdapter(this);
    private CallbackResult callbackResult;

    public interface CallbackResult {
        void selectedSources(List<String> sources);
        void refresh();
    }

    @Override
    public void maxSources() {
        Toast.makeText(getContext(), R.string.max_source, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        callbackResult.refresh();
    }

    void setOnCallbackResult(CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    void setSources(List<Source> sources) {
        if (sources.isEmpty()) {
            noSource.setVisibility(VISIBLE);
        } else {
            noSource.setVisibility(GONE);
        }

        refreshLayout.setRefreshing(false);
        loader.setVisibility(GONE);
        adapter.add(sources);
    }

    void setSelectedSources(List<String> selectedSources) {
        adapter.setSelectedSources(selectedSources);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_sources, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {
        refreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @OnClick(R.id.bt_close)
    void close() {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @OnClick(R.id.bt_save)
    void save() {
        sendData();
        close();
    }

    private void sendData() {
        if (callbackResult != null) {
            callbackResult.selectedSources(adapter.get());
        }
    }
}