package com.appspiriment.androidutils

import android.util.Log
import com.androidnetworking.BuildConfig


/********************************************
 * Class for methods related actionTo Logging*
 *******************************************/
object LogUtils {
    /**
     * ******************************************
     * Method actionTo Print Stacktracce
     * ******************************************
     */
    fun printStacktrace(e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }

    /**
     * ******************************************
     * Method actionTo Print Log
     * ******************************************
     */
    fun printLog(message: Any?, tag: String = "LogTag: ", isError: Boolean = false) {
        if (!BuildConfig.DEBUG) return
        val logtag = "TestLog - $tag"
        if (isError)
            Log.e(logtag, message.toString())
        else
            Log.w(logtag, message.toString())
    }
}