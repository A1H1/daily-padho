package in.devco.dailypadho.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import androidx.annotation.ColorInt;

public class AppUtils {
    public static void changeMenuIconColor(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable == null) continue;
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
