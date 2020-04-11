package in.devco.dailypadho.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.devco.dailypadho.R;
import in.devco.dailypadho.model.Source;
import in.devco.dailypadho.presenter.FilterPresenter;
import in.devco.dailypadho.utils.AdUtils;
import in.devco.dailypadho.utils.AppUtils;
import in.devco.dailypadho.view.FilterView;

import static in.devco.dailypadho.utils.AppConst.CATEGORY;
import static in.devco.dailypadho.utils.AppConst.LANGUAGES;
import static in.devco.dailypadho.utils.AppConst.LANGUAGES_KEY;
import static in.devco.dailypadho.utils.AppConst.SORTS;
import static in.devco.dailypadho.utils.AppConst.SORTS_KEY;

public class Filters extends Fragment implements FilterView, SourceSelect.CallbackResult {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.language)
    TextView languageTV;
    @BindView(R.id.sort_by)
    TextView sortByTV;
    @BindView(R.id.category)
    TextView categoryTV;
    @BindView(R.id.sources)
    TextView sourcesTV;

    @BindView(R.id.query)
    EditText query;

    private boolean update = false;

    private String language = LANGUAGES_KEY[0];
    private String sortBy = SORTS_KEY[0];
    private String category = CATEGORY[0].toLowerCase();

    private List<String> sources = new ArrayList<>();

    private AlertDialog.Builder builder;
    private FragmentManager fragmentManager;

    private FilterPresenter presenter;
    private SourceSelect fragment;

    public Filters() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
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
        AdUtils.bannerAds(getContext(), mAdView);
        AdUtils.showAd(requireContext());

        presenter = new FilterPresenter(this);
        fragmentManager = getFragmentManager();
        fragment = new SourceSelect();
        fragment.setOnCallbackResult(this);

        if (getContext() != null) {
            builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        }
    }

    private void setToolbar() {
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(R.string.filters);
            }
        }
    }

    @OnClick(R.id.filter_language)
    void setLanguage() {
        builder.setTitle(R.string.language);
        builder.setSingleChoiceItems(LANGUAGES, Arrays.asList(LANGUAGES_KEY).indexOf(language), (dialogInterface, i) -> {
            languageTV.setText(LANGUAGES[i]);
            language = LANGUAGES_KEY[i];
            update = true;
            presenter.fetchSources(language, category);
        });
        builder.show();
    }

    @OnClick(R.id.filter_sort_by)
    void setSortBy() {
        builder.setTitle(R.string.sort_by);
        builder.setSingleChoiceItems(SORTS, Arrays.asList(SORTS_KEY).indexOf(sortBy), (dialogInterface, i) -> {
            sortByTV.setText(SORTS[i]);
            sortBy = SORTS_KEY[i];
        });
        builder.show();
    }

    @OnClick(R.id.filter_category)
    void setCategory() {
        builder.setTitle(R.string.category);
        builder.setSingleChoiceItems(CATEGORY, Arrays.asList(CATEGORY).indexOf(category.substring(0, 1).toUpperCase() + category.substring(1)), (dialogInterface, i) -> {
            categoryTV.setText(CATEGORY[i]);
            category = CATEGORY[i].toLowerCase();
            update = true;
            presenter.fetchSources(language, category);
        });
        builder.show();
    }

    @OnClick(R.id.filter_sources)
    void setSources() {
        fragment.setSelectedSources(sources);
        presenter.fetchSources(language, category);
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(android.R.id.content, fragment).addToBackStack(null).commit();
        }
    }

    @OnClick(R.id.filter)
    void filter() {
        if (sources.isEmpty() && query.getText().toString().equals("")) {
            Toast.makeText(getContext(), R.string.enter_query_source, Toast.LENGTH_LONG).show();
        } else {
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.replace(android.R.id.content, FilterResult.newInstance(language, sortBy, sources, query.getText().toString(), null, null)).addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void selectedSources(List<String> sources) {
        this.sources = sources;
        String s = "";

        if (sources == null || sources.isEmpty()) {
            sourcesTV.setText(R.string.none);
        } else if (sources.size() == 1) {
            s = String.format(Locale.getDefault(), "%d Source", sources.size());
        } else {
            s = String.format(Locale.getDefault(), "%d Sources", sources.size());
        }
        sourcesTV.setText(s);
    }

    @Override
    public void refresh() {
        presenter.fetchSources(language, category);
    }

    @Override
    public void setSources(List<Source> sources) {
        if (update) {
            update = false;
            this.sources = new ArrayList<>();
            if (sources == null || sources.isEmpty()) {
                sourcesTV.setText(R.string.no_sources);
            } else {
                sourcesTV.setText(R.string.none);
            }
        } else {
            fragment.setSources(sources);
        }
    }

    @Override
    public void error(int error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}
