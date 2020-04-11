package `in`.devco.dailypadho.utils

import `in`.devco.dailypadho.utils.SharedPref.getClickCount
import `in`.devco.dailypadho.utils.SharedPref.resetClickCount
import `in`.devco.dailypadho.utils.SharedPref.setClickCount
import android.content.Context

object AdUtils {
    fun showAd(context: Context) {
        if (getClickCount(context) > 3) {
            resetClickCount(context)
            //display ad
        } else {
            setClickCount(context)
        }
    }
}

