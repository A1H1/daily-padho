package in.devco.dailypadho.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.activity.ArticleDetails;
import in.devco.dailypadho.adapter.ArticleAdapter;
import in.devco.dailypadho.listener.ScrollListener;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.presenter.FilterResultPresenter;
import in.devco.dailypadho.utils.AppUtils;
import in.devco.dailypadho.view.MainView;
import in.devco.dailypadho.widget.ViewLoadingDotsBounce;

import static android.view.View.GONE;
import static in.devco.dailypadho.utils.AppConst.INTENT_KEY_ARTICLE;

public class FilterResult extends Fragment implements SearchView.OnQueryTextListener, ArticleAdapter.OnClickListener, ScrollListener.ScrollUpdate, SwipeRefreshLayout.OnRefreshListener, MainView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.loader)
    ViewLoadingDotsBounce loader;

    private ArticleAdapter adapter;
    private FilterResultPresenter presenter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String language;
    private String sortBy;
    private String q;
    private List<String> sources;

    public FilterResult() {
    }

    static FilterResult newInstance(String language, String sortBy, List<String> sources, String q, String from, String to) {
        FilterResult fragment = new FilterResult();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, language);
        args.putString(ARG_PARAM2, sortBy);
        args.putStringArrayList(ARG_PARAM3, new ArrayList<>(sources));
        args.putString(ARG_PARAM4, q);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            language = getArguments().getString(ARG_PARAM1);
            sortBy = getArguments().getString(ARG_PARAM2);
            sources = getArguments().getStringArrayList(ARG_PARAM3);
            q = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        AppUtils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_result, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        setToolbar();
        AppUtils.loadAds(getContext(), mAdView);

        adapter = new ArticleAdapter(this);
        presenter = new FilterResultPresenter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ScrollListener(layoutManager, this));

        presenter.fetchAllArticles(language, sortBy, sources, q);
        refreshLayout.setOnRefreshListener(this);
    }

    private void setToolbar() {
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.news);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.search(newText);
        return false;
    }

    @Override
    public void displayArticle(Article article) {
        Intent intent = new Intent(getContext(), ArticleDetails.class);
        intent.putExtra(INTENT_KEY_ARTICLE, new Gson().toJson(article));
        startActivity(intent);
    }

    @Override
    public void nextPage() {
        presenter.loadMore();
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        presenter.refresh();
    }

    @Override
    public void loadArticles(List<Article> articles) {
        loader.setVisibility(GONE);
        adapter.add(articles);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void loadMoreArticles(List<Article> articles) {
        adapter.update(articles);
    }

    @Override
    public void error(int error) {
        loader.setVisibility(GONE);
        Snackbar.make(recyclerView, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, v -> onRefresh())
                .show();
    }
}
