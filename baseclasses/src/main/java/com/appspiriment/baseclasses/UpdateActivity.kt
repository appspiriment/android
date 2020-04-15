package com.appspiriment.baseclasses

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.appspiriment.baseclasses.utils.UpdateConstants.REQ_CODE_IMMD_UPDATE
import com.appspiriment.baseclasses.utils.UpdateConstants.REQ_CODE_FLEXI_UPDATE
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlin.reflect.KClass

abstract class UpdateActivity<
        out ViewModelType : BaseViewModel,
        out DataBindingType : ViewDataBinding>(
    viewModelClass: KClass<ViewModelType>,
    layoutId: Int, viewModelVarId : Int
) : BaseActivity<ViewModelType, DataBindingType>(viewModelClass, layoutId, viewModelVarId) {

    val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(applicationContext)
    }


    /***************************************
     * On Backpress
     ***************************************/
    fun updateAppIfAvailable() {
        val appUpdateInfoTask =

            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.updatePriority() >= 0
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE, this, REQ_CODE_IMMD_UPDATE
                    )
                } else {
                }
            }
    }

    /***************************************
     * Show Exit Confirmation
     ***************************************/
    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    this,
                    REQ_CODE_IMMD_UPDATE
                )
            }
        }
    }

    /***************************************
     * Show Exit Confirmation
     ***************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE_FLEXI_UPDATE) {
            when (resultCode) {
                RESULT_OK -> {
                }
                RESULT_CANCELED -> {
                    MessageUtils.showMsgDialog(this, "Update Required",
                        "The app requires an update to function. Please update the app from playstore!",
                        positiveButton = "OK",
                        positiveClickListen = { finish() })
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                }
            }
        }
    }
}