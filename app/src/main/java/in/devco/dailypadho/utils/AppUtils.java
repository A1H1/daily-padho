package in.devco.dailypadho.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.Menu;

import androidx.annotation.ColorInt;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import in.devco.dailypadho.R;

public class AppUtils {
    public static void changeMenuIconColor(Menu menu, @ColorInt int color) {
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable == null) continue;
            drawable.mutate();
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static String stringListToString(List<String> list) {
        StringBuilder r = new StringBuilder();

        for (String s : list) {
            r.append(s);
            r.append(",");
        }

        r.setLength(r.length()-1);

        return r.toString();
    }


}
