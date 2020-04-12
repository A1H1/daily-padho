package in.devco.dailypadho.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.devco.dailypadho.R;
import in.devco.dailypadho.model.Article;
import in.devco.dailypadho.utils.AdUtils;
import in.devco.dailypadho.utils.AppUtils;

import static android.view.View.GONE;
import static in.devco.dailypadho.utils.AppConst.INTENT_KEY_ARTICLE;

public class ArticleDetails extends AppCompatActivity {
    @BindView(R.id.article_details_title)
    TextView title;
    @BindView(R.id.article_details_details)
    TextView details;
    @BindView(R.id.article_details_time)
    TextView time;
    @BindView(R.id.article_details_source)
    TextView source;
    @BindView(R.id.article_details_content)
    TextView content;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.article_details_image)
    ImageView image;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar();
        AdUtils.showAd(this);
        AdUtils.bannerAds(this,mAdView);

        article = new Gson().fromJson(getIntent().getStringExtra(INTENT_KEY_ARTICLE), Article.class);

        title.setText(article.getTitle());
        details.setText(article.getDescription());
        time.setText(article.getPublishedAt());
        source.setText(article.getSource().getName());
        content.setText(article.getContent());

        if (article.getImage() == null) {
            image.setVisibility(GONE);
        } else {
            Picasso.get().load(article.getImage()).into(image);
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_details, menu);
        AppUtils.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case  android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.action_share:
                share();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.article_details_read_more)
    void readMore() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(article.getUrl()));
    }

    private void share() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getUrl());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}
