package com.appspiriment.androidutils

import android.content.Context
import android.net.ConnectivityManager
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

/********************************************
 * Class for methods related actionTo Device
 *******************************************/
object NetworkUtils {
    val API_REQUEST_STRING = 101
    val API_REQUEST_JSON = 102
    val API_REQUEST_JSON_ARRAY = 103

    /**
     * ******************************************
     * Method actionTo check whether the Internet is Connected
     * ******************************************
     */
    fun isNetworkConnected(context: Context): Boolean {
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
        url: String, stringSuccessListener: (String) -> Unit = {},
        jsonSuccessListener: (JSONObject) -> Unit = {},
        jsonArraySuccessListener: (JSONArray) -> Unit = {},
        errorListen: (error: ANError) -> Unit = {},
        requestType: Int = API_REQUEST_STRING
    ) {
        LogUtils.printLog(" URL   $url")
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(3, TimeUnit.SECONDS)
            .build()
        AndroidNetworking.get(url)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .apply {
                when (requestType) {
                    API_REQUEST_JSON_ARRAY -> getAsJSONArray(object : JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray) {
                            jsonArraySuccessListener(response)
                        }

                        override fun onError(error: ANError) {
                            errorListen(error)
                        }
                    })

                    API_REQUEST_JSON -> getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            jsonSuccessListener(response)
                        }

                        override fun onError(error: ANError) {
                            errorListen(error)
                        }
                    })

                    else -> getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            stringSuccessListener(response)
                        }

                        override fun onError(anError: ANError) {
                            errorListen(anError)
                        }
                    })
                }
            }
    }
}

