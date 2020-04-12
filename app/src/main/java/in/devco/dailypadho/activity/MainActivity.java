package in.devco.dailypadho.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.devco.dailypadho.R;
import in.devco.dailypadho.adapter.ArticleAdapter;
import in.devco.dailypadho.listener.ScrollListener;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.presenter.MainPresenter;
import in.devco.dailypadho.utils.AdUtils;
import in.devco.dailypadho.utils.AppUtils;
import in.devco.dailypadho.view.MainView;
import in.devco.dailypadho.widget.ViewLoadingDotsBounce;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

import static android.view.View.GONE;
import static in.devco.dailypadho.utils.AppConst.INTENT_KEY_ARTICLE;

public class MainActivity extends AppCompatActivity implements MainView, ScrollListener.ScrollUpdate, ArticleAdapter.OnClickListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.loader)
    ViewLoadingDotsBounce loader;

    @BindView(R.id.sort)
    FloatingActionButton floatingActionButton;

    private MainPresenter presenter;
    private ArticleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        init();
    }

    private void init() {
        setToolbar();
        intro();
        AdUtils.bannerAds(this, mAdView);
        AdUtils.showAd(this);

        presenter = new MainPresenter(this, getResources().getConfiguration().locale.getCountry().toLowerCase());
        adapter = new ArticleAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ScrollListener(layoutManager, this));

        presenter.fetchAllArticles(null);
        refreshLayout.setOnRefreshListener(this);
    }

    private void intro() {
        new MaterialShowcaseView.Builder(this)
                .setTarget(floatingActionButton)
                .setDismissText("GOT IT")
                .setContentText("Filter, sort and search news")
                .setMaskColour(Color.parseColor("#BF666666"))
                .setFadeDuration(250)
                .setDelay(1000)
                .singleUse("filter")
                .show();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.top_headlines);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));

        AppUtils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            AppUtils.about(this);
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void nextPage() {
        presenter.loadMore();
    }

    @Override
    public void displayArticle(Article article) {
        Intent intent = new Intent(this, ArticleDetails.class);
        intent.putExtra(INTENT_KEY_ARTICLE, new Gson().toJson(article));
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String q) {
        presenter.search(q);
        return false;
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        presenter.refresh();
    }

    @OnClick(R.id.sort)
    void sortArticles() {
        startActivity(new Intent(this, FilterArticle.class));
    }
}
