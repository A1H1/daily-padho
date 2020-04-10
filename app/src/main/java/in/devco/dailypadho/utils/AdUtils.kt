package `in`.devco.dailypadho.utils

import `in`.devco.dailypadho.utils.SharedPref.SharedPref.getClickCount
import `in`.devco.dailypadho.utils.SharedPref.SharedPref.resetClickCount
import `in`.devco.dailypadho.utils.SharedPref.SharedPref.setClickCount


class AdUtils {

    object AdUtils {
        fun showAd() {
            SharedPref.setClickCount
            if(SharedPref.getClickCount > 3) {
                SharedPref.resetClickCount
                //display ad
            }
        }
    }
}

