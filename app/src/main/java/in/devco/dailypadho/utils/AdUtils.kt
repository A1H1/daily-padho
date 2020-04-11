package `in`.devco.dailypadho.utils

import `in`.devco.dailypadho.R
import `in`.devco.dailypadho.utils.SharedPref.getClickCount
import `in`.devco.dailypadho.utils.SharedPref.resetClickCount
import `in`.devco.dailypadho.utils.SharedPref.setClickCount
import android.content.Context
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd


object AdUtils {


        @JvmStatic fun showAd(context: Context) {
            if (getClickCount(context) > 3) {
                resetClickCount(context)
                val interstitialAd = InterstitialAd(context)
                interstitialAd.adUnitId = context.getString(R.string.interstitial_release)
                interstitialAd.loadAd(AdRequest.Builder().build())
                interstitialAd.adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        interstitialAd.show()
                    }
                }
            } else {
                setClickCount(context)
            }
        }

        @JvmStatic fun bannerAds(context: Context?, adView: AdView) {
            adView.loadAd(AdRequest.Builder().build())
        }
    }




