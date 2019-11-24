package in.devco.dailypadho.listener;

import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollListener extends RecyclerView.OnScrollListener {
    private boolean isScrolling = false;

    private LinearLayoutManager layoutManager;
    private ScrollUpdate scrollUpdate;

    public interface ScrollUpdate {
        void nextPage();
    }

    public ScrollListener(LinearLayoutManager layoutManager, ScrollUpdate scrollUpdate) {
        this.layoutManager = layoutManager;
        this.scrollUpdate = scrollUpdate;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
            isScrolling = true;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (isScrolling && (layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition() == layoutManager.getItemCount())) {
            isScrolling = false;
            scrollUpdate.nextPage();
        }
    }
}
