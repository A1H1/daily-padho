package in.devco.dailypadho.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.adapter.ArticleAdapter;
import in.devco.dailypadho.listener.ScrollListener;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.presenter.MainPresenter;
import in.devco.dailypadho.utils.AppUtils;
import in.devco.dailypadho.view.MainView;

import static in.devco.dailypadho.utils.AppConst.INTENT_KEY_ARTICLE;

public class MainActivity extends AppCompatActivity implements MainView, ScrollListener.ScrollUpdate, ArticleAdapter.OnClickListener, SearchView.OnQueryTextListener {
    @BindView(R.id.main_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        presenter = new MainPresenter(this, getResources().getConfiguration().locale.getCountry().toLowerCase());
        adapter = new ArticleAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new ScrollListener(layoutManager, this));

        presenter.fetchAllArticles();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Top Headlines");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);

        AppUtils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public void loadArticles(List<Article> articles) {
        adapter.add(articles);
    }

    @Override
    public void loadMoreArticles(List<Article> articles) {
        adapter.update(articles);
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
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
