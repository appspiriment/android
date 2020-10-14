package com.appspiriment.networkutils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import org.koin.core.KoinComponent
import java.lang.Exception
import java.util.concurrent.TimeUnit


/********************************************
 * Class for methods related actionTo Device
 *******************************************/
@Suppress("unused")
class NetworkUtils(val context: Context) : KoinComponent {
    companion object {
        const val API_REQUEST_STRING = 101
        const val API_REQUEST_JSON = 102
        const val API_REQUEST_JSON_ARRAY = 103
        const val API_REQUEST_OBJECT = 104
        const val API_REQUEST_OBJECT_LIST = 105

        /**
         * ******************************************
         * Method actionTo check whether the Internet is Connected
         * ******************************************
         */
        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT < 23) {
                cm.activeNetworkInfo?.run {
                    return this.isConnected && (this.type == ConnectivityManager.TYPE_WIFI
                            || this.type == ConnectivityManager.TYPE_MOBILE)

                }

            } else {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    return this.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            this.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            this.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                }
            }
            return false
        }
    }

    init {
        AndroidNetworking.initialize(context)
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
        errorListen: (errorCode: Int, errorDetail: String) -> Unit,
        fallBackListener: (errorCode: Int, errorDetail: String) -> Unit = errorListen,
        errorLogger: (error: ANError) -> Unit = {},
        requestType: Int = API_REQUEST_STRING,
        connectTimeout: Int = 3,
        readTimeout: Int = 5,
        writeTimeout: Int =3
    ) {

        val errorHandler = { code: Int, detail: String ->
            var errorCode = code
            var errorDetail = detail

            if(errorCode == 0 && errorDetail == "connectionError"){
                errorCode = ErrorCodes.NONET
                errorDetail = "No Internet Connection!"
            }
            try {
                fallBackListener(errorCode, errorDetail)
            } catch (e: Exception) {
                errorListen(errorCode, errorDetail)
            }
        }

        val errorObjHandler = { error: ANError ->
            error.run {
                errorHandler(errorCode, errorBody ?: errorDetail ?: "Unexpected Error!")
                errorLogger(this)
            }
        }

        if (!isNetworkConnected(
                context
            )
        ) {
            errorHandler(ErrorCodes.NONET,"No internet connection!")
        } else {
            processApiRequest(
                url,
                stringSuccessListener,
                jsonSuccessListener,
                jsonArraySuccessListener,
                errorObjHandler,
                requestType,
                connectTimeout,
                readTimeout,
                writeTimeout
            )
        }
    }

    /**
     * ******************************************
     * Method actionTo start Volley Request
     * ******************************************
     */
    private fun processApiRequest(
        url: String, stringSuccessListener: (String) -> Unit = {},
        jsonSuccessListener: (JSONObject) -> Unit = {},
        jsonArraySuccessListener: (JSONArray) -> Unit = {},
        errorObjHandler: (error: ANError) -> Unit = {},
        requestType: Int = API_REQUEST_STRING,
        connectTimeout: Int = 3,
        readTimeout: Int = 5,
        writeTimeout: Int =3
    ) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(connectTimeout.toLong(), TimeUnit.SECONDS)
            .readTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(readTimeout.toLong(), TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .setOkHttpClient(okHttpClient)
            .build()
            .apply {
                when (requestType) {
                    API_REQUEST_JSON_ARRAY -> getAsJSONArray(object : JSONArrayRequestListener {
                        override fun onResponse(response: JSONArray) {
                            jsonArraySuccessListener(response)
                        }

                        override fun onError(error: ANError) {
                            errorObjHandler(error)
                        }
                    })

                    API_REQUEST_JSON -> getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            jsonSuccessListener(response)
                        }

                        override fun onError(error: ANError) {
                            errorObjHandler(error)
                        }
                    })
                    else -> getAsString(object : StringRequestListener {
                        override fun onResponse(response: String) {
                            stringSuccessListener(response)
                        }

                        override fun onError(error: ANError) {
                            errorObjHandler(error)
                        }
                    })
                }
            }
    }
}

