package com.appspiriment.baseclasses

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.appspiriment.androidutils.UiUtils.showMsgDialog
import com.appspiriment.baseclasses.utils.BaseViewStates
import com.google.android.gms.ads.AdSize
import org.koin.android.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass


/*********************************************************
 * Class : BaseActivity
 * Purpose : Baseclass for Activities
 *  ******************************************************
 * Rework Details:
 * 1) {Author} :  {Date} : {Details}
 *********************************************************/
@SuppressLint("Registered")
abstract class BaseActivity<
        out ViewModelType : BaseViewModel,
        out DataBindingType : ViewDataBinding>(
    viewModelClass: KClass<ViewModelType>,
    val layoutId: Int,
    val viewModelVarId : Int
) : AppCompatActivity() {

    /***************************************
     * Declarations
     ***************************************/
    protected val isFullScreen: Boolean = false
    protected val viewModel by viewModelByClass<ViewModelType>(viewModelClass)
    val binding by lazy {
        DataBindingUtil.setContentView(
            this,
            layoutId
        ) as DataBindingType
    }


    /***************************************
     * OnCreate
     ***************************************/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setVariable(viewModelVarId, viewModel)
        binding.lifecycleOwner = this

        initializeViews()

        setObservers()

        initializeAdView()

        initializationAfterObserving()

    }

    /***************************************
     * On Backpress
     ***************************************/

    /***************************************
     * On Backpress
     ***************************************/
    open fun getAdSize(): AdSize? {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
            this,
            adWidth
        )
    }

//    /***************************************
//     * On Backpress
//     ***************************************/
//    override fun onBackPressed() {
//        showConfirmFinishActivityDialog()
//    }

    /***************************************
     * Setting Observers
     ***************************************/
    open fun <T> setBaseObserver(liveData: LiveData<T>, vararg functions: (value: T) -> Unit) {
        liveData.observe(this, Observer { value ->
            for (function in functions)
                function(value)
        })
    }

    /***************************************
     * Setting Observers
     ***************************************/
    private fun setObservers() {
        viewModel.viewState.observe(this, Observer { state ->
            run {
                when (state) {
                    BaseViewStates.STATE_FINISH -> finish()
                    BaseViewStates.STATE_FINISH_WITH_DIALOG -> showConfirmFinishActivityDialog()
                    else -> observeViewState(state)
                }

                if (state != BaseViewStates.STATE_NONE)
                    viewModel.setViewState(BaseViewStates.STATE_NONE)
            }
        })

        setViewObservers()
    }

    /***************************************
     * Show Exit Confirmation
     ***************************************/
    open fun showConfirmFinishActivityDialog() {
        showMsgDialog(
            getString(R.string.app_name),
            "Are you sure want to exit from this application?",
            positiveButton = "EXIT", negativeButton = "CANCEL",
            finishActivityOnOk = true
        )
    }

    /***************************************
     * On Backpress
     ***************************************/
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        initializeAdView()
    }

    /***************************************
     * On Backpress
     ***************************************/
    open fun observeViewState(state: Int) {}
    open fun setViewObservers() {}
    open fun initializeViews() {}
    open fun initializationAfterObserving() {}
    open fun initializeAdView() {}

}
