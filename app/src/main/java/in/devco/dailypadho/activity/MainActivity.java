package in.devco.dailypadho.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.adapter.ArticleAdapter;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.presenter.MainPresenter;
import in.devco.dailypadho.view.MainView;

public class MainActivity extends AppCompatActivity implements MainView {
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
        presenter = new MainPresenter(this);
        adapter = new ArticleAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        presenter.fetchAllArticles();
    }

    @Override
    public void loadArticles(List<Article> articles) {
        adapter.update(articles);
    }
}
