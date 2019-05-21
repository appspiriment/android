package com.appspiriment.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * ******************************************
 * open class : Utils
 * Purpose : Utility Functions
 * Author : Appspiriment Labs
 * Date : 24-01-2019
 * CopyRight © Appspiriment Labs. 2019
 * ******************************************
 */

/********************************************
 *                                          *
 * open class for methods related actionTo Logging     *
 *                                          *
 *******************************************/
open class LogUtils {

    companion object {
        /**
         * ******************************************
         * Method actionTo Print Stacktracce
         * ******************************************
         */
        fun printStacktrace(e: Exception) {
            e.printStackTrace()
        }

        /**
         * ******************************************
         * Method actionTo Print Log
         * ******************************************
         */
        fun printLog(message: Any?, tag: String = "LogTag: ", isError: Boolean = false) {
            if (isError)
                Log.e(tag, message?.toString() ?: "NULL")
            else
                Log.w(tag, message?.toString() ?: "NULL")
        }
    }
}


/********************************************
 *                                          *
 * open class for methods related actionTo Device      *
 *                                          *
 *******************************************/
open class DeviceUtils {
    companion object {
        /*************************************
         * HideKeyboard
         *************************************/
        fun hideKeyboard(activity: Activity) {
            try {
                val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view = activity.currentFocus
                if (view == null) {
                    view = View(activity)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {
            }
        }

    }
}

/********************************************
 *                                          *
 * open class for methods related actionTo Device      *
 *                                          *
 *******************************************/
open class NetworkUtils(val context: Context) {

    init {
        AndroidNetworking.initialize(context)
    }

    val API_REQUEST_STRING = 101
    val API_REQUEST_JSON = 102
    val API_REQUEST_JSON_ARRAY = 103


    /**
     * ******************************************
     * Method actionTo check whether the Internet is Connected
     * ******************************************
     */
    fun isNetworkConnected(): Boolean {
        val connMgr = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    /**
     * ******************************************
     * Method actionTo start Volley Request
     * ******************************************
     */
    fun startApiRequest(
        url: String,
        stringSuccessListenerLambda: (String) -> Unit = {},
        jsonSuccessListenerLambda: (JSONObject) -> Unit = {},
        jsonArraySuccessListenerLambda: (JSONArray) -> Unit = {},
        errorListenLambda: (error: ANError) -> Unit = {},
        requestType: Int = API_REQUEST_STRING,
        connectTimeOutInMillis : Long = 7000,
        readTimeOutInMillis : Long = 10000,
        writeTimeOutInMillis : Long = 10000
    ) {
        LogUtils.printLog(" URL   $url")
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(connectTimeOutInMillis , TimeUnit.MILLISECONDS)
            .readTimeout(readTimeOutInMillis, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeOutInMillis, TimeUnit.MILLISECONDS)
            .build();
        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .setOkHttpClient(okHttpClient)
            .doNotCacheResponse()
            .build()
            .apply {
                when (requestType) {
                    API_REQUEST_JSON_ARRAY -> getAsJSONArray(object : JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray) {
                            jsonArraySuccessListenerLambda(response)
                        }

                        override fun onError(error: ANError) {
                            errorListenLambda(error)
                        }
                    })

                    API_REQUEST_JSON -> getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            jsonSuccessListenerLambda(response)
                        }

                        override fun onError(error: ANError) {
                            errorListenLambda(error)
                        }
                    })
                    else -> getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            stringSuccessListenerLambda(response)
                        }

                        override fun onError(anError: ANError) {
                            errorListenLambda(anError)
                        }
                    })
                }
            }
    }
}

/********************************************
 *                                          *
 * open class for methods related to MessageUtils    *
 *                                          *
 *******************************************/
object MessageUtils {
    /**
     * **************************************
     * Method actionTo show an Alert Dialog
     * ****************************************
     */
    fun showToast(context: Context, message: String, isLong: Boolean = false) {
        Toast.makeText(context, message, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }


    /**
     * **************************************
     * Method actionTo show an Alert Dialog with onClick Listeners
     * ****************************************
     */
    fun showMsgDialog(
        activityThis: Activity, title: String? = null, message: String? = null, titleRes: Int = -1,
        messageRes: Int = -1, viewRes: Int = -1, view: View? = null,
        positiveButton: String = "OK",
        negativeButton: String? = null,
        neutralButton: String? = null,
        finishActivityOnOk: Boolean = false,
        finishActivityOnCancel: Boolean = false,
        isCancellable: Boolean = false,
        onClickListen: (Int) -> Unit = { },
        onCancelListen: () -> Unit = {},
        iconRes: Int = R.mipmap.ic_launcher
    ) {


        val dialogView = if (view == null && viewRes > 0) {
            activityThis.layoutInflater.inflate(viewRes, null)
        } else view

        val titleText = if (title == null && titleRes > 0) {
            activityThis.resources.getString(titleRes)
        } else title

        val messageText = if (message == null && messageRes != -1) {
            activityThis.resources.getString(messageRes)
        } else message

        val onClickListener = DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            onClickListen(which)
            if (finishActivityOnOk && which == DialogInterface.BUTTON_POSITIVE) activityThis.finish()
        }

        val onCancelListener = DialogInterface.OnCancelListener { dialog ->
            dialog.dismiss()
            onCancelListen()
            if (finishActivityOnCancel) activityThis.finish()
        }

        AlertDialog.Builder(activityThis).apply {
            if (titleText != null) setTitle(titleText)
            if (messageText != null) setMessage((messageText))
            setCancelable(isCancellable)
            setPositiveButton(positiveButton, onClickListener)
            setIcon(iconRes)

            if (dialogView != null) setView(dialogView)

            if (negativeButton != null) setNegativeButton(negativeButton, onClickListener)
            if (neutralButton != null) setNeutralButton(neutralButton, onClickListener)

            setOnCancelListener(onCancelListener)
        }.create().show()
    }

}

