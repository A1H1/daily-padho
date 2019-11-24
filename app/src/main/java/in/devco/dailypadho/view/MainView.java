package in.devco.dailypadho.view;

import java.util.List;

import in.devco.dailypadho.model.Article;

public interface MainView {
    void loadArticles(List<Article> articles);
    void loadMoreArticles(List<Article> articles);
}
