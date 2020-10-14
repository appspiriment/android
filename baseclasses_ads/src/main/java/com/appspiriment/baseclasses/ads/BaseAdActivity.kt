package com.appspiriment.baseclasses.ads

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.ViewDataBinding
import com.appspiriment.baseclasses.BaseActivity
import com.appspiriment.baseclasses.BaseViewModel
import kotlin.reflect.KClass


/*********************************************************
 * Class : BaseActivity
 * Purpose : Baseclass for Activities
 *  ******************************************************
 * Rework Details:
 * 1) {Author} :  {Date} : {Details}
 *********************************************************/
@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("Registered")
abstract class BaseAdActivity<
        out ViewModelType : BaseViewModel,
        out DataBindingType : ViewDataBinding>(
    viewModelClass: KClass<ViewModelType>,
    override val layoutId: Int,
    override val menuId: Int = R.menu.basedefaultmenu,
    protected val adMobBannerId: String? = null,
    override val toolbarId: Int? = null,
    override val isChildActivity: Boolean = false,
    override val isUpdateActivity: Boolean = false
) : BaseActivity<ViewModelType, DataBindingType>(
    viewModelClass = viewModelClass,
    layoutId = layoutId,
    menuId = menuId,
    toolbarId = toolbarId,
    isChildActivity = isChildActivity,
    isUpdateActivity = isUpdateActivity
) {

    /***************************************
     * Show Exit Confirmation
     ***************************************/
    override fun initializeAdView() {
        findViewById<LinearLayout>(R.id.adView)?.let {
            it.visibility = if (isNetworkConnected) {
                populateAdView(it)
            } else {
                View.GONE
            }
        }
    }

    /***************************************
     * Show Exit Confirmation
     ***************************************/
    private fun populateAdView(container: LinearLayout): Int {
        return try {
            adMobBannerId?.let { bannerId ->
                container.removeAllViews()
                val adView = AdUtils.getBannerAdView(
                    applicationContext,
                    bannerId,
                    windowManager,
                    onFailAction = { adView ->
                        container.removeAllViews()
                        container.addView(adView)
                    })
                container.addView(adView)
                View.VISIBLE
            } ?: View.GONE
        } catch (ignored: Exception) {
            View.GONE
        }
    }


    /***************************************
     * OnCreate
     ***************************************/
    override fun onStart() {
        super.onStart()
        adMobBannerId?.let { registerReceiver(networkListener, intentFilter) }
    }

    /***************************************
     * OnCreate
     ***************************************/
    override fun onStop() {
        super.onStop()
        adMobBannerId?.let { unregisterReceiver(networkListener) }

    }

}
