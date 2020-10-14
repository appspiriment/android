package com.appspiriment.baseclasses.ads

import android.content.Context
import android.content.SharedPreferences
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.*

/*****************************
 * method to get shared Prefs
 *****************************/
object AdUtils {
    private var mInterstitialAd: InterstitialAd? = null
    val KEY_LAST_AD_SHOWN_AT = "~lastInter~"

    fun getSharedPrefs(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun saveCurrentInterstitialTime(context: Context) {
        getSharedPrefs(context).edit()
            .putLong(KEY_LAST_AD_SHOWN_AT, System.currentTimeMillis()).apply()
    }

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun getTimeElapsedFromLastInterstitial(
        context: Context,
        adTimeInterval: Int
    ): Boolean {
        val previousTimestamp = getSharedPrefs(context).getLong(
            KEY_LAST_AD_SHOWN_AT,
            System.currentTimeMillis() - (adTimeInterval + 1000)
        )
        return (System.currentTimeMillis() - previousTimestamp) > adTimeInterval
    }

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun isTurnForInterstitial(context: Context, adInterval: Int, adTimeInterval: Int): Boolean {
        if (adInterval < 1) return false

        val KEY_INTER_AD_NUM = "~intrAd~"
        val ads = getSharedPrefs(context).getInt(KEY_INTER_AD_NUM, 0)
        val isTurn = ads >= adInterval
        getSharedPrefs(context).edit().putInt(KEY_INTER_AD_NUM, if (isTurn) 0 else (ads + 1))
            .apply()
        return isTurn && getTimeElapsedFromLastInterstitial(context, adTimeInterval)
    }

    /***************************************
     * On Backpress
     ***************************************/
    fun getBannerAdSize(context: Context, windowManager: WindowManager): AdSize? {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            context, adWidth
        )
    }

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun getBannerAdView(
        context: Context,
        bannerAdId: String,
        windowManager: WindowManager? = null,
        onFailAction: (AdView) -> Unit = {}
    ): AdView {
        return AdView(context).apply {
            adUnitId = bannerAdId
            adSize = windowManager?.let { getBannerAdSize(context, it) } ?: AdSize.BANNER
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(p0: Int) {
                    val adview = when (adSize) {
                        AdSize.SMART_BANNER ->
                            AdView(context).apply {
                                adSize = AdSize.BANNER
                            }
                        AdSize.BANNER -> null
                        else -> AdView(context).apply {
                            adSize = AdSize.SMART_BANNER
                        }
                    }
                    adview?.run {
                        loadAd(AdRequest.Builder().build())
                        onFailAction(this)
                    }
                }
            }
            loadAd(AdRequest.Builder().build())
        }
    }

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun loadInterstitialAd(
        context: Context,
        adUnitId: String
    ) {
        if (mInterstitialAd == null) {
            mInterstitialAd = InterstitialAd(context).apply {
                this.adUnitId = adUnitId
                adListener = object : AdListener() {
                    override fun onAdClosed() {
                        loadAd(AdRequest.Builder().build())
                    }
                }
            }
        }

        mInterstitialAd?.run {
            if (!isLoaded && !isLoading)
                loadAd(AdRequest.Builder().build())
        }
    }

    /*****************************
     * method to get shared Prefs
     *****************************/
    fun showInterstitialAd(
        context: Context,
        adUnitId: String,
        adTimeInterval: Int,
        adInterval: Int
    ) {
        loadInterstitialAd(context, adUnitId)

        mInterstitialAd?.run {
            if (isTurnForInterstitial(context, adInterval, adTimeInterval)) {
                if (isLoaded) {
                    show()
                    saveCurrentInterstitialTime(context)
                }
            }
        }
    }
}