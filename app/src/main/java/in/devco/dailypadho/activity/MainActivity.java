package in.devco.dailypadho.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.adapter.ArticleAdapter;
import in.devco.dailypadho.listener.ScrollListener;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.presenter.MainPresenter;
import in.devco.dailypadho.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView, ScrollListener.ScrollUpdate {
    @BindView(R.id.main_recycler)
    RecyclerView recyclerView;

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
        presenter = new MainPresenter(this, getResources().getConfiguration().locale.getCountry().toLowerCase());
        adapter = new ArticleAdapter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnScrollListener(new ScrollListener(layoutManager, this));

        presenter.fetchAllArticles();
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
}
