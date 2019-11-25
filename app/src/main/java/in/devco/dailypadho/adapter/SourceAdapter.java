package in.devco.dailypadho.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.devco.dailypadho.R;
import in.devco.dailypadho.model.Source;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {
    private List<Source> sources;
    private List<String> selectedSources;
    private ClickListener clickListener;

    public interface ClickListener {
        void maxSources();
    }

    public SourceAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;

        selectedSources = new ArrayList<>();
        sources = new ArrayList<>();
    }

    public void add(List<Source> sources) {
        this.sources = sources;
        notifyDataSetChanged();
    }

    public void setSelectedSources(List<String> selectedSources) {
        this.selectedSources = selectedSources;
    }

    public List<String> get() {
        return selectedSources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_source, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder view, int position) {
        if (selectedSources.contains(sources.get(position).getId())) {
            view.source.setChecked(true);
        }

        view.source.setText(sources.get(position).getName());

        view.source.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                selectedSources.remove(sources.get(position).getId());
            } else {
                if (selectedSources.size() < 21) {
                    selectedSources.add(sources.get(position).getId());
                } else {
                    view.source.setChecked(false);
                    clickListener.maxSources();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.source)
        AppCompatCheckBox source;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
