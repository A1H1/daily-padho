package in.devco.dailypadho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.model.Article;

import static android.view.View.VISIBLE;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private OnClickListener clickListener;

    public interface OnClickListener {
        void displayArticle(Article article);
    }

    public ArticleAdapter(OnClickListener clickListener) {
        this.clickListener = clickListener;

        articles = new ArrayList<>();
    }

    public void add(List<Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }

    public void update(List<Article> articles) {
        this.articles.addAll(articles);
        notifyDataSetChanged();
    }

    public List<Article> get() {
        return articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder view, int position) {
        Article article = articles.get(position);

        view.title.setText(article.getTitle());
        view.description.setText(article.getDescription());
        view.source.setText(article.getSource().getName());

        if (article.getImage() != null) {
            view.image.setVisibility(VISIBLE);

            Picasso.get()
                    .load(article.getImage())
                    .into(view.image);
        }

        view.cardView.setOnClickListener(v -> clickListener.displayArticle(article));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.article_title)
        TextView title;
        @BindView(R.id.article_description)
        TextView description;
        @BindView(R.id.article_source)
        TextView source;

        @BindView(R.id.article_image)
        ImageView image;

        @BindView(R.id.article_container)
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
